package io.github.go.gerenciamento_adocoes.ong;

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

import io.github.go.gerenciamento_adocoes.common.pagination.Paginacao;
import io.github.go.gerenciamento_adocoes.ong.dto.OngForm;
import io.github.go.gerenciamento_adocoes.ong.dto.OngDetalhes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/ongs")
@Tag(name = "ongs", description = "Gerenciamento de ongs")
public class OngController {

    @Autowired
    private OngService service;

    @PostMapping
    @Operation(
            summary = "Criar ong",
            description = "Cria um novo ong"
    )
    public ResponseEntity<OngDetalhes> criar(@RequestBody @Valid OngForm novo){
        OngDetalhes detalhes = service.criar(novo);
        return ResponseEntity.status(HttpStatus.CREATED).body(detalhes);
    }

    @GetMapping
    @Operation(
            summary = "Listar as ongs",
            description = "Retorna todas as ongs cadastradas ou, com page/size, uma página delas"
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
            summary = "Obter detalhes da ong",
            description = "Retorna os detalhes de uma ong específico"
    )
    public ResponseEntity<OngDetalhes> obterDetalhes(@PathVariable Long id){
        OngDetalhes detalhes = service.obterDetalhes(id);
        return ResponseEntity.status(HttpStatus.OK).body(detalhes);
    }

    @PutMapping("{id}")
    @Operation(
            summary = "Atualizar ong",
            description = "Atualiza os dados de uma ong específica"
    )
    public ResponseEntity<OngDetalhes> atualizar(@PathVariable Long id, @RequestBody @Valid OngForm ongAtualizado){
        return ResponseEntity.ok(service.atualizar(id, ongAtualizado));
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "Excluir ong",
            description = "Exclui uma ong sem adoções vinculadas"
    )
    public ResponseEntity<Void> excluir(@PathVariable Long id){
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
