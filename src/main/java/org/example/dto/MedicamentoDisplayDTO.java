package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Este es un objeto para transferir datos a la vista.
// No está conectado a la base de datos.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicamentoDisplayDTO {
    // Datos generales del medicamento
    private String nombreDisplay;
    private String descripcion;
    private String imagenUrl;

    // Datos específicos de la oferta de una farmacia
    private String farmaciaNombre;
    private Double precioInternet;
    private Double precioFisico;
    private String urlProducto;
}