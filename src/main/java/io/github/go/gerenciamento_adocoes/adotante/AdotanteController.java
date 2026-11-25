package io.github.go.gerenciamento_adocoes.adotante;

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

import io.github.go.gerenciamento_adocoes.adotante.dto.AdotanteDetalhes;
import io.github.go.gerenciamento_adocoes.adotante.dto.AdotanteForm;
import io.github.go.gerenciamento_adocoes.common.pagination.Paginacao;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/adotantes")
@Tag(name = "adotantes", description = "Gerenciamento de adotantes")
public class AdotanteController {

    @Autowired
    private io.github.go.gerenciamento_adocoes.adotante.AdotanteService service;

    @PostMapping
    @Operation(
            summary = "Criar adotante",
            description = "Cria um novo adotante"
    )
    public ResponseEntity<AdotanteDetalhes> criar(@RequestBody @Valid AdotanteForm novo){
        AdotanteDetalhes detalhes = service.criar(novo);
        return ResponseEntity.status(HttpStatus.CREATED).body(detalhes);
    }

    @GetMapping
    @Operation(
            summary = "Listar os adotantes",
            description = "Retorna todos os adotantes cadastrados ou, com page/size, uma página deles"
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
            summary = "Obter detalhes do adotante",
            description = "Retorna os detalhes de um adotante específico"
    )
    public ResponseEntity<AdotanteDetalhes> obterDetalhes(@PathVariable Long id){
        AdotanteDetalhes detalhes = service.obterDetalhes(id);
        return ResponseEntity.status(HttpStatus.OK).body(detalhes);
    }

    @PutMapping("{id}")
    @Operation(
            summary = "Atualizar adotante",
            description = "Atualiza os dados de um adotante específico"
    )
    public ResponseEntity<AdotanteDetalhes> atualizar(@PathVariable Long id, @RequestBody @Valid AdotanteForm adotanteAtualizado){
        return ResponseEntity.ok(service.atualizar(id, adotanteAtualizado));
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "Excluir adotante",
            description = "Exclui um adotante sem adoções vinculadas"
    )
    public ResponseEntity<Void> excluir(@PathVariable Long id){
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
