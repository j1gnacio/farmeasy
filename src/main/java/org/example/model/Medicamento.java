package org.example.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "medicamentos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Medicamento {
    @Id
    private String id;
    private String nombre;
    private String descripcion;
    private String url_producto;
    private String imagen_url;
    private List<PrecioInfo> precios = new ArrayList<>();


}
