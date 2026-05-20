package org.acme.shop.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Global REST exception handler.
 *
 * @RestControllerAdvice applies to all REST controllers
 * within the application.
 *
 * This class centralises exception handling logic so that
 * controllers remain clean and focused only on processing
 * requests.
 *
 * Extending ResponseEntityExceptionHandler provides access
 * to Spring MVC's built-in exception handling support.
 */
@RestControllerAdvice
public class ProductControllerAdvice
        extends ResponseEntityExceptionHandler {

    /**
     * Handles ProductMinimumPriceException.
     *
     * This exception is thrown when a product search or
     * request uses an invalid minimum price value.
     *
     * Example:
     *
     * /products?min=-1.00
     *
     * Instead of allowing the application to fail with a
     * stack trace, this method converts the exception into
     * a clean HTTP 400 response.
     *
     * ProblemDetail is part of the modern RFC 7807
     * standard for REST API error responses.
     *
     * HTTP Status:
     * 400 Bad Request
     */
    @ExceptionHandler(ProductMinimumPriceException.class)
    public ProblemDetail handleProductMinimumPriceException(
            ProductMinimumPriceException ex
    ) {

        /**
         * Creates a ProblemDetail response object
         * containing:
         *
         * - HTTP status code
         * - Human-readable error message
         */
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );
    }
}
