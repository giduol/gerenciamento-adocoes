package io.github.go.gerenciamento_adocoes.adotante;

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

import io.github.go.gerenciamento_adocoes.adotante.dto.AdotanteDetalhes;
import io.github.go.gerenciamento_adocoes.adotante.dto.AdotanteForm;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/adotantes")
@CrossOrigin("*")
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
            summary = "Listar todos os adotantes",
            description = "Retorna a lista de todos os adotantes cadastrados"
    )
    public ResponseEntity<List<AdotanteDetalhes>> listarTodos(){
        List<AdotanteDetalhes> adotantes = service.listarTodos();
        return ResponseEntity.ok(adotantes);
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
        service.atualizar(id, adotanteAtualizado);
        return ResponseEntity.noContent().build();
    }
}
