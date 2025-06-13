package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrecioInfo {

    private String farmaciaNombre; // El nombre de la farmacia como String

    // Usamos Double para poder hacer comparaciones numéricas.
    // También podrías usar BigDecimal si necesitas máxima precisión financiera.
    private Double precioInternet;

    private Double precioFisico;

    // La URL del producto es específica de cada farmacia.
    private String urlProducto;
}