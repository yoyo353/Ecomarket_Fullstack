package com.ecomarket.userservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${server.port:8091}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .servers(List.of(
                    new Server()
                        .url("http://localhost:" + serverPort)
                        .description("Servidor de Desarrollo"),
                    new Server()
                        .url("https://api.ecomarket.com")
                        .description("Servidor de Producción (Ejemplo)")
                ))
                .tags(List.of(
                    new Tag()
                        .name("Usuarios V1")
                        .description("API REST básica para gestión de usuarios"),
                    new Tag()
                        .name("Usuarios V2")
                        .description("API REST con HATEOAS para gestión avanzada de usuarios"),
                    new Tag()
                        .name("Sistema")
                        .description("Endpoints del sistema y monitoreo")
                ));
    }

    private Info apiInfo() {
        return new Info()
                .title("EcoMarket User Service API")
                .description(buildDescription())
                .version("1.0.0")
                .contact(new Contact()
                        .name("Equipo de Desarrollo EcoMarket")
                        .email("desarrollo@ecomarket.com")
                        .url("https://github.com/ecomarket/user-service"))
                .license(new License()
                        .name("MIT License")
                        .url("https://opensource.org/licenses/MIT"));
    }

    private String buildDescription() {
        return """
                ## 🌱 EcoMarket User Service
                
                Microservicio para la gestión completa de usuarios en la plataforma EcoMarket.
                
                ### 🚀 Características Principales:
                - ✅ **CRUD completo** de usuarios
                - ✅ **Dos versiones de API**: básica (v1) y con HATEOAS (v2)
                - ✅ **Filtros avanzados** por estado, rol y tienda
                - ✅ **Validaciones** de datos y reglas de negocio
                - ✅ **Base de datos H2** para desarrollo y testing
                
                ### 🔗 Navegación HATEOAS (API v2):
                La versión 2 de la API incluye enlaces hipermedia que permiten:
                - Navegación dinámica entre recursos
                - Enlaces condicionales según el estado del usuario
                - Descubrimiento automático de operaciones disponibles
                
                ### 📊 Endpoints Disponibles:
                - **Gestión básica**: CRUD de usuarios
                - **Filtros**: usuarios activos/inactivos
                - **Estadísticas**: métricas y reportes del sistema
                
                ### 🛠️ Tecnologías:
                - Spring Boot 3.4.5
                - Spring Data JPA
                - Spring HATEOAS
                - H2 Database
                - SpringDoc OpenAPI 3
                """;
    }
}
