package org.example.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "medicamentos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Medicamento {
    @Id
    private String id;
    private String nombre;
    private String precio_internet;
    private String descripcion;
    private String url_producto;
    private String imagen_url;

}
