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
 * OpenAPI/Swagger configuration.
 *
 * @author Nonito's Food Team
 * @since 1.0
 */
@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI nonitosFoodOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Nonito's Food API")
                        .description("REST API for prep meal management system")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Nonito's Food Team")
                                .email("dev@nonitosfood.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .addSecurityItem(new SecurityRequirement().addList("bearer-jwt"))
                .components(new Components()
                        .addSecuritySchemes("bearer-jwt", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT token authentication")));
    }
}
