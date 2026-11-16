package io.github.go.gerenciamento_adocoes.animal;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.go.gerenciamento_adocoes.animal.dto.AnimalDetalhes;
import io.github.go.gerenciamento_adocoes.animal.dto.AnimalForm;
import io.github.go.gerenciamento_adocoes.animal.mapper.AnimalMapper;
import io.github.go.gerenciamento_adocoes.animal.model.AnimalEntity;
import io.github.go.gerenciamento_adocoes.common.exceptions.ResourceNotFoundException;
import io.github.go.gerenciamento_adocoes.common.exceptions.ValidationException;
import jakarta.transaction.Transactional;

@Service
public class AnimalService {

    @Autowired
    private AnimalValidator validator;

    @Autowired
    private AnimalRepository repository;

    @Autowired
    private AnimalMapper mapper;

    public AnimalDetalhes criar(AnimalForm form){
        var result = validator.validar(form, null);

        if(result.isInvalido()){
            throw new ValidationException(result.getCamposInvalidos());
        }

        AnimalEntity entity = mapper.toEntity(form);
        repository.save(entity);
        return mapper.toDetalhes(entity);
    }

    public List<AnimalDetalhes> listarTodos() {
        return repository.findAll()
                .stream()
                .map(mapper::toDetalhes)
                .toList();
    }

    public AnimalDetalhes obterDetalhes(Long id){
        return repository.findById(id)
                .map(mapper::toDetalhes)
                .orElseThrow(() -> new ResourceNotFoundException());
    }

    @Transactional
    public void atualizar(Long id, AnimalForm animalAtualizado){
        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException());

        var result = validator.validar(animalAtualizado, id);
        if(result.isInvalido()) {
            throw new ValidationException(result.getCamposInvalidos());
        }
        mapper.updateEntity(entity, animalAtualizado);
    }
}
