package org.acme.shop.controllers;

import jakarta.validation.Valid;
import org.acme.shop.entities.Product;
import org.acme.shop.services.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * Traditional Spring MVC controller used for
 * rendering HTML pages using Thymeleaf templates.
 *
 * Unlike @RestController, this controller does NOT
 * return JSON responses.
 *
 * Instead, it returns the names of HTML template files.
 *
 * Example:
 *
 * return "products";
 *
 * maps to:
 *
 * src/main/resources/templates/products.html
 */
@Controller

/**
 * Base URL for all view-based product pages.
 *
 * Example:
 *
 * /web/products
 */
@RequestMapping("/web/products")
public class ProductViewController {

    /**
     * Service layer dependency.
     *
     * The controller delegates business logic
     * to ProductService.
     */
    private final ProductService service;

    /**
     * Constructor injection.
     *
     * Spring automatically injects ProductService.
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
     * The Model object transfers data from
     * the controller to the Thymeleaf view.
     */
    @GetMapping
    public String getProducts(Model model) {

        /**
         * Adds a collection of products to the model.
         *
         * "products" becomes accessible inside the
         * Thymeleaf template.
         *
         * Example:
         *
         * ${products}
         */
        model.addAttribute(
                "products",
                service.findAllProducts()
        );

        /**
         * Returns the Thymeleaf template:
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
         * orElseThrow() throws an exception if
         * the product is not found.
         */
        Product product =
                service.findProductById(id)
                        .orElseThrow();

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
     * Displays the create product form.
     */
    @GetMapping("/new")
    public String createForm(Model model) {

        /**
         * Adds an empty Product object.
         *
         * Thymeleaf uses this object for
         * form binding.
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
     * POST /web/products
     *
     * Handles form submission when creating
     * or updating a product.
     */
    @PostMapping
    public String saveProduct(

            /**
             * Reads form data and maps it
             * into a Product object.
             *
             * @Valid triggers validation rules.
             */
            @Valid Product product,

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
         * Save the product using the service layer.
         */
        service.saveProduct(product);

        /**
         * Redirects the browser to:
         *
         * /web/products
         *
         * Prevents duplicate form submission
         * if the user refreshes the page.
         */
        return "redirect:/web/products";
    }
}
