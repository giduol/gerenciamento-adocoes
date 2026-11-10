package io.github.go.gerenciamento_adocoes.adotante;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.github.go.gerenciamento_adocoes.adotante.dto.AdotanteDetalhes;
import io.github.go.gerenciamento_adocoes.adotante.dto.AdotanteForm;
import io.github.go.gerenciamento_adocoes.adotante.mapper.AdotanteMapper;
import io.github.go.gerenciamento_adocoes.adotante.model.AdotanteEntity;
import io.github.go.gerenciamento_adocoes.common.exceptions.ResourceNotFoundException;
import io.github.go.gerenciamento_adocoes.common.exceptions.ValidationException;
import jakarta.transaction.Transactional;

@Service
public class AdotanteService {

    @Autowired
    private io.github.go.gerenciamento_adocoes.adotante.AdotanteValidator validator;

    @Autowired
    private io.github.go.gerenciamento_adocoes.adotante.AdotanteRepository repository;

    @Autowired
    private AdotanteMapper mapper;

    public AdotanteDetalhes criar(AdotanteForm form){
        var result = validator.validar(form, null);

        if(result.isInvalido()){
            throw new ValidationException(result.getCamposInvalidos());
        }

        AdotanteEntity entity = mapper.toEntity(form);
        repository.save(entity);
        return mapper.toDetalhes(entity);
    }

    public List<AdotanteDetalhes> listarTodos() {
        return repository.findAll()
                .stream()
                .map(mapper::toDetalhes)
                .toList();
    }

    public AdotanteDetalhes obterDetalhes(Long id){
        return repository.findById(id)
                .map(mapper::toDetalhes)
                .orElseThrow(() -> new ResourceNotFoundException());
    }

    @Transactional
    public void atualizar(Long id, AdotanteForm adotanteAtualizado){
        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException());

        var result = validator.validar(adotanteAtualizado, id);
        if(result.isInvalido()) {
            throw new ValidationException(result.getCamposInvalidos());
        }
        mapper.updateEntity(entity, adotanteAtualizado);
    }
}
