package org.acme.shop.controllers;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global validation exception handler.
 *
 * @RestControllerAdvice applies to ALL REST controllers
 * within the application.
 *
 * This class centralises exception handling logic so that
 * validation errors can be processed consistently in one place.
 *
 * Without this class, validation exceptions would typically
 * produce large stack traces and generic server error pages.
 *
 * Using a global exception handler helps create cleaner,
 * more user-friendly REST API responses.
 */
@RestControllerAdvice
public class ValidationExceptionHandler {

    /**
     * Handles ConstraintViolationException.
     *
     * This exception is commonly triggered when validation
     * annotations fail.
     *
     * Example:
     *
     * @DecimalMin("0.00")
     *
     * If a user attempts:
     *
     * /products?min=-1.00
     *
     * validation fails and this method handles the exception.
     *
     * @ExceptionHandler tells Spring which exception
     * this method is responsible for processing.
     */
    @ExceptionHandler(
            ConstraintViolationException.class
    )

    /**
     * Returns HTTP 400 Bad Request.
     *
     * This indicates the client supplied invalid data.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleConstraintViolation(

            /**
             * The validation exception thrown by Spring.
             */
            ConstraintViolationException ex
    ) {

        /**
         * Returns the validation error message
         * directly to the client.
         *
         * Example response:
         *
         * getProducts.min:
         * must be greater than or equal to 0.00
         */
        return ex.getMessage();
    }
}