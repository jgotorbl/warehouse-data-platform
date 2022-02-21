package com.jaime.gotor.warehouse.software.exception;

/**
 * NotEnoughQuantityException
 * <br>
 * <code>com.jaime.gotor.warehouse.software.exception.NotEnoughQuantityException</code>
 * <br>
 *
 * @author Jaime Gotor Blazquez
 * @since 20 February 2022
 */
public class NotEnoughQuantityException extends Exception {

    public NotEnoughQuantityException(String message) {
        super(message);
    }
}
