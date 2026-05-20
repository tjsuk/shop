package org.acme.shop.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import org.acme.shop.entities.Product;
import org.acme.shop.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * REST controller responsible for handling
 * HTTP requests related to products.
 *
 * REST controllers automatically convert Java
 * objects into JSON responses.
 *
 * Example:
 *
 * Java Object  -> JSON
 *
 * Product      -> {"id":1,"name":"Laptop","price":999.99}
 */
@RestController

/**
 * Base URL used for all endpoints inside
 * this controller.
 *
 * All mappings begin with:
 *
 * /products
 */
@RequestMapping("/products")

/**
 * Enables validation for method parameters.
 *
 * This allows annotations such as:
 *
 * @DecimalMin
 *
 * to validate incoming request values.
 */
@Validated
public class ProductRestController {

    /**
     * Service layer dependency.
     *
     * The controller delegates business logic
     * to the ProductService.
     */
    private final ProductService service;

    /**
     * Constructor injection.
     *
     * Spring automatically injects the
     * ProductService dependency.
     */
    public ProductRestController(
            ProductService service
    ) {

        this.service = service;
    }

    /**
     * GET /products
     *
     * Returns all products.
     *
     * Supports optional filtering using:
     *
     * /products?min=100
     *
     * If the min parameter is supplied,
     * only products with prices greater
     * than or equal to the value are returned.
     *
     * Example:
     *
     * /products?min=200
     */
    @GetMapping
    public List<Product> getProducts(

            /**
             * Reads a query parameter from the URL.
             *
             * Example:
             *
             * ?min=100
             *
             * required = false means the parameter
             * is optional.
             */
            @RequestParam(required = false)

            /**
             * Validation annotation.
             *
             * Ensures the value cannot be less than 0.
             *
             * Invalid values trigger validation exceptions.
             */
            @DecimalMin("0.00")
            BigDecimal min
    ) {

        /**
         * If a minimum price is supplied,
         * return filtered products.
         */
        if (min != null) {

            return service.findProductsWithMinPrice(min);
        }

        /**
         * Otherwise return all products.
         */
        return service.findAllProducts();
    }

    /**
     * GET /products/{id}
     *
     * Returns a single product using its ID.
     *
     * Example:
     *
     * /products/1
     */
    @GetMapping("{id}")
    public ResponseEntity<Product> getProductById(

            /**
             * Reads the ID value from the URL.
             */
            @PathVariable Long id
    ) {

        /**
         * ResponseEntity.of(Optional)
         * automatically returns:
         *
         * 200 OK if present
         * 404 Not Found if empty
         */
        return ResponseEntity.of(
                service.findProductById(id)
        );
    }

    /**
     * POST /products
     *
     * Creates a new product.
     *
     * Example request body:
     *
     * {
     *   "name":"Laptop",
     *   "price":999.99
     * }
     */
    @PostMapping

    /**
     * Returns HTTP 201 Created when successful.
     */
    @ResponseStatus(HttpStatus.CREATED)
    public Product insertProduct(

            /**
             * Reads JSON request body data and
             * converts it into a Product object.
             *
             * @Valid triggers validation.
             */
            @Valid @RequestBody Product product
    ) {

        /**
         * Saves the product using the service layer.
         */
        return service.saveProduct(product);
    }

    /**
     * PUT /products/{id}
     *
     * Updates an existing product.
     *
     * Example:
     *
     * PUT /products/1
     */
    @PutMapping("{id}")
    public ResponseEntity<Product> updateProduct(

            /**
             * Product ID from the URL.
             */
            @PathVariable Long id,

            /**
             * Updated product data from the request body.
             */
            @Valid @RequestBody Product product
    ) {

        /**
         * Attempts to locate the product first.
         */
        return service.findProductById(id)

                /**
                 * If found:
                 */
                .map(p -> {

                    /**
                     * Update the existing entity values.
                     */
                    p.setName(product.getName());
                    p.setPrice(product.getPrice());

                    /**
                     * Save updated entity.
                     *
                     * Returns:
                     * 200 OK
                     */
                    return ResponseEntity.ok(
                            service.saveProduct(p)
                    );
                })

                /**
                 * If product not found:
                 *
                 * Returns:
                 * 404 Not Found
                 */
                .orElse(
                        ResponseEntity
                                .<Product>notFound()
                                .build()
                );
    }

    /**
     * DELETE /products/{id}
     *
     * Deletes a single product.
     *
     * Example:
     *
     * DELETE /products/1
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteProduct(

            /**
             * Product ID from the URL.
             */
            @PathVariable Long id
    ) {

        /**
         * Locate the product first.
         */
        return service.findProductById(id)

                /**
                 * If found:
                 */
                .map(p -> {

                    /**
                     * Delete the product.
                     */
                    service.deleteProduct(id);

                    /**
                     * Returns:
                     * 204 No Content
                     */
                    return ResponseEntity
                            .noContent()
                            .<Void>build();
                })

                /**
                 * If product does not exist:
                 *
                 * Returns:
                 * 404 Not Found
                 */
                .orElse(
                        ResponseEntity.notFound().build()
                );
    }

    /**
     * DELETE /products
     *
     * Deletes ALL products from the database.
     */
    @DeleteMapping

    /**
     * Returns:
     * 204 No Content
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllProducts() {

        /**
         * Delegates deletion logic to the service layer.
         */
        service.deleteAllProducts();
    }
}