package org.acme.shop.dao;

import org.acme.shop.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByPriceGreaterThanEqual(
            BigDecimal min
    );
}
