package org.example.repository;

import org.example.model.Medicamento;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MedicamentoRepository extends MongoRepository<Medicamento, String> {

    List<Medicamento> findByNombreContainingIgnoreCase(String nombre);

    // Nuevo método para buscar por farmacia (ignorando mayúsculas/minúsculas)
    List<Medicamento> findByFarmaciaIgnoreCase(String farmacia);
}
