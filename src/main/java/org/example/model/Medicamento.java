package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field; // Asegúrate de que este import esté

import java.util.ArrayList;
import java.util.List;

@Document(collection = "medicamentos2")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Medicamento {

    @Id
    private String id;

    // Campos normalizados para búsqueda y visualización
    @Field("nombre_display") // Así se llama en la DB
    private String nombreDisplay;    // Así lo usamos en Java (camelCase)

    @Field("nombre_normalizado") // Así se llama en la DB
    private String nombreNormalizado;  // Así lo usamos en Java (camelCase)

    private String descripcion;

    @Field("imagen_url") // Así se llama en la DB
    private String imagenUrl;      // Así lo usamos en Java (camelCase)

    private List<PrecioInfo> precios = new ArrayList<>();
}