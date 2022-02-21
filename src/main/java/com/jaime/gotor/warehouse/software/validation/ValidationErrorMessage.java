package com.jaime.gotor.warehouse.software.validation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * ValidationErrorMessage
 * <br>
 * <code>com.jaime.gotor.warehouse.software.validation.ValidationErrorMessage</code>
 * <br>
 *
 * @author Jaime Gotor Blazquez
 * @since 19 February 2022
 */
@Getter
@RequiredArgsConstructor
public enum ValidationErrorMessage {

    FILE_EMPTY("empty_file"),
    INVALID_FILE_CONTENT("invalid_file_content"),
    UNSUPPORTED_FILE_EXTENSION("unsupported_file_extension");

    private final String message;

}
