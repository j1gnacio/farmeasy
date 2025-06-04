package org.example.model;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "farmacias")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Farmacia {
    @Id
    private String id;

    private String nombre;

    // Lista de medicamentos como referencias (opcional si usas relaciones)
    private List<Medicamento> listaMedicamentos;
}
