package io.github.go.gerenciamento_adocoes.adocao;

import java.time.LocalDateTime;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import io.github.go.gerenciamento_adocoes.adotante.AdotanteRepository;
import io.github.go.gerenciamento_adocoes.animal.AnimalRepository;
import io.github.go.gerenciamento_adocoes.adocao.dto.AdocaoDetalhes;
import io.github.go.gerenciamento_adocoes.adocao.dto.AdocaoForm;
import io.github.go.gerenciamento_adocoes.adocao.mapper.AdocaoMapper;
import io.github.go.gerenciamento_adocoes.adocao.model.AdocaoEntity;
import io.github.go.gerenciamento_adocoes.common.exceptions.ResourceNotFoundException;
import io.github.go.gerenciamento_adocoes.common.exceptions.ValidationException;
import io.github.go.gerenciamento_adocoes.ong.OngRepository;
import jakarta.transaction.Transactional;

@Service
public class AdocaoService {

    @Autowired
    private AdocaoValidator validator;

    @Autowired
    private AdocaoRepository repository;

    @Autowired
    private AdocaoMapper mapper;

    @Autowired
    private OngRepository ongRepository;

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private AdotanteRepository adotanteRepository;

    @Transactional
    public AdocaoDetalhes criar(AdocaoForm form){
        return criar(form, null);
    }

    @Transactional
    public AdocaoDetalhes criar(AdocaoForm form, MultipartFile document){
        var result = validator.validar(form, null);
        var documentResult = validator.validarDocumento(document);
        documentResult.getCamposInvalidos().forEach(result::add);

        if(result.isInvalido()){
            throw new ValidationException(result.getCamposInvalidos());
        }

        AdocaoEntity entity = mapper.toEntity(form);
        if (document != null && !document.isEmpty()) {
            entity.setAdoptionDocument(readDocument(document));
        }
        entity.setAdoptionDate(LocalDateTime.now());
        associarEntidades(entity, form);
        repository.save(entity);
        return mapper.toDetalhes(entity);
    }

    @Transactional
    public List<AdocaoDetalhes> listarTodos() {
        return repository.findAll()
                .stream()
                .map(mapper::toDetalhes)
                .toList();
    }

    @Transactional
    public AdocaoDetalhes obterDetalhes(Long id){
        return repository.findById(id)
                .map(mapper::toDetalhes)
                .orElseThrow(() -> new ResourceNotFoundException());
    }

    @Transactional
    public void atualizar(Long id, AdocaoForm adotanteAtualizado){
        atualizar(id, adotanteAtualizado, null);
    }

    @Transactional
    public void atualizar(Long id, AdocaoForm adotanteAtualizado, MultipartFile document){
        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException());
        byte[] existingDocument = entity.getAdoptionDocument();

        var result = validator.validar(adotanteAtualizado, id);
        var documentResult = validator.validarDocumento(document);
        documentResult.getCamposInvalidos().forEach(result::add);
        if(result.isInvalido()) {
            throw new ValidationException(result.getCamposInvalidos());
        }
        mapper.updateEntity(entity, adotanteAtualizado);
        if (document != null && !document.isEmpty()) {
            entity.setAdoptionDocument(readDocument(document));
        } else if (adotanteAtualizado.getAdoptionDocument() == null) {
            entity.setAdoptionDocument(existingDocument);
        }
        associarEntidades(entity, adotanteAtualizado);
    }

    @Transactional
    public byte[] obterDocumento(Long id) {
        return repository.findById(id)
                .map(AdocaoEntity::getAdoptionDocument)
                .filter(document -> document.length > 0)
                .orElseThrow(ResourceNotFoundException::new);
    }

    private byte[] readDocument(MultipartFile document) {
        try {
            return document.getBytes();
        } catch (IOException exception) {
            throw new IllegalStateException("Could not read the uploaded adoption PDF.", exception);
        }
    }

    private void associarEntidades(AdocaoEntity entity, AdocaoForm form) {
        entity.setOng(ongRepository.findById(form.getOngId())
                .orElseThrow(ResourceNotFoundException::new));
        entity.setAnimal(animalRepository.findById(form.getAnimalId())
                .orElseThrow(ResourceNotFoundException::new));
        entity.setAdotante(adotanteRepository.findById(form.getAdotanteId())
                .orElseThrow(ResourceNotFoundException::new));
    }

}
