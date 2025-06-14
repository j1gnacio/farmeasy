package org.example.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "historial_precio_farmacia")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistorialPrecioFarmacia {
    @Id
    private String idHistorial = UUID.randomUUID().toString();

    @Field("historial_precios")
    private List<RegistroPrecio> historialPrecios = new ArrayList<>();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegistroPrecio {
        private PrecioFarmacia precioFarmacia;
        private LocalDateTime fecha;
    }

    public HistorialPrecioFarmacia(List<RegistroPrecio> historialPrecios) {
        this.historialPrecios = historialPrecios;
    }
}
