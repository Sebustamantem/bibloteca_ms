package com.microservice.libros;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.libros.controller.LibrosController;
import com.microservice.libros.model.Libros;
import com.microservice.libros.service.LibrosService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LibrosController.class)
class LibrosControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LibrosService librosService;

    @Autowired
    private ObjectMapper objectMapper;

    private Libros libro;

    // ============================================================
    // Antes de cada test, crear un libro de prueba
    // ============================================================
    @BeforeEach
    void setup() {
        libro = Libros.builder()
                .id(1)
                .titulo("Libro 1")
                .autor("Autor 1")
                .editorial("Editorial")
                .fechaPublicacion(LocalDate.of(2020, 1, 1))
                .categoria("Drama")
                .stock(10)
                .precio(BigDecimal.valueOf(5000))
                .idioma("ES")
                .descripcion("Desc")
                .disponible(true)
                .build();
    }

    // ============================================================
    // GET ALL — listar todos los libros
    // ============================================================
    @Test
    @DisplayName("GET /api/v1/libros → lista OK")
    void testGetAllLibros() throws Exception {

        // Simular que el servicio retorna una lista con 1 libro
        when(librosService.getAllLibros()).thenReturn(Collections.singletonList(libro));

        mockMvc.perform(get("/api/v1/libros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.librosList[0].titulo").value("Libro 1"));
    }

    // ============================================================
    //  GET BY ID — libro encontrado
    // ============================================================
    @Test
    @DisplayName("GET /api/v1/libros/{id} → encontrado")
    void testGetLibroByIdFound() throws Exception {

        when(librosService.getLibroById(1)).thenReturn(Optional.of(libro));

        mockMvc.perform(get("/api/v1/libros/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Libro 1"));
    }

    // ============================================================
    //  GET BY ID — libro NO encontrado
    // ============================================================
    @Test
    @DisplayName("GET /api/v1/libros/{id} → no encontrado")
    void testGetLibroByIdNotFound() throws Exception {

        when(librosService.getLibroById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/libros/1"))
                .andExpect(status().isNotFound());
    }

    // ============================================================
    //  PUT — actualizar un libro completo
    // ============================================================
    @Test
    @DisplayName("PUT /api/v1/libros/{id} → actualizado correctamente")
    void testUpdateLibro() throws Exception {

        when(librosService.updateLibro(eq(1), any(Libros.class))).thenReturn(libro);

        String libroJson = objectMapper.writeValueAsString(libro);

        mockMvc.perform(put("/api/v1/libros/1")
                        .contentType("application/json")
                        .content(libroJson != null ? libroJson : ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Libro 1"));
    }

    // ============================================================
    // PATCH — actualizar solo el stock
    // ============================================================
    @Test
    @DisplayName("PATCH /api/v1/libros/{id}/stock → actualizado")
    void testUpdateStockSuccess() throws Exception {

        when(librosService.updateStock(1, 25)).thenReturn(libro);

        mockMvc.perform(patch("/api/v1/libros/1/stock")
                        .contentType("application/json")
                        .content("{\"stock\":25}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Libro 1"));
    }

    // ============================================================
    // PATCH — stock faltante
    // ============================================================
    @Test
    @DisplayName("PATCH /api/v1/libros/{id}/stock → falta campo stock")
    void testUpdateStockMissingField() throws Exception {

        mockMvc.perform(patch("/api/v1/libros/1/stock")
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    // ============================================================
    // DELETE — eliminación exitosa
    // ============================================================
    @Test
    @DisplayName("DELETE /api/v1/libros/{id} → eliminado")
    void testDeleteLibroSuccess() throws Exception {

        when(librosService.deleteLibroAndReturn(1)).thenReturn(Optional.of(libro));

        mockMvc.perform(delete("/api/v1/libros/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Libro eliminado correctamente"));
    }

    // ============================================================
    // DELETE — libro no encontrado
    // ============================================================
    @Test
    @DisplayName("DELETE /api/v1/libros/{id} → no encontrado")
    void testDeleteLibroNotFound() throws Exception {

        when(librosService.deleteLibroAndReturn(1)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/v1/libros/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Libro no encontrado"));
    }
}
