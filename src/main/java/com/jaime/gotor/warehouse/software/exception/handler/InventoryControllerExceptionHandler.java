package com.jaime.gotor.warehouse.software.exception.handler;

import com.jaime.gotor.warehouse.software.exception.DataValidationException;
import com.jaime.gotor.warehouse.software.exception.FileValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

/**
 * FileValidationExceptionHandler
 * <br>
 * <code>com.jaime.gotor.warehouse.software.exception.handler.FileValidationExceptionHandler</code>
 * <br>
 *
 * @author Jaime Gotor Blazquez
 * @since 19 February 2022
 */
@ControllerAdvice(basePackageClasses = com.jaime.gotor.warehouse.software.controller.InventoryController.class)
public class InventoryControllerExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles FileValidationException, DataValidationException and IOException thrown during InventoryController calls
     * @param e exception thrown
     * @return ResponseEntity with HttpStatus.BadRequest and an error message
     */
    @ExceptionHandler(value = {FileValidationException.class, DataValidationException.class, IOException.class})
    protected ResponseEntity<Object> handleCheckedException(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    /**
     * Handles IllegalArgumentException thrown in InventoryController
     * @param e IllegalArgumentException thrown
     * @return ResponseEntity with HttpStatus.BadRequest and an error message
     */
    @ExceptionHandler(value = IllegalArgumentException.class)
    protected ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.internalServerError().body(e.getMessage());
    }

    /**
     * Handles RuntimeException thrown in InventoryController
     * @param e RuntimeException
     * @return ResponseEntity with interval server error and an error message
     */
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.internalServerError().body(e.getMessage());
    }

}
