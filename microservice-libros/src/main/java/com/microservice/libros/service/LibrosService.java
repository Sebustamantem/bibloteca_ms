package com.microservice.libros.service;

import com.microservice.libros.model.Libros;
import com.microservice.libros.repository.LibrosRepository;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LibrosService {

    private final LibrosRepository librosRepository;

    public LibrosService(LibrosRepository librosRepository) {
        this.librosRepository = librosRepository;
    }

    // ============================================================
    //  Excepción personalizada
    // ============================================================
    public static class LibroNotFoundException extends RuntimeException {
        public LibroNotFoundException(String msg) {
            super(msg);
        }
    }

    // ============================================================
    //  Obtener todos los libros
    // ============================================================
    public List<Libros> getAllLibros() {
        return librosRepository.findAll();
    }

    // ============================================================
    //  Buscar libro por ID
    // ============================================================
    public Optional<Libros> getLibroById(int id) {
        return librosRepository.findById(id);
    }

    // ============================================================
    //  Crear libro
    // ============================================================
@Transactional
public Libros createLibro(Libros libro) {

    if (libro.getTitulo() == null || libro.getTitulo().isBlank()) {
        throw new IllegalArgumentException("El título no puede estar vacío");
    }

    if (libro.getAutor() == null || libro.getAutor().isBlank()) {
        throw new IllegalArgumentException("El autor no puede estar vacío");
    }

    return librosRepository.save(libro);
}


    // ============================================================
    // Eliminar un libro y retornar el eliminado
    // Versión limpia sin lambdas ni warnings
    // ============================================================
    @Transactional
    public Optional<Libros> deleteLibroAndReturn(int id) {

        Optional<Libros> encontrado = librosRepository.findById(id);

        if (encontrado.isPresent()) {
            librosRepository.deleteById(id);
        }

        return encontrado;
    }

    // ============================================================
    // Actualizar libro COMPLETO (PUT)
    // ============================================================
    @Transactional
    public Libros updateLibro(int id, Libros data) {

        Libros libro = librosRepository.findById(id)
                .orElseThrow(() -> new LibroNotFoundException("Libro no encontrado: " + id));

        libro.setTitulo(data.getTitulo());
        libro.setAutor(data.getAutor());
        libro.setEditorial(data.getEditorial());
        libro.setFechaPublicacion(data.getFechaPublicacion());
        libro.setCategoria(data.getCategoria());
        libro.setStock(data.getStock());
        libro.setPrecio(data.getPrecio());
        libro.setIdioma(data.getIdioma());
        libro.setDescripcion(data.getDescripcion());
        libro.setDisponible(data.getDisponible());

        return librosRepository.save(libro);
    }

    // ============================================================
    //  PATCH — actualizar solo stock
    // ============================================================
    @Transactional
    public Libros updateStock(int id, int nuevoStock) {

        Libros libro = librosRepository.findById(id)
                .orElseThrow(() -> new LibroNotFoundException("Libro no encontrado: " + id));

        libro.setStock(nuevoStock);

        return librosRepository.save(libro);
    }
}
