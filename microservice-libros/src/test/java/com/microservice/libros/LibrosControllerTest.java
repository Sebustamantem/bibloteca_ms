package com.microservice.libros;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.libros.controller.LibrosController;
import com.microservice.libros.model.Libros;
import com.microservice.libros.service.LibrosService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings({"null"})  
@WebMvcTest(LibrosController.class)
class LibrosControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LibrosService librosService;

    @Autowired
    private ObjectMapper objectMapper;

    private Libros libro;

    @BeforeEach
    void setUp() {
        libro = new Libros(
                1,
                "Libro 1",
                "Autor 1",
                "Editorial",
                LocalDate.of(2020, 1, 1),
                "Drama",
                10,
                BigDecimal.valueOf(5000),
                "ES",
                "Desc",
                true
        );
    }

    // =======================================================
    // GET ALL
    // =======================================================
    @Test
    void testGetAllLibros() throws Exception {

        when(librosService.getAllLibros())
                .thenReturn(Collections.singletonList(libro));

        mockMvc.perform(get("/api/v1/libros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Libro 1"));
    }

    // =======================================================
    // GET BY ID — FOUND
    // =======================================================
    @Test
    void testGetLibroByIdFound() throws Exception {

        when(librosService.getLibroById(1))
                .thenReturn(Optional.of(libro));

        mockMvc.perform(get("/api/v1/libros/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Libro 1"));
    }

    // =======================================================
    // GET BY ID — NOT FOUND
    // =======================================================
    @Test
    void testGetLibroByIdNotFound() throws Exception {

        when(librosService.getLibroById(1))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/libros/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Libro no encontrado"));
    }

    // =======================================================
    // CREATE
    // =======================================================
    @Test
    void testCreateLibro() throws Exception {

        when(librosService.createLibro(any(Libros.class)))
                .thenReturn(libro);

        mockMvc.perform(
                post("/api/v1/libros")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(libro))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value("Libro 1"));
    }

    // =======================================================
    // DELETE — FOUND
    // =======================================================
    @Test
    void testDeleteLibroSuccess() throws Exception {

        when(librosService.deleteLibroAndReturn(1))
                .thenReturn(Optional.of(libro));

        mockMvc.perform(delete("/api/v1/libros/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Libro eliminado correctamente"));
    }

    // =======================================================
    // DELETE — NOT FOUND
    // =======================================================
    @Test
    void testDeleteLibroNotFound() throws Exception {

        when(librosService.deleteLibroAndReturn(1))
                .thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/v1/libros/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Libro no encontrado"));
    }

    // =======================================================
    // UPDATE FULL (PUT)
    // =======================================================
    @Test
    void testUpdateLibro() throws Exception {

        when(librosService.updateLibro(eq(1), any(Libros.class)))
                .thenReturn(libro);

        mockMvc.perform(
                put("/api/v1/libros/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(libro))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Libro 1"));
    }

    // =======================================================
    // PATCH STOCK — SUCCESS
    // =======================================================
    @Test
    void testUpdateStockSuccess() throws Exception {

        when(librosService.updateStock(1, 25))
                .thenReturn(libro);

        mockMvc.perform(
                patch("/api/v1/libros/1/stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"stock\":25}")
        )
                .andExpect(status().isOk());
    }

    // =======================================================
    // PATCH STOCK — MISSING FIELD
    // =======================================================
    @Test
    void testUpdateStockMissingField() throws Exception {

        mockMvc.perform(
                patch("/api/v1/libros/1/stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Falta el campo 'stock'"));
    }
}
