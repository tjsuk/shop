package org.acme.shop.services;

import org.acme.shop.dao.ProductRepository;
import org.acme.shop.entities.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Service layer responsible for handling
 * business logic related to products.
 *
 * The service layer sits between:
 *
 * Controller Layer
 *        ↓
 * Service Layer
 *        ↓
 * Repository Layer
 *
 * Controllers should delegate business logic
 * to services rather than interacting directly
 * with repositories.
 */
@Service

/**
 * Enables transaction management.
 *
 * All public methods automatically execute
 * within a database transaction.
 *
 * If an error occurs during a transaction,
 * changes can be rolled back automatically.
 */
@Transactional
public class ProductService {

    /**
     * Repository dependency used for
     * database access operations.
     */
    private final ProductRepository productRepository;

    /**
     * Constructor injection.
     *
     * Spring automatically injects the
     * ProductRepository dependency.
     */
    public ProductService(
            ProductRepository repository
    ) {

        this.productRepository = repository;
    }

    /**
     * Inserts starter data into the database.
     *
     * This method only inserts data if the
     * database is currently empty.
     *
     * Useful for:
     *
     * demonstrations
     * development
     * testing
     * student exercises
     */
    public void initialiseDatabase() {

        /**
         * Prevent duplicate data insertion.
         */
        if (productRepository.count() == 0) {

            /**
             * saveAll() inserts multiple entities.
             */
            productRepository.saveAll(

                    List.of(

                            new Product(
                                    "AMD Ryzen 7 5800X Processor",
                                    BigDecimal.valueOf(199.00)
                            ),

                            new Product(
                                    "Gigabyte GeForce RTX 5060 Ti Gaming OC 16G Graphics Card",
                                    BigDecimal.valueOf(531.97)
                            ),

                            new Product(
                                    "KingSpec 1TB NVMe SSD PCIe Gen4 x4",
                                    BigDecimal.valueOf(122.99)
                            ),

                            new Product(
                                    "Corsair Vengeance LPX 16GB DDR4-3200 Memory",
                                    BigDecimal.valueOf(379.99)
                            )
                    )
            );
        }
    }

    /**
     * Returns all products from the database.
     */
    public List<Product> findAllProducts() {

        return productRepository.findAll();
    }

    /**
     * Returns a single product using its ID.
     *
     * Optional is used because the product
     * may not exist.
     *
     * Example:
     *
     * Existing ID   -> Product returned
     * Missing ID    -> Optional.empty()
     */
    public Optional<Product> findProductById(
            Long id
    ) {

        return productRepository.findById(id);
    }

    /**
     * Returns all products greater than or equal
     * to the supplied minimum price.
     *
     * Example:
     *
     * min = 100
     *
     * Returns all products costing £100 or more.
     */
    public List<Product> findProductsWithMinPrice(
            BigDecimal min
    ) {

        return productRepository
                .findByPriceGreaterThanEqual(min);
    }

    /**
     * Inserts or updates a product.
     *
     * Spring Data JPA automatically determines:
     *
     * INSERT -> if ID is null
     * UPDATE -> if ID already exists
     *
     * This behaviour is provided by:
     *
     * JpaRepository.save()
     */
    public Product saveProduct(
            Product product
    ) {

        return productRepository.save(product);
    }

    /**
     * Deletes a product entity.
     *
     * The entire object is supplied rather
     * than just the ID.
     */
    public void deleteProduct(
            Product product
    ) {

        productRepository.delete(product);
    }

    /**
     * Deletes a product using its ID.
     */
    public void deleteProductById(
            Long id
    ) {

        productRepository.deleteById(id);
    }

    /**
     * Deletes ALL products from the database.
     *
     * Useful for:
     *
     * testing
     * resetting data
     * demonstrations
     */
    public void deleteAllProducts() {

        productRepository.deleteAll();
    }

    /**
     * Returns the total number of products
     * currently stored in the database.
     *
     * Example:
     *
     * 4
     */
    public long countProducts() {

        return productRepository.count();
    }
}