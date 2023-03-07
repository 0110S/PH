package com.accountbook.phoenix.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition
public class SwaggerConfig {

    @Bean
    OpenAPI openAPI(){
        return new OpenAPI().info(new Info().title("API Documentation ").version("1.0"));
    }
}
//    @Bean
//    public OpenAPI openAPI() {
//        return new OpenAPI()
//                .info(new Info()
//                        .title("API Documentation")
//                        .version("1.0")
//                        .description("Spring doc"))
//                .addServersItem(new Server().url("http://localhost:8089"))
//                .addSecurityItem(new SecurityRequirement().addList("Bearer "))
//                .components(new Components()
//                        .addSecuritySchemes("Bearer ",
//                                new SecurityScheme()
//                                        .type(SecurityScheme.Type.HTTP)
//                                        .scheme("bearer")
//                                        .bearerFormat("JWT")));
//    }
//
//    @Bean
//    public GroupedOpenApi customApi() {
//        return GroupedOpenApi.builder()
//                .group("custom")
//                .pathsToMatch("/custom/**")
//                .build();
//    }