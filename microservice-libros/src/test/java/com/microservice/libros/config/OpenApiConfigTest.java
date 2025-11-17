package com.microservice.libros.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpenApiConfigTest {

    @Test
    void testOpenApiBeanCreation() {
        // Instanciar config
        OpenApiConfig config = new OpenApiConfig();

        // Ejecutar el método @Bean manualmente
        OpenAPI openAPI = config.customOpenAPI();

        // VALIDACIONES
        assertNotNull(openAPI, "El bean OpenAPI no debe ser nulo");

        assertNotNull(openAPI.getInfo(), "La info no debe ser nula");
        assertEquals("API Biblioteca – Libros", openAPI.getInfo().getTitle());
        assertEquals("1.0", openAPI.getInfo().getVersion());
        assertEquals("Documentación del microservicio de gestión de libros",
                openAPI.getInfo().getDescription());
    }
}
