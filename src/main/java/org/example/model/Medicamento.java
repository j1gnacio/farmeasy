package org.example.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "medicamentos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Medicamento {
    @Id
    private String id;
    private String nombre;
    private Double precio_internet;
    private Double precio_farmacia;
    private String descripcion;
    private String url_producto;
    private String imagen_url;
    private String farmacia;
    private String dosis;

    public String getDosis() {
        return dosis;
    }

    public Double getPrecioInternet() {
        return precio_internet;
    }

    public Double getPrecioNormal() {
        return precio_farmacia;
    }

    public String getUrl() {
        return url_producto;
    }

    public Medicamento(String id, String nombre, String dosis, String descripcion, Double precio_internet, Double precio_farmacia, String url_producto, String farmacia) {
        this.id = id;
        this.nombre = nombre;
        this.dosis = dosis;
        this.descripcion = descripcion;
        this.precio_internet = precio_internet;
        this.precio_farmacia = precio_farmacia;
        this.url_producto = url_producto;
        this.farmacia = farmacia;
    }

    public String getNombre() { return nombre;
    }

    public String getDescripcion() { return descripcion;
    }

    public String getFarmacia() { return farmacia;
    }
}
