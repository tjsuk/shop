package org.acme.shop;

import org.acme.shop.services.ProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Main entry point for the Spring Boot application.
 *
 * @SpringBootApplication is a convenience annotation
 * that combines:
 *
 * @Configuration
 * @EnableAutoConfiguration
 * @ComponentScan
 *
 * Together these enable Spring Boot auto-configuration
 * and component discovery.
 */
@SpringBootApplication
public class ShopApplication {

    /**
     * Application starting point.
     *
     * SpringApplication.run():
     *
     * - Starts the Spring container
     * - Creates the application context
     * - Configures embedded Tomcat
     * - Scans for Spring components
     * - Starts the web server
     *
     * args contains command-line arguments.
     */
    public static void main(String[] args) {

        SpringApplication.run(
                ShopApplication.class,
                args
        );
    }

    /**
     * CommandLineRunner bean.
     *
     * Runs automatically AFTER the Spring
     * application has fully started.
     *
     * Useful for:
     *
     * - database initialisation
     * - startup tasks
     * - loading sample data
     * - diagnostics
     * - demonstrations
     */
    @Bean
    public CommandLineRunner initialiseDatabase(

            /**
             * ProductService dependency injected
             * automatically by Spring.
             */
            ProductService service
    ) {

        /**
         * Lambda expression implementation.
         *
         * Executes:
         *
         * service.initialiseDatabase()
         *
         * once the application starts.
         */
        return args -> service.initialiseDatabase();
    }
}