package org.example.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "farmacias")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Farmacia {
    @Id
    private String id;

    private String nombre;

    // Ubicación de la farmacia
    private Ubicacion ubicacion;

    // Lista de medicamentos como referencias (opcional si usas relaciones)
    private List<Medicamento> listaMedicamentos;
}
