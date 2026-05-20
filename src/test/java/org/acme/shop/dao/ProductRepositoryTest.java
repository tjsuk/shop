package org.acme.shop.dao;

import org.acme.shop.entities.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for ProductRepository.
 *
 * These tests verify that Spring Data JPA
 * repository operations work correctly
 * against the database.
 *
 * @SpringBootTest loads the complete
 * Spring Boot application context.
 *
 * @Transactional ensures database changes
 * are rolled back after each test,
 * keeping tests isolated and repeatable.
 */
@SpringBootTest
@Transactional
class ProductRepositoryTest {

    /**
     * Repository dependency injected by Spring.
     */
    @Autowired
    private ProductRepository repository;

    /**
     * Tests the total number of products
     * currently stored in the database.
     *
     * Expected result:
     * 4 products
     *
     * Verifies:
     * - database initialisation worked
     * - repository.count() functions correctly
     */
    @Test
    void countProducts() {

        assertEquals(
                4,
                repository.count()
        );
    }

    /**
     * Tests retrieving an existing product by ID.
     *
     * Expected result:
     * Product is found.
     *
     * Verifies:
     * - repository.findById() works correctly
     * - Optional contains a value
     */
    @Test
    void findProductById() {

        assertTrue(
                repository.findById(1L)
                        .isPresent()
        );
    }

    /**
     * Tests retrieving a product that does not exist.
     *
     * Expected result:
     * Product is NOT found.
     *
     * Verifies:
     * - repository handles missing records correctly
     * - Optional.empty() is returned
     */
    @Test
    void findProductByIdNotFound() {

        assertFalse(
                repository.findById(100L)
                        .isPresent()
        );
    }

    /**
     * Tests retrieving all products.
     *
     * Expected result:
     * 4 products returned.
     *
     * Verifies:
     * - repository.findAll() works correctly
     * - all database records are retrieved
     */
    @Test
    void findAllProducts() {

        assertEquals(
                4,
                repository.findAll().size()
        );

        /**
         * Prints products to the console.
         *
         * Useful for debugging and demonstrations.
         */
        repository.findAll()
                .forEach(System.out::println);
    }

    /**
     * Tests inserting a new product.
     *
     * The test:
     * - creates a Product object
     * - saves it using the repository
     * - verifies insertion succeeded
     *
     * Expected result:
     * - total product count increases to 5
     * - generated ID is not null
     */
    @Test
    void insertProduct() {

        /**
         * Creates a new product entity.
         */
        Product p =
                new Product(
                        "Test Product",
                        BigDecimal.valueOf(100.00)
                );

        /**
         * Saves the product to the database.
         */
        repository.save(p);

        /**
         * assertAll allows multiple assertions
         * to be evaluated together.
         */
        assertAll(

                /**
                 * Verifies product count increased.
                 */
                () -> assertEquals(
                        5,
                        repository.count()
                ),

                /**
                 * Verifies the database generated
                 * a primary key ID.
                 */
                () -> assertNotNull(
                        p.getId()
                )
        );
    }

    /**
     * Tests deleting a single product.
     *
     * Expected result:
     * Product count decreases from 4 to 3.
     *
     * Verifies:
     * - deleteById() works correctly
     * - records are removed from the database
     */
    @Test
    void deleteProduct() {

        /**
         * Deletes product with ID 1.
         */
        repository.deleteById(1L);

        /**
         * Confirms product count decreased.
         */
        assertEquals(
                3,
                repository.count()
        );
    }

    /**
     * Tests deleting ALL products.
     *
     * Expected result:
     * Database becomes empty.
     *
     * Verifies:
     * - deleteAll() works correctly
     * - all records are removed
     */
    @Test
    void deleteAllProducts() {

        /**
         * Removes every product.
         */
        repository.deleteAll();

        /**
         * Verifies database is empty.
         */
        assertEquals(
                0,
                repository.count()
        );
    }
}