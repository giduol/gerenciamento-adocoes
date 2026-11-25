package io.github.go.gerenciamento_adocoes.common.config;

import java.util.Comparator;
import java.util.List;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.tags.Tag;

@Configuration
public class OpenApiConfiguration {

    /** Ordem em que os módulos aparecem no Swagger UI. Tags fora da lista vão para o final. */
    private static final List<String> ORDEM_TAGS = List.of(
            "autenticacao",
            "ongs",
            "animais",
            "adotantes",
            "adocoes",
            "acompanhamentos");

    @Bean
    public OpenApiCustomizer ordenarTags() {
        return openApi -> {
            List<Tag> tags = openApi.getTags();
            if (tags == null) {
                return;
            }
            tags.sort(Comparator.comparingInt(tag -> {
                int indice = ORDEM_TAGS.indexOf(tag.getName());
                return indice < 0 ? Integer.MAX_VALUE : indice;
            }));
        };
    }
}
