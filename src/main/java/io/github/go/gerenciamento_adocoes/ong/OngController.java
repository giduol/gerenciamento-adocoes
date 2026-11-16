package io.github.go.gerenciamento_adocoes.ong;

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

import io.github.go.gerenciamento_adocoes.ong.dto.OngForm;
import io.github.go.gerenciamento_adocoes.ong.dto.OngDetalhes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/ongs")
@CrossOrigin("*")
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
            summary = "Listar todos as ongs",
            description = "Retorna a lista de todos as ongs cadastradas"
    )
    public ResponseEntity<List<OngDetalhes>> listarTodos(){
        List<OngDetalhes> ongs = service.listarTodos();
        return ResponseEntity.ok(ongs);
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
        service.atualizar(id, ongAtualizado);
        return ResponseEntity.noContent().build();
    }
}
