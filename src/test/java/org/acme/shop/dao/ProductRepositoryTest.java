package org.acme.shop.dao;

import org.acme.shop.entities.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ProductRepositoryTest {

    @Autowired
    private ProductRepository repository;

    @Test
    void countProducts() {
        assertEquals(4, repository.count());
    }

    @Test
    void findProductById() {
        assertTrue(repository.findById(1L).isPresent());
    }

    @Test
    void findProductByIdNotFound() {
        assertFalse(repository.findById(100L).isPresent());
    }

    @Test
    void findAllProducts() {
        assertEquals(4, repository.findAll().size());
        repository.findAll().forEach(System.out::println);
    }

    @Test
    void insertProduct() {
        Product p = (new Product("Test Product", BigDecimal.valueOf(100.00)));
        repository.save(p);
        assertAll(
                () -> assertEquals(5, repository.count()),
                () -> assertNotNull(p.getId())
        );
    }

    @Test
    void deleteProduct() {
        repository.deleteById(1L);
        assertEquals(3, repository.count());
    }

    @Test
    void deleteAllProducts() {
        repository.deleteAll();
        assertEquals(0, repository.count());
    }
}