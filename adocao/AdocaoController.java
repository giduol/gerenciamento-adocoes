package io.github.go.gerenciamento_adocoes.adocao;

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
import io.github.go.gerenciamento_adocoes.adocao.dto.AdocaoDetalhes;
import io.github.go.gerenciamento_adocoes.adocao.dto.AdocaoForm;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/adocoes")
@CrossOrigin("*")
@Tag(name = "adocoes", description = "Gerenciamento de adoções")
public class AdocaoController {

    @Autowired
    private AdocaoService service;

    @PostMapping
    @Operation(
            summary = "Criar adoção",
            description = "Cria um nova adoção"
    )
    public ResponseEntity<AdocaoDetalhes> criar(@RequestBody @Valid AdocaoForm novo){
        AdocaoDetalhes detalhes = service.criar(novo);
        return ResponseEntity.status(HttpStatus.CREATED).body(detalhes);
    }

    @GetMapping
    @Operation(
            summary = "Listar todos as adoções",
            description = "Retorna a lista de todas as adoções cadastradas"
    )
    public ResponseEntity<List<AdocaoDetalhes>> listarTodos(){
        List<AdocaoDetalhes> adotantes = service.listarTodos();
        return ResponseEntity.ok(adotantes);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "Obter detalhes da adoção",
            description = "Retorna os detalhes de uma adoção específica"
    )
    public ResponseEntity<AdocaoDetalhes> obterDetalhes(@PathVariable Long id){
        AdocaoDetalhes detalhes = service.obterDetalhes(id);
        return ResponseEntity.status(HttpStatus.OK).body(detalhes);
    }

    @PutMapping("{id}")
    @Operation(
            summary = "Atualizar adoção",
            description = "Atualiza os dados de uma adoção específica"
    )
    public ResponseEntity<AdocaoDetalhes> atualizar(@PathVariable Long id, @RequestBody @Valid AdocaoForm adotanteAtualizado){
        service.atualizar(id, adotanteAtualizado);
        return ResponseEntity.noContent().build();
    }
}
