package com.test.hft.presentation.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Order Simulator API")
                        .version("1.0.0")
                        .description("HFT Order Simulator for testing trading pipelines")
                        .contact(new Contact()
                                .name("HFT")
                                .email("dvvu0211@gmail.com")));
    }
}