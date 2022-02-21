package com.jaime.gotor.warehouse.software.exception.handler;

import com.jaime.gotor.warehouse.software.exception.DataValidationException;
import com.jaime.gotor.warehouse.software.exception.NotEnoughFundsException;
import com.jaime.gotor.warehouse.software.exception.NotEnoughQuantityException;
import com.jaime.gotor.warehouse.software.exception.ProductNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * ProductsControllerExceptionHandler
 * <br>
 * <code>com.jaime.gotor.warehouse.software.exception.handler.ProductsControllerExceptionHandler</code>
 * <br>
 *
 * @author Jaime Gotor Blazquez
 * @since 20 February 2022
 */
@ControllerAdvice(basePackageClasses = com.jaime.gotor.warehouse.software.controller.ProductsController.class)
public class ProductsControllerExceptionHandler {

    /**
     * Handles ProductNotFoundException, NotEnoughQuantityException, NotEnoughFundsException
     * and DataValidationException thrown during ProductsController calls
     * @param e exception thrown
     * @return ResponseEntity with HttpStatus.BadRequest and an error message
     */
    @ExceptionHandler(value = {
            ProductNotFoundException.class,
            NotEnoughQuantityException.class,
            NotEnoughFundsException.class,
            DataValidationException.class
    })
    public ResponseEntity<Object> customException(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    /**
     * Handles IllegalArgumentException and RuntimeException thrown during ProductsController calls
     * @param e exception thrown
     * @return ResponseEntity with interval server error and an error message
     */
    @ExceptionHandler(value = {IllegalArgumentException.class, RuntimeException.class})
    public ResponseEntity<Object> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.internalServerError().body(e.getMessage());
    }

}
