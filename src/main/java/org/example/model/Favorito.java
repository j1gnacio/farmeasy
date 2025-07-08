package org.example.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa la relación de un medicamento marcado como favorito por un usuario.
 */
@Document(collection = "favoritos")
@Data
@NoArgsConstructor
public class Favorito {

    @Id
    private String id;

    /**
     * Referencia al documento del Usuario que marcó el medicamento como favorito.
     */
    @DBRef
    private Usuario usuario;

    /**
     * Referencia al documento del Medicamento que fue marcado como favorito.
     */
    @DBRef
    private Medicamento medicamento;

    /**
     * Constructor para crear una nueva instancia de Favorito.
     *
     * @param usuario El usuario que marca el favorito.
     * @param medicamento El medicamento que se marca como favorito.
     */
    public Favorito(Usuario usuario, Medicamento medicamento) {
        this.usuario = usuario;
        this.medicamento = medicamento;
    }
}