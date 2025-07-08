package org.example.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Representa una entrada en el historial de búsqueda de un usuario.
 */
@Document(collection = "historial_busquedas")
@Data
@NoArgsConstructor
public class HistorialBusqueda {

    @Id
    private String id;

    /**
     * El término de búsqueda que el usuario ingresó.
     */
    private String terminoBuscado;

    /**
     * La fecha y hora en que se realizó la búsqueda.
     */
    private LocalDateTime fechaBusqueda;

    /**
     * Referencia al usuario que realizó la búsqueda.
     */
    @DBRef
    private Usuario usuario;

    /**
     * Constructor para crear un nuevo registro de historial de búsqueda.
     *
     * @param terminoBuscado El término de búsqueda.
     * @param usuario El usuario que realizó la búsqueda.
     */
    public HistorialBusqueda(String terminoBuscado, Usuario usuario) {
        this.terminoBuscado = terminoBuscado;
        this.usuario = usuario;
        this.fechaBusqueda = LocalDateTime.now();
    }
}