package io.github.go.gerenciamento_adocoes.acompanhamento.persistence;

import java.util.LinkedHashSet;
import java.util.Set;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.go.gerenciamento_adocoes.acompanhamento.model.MeioAcompanhamento;
import org.springframework.stereotype.Component;

@Component
public class MeiosAcompanhamentoConverter {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final TypeReference<Set<MeioAcompanhamento>> TYPE = new TypeReference<>() {};

    public String convertToDatabaseColumn(Set<MeioAcompanhamento> meios) {
        try {
            return OBJECT_MAPPER.writeValueAsString(meios == null ? Set.of() : meios);
        } catch (Exception exception) {
            throw new IllegalArgumentException("Não foi possível serializar os meios de acompanhamento.", exception);
        }
    }

    public Set<MeioAcompanhamento> convertToEntityAttribute(String meiosJson) {
        if (meiosJson == null || meiosJson.isBlank()) {
            return new LinkedHashSet<>();
        }
        try {
            return new LinkedHashSet<>(OBJECT_MAPPER.readValue(meiosJson, TYPE));
        } catch (Exception exception) {
            throw new IllegalArgumentException("Não foi possível ler os meios de acompanhamento.", exception);
        }
    }
}
