package com.jobtrackr.jobtrackr.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("JobTrackr API")
                .version("1.0")
                .description("A Job Application Tracker REST API built with Spring Boot and MySQL. " +
                             "Use /api/auth/login to get a JWT token, then click Authorize to use protected endpoints.")
                .contact(new Contact()
                    .name("Shweta Kumari")
                    .email("skumari1340@github.com")))
            .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
            .components(new Components()
                .addSecuritySchemes("Bearer Authentication",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("Enter your JWT token here. Get it from /api/auth/login")));
    }
}
