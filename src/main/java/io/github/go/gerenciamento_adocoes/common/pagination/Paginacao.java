package io.github.go.gerenciamento_adocoes.common.pagination;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;

import io.github.go.gerenciamento_adocoes.common.exceptions.ValidationException;
import io.github.go.gerenciamento_adocoes.common.validation.CampoInvalido;

/**
 * Paginação opcional das listagens.
 * <p>
 * Sem {@code page} e sem {@code size}, a listagem devolve o array completo, como sempre fez
 * (o app usa isso nos selects do cadastro de adoção). Com qualquer um dos dois, devolve uma
 * {@link Pagina}, ordenada por id para que as páginas sejam estáveis.
 */
public final class Paginacao {

    public static final int TAMANHO_PADRAO = 20;
    public static final int TAMANHO_MAXIMO = 100;

    public static final String DESCRICAO_PAGE =
            "Número da página, começando em 0. Informar page ou size ativa a paginação.";
    public static final String DESCRICAO_SIZE =
            "Itens por página (1 a " + TAMANHO_MAXIMO + ", padrão " + TAMANHO_PADRAO + ").";
    public static final String DESCRICAO_RESPOSTA =
            "Sem page e size: array com todos os registros. "
                    + "Com page ou size: objeto {content, page, size, totalElements, totalPages}.";

    private Paginacao() {
    }

    public static Optional<Pageable> resolver(Integer page, Integer size) {
        if (page == null && size == null) {
            return Optional.empty();
        }

        int pagina = page == null ? 0 : page;
        int tamanho = size == null ? TAMANHO_PADRAO : size;

        List<CampoInvalido> erros = new ArrayList<>();
        if (pagina < 0) {
            erros.add(new CampoInvalido("page", "A página deve ser maior ou igual a 0."));
        }
        if (tamanho < 1 || tamanho > TAMANHO_MAXIMO) {
            erros.add(new CampoInvalido("size", "O tamanho da página deve estar entre 1 e " + TAMANHO_MAXIMO + "."));
        }
        if (!erros.isEmpty()) {
            throw new ValidationException(erros);
        }

        return Optional.of(PageRequest.of(pagina, tamanho, Sort.by("id")));
    }

    /** Monta a resposta de uma listagem: página se pedida, lista completa caso contrário. */
    public static <T> ResponseEntity<?> responder(
            Integer page,
            Integer size,
            Function<Pageable, Page<T>> listarPagina,
            Supplier<List<T>> listarTodos
    ) {
        return resolver(page, size)
                .<ResponseEntity<?>>map(pageable -> ResponseEntity.ok(Pagina.de(listarPagina.apply(pageable))))
                .orElseGet(() -> ResponseEntity.ok(listarTodos.get()));
    }
}
