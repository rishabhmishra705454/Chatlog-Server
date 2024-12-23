package com.chatlog.chatlog_server.utils;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "ChatLog Server API",
                version = "1.0.0",
                description = "API for managing user chat logs. This includes creating, retrieving, updating, and deleting chat logs."
        )
)
public class OpenAPIConfig {

        @Bean
        public OpenAPI customOpenAPI() {
                String securitySchemeName = "bearerAuth";
                return new OpenAPI()
                        .addSecurityItem(new SecurityRequirement().addList(securitySchemeName)) // Apply globally
                        .components(new Components()
                                .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP) // HTTP-based scheme
                                        .scheme("bearer") // Bearer token
                                        .bearerFormat("JWT") // Specify token format
                                        .description("Enter your Bearer token here")
                                ));
        }
}