package org.example.model;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "medicamentos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medicamento {

    @Id
    private String id;

    private String nombre;
    private String descripcion;
    private String descripcionBreve;
    private String dosis;
    private String principioActivo;
    private String composicion;
    private String precioFarmacia;
    private String precioInternet;
    private String imagenUrl;
    private String urlProducto;
    private String farmacia;
}
