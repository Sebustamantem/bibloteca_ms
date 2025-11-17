package com.microservice.libros.controller;

import com.microservice.libros.model.Libros;
import com.microservice.libros.service.LibrosService;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@SuppressWarnings("null")
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
    @Operation(summary = "Obtener todos los libros")
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Libros>>> getAllLibros() {

        List<EntityModel<Libros>> lista = librosService.getAllLibros()
                .stream()
                .map(libro -> EntityModel.of(
                        libro,
                        linkTo(methodOn(LibrosController.class).getLibroById(libro.getId())).withSelfRel(),
                        linkTo(methodOn(LibrosController.class).deleteLibro(libro.getId())).withRel("delete"),
                        linkTo(methodOn(LibrosController.class).updateLibro(libro.getId(), libro)).withRel("update")
                ))
                .toList();

        return ResponseEntity.ok(
                CollectionModel.of(
                        lista,
                        linkTo(methodOn(LibrosController.class).getAllLibros()).withSelfRel()
                )
        );
    }

    // ============================================================
    // GET BY ID
    // ============================================================
    @Operation(summary = "Obtener un libro por ID")
    @ApiResponse(responseCode = "200", description = "Libro encontrado")
    @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<?> getLibroById(@PathVariable Integer id) {

        Optional<Libros> libroOpt = librosService.getLibroById(id);

        if (libroOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Libro no encontrado");
        }

        Libros libro = libroOpt.get();

        EntityModel<Libros> model = EntityModel.of(
                libro,
                linkTo(methodOn(LibrosController.class).getLibroById(id)).withSelfRel(),
                linkTo(methodOn(LibrosController.class).deleteLibro(id)).withRel("delete"),
                linkTo(methodOn(LibrosController.class).updateLibro(id, libro)).withRel("update")
        );

        return ResponseEntity.ok(model);
    }

    // ============================================================
    // CREATE
    // ============================================================
    @Operation(summary = "Crear un libro nuevo")
    @ApiResponse(responseCode = "201", description = "Libro creado")
    @PostMapping
    public ResponseEntity<EntityModel<Libros>> createLibro(@RequestBody Libros libro) {

        Libros creado = librosService.createLibro(libro);

        EntityModel<Libros> model = EntityModel.of(
                creado,
                linkTo(methodOn(LibrosController.class).getLibroById(creado.getId())).withSelfRel(),
                linkTo(methodOn(LibrosController.class).getAllLibros()).withRel("all")
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

    // ============================================================
    // UPDATE (PUT)
    // ============================================================
    @Operation(summary = "Actualizar un libro completo")
    @ApiResponse(responseCode = "200", description = "Libro actualizado")
    @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateLibro(@PathVariable Integer id, @RequestBody Libros libroRequest) {

        Libros updated = librosService.updateLibro(id, libroRequest);

        if (updated == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Libro no encontrado");
        }

        EntityModel<Libros> model = EntityModel.of(
                updated,
                linkTo(methodOn(LibrosController.class).getLibroById(id)).withSelfRel(),
                linkTo(methodOn(LibrosController.class).getAllLibros()).withRel("all")
        );

        return ResponseEntity.ok(model);
    }

    // ============================================================
    // PATCH (STOCK)
    // ============================================================
    @Operation(summary = "Actualizar solo el stock de un libro")
    @ApiResponse(responseCode = "200", description = "Stock actualizado")
    @ApiResponse(responseCode = "400", description = "Campo faltante")
    @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    @PatchMapping("/{id}/stock")
    public ResponseEntity<?> updateStock(@PathVariable Integer id, @RequestBody Libros body) {

        if (body.getStock() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Falta el campo 'stock'");
        }

        Libros updated = librosService.updateStock(id, body.getStock());

        if (updated == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Libro no encontrado");
        }

        return ResponseEntity.ok(
                EntityModel.of(
                        updated,
                        linkTo(methodOn(LibrosController.class).getLibroById(id)).withSelfRel()
                )
        );
    }

    // ============================================================
    // DELETE
    // ============================================================
    @Operation(summary = "Eliminar un libro")
    @ApiResponse(responseCode = "200", description = "Libro eliminado correctamente")
    @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLibro(@PathVariable Integer id) {

        return librosService.deleteLibroAndReturn(id)
                .map(l -> ResponseEntity.ok("Libro eliminado correctamente"))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Libro no encontrado"));
    }
}
