package org.acme.shop.controllers;

import org.acme.shop.entities.Product;
import org.acme.shop.services.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests for ProductViewController.
 *
 * These tests verify:
 * - MVC routing
 * - Thymeleaf view rendering
 * - model population
 * - form handling
 * - redirects
 */
@SuppressWarnings("removal")
@WebMvcTest(ProductViewController.class)
class ProductViewControllerTest {

    /**
     * MockMvc used for MVC endpoint testing.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Mock ProductService.
     *
     * Prevents use of the real database.
     */
    @MockBean
    private ProductService service;

    /**
     * Tests loading the products page.
     *
     * Expected:
     * - HTTP 200 OK
     * - products view returned
     * - products added to the model
     */
    @Test
    void getProducts() throws Exception {

        when(service.findAllProducts())
                .thenReturn(
                        List.of(
                                new Product(
                                        "Laptop",
                                        BigDecimal.valueOf(999.99)
                                ),
                                new Product(
                                        "Keyboard",
                                        BigDecimal.valueOf(49.99)
                                )
                        )
                );

        mockMvc.perform(get("/web/products"))

                .andExpect(status().isOk())

                .andExpect(
                        view().name("products")
                )

                .andExpect(
                        model().attributeExists("products")
                );
    }

    /**
     * Tests loading a single product details page.
     *
     * Expected:
     * - HTTP 200 OK
     * - product-details view returned
     * - product added to the model
     */
    @Test
    void getProduct() throws Exception {

        Product product =
                new Product(
                        "Gaming Mouse",
                        BigDecimal.valueOf(79.99)
                );

        product.setId(1L);

        when(service.findProductById(1L))
                .thenReturn(Optional.of(product));

        mockMvc.perform(
                        get("/web/products/1")
                )

                .andExpect(status().isOk())

                .andExpect(
                        view().name("product-details")
                )

                .andExpect(
                        model().attributeExists("product")
                );
    }

    /**
     * Tests loading the create product form.
     *
     * Expected:
     * - HTTP 200 OK
     * - product-form view returned
     * - empty product object added to model
     */
    @Test
    void createForm() throws Exception {

        mockMvc.perform(
                        get("/web/products/new")
                )

                .andExpect(status().isOk())

                .andExpect(
                        view().name("product-form")
                )

                .andExpect(
                        model().attributeExists("product")
                );
    }

    /**
     * Tests submitting the create product form.
     *
     * Expected:
     * - HTTP 3xx redirect
     * - redirect to /web/products
     * - service.saveProduct() invoked
     */
    @Test
    void saveProduct() throws Exception {

        Product product =
                new Product(
                        "Monitor",
                        BigDecimal.valueOf(199.99)
                );

        when(service.saveProduct(any(Product.class)))
                .thenReturn(product);

        mockMvc.perform(

                        post("/web/products")

                                .param("name", "Monitor")

                                .param("price", "199.99")
                )

                .andExpect(status().is3xxRedirection())

                .andExpect(
                        redirectedUrl("/web/products")
                );

        /**
         * Verifies service method execution.
         */
        verify(service)
                .saveProduct(any(Product.class));
    }
}

