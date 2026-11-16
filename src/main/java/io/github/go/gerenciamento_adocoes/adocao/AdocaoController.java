package io.github.go.gerenciamento_adocoes.adocao;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
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

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping
    @Operation(
            summary = "Criar adoção",
            description = "Cria um nova adoção"
    )
    public ResponseEntity<AdocaoDetalhes> criar(@RequestBody @Valid AdocaoForm novo){
        AdocaoDetalhes detalhes = service.criar(novo);
        return ResponseEntity.status(HttpStatus.CREATED).body(detalhes);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create an adoption and upload its PDF document")
    public ResponseEntity<AdocaoDetalhes> criarComDocumento(
            @RequestParam("data") String data,
            @RequestParam(value = "document", required = false) MultipartFile document,
            @RequestParam(value = "media", required = false) MultipartFile media
    ) {
        var form = parseForm(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(form, selectDocument(document, media)));
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

    @GetMapping(value = "{id}/document", produces = MediaType.APPLICATION_PDF_VALUE)
    @Operation(summary = "Download the adoption PDF document")
    public ResponseEntity<ByteArrayResource> obterDocumento(@PathVariable Long id) {
        byte[] document = service.obterDocumento(id);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(document.length)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        org.springframework.http.ContentDisposition.inline()
                                .filename("adoption-" + id + ".pdf")
                                .build()
                                .toString()
                )
                .body(new ByteArrayResource(document));
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

    @PutMapping(value = "{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update an adoption and optionally replace its PDF document")
    public ResponseEntity<Void> atualizarComDocumento(
            @PathVariable Long id,
            @RequestParam("data") String data,
            @RequestParam(value = "document", required = false) MultipartFile document,
            @RequestParam(value = "media", required = false) MultipartFile media
    ) {
        service.atualizar(id, parseForm(data), selectDocument(document, media));
        return ResponseEntity.noContent().build();
    }

    private MultipartFile selectDocument(MultipartFile document, MultipartFile media) {
        return document != null ? document : media;
    }

    private AdocaoForm parseForm(String data) {
        try {
            return objectMapper.readValue(data, AdocaoForm.class);
        } catch (JsonProcessingException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The data part must contain valid JSON.", exception);
        }
    }
}
