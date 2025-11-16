package com.microservice.libros.controller;

import com.microservice.libros.model.Libros;
import com.microservice.libros.service.LibrosService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/libros")
public class LibrosController {

    private final LibrosService librosService;

    public LibrosController(LibrosService librosService) {
        this.librosService = librosService;
    }

    // ============================================================
    // GET ALL
    // ============================================================
    @GetMapping
    public ResponseEntity<List<Libros>> getAllLibros() {
        return ResponseEntity.ok(librosService.getAllLibros());
    }

    // ============================================================
    // GET BY ID
    // ============================================================
    @GetMapping("/{id}")
    public ResponseEntity<?> getLibroById(@PathVariable Integer id) {
        Optional<Libros> libro = librosService.getLibroById(id);

        if (libro.isPresent()) {
            return ResponseEntity.ok(libro.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Libro no encontrado");
        }
    }

    // ============================================================
    // CREATE
    // ============================================================
    @PostMapping
    public ResponseEntity<Libros> createLibro(@RequestBody Libros libro) {
        Libros nuevo = librosService.createLibro(libro);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    // ============================================================
    // DELETE
    // ============================================================
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLibro(@PathVariable Integer id) {
        Optional<Libros> eliminado = librosService.deleteLibroAndReturn(id);

        if (eliminado.isPresent()) {
            return ResponseEntity.ok("Libro eliminado correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Libro no encontrado");
        }
    }

    // ============================================================
    // FULL UPDATE (PUT)
    // ============================================================
    @PutMapping("/{id}")
    public ResponseEntity<?> updateLibro(
            @PathVariable Integer id,
            @RequestBody Libros libroNuevo
    ) {
        try {
            Libros actualizado = librosService.updateLibro(id, libroNuevo);
            return ResponseEntity.ok(actualizado);

        } catch (LibrosService.LibroNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Libro no encontrado");
        }
    }

    // ============================================================
    // PATCH: UPDATE STOCK
    // ============================================================
    @PatchMapping("/{id}/stock")
    public ResponseEntity<?> updateStock(
            @PathVariable Integer id,
            @RequestBody Map<String, Object> body
    ) {

        // Validación exacta esperada por los tests
        if (!body.containsKey("stock")) {
            return ResponseEntity.badRequest()
                    .body("Falta el campo 'stock'");
        }

        try {
            int nuevoStock = (int) body.get("stock");
            Libros actualizado = librosService.updateStock(id, nuevoStock);
            return ResponseEntity.ok(actualizado);

        } catch (LibrosService.LibroNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Libro no encontrado");
        }
    }
}
