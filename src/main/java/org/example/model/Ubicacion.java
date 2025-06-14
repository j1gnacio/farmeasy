package org.example.model;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "ubicaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ubicacion {
    @Id
    private String id = UUID.randomUUID().toString();

    private String direccion;

    private String ciudad;

    private double latitud;

    private double longitud;

    public Ubicacion(String direccion, String ciudad) {
        this.id = UUID.randomUUID().toString();
        this.direccion = direccion;
        this.ciudad = ciudad;
    }

    public Ubicacion(String direccion, String ciudad, double latitud, double longitud) {
        this.id = UUID.randomUUID().toString();
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.latitud = latitud;
        this.longitud = longitud;
    }
}
