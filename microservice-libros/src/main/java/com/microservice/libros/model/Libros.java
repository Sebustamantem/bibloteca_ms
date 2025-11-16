package com.microservice.libros.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "libros")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa un libro dentro del sistema de biblioteca")
public class Libros {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_libro")
    @Schema(description = "ID único del libro", example = "1")
    private Integer id;   // ✔ YA ACEPTA NULL

    @NotBlank(message = "El título es obligatorio")
    @Size(min = 3, max = 150, message = "El título debe tener entre 3 y 150 caracteres")
    @Column(name = "titulo", nullable = false, length = 150)
    @Schema(description = "Título del libro", example = "Harry Potter y la Piedra Filosofal")
    private String titulo;

    @NotBlank(message = "El autor es obligatorio")
    @Size(min = 3, max = 120, message = "El autor debe tener entre 3 y 120 caracteres")
    @Column(name = "autor", nullable = false, length = 120)
    @Schema(description = "Autor del libro", example = "J.K. Rowling")
    private String autor;

    @Size(max = 120)
    @Column(name = "editorial", length = 120)
    @Schema(description = "Editorial del libro", example = "Bloomsbury")
    private String editorial;

    @PastOrPresent(message = "La fecha no puede ser futura")
    @Column(name = "fecha_publicacion")
    @Schema(description = "Fecha de publicación", example = "1997-06-26", format = "date")
    private LocalDate fechaPublicacion;

    @Size(max = 60)
    @Column(name = "categoria", length = 60)
    @Schema(description = "Categoría o género del libro", example = "Fantasía")
    private String categoria;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Column(name = "stock", nullable = false)
    @Schema(description = "Cantidad disponible", example = "25")
    private Integer stock;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El precio no puede ser negativo")
    @Digits(integer = 10, fraction = 2)
    @Column(name = "precio", precision = 12, scale = 2, nullable = false)
    @Schema(description = "Precio del libro (CLP)", example = "12990.50")
    private BigDecimal precio;

    @Size(max = 2)
    @Column(name = "idioma", length = 2)
    @Schema(description = "Idioma (ISO 639-1)", example = "ES")
    private String idioma;

    @Size(max = 500)
    @Column(name = "descripcion", length = 500)
    @Schema(description = "Sinopsis o descripción del libro")
    private String descripcion;

    @Builder.Default
    @Column(name = "disponible", nullable = false)
    @Schema(description = "Indica si está disponible", example = "true")
    private Boolean disponible = true;
}
