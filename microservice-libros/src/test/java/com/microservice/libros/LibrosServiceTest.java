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

// 🔥 Evita falsos positivos de Null Type Safety en VSCode
@SuppressWarnings("all")
class LibrosServiceTest {

    private LibrosRepository librosRepository;
    private LibrosService librosService;

    // ============================================================
    // 🔧 Configuración inicial para cada test
    // ============================================================
    @BeforeEach
    void setUp() {
        librosRepository = mock(LibrosRepository.class);  // Se simula el repositorio
        librosService = new LibrosService(librosRepository); // Se inyecta el mock al servicio
        MockitoAnnotations.openMocks(this);
    }

    // ============================================================
    // Método auxiliar para generar libros de prueba
    // ============================================================
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
    //  TEST: Obtener lista de libros
    // ============================================================
    @Test
    void testGetAllLibros() {

        // Simulamos que el repositorio retorna 2 libros
        when(librosRepository.findAll())
                .thenReturn(List.of(libroMock(1), libroMock(2)));

        List<Libros> result = librosService.getAllLibros();

        assertNotNull(result);
        assertEquals(2, result.size()); // Debe haber 2 libros
        verify(librosRepository, times(1)).findAll();
    }

    // ============================================================
    // TEST: Buscar libro por ID (encontrado)
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
    //  TEST: Buscar libro por ID (NO encontrado)
    // ============================================================
    @Test
    void testGetLibroByIdNotFound() {

        when(librosRepository.findById(999))
                .thenReturn(Optional.empty());

        Optional<Libros> result = librosService.getLibroById(999);

        assertTrue(result.isEmpty());
    }

    // ============================================================
    // TEST: Crear libro
    // ============================================================
    @Test
    void testCreateLibro() {

        Libros libro = libroMock(null); // Libro nuevo sin ID

        when(librosRepository.save(libro)).thenReturn(libro);

        Libros result = librosService.createLibro(libro);

        assertNotNull(result);
        verify(librosRepository).save(libro);
    }

    // ============================================================
    // TEST: Eliminar libro (encontrado)
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
    // TEST: Eliminar libro (NO encontrado)
    // ============================================================
    @Test
    void testDeleteLibroNotFound() {

        when(librosRepository.findById(1)).thenReturn(Optional.empty());

        Optional<Libros> deleted = librosService.deleteLibroAndReturn(1);

        assertTrue(deleted.isEmpty());
        verify(librosRepository, never()).deleteById(any());
    }

    // ============================================================
    //  TEST: Actualizar libro completo (SUCCESS)
    // ============================================================
    @Test
    void testUpdateLibroSuccess() {

        Libros original = libroMock(1);
        Libros nuevo = libroMock(null); // Datos nuevos

        when(librosRepository.findById(1)).thenReturn(Optional.of(original));
        when(librosRepository.save(original)).thenReturn(original);

        Libros updated = librosService.updateLibro(1, nuevo);

        assertNotNull(updated);
        assertEquals(nuevo.getTitulo(), updated.getTitulo());
        verify(librosRepository).save(original);
    }

    // ============================================================
    // TEST: Actualizar libro completo (NOT FOUND)
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
    // TEST: Actualizar stock (SUCCESS)
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
    // TEST: Actualizar stock (NOT FOUND)
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
