package io.github.go.gerenciamento_adocoes.ong;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.github.go.gerenciamento_adocoes.ong.dto.OngDetalhes;
import io.github.go.gerenciamento_adocoes.ong.dto.OngForm;
import io.github.go.gerenciamento_adocoes.ong.mapper.OngMapper;
import io.github.go.gerenciamento_adocoes.ong.model.OngEntity;
import io.github.go.gerenciamento_adocoes.common.exceptions.ResourceNotFoundException;
import io.github.go.gerenciamento_adocoes.common.exceptions.ValidationException;
import jakarta.transaction.Transactional;

@Service
public class OngService {

    @Autowired
    private OngValidator validator;

    @Autowired
    private OngRepository repository;

    @Autowired
    private OngMapper mapper;

    public OngDetalhes criar(OngForm form){
        var result = validator.validar(form, null);

        if(result.isInvalido()){
            throw new ValidationException(result.getCamposInvalidos());
        }

        OngEntity entity = mapper.toEntity(form);
        repository.save(entity);
        return mapper.toDetalhes(entity);
    }

    public List<OngDetalhes> listarTodos() {
        return repository.findAll()
                .stream()
                .map(mapper::toDetalhes)
                .toList();
    }

    public OngDetalhes obterDetalhes(Long id){
        return repository.findById(id)
                .map(mapper::toDetalhes)
                .orElseThrow(() -> new ResourceNotFoundException());
    }

    @Transactional
    public void atualizar(Long id, OngForm ongAtualizado){
        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException());

        var result = validator.validar(ongAtualizado, id);
        if(result.isInvalido()) {
            throw new ValidationException(result.getCamposInvalidos());
        }
        mapper.updateEntity(entity, ongAtualizado);
    }
}
