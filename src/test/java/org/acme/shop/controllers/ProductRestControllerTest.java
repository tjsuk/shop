package org.acme.shop.controllers;

import org.acme.shop.entities.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Suppresses IntelliJ SQL inspection warnings.
 */
@SuppressWarnings(
        {
                "SqlNoDataSourceInspection",
                "SqlResolve"
        })

/**
 * Loads the full Spring Boot application context.
 *
 * This creates a realistic integration testing
 * environment.
 */
@SpringBootTest

/**
 * Automatically configures MockMvc.
 *
 * MockMvc allows REST endpoints to be tested
 * without deploying the application to a
 * real web server.
 */
@AutoConfigureMockMvc

/**
 * Uses a single test instance for all tests.
 *
 * Useful when sharing helper methods or state.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)

/**
 * Rolls back database changes after each test.
 *
 * Ensures tests remain isolated and repeatable.
 */
@Transactional
class ProductRestControllerTest {

    /**
     * MockMvc dependency used for testing
     * REST API endpoints.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * JdbcClient used for direct SQL access
     * during testing.
     */
    @Autowired
    private JdbcClient jdbcClient;

    /**
     * Converts Java objects into JSON.
     */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Retrieves all product IDs directly
     * from the database.
     *
     * Used by parameterised tests.
     */
    private List<Long> getIds() {

        return jdbcClient.sql("SELECT id FROM product")
                .query(Long.class)
                .list();
    }

    /**
     * Retrieves a single product directly
     * from the database.
     */
    private Product getProduct(Long id) {

        return jdbcClient.sql(
                        "SELECT * FROM product WHERE id = ?"
                )
                .param(id)
                .query(Product.class)
                .single();
    }

    /**
     * Tests retrieving all products.
     *
     * Expected result:
     * HTTP 200 OK
     *
     * Verifies that:
     * - the endpoint exists
     * - requests are processed correctly
     * - the controller returns a successful response
     */
    @Test
    void getAllProducts() throws Exception {

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk());
    }

    /**
     * Tests retrieving products that exist.
     *
     * Uses a parameterised test to automatically
     * test every product ID found in the database.
     *
     * Expected result:
     * HTTP 200 OK
     *
     * Verifies:
     * - valid product IDs are accessible
     * - the endpoint correctly handles path variables
     */
    @ParameterizedTest(name = "Product ID: {0}")
    @MethodSource("getIds")
    void getProductsThatExist(
            Long id
    ) throws Exception {

        mockMvc.perform(
                        get("/products/{id}", id)
                )
                .andExpect(status().isOk());
    }

    /**
     * Tests retrieving a product that does not exist.
     *
     * Expected result:
     * HTTP 404 Not Found
     *
     * Verifies:
     * - invalid IDs are handled correctly
     * - the API returns appropriate error responses
     */
    @Test
    void getProductThatDoesNotExist()
            throws Exception {

        mockMvc.perform(get("/products/999"))
                .andExpect(status().isNotFound());
    }

    /**
     * Tests filtering products using a valid
     * minimum price query parameter.
     *
     * Example:
     * /products?min=5.00
     *
     * Expected result:
     * HTTP 200 OK
     *
     * Verifies:
     * - filtering logic works
     * - validation accepts valid values
     */
    @Test
    void productsWithValidMinPrice()
            throws Exception {

        mockMvc.perform(
                        get("/products?min=5.00")
                )
                .andExpect(status().isOk());
    }

    /**
     * Tests filtering products using an
     * invalid minimum price.
     *
     * Example:
     * /products?min=-1.00
     *
     * Expected result:
     * HTTP 400 Bad Request
     *
     * Verifies:
     * - validation rejects negative values
     * - validation exception handling works
     */
    @Test
    void productsWithInvalidMinPrice()
            throws Exception {

        mockMvc.perform(
                        get("/products?min=-1.00")
                )
                .andExpect(status().isBadRequest());
    }

    /**
     * Tests inserting a new product.
     *
     * Sends a JSON request body using HTTP POST.
     *
     * Expected result:
     * HTTP 201 Created
     *
     * Verifies:
     * - product creation works
     * - JSON request handling works
     * - validation passes correctly
     */
    @Test
    void insertProduct() throws Exception {

        Product product =
                new Product(
                        "Chair",
                        BigDecimal.valueOf(49.99)
                );

        mockMvc.perform(
                        post("/products")
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(
                                        objectMapper
                                                .writeValueAsString(product)
                                )
                )
                .andExpect(status().isCreated());
    }

    /**
     * Tests updating an existing product.
     *
     * The test:
     * - retrieves an existing product
     * - modifies the price
     * - sends an HTTP PUT request
     *
     * Expected result:
     * HTTP 200 OK
     *
     * Verifies:
     * - update logic works
     * - existing entities can be modified
     * - PUT endpoint functions correctly
     */
    @Test
    void updateProduct() throws Exception {

        Product product =
                getProduct(getIds().get(0));

        /**
         * Increases the existing price by 1.
         */
        product.setPrice(
                product.getPrice()
                        .add(BigDecimal.ONE)
        );

        mockMvc.perform(
                        put(
                                "/products/{id}",
                                product.getId()
                        )
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(
                                        objectMapper
                                                .writeValueAsString(product)
                                )
                )
                .andExpect(status().isOk());
    }

    /**
     * Tests deleting a single product.
     *
     * Expected result:
     * HTTP 204 No Content
     *
     * Verifies:
     * - delete endpoint works
     * - products can be removed successfully
     */
    @Test
    void deleteSingleProduct()
            throws Exception {

        List<Long> ids = getIds();

        /**
         * Ensures the database contains products
         * before deletion occurs.
         */
        assertThat(ids)
                .isNotEmpty();

        mockMvc.perform(
                        delete(
                                "/products/{id}",
                                ids.get(0)
                        )
                )
                .andExpect(status().isNoContent());
    }

    /**
     * Tests deleting ALL products.
     *
     * Expected result:
     * HTTP 204 No Content
     *
     * Verifies:
     * - bulk deletion works
     * - DELETE endpoint functions correctly
     */
    @Test
    void deleteAllProducts()
            throws Exception {

        mockMvc.perform(delete("/products"))
                .andExpect(status().isNoContent());
    }
}
