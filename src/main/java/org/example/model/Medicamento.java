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
    private String precio_internet;
    private String precio_farmacia;
    private String descripcion;
    private String url_producto;
    private String imagen_url;
    private String farmacia;
}
