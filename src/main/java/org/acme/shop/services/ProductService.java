package org.acme.shop.services;

import org.acme.shop.dao.ProductRepository;
import org.acme.shop.entities.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository repository) {

        this.productRepository = repository;
    }

    /**
     * Populates the database with starter data.
     */
    public void initialiseDatabase() {

        if (productRepository.count() == 0) {

            productRepository.saveAll(List.of(
                    new Product("AMD Ryzen 7 5800X Processor", BigDecimal.valueOf(199.00)),
                    new Product("Gigabyte GeForce RTX 5060 Ti Gaming OC 16G Graphics Card", BigDecimal.valueOf(531.97)),
                    new Product("KingSpec 1TB NVMe SSD PCIe Gen4 x4", BigDecimal.valueOf(122.99)),
                    new Product("Corsair Vengeance LPX 16GB DDR4-3200 Memory", BigDecimal.valueOf(379.99))))
                    .forEach(System.out::println);
        }
    }

    /**
     * Returns all products.
     */
    public List<Product> findAllProducts() {

        return productRepository.findAll();
    }

    /**
     * Returns a single product.
     */
    public Optional<Product> findProductById(Long id) {

        return productRepository.findById(id);
    }

    /**
     * Returns products with a minimum price.
     */
    public List<Product> findProductsWithMinPrice(BigDecimal min) {

        return productRepository.findByPriceGreaterThanEqual(min);
    }

    /**
     * Inserts a new product.
     */
    public Product insertProduct(Product product) {

        return productRepository.save(product);
    }

    /**
     * Updates an existing product.
     */
    public Optional<Product> updateProduct(Product product) {

        if (!productRepository.existsById(product.getId())) {
            return Optional.empty();
        }

        Product updated = productRepository.save(product);

        return Optional.of(updated);
    }

    /**
     * Deletes a single product.
     */
    public void deleteProductById(Long id) {

        productRepository.deleteById(id);
    }

    /**
     * Deletes all products.
     */
    public void deleteAllProducts() {

        productRepository.deleteAll();
    }

    public long countProducts() {
        return productRepository.count();
    }

    /**
     * Saves a product.
     * Used for both insert and update operations.
     */
    public Product saveProduct(Product product) {

        return productRepository.save(product);
    }

    /**
     * Deletes a product.
     */
    public void deleteProduct(Product product) {

        productRepository.delete(product);
    }
}