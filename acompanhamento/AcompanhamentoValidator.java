package io.github.go.gerenciamento_adocoes.acompanhamento;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import io.github.go.gerenciamento_adocoes.acompanhamento.dto.AcompanhamentoForm;
import io.github.go.gerenciamento_adocoes.common.validation.CampoInvalido;
import io.github.go.gerenciamento_adocoes.common.validation.ValidationResult;

@Component
public class AcompanhamentoValidator {

    private static final Set<String> ALLOWED_MEDIA_TYPES = Set.of(
            "image/jpeg", "image/png", "image/gif", "image/webp",
            "video/mp4", "video/webm", "video/quicktime"
    );
    private static final long MAX_FILE_SIZE = 5L * 1024 * 1024;
    private static final int MAX_FILES = 5;

    public ValidationResult validar(AcompanhamentoForm form, boolean hasUploads) {
        var result = ValidationResult.novo();

        if (form.getPeriodDays() == null
                || !java.util.Set.of(7, 30, 60, 90, 180, 365).contains(form.getPeriodDays())) {
            result.add(new CampoInvalido(
                    "periodDays",
                    "The period must be 7, 30, 60, 90, 180, or 365 days."
            ));
        }

        boolean hasAnswer = form.getAnimalWithAdopter() != null
                || form.getAnimalHealthy() != null
                || form.getAnimalReturned() != null
                || form.getAnimalNotFound() != null;
        boolean hasMethod = form.getMethods() != null && !form.getMethods().isEmpty();
        boolean hasNote = form.getNotes() != null && !form.getNotes().isBlank();
        if (!hasAnswer && !hasMethod && !hasUploads && !hasNote) {
            result.add(new CampoInvalido(
                    "followUp",
                    "Provide at least one answer, follow-up method, uploaded file, or note."
            ));
        }

        if (Boolean.TRUE.equals(form.getAnimalReturned())
                && Boolean.TRUE.equals(form.getAnimalWithAdopter())) {
            result.add(new CampoInvalido(
                    "animalWithAdopter",
                    "A returned animal cannot be with the adopter."
            ));
        }

        if (Boolean.TRUE.equals(form.getAnimalNotFound())
                && Boolean.TRUE.equals(form.getAnimalWithAdopter())) {
            result.add(new CampoInvalido(
                    "animalWithAdopter",
                    "An animal that was not found cannot be with the adopter."
            ));
        }

        if ((Boolean.TRUE.equals(form.getAnimalReturned())
                || Boolean.TRUE.equals(form.getAnimalNotFound()))
                && (form.getNotes() == null || form.getNotes().isBlank())) {
            result.add(new CampoInvalido(
                    "notes",
                    "Describe what happened when the animal was returned or could not be found."
            ));
        }

        if (form.getNotes() != null && form.getNotes().length() > 10000) {
            result.add(new CampoInvalido("notes", "Notes must not exceed 10000 characters."));
        }

        return result;
    }

    public ValidationResult validarArquivos(List<MultipartFile> files) {
        var result = ValidationResult.novo();
        if (files == null || files.isEmpty()) {
            return result;
        }
        if (files.size() > MAX_FILES) {
            result.add(new CampoInvalido("files", "Upload no more than 5 files per follow-up."));
        }
        for (var file : files) {
            if (file == null || file.isEmpty()) {
                result.add(new CampoInvalido("files", "Uploaded files must not be empty."));
                continue;
            }
            if (file.getSize() > MAX_FILE_SIZE) {
                result.add(new CampoInvalido("files", "Each file must be no larger than 5 MB."));
            }
            if (file.getContentType() == null || !ALLOWED_MEDIA_TYPES.contains(file.getContentType())) {
                result.add(new CampoInvalido(
                        "files",
                        "Only JPEG, PNG, GIF, WebP, MP4, WebM, and QuickTime files are allowed."
                ));
            }
            if (file.getOriginalFilename() == null || file.getOriginalFilename().isBlank()) {
                result.add(new CampoInvalido("files", "Each file must have a filename."));
            }
        }
        return result;
    }
}
