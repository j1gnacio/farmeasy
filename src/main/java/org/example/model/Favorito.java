package org.example.model;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "favoritos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Favorito {
    @Id
    private String idFavorito = UUID.randomUUID().toString();

    @DBRef
    private Usuario usuario;

    @DBRef
    private Medicamento medicamento;

    public Favorito(Usuario usuario, Medicamento medicamento) {
        this.usuario = usuario;
        this.medicamento = medicamento;
        this.idFavorito = UUID.randomUUID().toString();
    }
}
