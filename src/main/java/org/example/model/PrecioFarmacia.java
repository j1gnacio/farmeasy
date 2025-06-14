package org.example.model;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "precio_farmacia")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrecioFarmacia {
    @Id
    private String id = UUID.randomUUID().toString();

    @DBRef
    private Farmacia farmacia;

    @DBRef
    private Medicamento medicamento;

    private double precioNormal;

    private double precioOferta;

    public PrecioFarmacia(Farmacia farmacia, Medicamento medicamento, double precioNormal, double precioOferta) {
        this.farmacia = farmacia;
        this.medicamento = medicamento;
        this.precioNormal = precioNormal;
        this.precioOferta = precioOferta;
    }
}
