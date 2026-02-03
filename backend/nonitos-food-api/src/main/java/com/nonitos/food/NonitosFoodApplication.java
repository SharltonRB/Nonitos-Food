package com.nonitos.food;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for Nonito's Food API.
 * 
 * <p>This is the entry point for the Spring Boot application. The @SpringBootApplication
 * annotation enables auto-configuration, component scanning, and configuration properties.</p>
 * 
 * <h2>What @SpringBootApplication does:</h2>
 * <ul>
 *   <li><b>@Configuration:</b> Marks this as a configuration class</li>
 *   <li><b>@EnableAutoConfiguration:</b> Automatically configures Spring based on
 *       dependencies (JPA, Security, Web, etc.)</li>
 *   <li><b>@ComponentScan:</b> Scans com.nonitos.food package for @Component,
 *       @Service, @Repository, @Controller annotations</li>
 * </ul>
 * 
 * <h2>Application startup:</h2>
 * <ol>
 *   <li>Spring Boot initializes application context</li>
 *   <li>Auto-configuration detects dependencies and configures beans</li>
 *   <li>Component scanning finds and registers all Spring components</li>
 *   <li>Database migrations run (Flyway)</li>
 *   <li>Embedded Tomcat server starts on port 8080</li>
 *   <li>Application is ready to accept requests</li>
 * </ol>
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@SpringBootApplication
public class NonitosFoodApplication {
    
    /**
     * Main method - application entry point.
     * 
     * <p>Starts the Spring Boot application with default configuration.
     * Configuration can be overridden via application.yml, environment variables,
     * or command-line arguments.</p>
     *
     * @param args command-line arguments (e.g., --server.port=9090)
     */
    public static void main(String[] args) {
        SpringApplication.run(NonitosFoodApplication.class, args);
    }
}
