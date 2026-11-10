package io.github.go.gerenciamento_adocoes.adotante;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import io.github.go.gerenciamento_adocoes.adotante.dto.AdotanteForm;
import io.github.go.gerenciamento_adocoes.common.validation.CampoInvalido;
import io.github.go.gerenciamento_adocoes.common.validation.ValidationResult;

@Component
public class AdotanteValidator {

    @Autowired
    private io.github.go.gerenciamento_adocoes.adotante.AdotanteRepository adotanteRepository;

    public ValidationResult validar(AdotanteForm form, Long id){
        var result = ValidationResult.novo();

        var isListaNaoVazia = !adotanteRepository.findByNameAndNotId((form.name()), id).isEmpty();
        if(isListaNaoVazia) {
            result.add(new CampoInvalido("name", "Já existe um adotante com esse nome."));
        }

        return result;
    };
}
