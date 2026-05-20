package org.acme.shop.controllers;

/**
 * Custom runtime exception used when an invalid
 * minimum product price is supplied.
 *
 * Example:
 *
 * /products?min=-1.00
 *
 * Extending RuntimeException means this is an
 * unchecked exception.
 *
 * Unchecked exceptions do not need to be declared
 * using the throws keyword.
 */
public class ProductMinimumPriceException
        extends RuntimeException {

    /**
     * Default constructor.
     *
     * Uses a default error message.
     */
    public ProductMinimumPriceException() {

        this("Minimum price must be greater than zero");
    }

    /**
     * Constructor allowing a custom error message.
     *
     * Calls the parent RuntimeException constructor.
     */
    public ProductMinimumPriceException(
            String message
    ) {

        super(message);
    }

    /**
     * Constructor that includes the invalid value
     * supplied by the user.
     *
     * Example output:
     *
     * Minimum price must be greater than zero,
     * but was -1.0
     */
    public ProductMinimumPriceException(
            double minPrice
    ) {

        this(
                "Minimum price must be greater than zero, but was "
                        + minPrice
        );
    }
}