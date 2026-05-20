package org.acme.shop.entities;

import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Validation tests for the Product entity.
 *
 * These tests verify that Jakarta Validation
 * annotations applied to Product fields
 * behave correctly.
 *
 * The tests use Spring Boot to automatically
 * configure a Validator instance.
 */
@SpringBootTest
class ProductTest {

    /**
     * Validator dependency provided by Spring.
     *
     * Used to manually validate Product objects.
     */
    @Autowired
    private Validator validator;

    /**
     * Tests a valid Product object.
     *
     * Product values:
     * - valid name
     * - positive price
     *
     * Expected result:
     * No validation violations.
     *
     * Verifies:
     * - @NotBlank passes
     * - @DecimalMin passes
     */
    @Test
    void validProduct() {

        /**
         * Creates a valid product.
         */
        Product product =
                new Product(
                        "Valid Product",
                        BigDecimal.valueOf(100.00)
                );

        /**
         * Performs validation.
         */
        var violations =
                validator.validate(product);

        /**
         * Confirms no validation errors exist.
         */
        assertTrue(
                violations.isEmpty()
        );
    }

    /**
     * Tests validation failure caused by
     * an invalid product name.
     *
     * Product name contains only whitespace.
     *
     * Expected result:
     * Validation violations exist.
     *
     * Verifies:
     * - @NotBlank rejects blank values
     */
    @Test
    void invalidProduct() {

        /**
         * Creates product with invalid name.
         */
        Product product =
                new Product(
                        "  ",
                        BigDecimal.valueOf(100.00)
                );

        /**
         * Performs validation.
         */
        var violations =
                validator.validate(product);

        /**
         * Confirms validation errors exist.
         */
        assertFalse(
                violations.isEmpty()
        );

        /**
         * Prints validation messages
         * to the console.
         *
         * Useful for debugging and learning.
         */
        violations.forEach(System.out::println);
    }

    /**
     * Tests validation failure caused by
     * a negative price.
     *
     * Expected result:
     * Exactly one validation violation.
     *
     * Verifies:
     * - @DecimalMin rejects negative values
     */
    @Test
    void invalidPrice() {

        /**
         * Creates product with invalid price.
         */
        Product product =
                new Product(
                        "Valid Product",
                        BigDecimal.valueOf(-100.00)
                );

        /**
         * Performs validation.
         */
        var violations =
                validator.validate(product);

        /**
         * Confirms exactly one validation error exists.
         */
        assertEquals(
                1,
                violations.size()
        );

        /**
         * Displays validation errors
         * in the console.
         */
        violations.forEach(System.out::println);
    }
}