package com.microservice.libros;

import com.microservice.libros.model.Libros;
import com.microservice.libros.repository.LibrosRepository;
import com.microservice.libros.service.LibrosService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// 🔥 ESTA LÍNEA ELIMINA TODOS LOS FALSE POSITIVES DE NULL TYPE SAFETY
@SuppressWarnings("all")
class LibrosServiceTest {

    private LibrosRepository librosRepository;
    private LibrosService librosService;

    @BeforeEach
    void setUp() {
        librosRepository = mock(LibrosRepository.class);
        librosService = new LibrosService(librosRepository);
        MockitoAnnotations.openMocks(this);
    }

    private Libros libroMock(Integer id) {
        return new Libros(
                id,
                "Libro " + (id != null ? id : "Nuevo"),
                "Autor",
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

    // ============================================================
    // GET ALL
    // ============================================================
    @Test
    void testGetAllLibros() {
        when(librosRepository.findAll())
                .thenReturn(List.of(libroMock(1), libroMock(2)));

        List<Libros> result = librosService.getAllLibros();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(librosRepository, times(1)).findAll();
    }

    // ============================================================
    // GET BY ID FOUND
    // ============================================================
    @Test
    void testGetLibroByIdFound() {
        when(librosRepository.findById(1))
                .thenReturn(Optional.of(libroMock(1)));

        Optional<Libros> result = librosService.getLibroById(1);

        assertTrue(result.isPresent());
        assertEquals("Libro 1", result.get().getTitulo());
    }

    // ============================================================
    // GET BY ID NOT FOUND
    // ============================================================
    @Test
    void testGetLibroByIdNotFound() {
        when(librosRepository.findById(999))
                .thenReturn(Optional.empty());

        Optional<Libros> result = librosService.getLibroById(999);

        assertTrue(result.isEmpty());
    }

    // ============================================================
    // CREATE LIBRO
    // ============================================================
    @Test
    void testCreateLibro() {
        Libros libro = libroMock(null);

        when(librosRepository.save(libro)).thenReturn(libro);

        Libros result = librosService.createLibro(libro);

        assertNotNull(result);
        verify(librosRepository).save(libro);
    }

    // ============================================================
    // DELETE — FOUND
    // ============================================================
    @Test
    void testDeleteLibroSuccess() {
        Libros libro = libroMock(1);

        when(librosRepository.findById(1)).thenReturn(Optional.of(libro));

        Optional<Libros> deleted = librosService.deleteLibroAndReturn(1);

        assertTrue(deleted.isPresent());
        verify(librosRepository).deleteById(1);
    }

    // ============================================================
    // DELETE — NOT FOUND
    // ============================================================
    @Test
    void testDeleteLibroNotFound() {
        when(librosRepository.findById(1)).thenReturn(Optional.empty());

        Optional<Libros> deleted = librosService.deleteLibroAndReturn(1);

        assertTrue(deleted.isEmpty());
        verify(librosRepository, never()).deleteById(any());
    }

    // ============================================================
    // UPDATE FULL — SUCCESS
    // ============================================================
    @Test
    void testUpdateLibroSuccess() {
        Libros original = libroMock(1);
        Libros nuevo = libroMock(null);

        when(librosRepository.findById(1)).thenReturn(Optional.of(original));
        when(librosRepository.save(original)).thenReturn(original);

        Libros updated = librosService.updateLibro(1, nuevo);

        assertNotNull(updated);
        assertEquals(nuevo.getTitulo(), updated.getTitulo());
        verify(librosRepository).save(original);
    }

    // ============================================================
    // UPDATE FULL — NOT FOUND
    // ============================================================
    @Test
    void testUpdateLibroNotFound() {
        when(librosRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(
                LibrosService.LibroNotFoundException.class,
                () -> librosService.updateLibro(1, libroMock(null))
        );
    }

    // ============================================================
    // PATCH STOCK — SUCCESS
    // ============================================================
    @Test
    void testUpdateStockSuccess() {
        Libros libro = libroMock(1);

        when(librosRepository.findById(1)).thenReturn(Optional.of(libro));
        when(librosRepository.save(libro)).thenReturn(libro);

        Libros updated = librosService.updateStock(1, 50);

        assertNotNull(updated);
        assertEquals(50, updated.getStock());
        verify(librosRepository).save(libro);
    }

    // ============================================================
    // PATCH STOCK — NOT FOUND
    // ============================================================
    @Test
    void testUpdateStockNotFound() {
        when(librosRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(
                LibrosService.LibroNotFoundException.class,
                () -> librosService.updateStock(1, 50)
        );
    }
}
