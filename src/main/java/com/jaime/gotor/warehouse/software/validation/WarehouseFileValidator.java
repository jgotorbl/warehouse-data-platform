package com.jaime.gotor.warehouse.software.validation;

import com.jaime.gotor.warehouse.software.exception.FileValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * WarehouseFileValidator
 * <br>
 * <code>com.jaime.gotor.warehouse.software.validation.WarehouseFileValidator</code>
 * <br>
 *
 * @author Jaime Gotor Blazquez
 * @since 19 February 2022
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WarehouseFileValidator {

    private final List<String> SUPPORTED_CONTENT_FILES = List.of("application/json");

    /**
     * Checks if file is valid
     * @param file multipart file being validated
     * @throws FileValidationException if file is null, empty or its content type is unsupported.
     */
    public void validateFile(MultipartFile file) throws FileValidationException {
        ValidationErrorMessage message = null;
        if (file == null || file.isEmpty()) {
            message = ValidationErrorMessage.FILE_EMPTY;
        } else if (!SUPPORTED_CONTENT_FILES.contains(file.getContentType())) {
            message = ValidationErrorMessage.INVALID_FILE_CONTENT;
        }
        if (message != null) {
            log.error("Error uploading file {}", message.getMessage());
            throw new FileValidationException(message.getMessage());
        }
    }
}
