package org.example.model;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Representa un medicamento en el sistema.
 * Almacena información detallada sobre el medicamento, incluyendo precios y datos de la farmacia.
 */
@Document(collection = "medicamentos")
@Data
@Builder
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

    /**
     * Constructor para crear una instancia de Medicamento con todos los detalles.
     *
     * @param id El identificador único del medicamento.
     * @param nombre El nombre del medicamento.
     * @param descripcion Una breve descripción del medicamento.
     * @param precioInternet El precio del medicamento en línea.
     * @param precioFarmacia El precio del medicamento en la farmacia física.
     * @param urlProducto La URL de la página del producto.
     * @param farmacia El nombre de la farmacia que vende el medicamento.
     */
    public Medicamento(String id, String nombre, String descripcion, Double precioInternet, Double precioFarmacia, String urlProducto, String farmacia) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioInternet = precioInternet;
        this.precioFarmacia = precioFarmacia;
        this.urlProducto = urlProducto;
        this.farmacia = farmacia;
    }

    /**
     * Obtiene el precio en línea del medicamento.
     *
     * @return El precio en línea del medicamento.
     */
    public Double getPrecioInternet() {
        return precioInternet;
    }

    /**
     * Obtiene el precio en la farmacia física.
     *
     * @return El precio en la farmacia física.
     */
    public Double getPrecioNormal() {
        return precioFarmacia;
    }

    /**
     * Obtiene la URL de la página del producto.
     *
     * @return La URL del producto.
     */
    public String getUrl() {
        return urlProducto;
    }

    /**
     * Obtiene el nombre del medicamento.
     *
     * @return El nombre del medicamento.
     */
    public String getNombre() { return nombre;
    }

    /**
     * Obtiene la descripción del medicamento.
     *
     * @return La descripción del medicamento.
     */
    public String getDescripcion() { return descripcion;
    }

    /**
     * Obtiene el nombre de la farmacia.
     *
     * @return El nombre de la farmacia.
     */
    public String getFarmacia() { return farmacia;
    }
}
