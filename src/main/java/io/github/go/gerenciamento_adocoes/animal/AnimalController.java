package io.github.go.gerenciamento_adocoes.animal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.github.go.gerenciamento_adocoes.animal.dto.AnimalDetalhes;
import io.github.go.gerenciamento_adocoes.animal.dto.AnimalForm;
import io.github.go.gerenciamento_adocoes.common.pagination.Paginacao;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/animais")
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
            summary = "Listar os animais",
            description = "Retorna todos os animais cadastrados ou, com page/size, uma página deles"
    )
    @ApiResponse(responseCode = "200", description = Paginacao.DESCRICAO_RESPOSTA)
    public ResponseEntity<?> listarTodos(
            @Parameter(description = Paginacao.DESCRICAO_PAGE) @RequestParam(required = false) Integer page,
            @Parameter(description = Paginacao.DESCRICAO_SIZE) @RequestParam(required = false) Integer size
    ){
        return Paginacao.responder(page, size, service::listarPagina, service::listarTodos);
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
        return ResponseEntity.ok(service.atualizar(id, animalAtualizado));
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "Excluir animal",
            description = "Exclui um animal sem adoções vinculadas"
    )
    public ResponseEntity<Void> excluir(@PathVariable Long id){
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
