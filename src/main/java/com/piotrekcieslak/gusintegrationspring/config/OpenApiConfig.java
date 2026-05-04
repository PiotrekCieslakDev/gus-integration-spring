package com.piotrekcieslak.gusintegrationspring.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("GUS Integration API")
                .description("Bramka SOAP -> REST dla REGON BIR 1.1/1.2")
                .version("1.0"));
    }
}