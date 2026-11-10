package io.github.go.gerenciamento_adocoes.acompanhamento.mapper;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import io.github.go.gerenciamento_adocoes.acompanhamento.dto.AcompanhamentoDetalhes;
import io.github.go.gerenciamento_adocoes.acompanhamento.dto.MediaFileDetails;
import io.github.go.gerenciamento_adocoes.acompanhamento.model.AcompanhamentoEntity;
import io.github.go.gerenciamento_adocoes.acompanhamento.model.StoredMediaFile;
import io.github.go.gerenciamento_adocoes.acompanhamento.persistence.MeiosAcompanhamentoConverter;

@Component
public class AcompanhamentoMapper {

    private final MeiosAcompanhamentoConverter meiosConverter;
    private final ObjectMapper objectMapper;

    public AcompanhamentoMapper(
            MeiosAcompanhamentoConverter meiosConverter,
            ObjectMapper objectMapper
    ) {
        this.meiosConverter = meiosConverter;
        this.objectMapper = objectMapper;
    }

    public AcompanhamentoDetalhes toDetalhes(AcompanhamentoEntity entity) {
        var adoption = entity.getAdoption();
        var mediaFiles = readMediaFiles(entity);
        var mediaDetails = new ArrayList<MediaFileDetails>(mediaFiles.size());
        for (int index = 0; index < mediaFiles.size(); index++) {
            var media = mediaFiles.get(index);
            mediaDetails.add(new MediaFileDetails(
                    index,
                    media.fileName(),
                    media.contentType(),
                    (long) Base64.getDecoder().decode(media.data()).length,
                    "/acompanhamentos/" + entity.getId() + "/media/" + index
            ));
        }

        return new AcompanhamentoDetalhes(
                entity.getId(),
                adoption.getId(),
                adoption.getAdotante().getId(),
                adoption.getAdotante().getName(),
                adoption.getAnimal().getId(),
                adoption.getAnimal().getName(),
                entity.getPeriodDays(),
                entity.getPerformedAt(),
                entity.getAnimalWithAdopter(),
                entity.getAnimalHealthy(),
                entity.getAnimalReturned(),
                entity.getAnimalNotFound(),
                Set.copyOf(meiosConverter.convertToEntityAttribute(entity.getMethodsJson())),
                List.copyOf(mediaDetails),
                entity.getNotes()
        );
    }

    public List<StoredMediaFile> readMediaFiles(AcompanhamentoEntity entity) {
        try {
            if (entity.getMediaFiles() == null || entity.getMediaFiles().isNull()) {
                return List.of();
            }
            return objectMapper.readerForListOf(StoredMediaFile.class).readValue(entity.getMediaFiles());
        } catch (Exception exception) {
            throw new IllegalStateException("Could not read uploaded media from the database.", exception);
        }
    }
}
