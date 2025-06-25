package com.ecomarket.productservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
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
                        .title("EcoMarket 2025 - API de Productos")
                        .version("1.0")
                        .description("Documentación de la API para el sistema de productos ecológicos de EcoMarket"))
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
