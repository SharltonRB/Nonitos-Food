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
 * 
 * <p>Configures Swagger UI with API information, security schemes, and interactive
 * documentation. This allows developers and API consumers to explore and test
 * endpoints without writing code.</p>
 * 
 * <h2>Access points:</h2>
 * <ul>
 *   <li><b>Swagger UI:</b> http://localhost:8080/swagger-ui.html</li>
 *   <li><b>OpenAPI JSON:</b> http://localhost:8080/v3/api-docs</li>
 *   <li><b>OpenAPI YAML:</b> http://localhost:8080/v3/api-docs.yaml</li>
 * </ul>
 * 
 * <h2>Why use OpenAPI/Swagger?</h2>
 * <ul>
 *   <li><b>Interactive testing:</b> Test endpoints directly from browser</li>
 *   <li><b>Auto-generated docs:</b> Documentation stays in sync with code</li>
 *   <li><b>Client generation:</b> Generate TypeScript/Python/etc. clients</li>
 *   <li><b>API contracts:</b> Clear specification for frontend developers</li>
 *   <li><b>Onboarding:</b> New developers understand API quickly</li>
 * </ul>
 * 
 * <h2>JWT Authentication in Swagger:</h2>
 * <p>To test protected endpoints:
 * <ol>
 *   <li>Call /api/auth/login or /api/auth/register to get JWT token</li>
 *   <li>Click "Authorize" button in Swagger UI</li>
 *   <li>Enter token in format: Bearer {your-token-here}</li>
 *   <li>All subsequent requests will include the token</li>
 * </ol>
 * </p>
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Configuration
public class OpenAPIConfig {

    /**
     * Configures OpenAPI documentation with API metadata and security schemes.
     * 
     * <p>Sets up API information (title, description, version, contact) and
     * configures JWT Bearer authentication scheme for protected endpoints.</p>
     *
     * @return configured OpenAPI instance with metadata and security
     */
    @Bean
    public OpenAPI nonitosFoodOpenAPI() {
        final String securitySchemeName = "bearer-jwt";
        
        return new OpenAPI()
            // API metadata
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
            // Global security requirement (applies to all endpoints by default)
            .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
            // Security scheme definition
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
