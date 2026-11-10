package io.github.go.gerenciamento_adocoes.ong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.go.gerenciamento_adocoes.ong.dto.OngForm;
import io.github.go.gerenciamento_adocoes.common.validation.CampoInvalido;
import io.github.go.gerenciamento_adocoes.common.validation.ValidationResult;

@Component
public class OngValidator {

    @Autowired
    private io.github.go.gerenciamento_adocoes.ong.OngRepository ongRepository;

    public ValidationResult validar(OngForm form, Long id){
        var result = ValidationResult.novo();

        var isListaNaoVazia = !ongRepository.findByNameAndNotId((form.name()), id).isEmpty();
        if(isListaNaoVazia) {
            result.add(new CampoInvalido("name", "Já existe uma ong com esse nome."));
        }

        return result;
    };
}
