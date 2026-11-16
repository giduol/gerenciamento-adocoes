package io.github.go.gerenciamento_adocoes.animal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.go.gerenciamento_adocoes.animal.dto.AnimalForm;
import io.github.go.gerenciamento_adocoes.common.validation.CampoInvalido;
import io.github.go.gerenciamento_adocoes.common.validation.ValidationResult;

@Component
public class AnimalValidator {

    @Autowired
    private AnimalRepository animalRepository;

    public ValidationResult validar(AnimalForm form, Long id){
        var result = ValidationResult.novo();

        var isListaNaoVazia = !animalRepository.findByNameAndNotId((form.name()), id).isEmpty();
        if(isListaNaoVazia) {
            result.add(new CampoInvalido("name", "Já existe um animal com esse nome."));
        }

        return result;
    };
}
