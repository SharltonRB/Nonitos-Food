package com.nonitos.food.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI/Swagger configuration for API documentation.
 * Configures Swagger UI with API information and JWT authentication scheme.
 * Access Swagger UI at: http://localhost:8080/swagger-ui.html
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Configuration
public class OpenAPIConfig {

    /**
     * Configures OpenAPI documentation with API metadata and security schemes.
     *
     * @return configured OpenAPI instance
     */
    @Bean
    public OpenAPI nonitosFoodOpenAPI() {
        final String securitySchemeName = "bearer-jwt";
        
        return new OpenAPI()
            .info(new Info()
                .title("Nonito's Food API")
                .description("REST API for prep meal management system. " +
                    "Allows users to register, login, view menus, and place orders. " +
                    "Administrators can manage dishes, menus, and orders.")
                .version("1.0.0")
                .contact(new Contact()
                    .name("Nonito's Food Team")
                    .email("dev@nonitosfood.com"))
                .license(new License()
                    .name("Apache 2.0")
                    .url("https://www.apache.org/licenses/LICENSE-2.0")))
            .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
            .components(new Components()
                .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                    .name(securitySchemeName)
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
                    .description("JWT token authentication. " +
                        "Obtain token from /api/auth/login or /api/auth/register endpoints. " +
                        "Format: Bearer {token}")));
    }
}
