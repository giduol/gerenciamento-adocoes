package io.github.go.gerenciamento_adocoes.adocao;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
        var result = validator.validar(form, null);

        if(result.isInvalido()){
            throw new ValidationException(result.getCamposInvalidos());
        }

        AdocaoEntity entity = mapper.toEntity(form);
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
        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException());

        var result = validator.validar(adotanteAtualizado, id);
        if(result.isInvalido()) {
            throw new ValidationException(result.getCamposInvalidos());
        }
        mapper.updateEntity(entity, adotanteAtualizado);
        associarEntidades(entity, adotanteAtualizado);
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
