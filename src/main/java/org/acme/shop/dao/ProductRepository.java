package org.acme.shop.dao;

import org.acme.shop.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repository interface responsible for database
 * access operations for Product entities.
 *
 * Spring Data JPA automatically generates the
 * implementation at runtime.
 *
 * This interface acts as the data access layer
 * of the application.
 *
 * The repository communicates directly with
 * the database.
 */
public interface ProductRepository
        extends JpaRepository<Product, Long> {

    /**
     * JpaRepository automatically provides many
     * useful database operations including:
     *
     * save()
     * findById()
     * findAll()
     * deleteById()
     * deleteAll()
     * existsById()
     * count()
     *
     * Because ProductRepository extends
     * JpaRepository, these methods are inherited
     * automatically.
     */

    /**
     * Derived query method.
     *
     * Spring Data JPA analyses the method name
     * and automatically generates the SQL query.
     *
     * Method breakdown:
     *
     * findBy               -> SELECT query
     * Price                -> product price field
     * GreaterThanEqual     -> >= comparison
     *
     * Equivalent SQL:
     *
     * SELECT *
     * FROM product
     * WHERE price >= ?
     *
     * Example usage:
     *
     * findByPriceGreaterThanEqual(
     *      BigDecimal.valueOf(100)
     * );
     *
     * This would return all products costing
     * £100 or more.
     */
    List<Product> findByPriceGreaterThanEqual(

            /**
             * Minimum product price.
             */
            BigDecimal min
    );
}