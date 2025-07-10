package org.example.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "favoritos")
@Data
@NoArgsConstructor
public class Favorito {

    @Id
    private String id;

    @DBRef // Referencia al documento del Usuario
    private Usuario usuario;

    @DBRef // Referencia al documento del Medicamento
    private Medicamento medicamento;

    public Favorito(Usuario usuario, Medicamento medicamento) {
        this.usuario = usuario;
        this.medicamento = medicamento;
    }

    // Getter manual para medicamento para evitar error Lombok
    public Medicamento getMedicamento() {
        return this.medicamento;
    }
}
