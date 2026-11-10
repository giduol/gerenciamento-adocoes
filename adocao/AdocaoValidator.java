package io.github.go.gerenciamento_adocoes.adocao;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import io.github.go.gerenciamento_adocoes.adocao.dto.AdocaoForm;
import io.github.go.gerenciamento_adocoes.common.validation.CampoInvalido;
import io.github.go.gerenciamento_adocoes.common.validation.ValidationResult;

@Component
public class AdocaoValidator {

    private static final long MAX_DOCUMENT_SIZE = 5L * 1024 * 1024;
    private static final byte[] PDF_SIGNATURE = {'%', 'P', 'D', 'F', '-'};

    public ValidationResult validar(AdocaoForm form, Long id){
        var result = ValidationResult.novo();

        if (form.getOngId() == null) {
            result.add(new CampoInvalido("ongId", "O ID da ONG é obrigatório."));
        }
        if (form.getAnimalId() == null) {
            result.add(new CampoInvalido("animalId", "O ID do animal é obrigatório."));
        }
        if (form.getAdotanteId() == null) {
            result.add(new CampoInvalido("adotanteId", "O ID do adotante é obrigatório."));
        }

        return result;
    };

    public ValidationResult validarDocumento(MultipartFile document) {
        var result = ValidationResult.novo();
        if (document == null || document.isEmpty()) {
            return result;
        }

        if (document.getSize() > MAX_DOCUMENT_SIZE) {
            result.add(new CampoInvalido("document", "The PDF must not exceed 5 MB."));
            return result;
        }

        if (document.getContentType() != null
                && !document.getContentType().equalsIgnoreCase("application/pdf")
                && !document.getContentType().equalsIgnoreCase("application/octet-stream")) {
            result.add(new CampoInvalido("document", "The uploaded file must be a PDF."));
            return result;
        }

        try {
            byte[] content = document.getBytes();
            boolean isPdf = content.length >= PDF_SIGNATURE.length;
            for (int index = 0; isPdf && index < PDF_SIGNATURE.length; index++) {
                isPdf = content[index] == PDF_SIGNATURE[index];
            }
            if (!isPdf) {
                result.add(new CampoInvalido("document", "The uploaded file is not a valid PDF."));
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Could not read the uploaded adoption PDF.", exception);
        }
        return result;
    }
}
