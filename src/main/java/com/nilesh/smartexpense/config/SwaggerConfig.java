package com.nilesh.smartexpense.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
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
                                .email("nileshkrsingh97@email.com")));
    }
}