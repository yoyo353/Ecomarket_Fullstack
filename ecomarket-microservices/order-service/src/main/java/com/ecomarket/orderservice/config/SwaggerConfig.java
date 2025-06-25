package com.ecomarket.orderservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuración de Swagger/OpenAPI para el Order Service de EcoMarket
 * 
 * Esta clase configura la documentación automática de la API REST
 * proporcionando información detallada sobre el microservicio de pedidos.
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("EcoMarket Order Service API")
                        .version("1.0.0")
                        .description("API REST para la gestión de pedidos y detalles de pedidos en EcoMarket SPA. " +
                                   "Este microservicio maneja todas las operaciones CRUD de pedidos, " +
                                   "cálculos automáticos de totales, estadísticas y reportes.")
                        .contact(new Contact()
                                .name("Equipo EcoMarket")
                                .email("dev@ecomarket.com")
                                .url("https://ecomarket.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8092")
                                .description("Servidor de Desarrollo"),
                        new Server()
                                .url("https://api.ecomarket.com")
                                .description("Servidor de Producción")
                ));
    }
}