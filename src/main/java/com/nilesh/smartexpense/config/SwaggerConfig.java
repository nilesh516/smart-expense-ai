package com.nilesh.smartexpense.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI smartExpenseOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SmartExpense AI API")
                        .description("AI-powered Expense Management System using Anthropic Claude")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Nilesh")
                                .email("your@email.com")))
                // Add JWT auth support in Swagger UI
                .addSecurityItem(new SecurityRequirement()
                        .addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Enter your JWT token here")));
    }
}