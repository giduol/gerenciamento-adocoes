package io.github.go.gerenciamento_adocoes.adocao;

import org.springframework.stereotype.Component;
import io.github.go.gerenciamento_adocoes.adocao.dto.AdocaoForm;
import io.github.go.gerenciamento_adocoes.common.validation.CampoInvalido;
import io.github.go.gerenciamento_adocoes.common.validation.ValidationResult;

@Component
public class AdocaoValidator {

    public ValidationResult validar(AdocaoForm form, Long id){
        var result = ValidationResult.novo();

        if (form.getOngId() == null) {
            result.add(new CampoInvalido("ongId", "O ID da ONG é obrigatório."));
        }
        if (form.getAnimalId() == null) {
            result.add(new CampoInvalido("animalId", "O ID do animal é obrigatório."));
        }
        if (form.getAdotanteId() == null) {
            result.add(new CampoInvalido("adotanteId", "O ID do adotante é obrigatório."));
        }

        return result;
    };
}
