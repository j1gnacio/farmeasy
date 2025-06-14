package org.example.repository;

import org.example.model.Medicamento;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MedicamentoRepository extends MongoRepository<Medicamento, String> {
    // Le decimos a Spring que busque en el campo "nombre_display"
    List<Medicamento> findByNombreDisplayContainingIgnoreCase(String nombreDisplay);
}