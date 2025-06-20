package org.example.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "medicamentos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Medicamento {
    @Id
    private String id;
    private String nombre;
    private Double precioInternet;
    private Double precioFarmacia;
    private String descripcion;
    @Field("url_producto")
    private String urlProducto;
    @Field("imagen_url")
    private String imagenUrl;
    private String farmacia;
    private String dosis;

    public String getDosis() {
        return dosis;
    }

    public Double getPrecioInternet() {
        return precioInternet;
    }

    public Double getPrecioNormal() {
        return precioFarmacia;
    }

    public String getUrl() {
        return urlProducto;
    }

    public Medicamento(String id, String nombre, String dosis, String descripcion, Double precioInternet, Double precioFarmacia, String urlProducto, String farmacia) {
        this.id = id;
        this.nombre = nombre;
        this.dosis = dosis;
        this.descripcion = descripcion;
        this.precioInternet = precioInternet;
        this.precioFarmacia = precioFarmacia;
        this.urlProducto = urlProducto;
        this.farmacia = farmacia;
    }

    public String getNombre() { return nombre;
    }

    public String getDescripcion() { return descripcion;
    }

    public String getFarmacia() { return farmacia;
    }
}
