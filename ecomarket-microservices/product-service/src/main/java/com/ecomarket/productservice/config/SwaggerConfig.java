package com.ecomarket.productservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("EcoMarket Product Service API")
                        .description("API RESTful para la gestión de productos ecológicos de EcoMarket SPA")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Equipo EcoMarket")
                                .email("desarrollo@ecomarket.cl")
                                .url("https://ecomarket.cl"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8090")
                                .description("Servidor de Desarrollo"),
                        new Server()
                                .url("https://api.ecomarket.cl")
                                .description("Servidor de Producción")
                ));
    }
}
