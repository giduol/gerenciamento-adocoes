package io.github.go.gerenciamento_adocoes.acompanhamento;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.github.go.gerenciamento_adocoes.acompanhamento.dto.AcompanhamentoDetalhes;
import io.github.go.gerenciamento_adocoes.acompanhamento.dto.AcompanhamentoForm;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping
@CrossOrigin("*")
@Tag(name = "acompanhamentos", description = "Acompanhamento pós-adoção")
public class AcompanhamentoController {

    private final AcompanhamentoService service;
    private final ObjectMapper objectMapper;

    public AcompanhamentoController(AcompanhamentoService service, ObjectMapper objectMapper) {
        this.service = service;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/adocoes/{adocaoId}/acompanhamentos")
    @Operation(summary = "Listar acompanhamentos de uma adoção")
    public ResponseEntity<List<AcompanhamentoDetalhes>> listarPorAdocao(@PathVariable Long adocaoId) {
        return ResponseEntity.ok(service.listarPorAdocao(adocaoId));
    }

    @PostMapping("/adocoes/{adocaoId}/acompanhamentos")
    @Operation(summary = "Registrar um acompanhamento realizado")
    public ResponseEntity<AcompanhamentoDetalhes> criar(
            @PathVariable Long adocaoId,
            @RequestBody @Valid AcompanhamentoForm form
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(adocaoId, form));
    }

    @PostMapping(
            value = "/adocoes/{adocaoId}/acompanhamentos",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Operation(summary = "Register a follow-up with uploaded photos or videos")
    public ResponseEntity<AcompanhamentoDetalhes> criarComArquivos(
            @PathVariable Long adocaoId,
            @RequestPart("data") String data,
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @RequestPart(value = "medias", required = false) List<MultipartFile> medias
    ) {
        AcompanhamentoForm form;
        try {
            form = objectMapper.readValue(data, AcompanhamentoForm.class);
        } catch (JsonProcessingException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The data part must contain valid JSON.", exception);
        }

        var uploadedFiles = new ArrayList<MultipartFile>();
        if (files != null) {
            uploadedFiles.addAll(files);
        }
        if (medias != null) {
            uploadedFiles.addAll(medias);
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.criar(adocaoId, form, uploadedFiles));
    }

    @GetMapping("/acompanhamentos/{id}")
    @Operation(summary = "Obter detalhes de um acompanhamento")
    public ResponseEntity<AcompanhamentoDetalhes> obterDetalhes(@PathVariable Long id) {
        return ResponseEntity.ok(service.obterDetalhes(id));
    }

    @GetMapping("/acompanhamentos/{id}/media/{index}")
    @Operation(summary = "Download an uploaded follow-up file")
    public ResponseEntity<ByteArrayResource> downloadMedia(
            @PathVariable Long id,
            @PathVariable int index
    ) {
        var media = service.downloadMedia(id, index);
        var contentType = MediaType.parseMediaType(media.contentType());
        var resource = new ByteArrayResource(media.content());
        return ResponseEntity.ok()
                .contentType(contentType)
                .contentLength(media.content().length)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        org.springframework.http.ContentDisposition.attachment()
                                .filename(media.fileName())
                                .build()
                                .toString()
                )
                .body(resource);
    }

}
