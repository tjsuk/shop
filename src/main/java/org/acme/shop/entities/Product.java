package org.acme.shop.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

/**
 * Product entity class.
 *
 * This class represents a product stored
 * inside the database.
 *
 * @Entity tells Spring Data JPA and Hibernate
 * that this class should be mapped to a
 * database table.
 *
 * By default, the table name becomes:
 *
 * product
 */
@Entity
public class Product {

    /**
     * Primary key field.
     *
     * Every product requires a unique ID.
     *
     * @Id marks this field as the table's
     * primary key.
     */
    @Id

    /**
     * Automatically generates ID values.
     *
     * GenerationType.IDENTITY allows the
     * database to generate IDs automatically.
     *
     * Example:
     *
     * 1
     * 2
     * 3
     */
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    /**
     * Product name.
     *
     * @NotBlank prevents:
     *
     * null
     * ""
     * whitespace-only values
     *
     * Validation errors occur if invalid
     * data is submitted.
     */
    @NotBlank
    private String name;

    /**
     * Product price.
     *
     * BigDecimal is used for monetary values
     * because floating-point types such as
     * double can introduce rounding errors.
     *
     * @DecimalMin ensures values cannot
     * be negative.
     *
     * Valid examples:
     *
     * 0.00
     * 19.99
     * 999.99
     *
     * Invalid examples:
     *
     * -1.00
     * -100.50
     */
    @DecimalMin("0.00")
    private BigDecimal price;

    /**
     * Default constructor.
     *
     * Required by JPA/Hibernate.
     *
     * Hibernate uses this constructor when
     * reconstructing objects from database rows.
     */
    public Product() {
    }

    /**
     * Convenience constructor.
     *
     * Allows products to be created quickly.
     *
     * Example:
     *
     * new Product(
     *      "Laptop",
     *      BigDecimal.valueOf(999.99)
     * );
     */
    public Product(
            String name,
            BigDecimal price
    ) {

        this.name = name;
        this.price = price;
    }

    /**
     * Returns the product ID.
     */
    public Long getId() {

        return id;
    }

    /**
     * Sets the product ID.
     *
     * Normally managed automatically by JPA.
     */
    public void setId(Long id) {

        this.id = id;
    }

    /**
     * Returns the product name.
     */
    public String getName() {

        return name;
    }

    /**
     * Updates the product name.
     */
    public void setName(String name) {

        this.name = name;
    }

    /**
     * Returns the product price.
     */
    public BigDecimal getPrice() {

        return price;
    }

    /**
     * Updates the product price.
     */
    public void setPrice(BigDecimal price) {

        this.price = price;
    }

    /**
     * Converts the object into a readable string.
     *
     * Useful for:
     *
     * debugging
     * logging
     * console output
     */
    @Override
    public String toString() {

        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}