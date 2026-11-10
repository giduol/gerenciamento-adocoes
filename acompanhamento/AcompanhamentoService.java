package io.github.go.gerenciamento_adocoes.acompanhamento;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashSet;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.github.go.gerenciamento_adocoes.acompanhamento.dto.AcompanhamentoDetalhes;
import io.github.go.gerenciamento_adocoes.acompanhamento.dto.AcompanhamentoForm;
import io.github.go.gerenciamento_adocoes.acompanhamento.mapper.AcompanhamentoMapper;
import io.github.go.gerenciamento_adocoes.acompanhamento.model.AcompanhamentoEntity;
import io.github.go.gerenciamento_adocoes.acompanhamento.model.StoredMediaFile;
import io.github.go.gerenciamento_adocoes.acompanhamento.persistence.MeiosAcompanhamentoConverter;
import io.github.go.gerenciamento_adocoes.common.exceptions.ResourceNotFoundException;
import io.github.go.gerenciamento_adocoes.common.exceptions.ValidationException;
import io.github.go.gerenciamento_adocoes.adocao.AdocaoRepository;
import jakarta.transaction.Transactional;
import java.io.IOException;

@Service
public class AcompanhamentoService {

    private final AcompanhamentoRepository repository;
    private final AdocaoRepository adocaoRepository;
    private final AcompanhamentoMapper mapper;
    private final AcompanhamentoValidator validator;
    private final MeiosAcompanhamentoConverter meiosConverter;
    private final ObjectMapper objectMapper;

    public AcompanhamentoService(
            AcompanhamentoRepository repository,
            AdocaoRepository adocaoRepository,
            AcompanhamentoMapper mapper,
            AcompanhamentoValidator validator,
            MeiosAcompanhamentoConverter meiosConverter,
            ObjectMapper objectMapper
    ) {
        this.repository = repository;
        this.adocaoRepository = adocaoRepository;
        this.mapper = mapper;
        this.validator = validator;
        this.meiosConverter = meiosConverter;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public List<AcompanhamentoDetalhes> listarPorAdocao(Long adocaoId) {
        if (!adocaoRepository.existsById(adocaoId)) {
            throw new ResourceNotFoundException();
        }
        return repository.findByAdoptionIdOrderByPerformedAtAsc(adocaoId)
                .stream()
                .map(mapper::toDetalhes)
                .toList();
    }

    @Transactional
    public AcompanhamentoDetalhes obterDetalhes(Long id) {
        return repository.findById(id)
                .map(mapper::toDetalhes)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional
    public AcompanhamentoDetalhes criar(Long adocaoId, AcompanhamentoForm form) {
        return criar(adocaoId, form, List.of());
    }

    @Transactional
    public AcompanhamentoDetalhes criar(Long adocaoId, AcompanhamentoForm form, List<MultipartFile> files) {
        var uploadedFiles = files == null ? List.<MultipartFile>of() : files;
        var result = validator.validar(form, !uploadedFiles.isEmpty());
        var fileValidation = validator.validarArquivos(uploadedFiles);
        fileValidation.getCamposInvalidos().forEach(result::add);
        if (result.isInvalido()) {
            throw new ValidationException(result.getCamposInvalidos());
        }

        var adocao = adocaoRepository.findById(adocaoId)
                .orElseThrow(ResourceNotFoundException::new);
        var entity = new AcompanhamentoEntity();
        entity.setAdoption(adocao);
        entity.setPeriodDays(form.getPeriodDays());
        entity.setAnimalWithAdopter(form.getAnimalWithAdopter());
        entity.setAnimalHealthy(form.getAnimalHealthy());
        entity.setAnimalReturned(form.getAnimalReturned());
        entity.setAnimalNotFound(form.getAnimalNotFound());
        entity.setMethodsJson(meiosConverter.convertToDatabaseColumn(
                form.getMethods() == null ? new LinkedHashSet<>() : new LinkedHashSet<>(form.getMethods())
        ));
        entity.setMediaFiles(serializeFiles(uploadedFiles));
        entity.setNotes(form.getNotes());
        entity.setPerformedAt(LocalDateTime.now());

        return mapper.toDetalhes(repository.save(entity));
    }

    @Transactional
    public DownloadedMedia downloadMedia(Long followUpId, int index) {
        var entity = repository.findById(followUpId).orElseThrow(ResourceNotFoundException::new);
        List<StoredMediaFile> files = mapper.readMediaFiles(entity);
        if (index < 0 || index >= files.size()) {
            throw new ResourceNotFoundException();
        }
        var file = files.get(index);
        return new DownloadedMedia(
                file.fileName(),
                file.contentType(),
                Base64.getDecoder().decode(file.data())
        );
    }

    private JsonNode serializeFiles(List<MultipartFile> files) {
        var storedFiles = new ArrayList<StoredMediaFile>(files.size());
        for (var file : files) {
            try {
                storedFiles.add(new StoredMediaFile(
                        file.getOriginalFilename(),
                        file.getContentType(),
                        Base64.getEncoder().encodeToString(file.getBytes())
                ));
            } catch (IOException exception) {
                throw new IllegalStateException("Could not read uploaded media file.", exception);
            }
        }
        return objectMapper.valueToTree(storedFiles);
    }

    public record DownloadedMedia(String fileName, String contentType, byte[] content) {}
}
