package com.transportbooker.rizla.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    GroupedOpenApi publicAPI() {
        return GroupedOpenApi.builder()
                .group("open-apis")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info().title("Rizla")
                .description("Rizla App")
                .version("v1.0.0"));
    }
}
