package com.example.NewBackEnd.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@OpenAPIDefinition(
        info = @io.swagger.v3.oas.annotations.info.Info(title = "My API", version = "v1", description = "API Documentation"),
        security = {@io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")}
)
@io.swagger.v3.oas.annotations.security.SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class OpenApiConfig {
    @Value("${server.port:7070}")
    private int serverPort;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info().title("Kabal Green Bag API")
                        .version("v1.0.0").description("Description")
                        .license(new License().name("API License").url("http://domain.vn/license")))
                .servers(List.of(
 //                   new Server().url("http://localhost:" + serverPort + "/").description("Local Server")
                       new Server().url("https://newsdbase123-azcubjauakbjh7ar.canadacentral-01.azurewebsites.net/").description("Azure Server")
                ))
                .components(new Components().addSecuritySchemes(
                        "bearerAuth",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .security(List.of(new SecurityRequirement().addList("bearerAuth")));
    }

   
}
