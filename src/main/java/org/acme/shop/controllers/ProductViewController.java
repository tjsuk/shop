package org.acme.shop.controllers;

import jakarta.validation.Valid;
import org.acme.shop.entities.Product;
import org.acme.shop.services.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * Traditional Spring MVC controller responsible for
 * rendering HTML pages using Thymeleaf templates.
 *
 * Unlike a REST controller, this controller does NOT
 * return JSON data.
 *
 * Instead, it returns the names of Thymeleaf template
 * files found inside:
 *
 * src/main/resources/templates
 *
 * Example:
 *
 * return "products";
 *
 * resolves to:
 *
 * templates/products.html
 *
 * This controller provides complete CRUD support:
 *
 * CREATE
 * READ
 * UPDATE
 * DELETE
 *
 * using:
 *
 * - Spring MVC
 * - Thymeleaf
 * - Spring Data JPA
 * - Bean Validation
 */
@Controller

/**
 * Base URL mapping for all product-related
 * web pages.
 *
 * All routes inside this controller begin with:
 *
 * /web/products
 */
@RequestMapping("/web/products")
public class ProductViewController {

    /**
     * Service layer dependency.
     *
     * The controller delegates business logic
     * to ProductService rather than interacting
     * directly with the repository layer.
     *
     * This follows proper layered architecture.
     */
    private final ProductService service;

    /**
     * Constructor injection.
     *
     * Spring automatically injects the
     * ProductService bean.
     *
     * Constructor injection is preferred because:
     *
     * - Dependencies become immutable
     * - Easier testing
     * - Better design
     * - Avoids field injection issues
     */
    public ProductViewController(
            ProductService service
    ) {

        this.service = service;
    }

    /**
     * GET /web/products
     *
     * Displays all products.
     *
     * Example:
     *
     * http://localhost:8080/web/products
     *
     * The Model object transfers data from
     * the controller to the Thymeleaf view.
     */
    @GetMapping
    public String getProducts(Model model) {

        /**
         * Adds the list of products to the model.
         *
         * Inside Thymeleaf:
         *
         * ${products}
         *
         * becomes accessible.
         */
        model.addAttribute(
                "products",
                service.findAllProducts()
        );

        /**
         * Returns:
         *
         * templates/products.html
         */
        return "products";
    }

    /**
     * GET /web/products/{id}
     *
     * Displays a single product.
     *
     * Example:
     *
     * /web/products/1
     */
    @GetMapping("/{id}")
    public String getProduct(

            /**
             * Reads the product ID from the URL.
             */
            @PathVariable Long id,

            /**
             * Used to pass data to the view.
             */
            Model model
    ) {

        /**
         * Attempts to retrieve the product.
         *
         * If no product exists:
         *
         * IllegalArgumentException is thrown.
         */
        Product product =
                service.findProductById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Product not found"
                                ));

        /**
         * Adds the product to the model.
         */
        model.addAttribute(
                "product",
                product
        );

        /**
         * Returns:
         *
         * templates/product-details.html
         */
        return "product-details";
    }

    /**
     * GET /web/products/new
     *
     * Displays an empty form used for
     * creating a new product.
     */
    @GetMapping("/new")
    public String createForm(Model model) {

        /**
         * Adds an empty Product object.
         *
         * Thymeleaf uses this object for:
         *
         * form binding
         *
         * Example:
         *
         * th:object="${product}"
         */
        model.addAttribute(
                "product",
                new Product()
        );

        /**
         * Returns:
         *
         * templates/product-form.html
         */
        return "product-form";
    }

    /**
     * GET /web/products/{id}/edit
     *
     * Displays a populated form for editing
     * an existing product.
     *
     * Example:
     *
     * /web/products/5/edit
     *
     * The same Thymeleaf form is reused for:
     *
     * - CREATE
     * - UPDATE
     *
     * This is a common enterprise MVC pattern.
     */
    @GetMapping("/{id}/edit")
    public String editForm(

            @PathVariable Long id,

            Model model
    ) {

        /**
         * Retrieves the existing product.
         */
        Product product = service
                .findProductById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Invalid product ID"
                        ));

        /**
         * Adds the existing product to the model.
         *
         * Thymeleaf automatically pre-populates
         * the form fields.
         */
        model.addAttribute(
                "product",
                product
        );

        /**
         * Reuses:
         *
         * templates/product-form.html
         */
        return "product-form";
    }

    /**
     * POST /web/products
     *
     * Handles BOTH:
     *
     * CREATE
     * UPDATE
     *
     * operations.
     *
     * Spring Data JPA determines whether to:
     *
     * INSERT
     * or
     * UPDATE
     *
     * based on whether the entity ID exists.
     */
    @PostMapping
    public String saveProduct(

            /**
             * Reads form data and maps it
             * into a Product object.
             *
             * @Valid triggers validation rules
             * defined inside the Product entity.
             *
             * Example:
             *
             * @NotBlank
             * @Positive
             */
            @Valid @ModelAttribute Product product,

            /**
             * Stores validation errors.
             *
             * Must appear immediately after
             * the validated object parameter.
             */
            BindingResult result
    ) {

        /**
         * If validation errors exist:
         *
         * Redisplay the form.
         */
        if (result.hasErrors()) {

            return "product-form";
        }

        /**
         * Saves the product using the
         * service layer.
         */
        service.saveProduct(product);

        /**
         * Redirects the browser to:
         *
         * /web/products
         *
         * Prevents duplicate submissions
         * if the user refreshes the page.
         *
         * This pattern is known as:
         *
         * PRG
         *
         * Post Redirect Get
         */
        return "redirect:/web/products";
    }

    /**
     * POST /web/products/{id}/delete
     *
     * Deletes a product.
     *
     * Example:
     *
     * /web/products/10/delete
     *
     * POST is used instead of GET because:
     *
     * - GET should never modify data
     * - Prevents accidental deletions
     * - Follows HTTP standards
     * - Improves security
     */
    @PostMapping("/{id}/delete")
    public String deleteProduct(

            @PathVariable Long id
    ) {

        /**
         * Delegates deletion to the service layer.
         */
        service.deleteProduct(id);

        /**
         * Redirect back to the products page.
         */
        return "redirect:/web/products";
    }
}