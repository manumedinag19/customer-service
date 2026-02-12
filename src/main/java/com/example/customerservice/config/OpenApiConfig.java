package com.example.customerservice.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customerServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Customer Service API")
                        .description("API de ejemplo para reto técnico (customers & accounts)")
                        .version("v1.0.0")
                        .contact(new Contact().name("Equipo") .email("team@example.com"))
                        .license(new License().name("MIT")))
                .externalDocs(new ExternalDocumentation().description("Repo").url("https://example.com"));
    }
}

