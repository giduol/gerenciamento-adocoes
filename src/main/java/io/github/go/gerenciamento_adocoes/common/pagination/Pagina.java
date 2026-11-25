package io.github.go.gerenciamento_adocoes.common.pagination;

import java.util.List;

import org.springframework.data.domain.Page;

/** Resposta das listagens quando a paginação é pedida (?page e/ou ?size). */
public record Pagina<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {

    public static <T> Pagina<T> de(Page<T> pagina) {
        return new Pagina<>(
                pagina.getContent(),
                pagina.getNumber(),
                pagina.getSize(),
                pagina.getTotalElements(),
                pagina.getTotalPages()
        );
    }
}
