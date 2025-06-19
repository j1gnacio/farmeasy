package org.example.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Document(collection = "historial_busquedas")
@Data
@NoArgsConstructor
public class HistorialBusqueda {

    @Id
    private String id;

    // Término que el usuario escribió en la barra de búsqueda.
    private String terminoBuscado;

    // Fecha y hora en que se realizó la búsqueda.
    private LocalDateTime fechaBusqueda;

    // Referencia al usuario que hizo la búsqueda.
    @DBRef
    private Usuario usuario;

    public HistorialBusqueda(String terminoBuscado, Usuario usuario) {
        this.terminoBuscado = terminoBuscado;
        this.usuario = usuario;
        this.fechaBusqueda = LocalDateTime.now();
    }
}