package org.acme.shop.controllers;

import org.acme.shop.entities.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings
        ({
                "SqlNoDataSourceInspection",
                "SqlResolve"
        })
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class ProductRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcClient jdbcClient;

    @Autowired
    private ObjectMapper objectMapper;

    private List<Long> getIds() {

        return jdbcClient.sql("SELECT id FROM product")
                .query(Long.class)
                .list();
    }

    private Product getProduct(Long id) {

        return jdbcClient.sql("SELECT * FROM product WHERE id = ?")
                .param(id)
                .query(Product.class)
                .single();
    }

    @Test
    void getAllProducts() throws Exception {

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk());
    }

    @ParameterizedTest(name = "Product ID: {0}")
    @MethodSource("getIds")
    void getProductsThatExist(Long id) throws Exception {

        mockMvc.perform(get("/products/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    void getProductThatDoesNotExist() throws Exception {

        mockMvc.perform(get("/products/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void productsWithValidMinPrice() throws Exception {

        mockMvc.perform(get("/products?min=5.00"))
                .andExpect(status().isOk());
    }

    @Test
    void productsWithInvalidMinPrice() throws Exception {

        mockMvc.perform(get("/products?min=-1.00"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void insertProduct() throws Exception {

        Product product =
                new Product(
                        "Chair",
                        BigDecimal.valueOf(49.99)
                );

        mockMvc.perform(
                        post("/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(product)
                                )
                )
                .andExpect(status().isCreated());
    }

    @Test
    void updateProduct() throws Exception {

        Product product =
                getProduct(getIds().get(0));

        product.setPrice(
                product.getPrice()
                        .add(BigDecimal.ONE)
        );

        mockMvc.perform(
                        put("/products/{id}", product.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(product)
                                )
                )
                .andExpect(status().isOk());
    }

    @Test
    void deleteSingleProduct() throws Exception {

        List<Long> ids = getIds();

        assertThat(ids)
                .isNotEmpty();

        mockMvc.perform(
                        delete("/products/{id}", ids.get(0))
                )
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteAllProducts() throws Exception {

        mockMvc.perform(delete("/products"))
                .andExpect(status().isNoContent());
    }
}