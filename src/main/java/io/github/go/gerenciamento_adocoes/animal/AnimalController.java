package io.github.go.gerenciamento_adocoes.animal;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.github.go.gerenciamento_adocoes.animal.dto.AnimalDetalhes;
import io.github.go.gerenciamento_adocoes.animal.dto.AnimalForm;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/animais")
@CrossOrigin("*")
@Tag(name = "animais", description = "Gerenciamento de animais")
public class AnimalController {

    @Autowired
    private AnimalService service;

    @PostMapping
    @Operation(
            summary = "Criar animal",
            description = "Cria um novo animal"
    )
    public ResponseEntity<AnimalDetalhes> criar(@RequestBody @Valid AnimalForm novo){
        AnimalDetalhes detalhes = service.criar(novo);
        return ResponseEntity.status(HttpStatus.CREATED).body(detalhes);
    }

    @GetMapping
    @Operation(
            summary = "Listar todos os animais",
            description = "Retorna a lista de todos os animais cadastrados"
    )
    public ResponseEntity<List<AnimalDetalhes>> listarTodos(){
        List<AnimalDetalhes> animais = service.listarTodos();
        return ResponseEntity.ok(animais);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "Obter detalhes do animal",
            description = "Retorna os detalhes de um animal específico"
    )
    public ResponseEntity<AnimalDetalhes> obterDetalhes(@PathVariable Long id){
        AnimalDetalhes detalhes = service.obterDetalhes(id);
        return ResponseEntity.status(HttpStatus.OK).body(detalhes);
    }

    @PutMapping("{id}")
    @Operation(
            summary = "Atualizar animal",
            description = "Atualiza os dados de um animal específico"
    )
    public ResponseEntity<AnimalDetalhes> atualizar(@PathVariable Long id, @RequestBody @Valid AnimalForm animalAtualizado){
        service.atualizar(id, animalAtualizado);
        return ResponseEntity.noContent().build();
    }
}
