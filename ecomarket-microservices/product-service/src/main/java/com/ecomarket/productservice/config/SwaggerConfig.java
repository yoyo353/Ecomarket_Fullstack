package com.ecomarket.productservice.config;

// Configuración de Swagger/OpenAPI para la documentación interactiva de la API.
// Permite personalizar la información pública de la API y los servidores disponibles.
// Mejora la experiencia de integración y pruebas para desarrolladores y clientes externos.
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    /**
     * Bean que configura la instancia principal de OpenAPI para Swagger UI.
     * Se personaliza el título, versión, descripción y los servidores (dev y producción).
     * Esto permite que la documentación refleje el entorno real y facilite pruebas desde Swagger UI.
     */
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
