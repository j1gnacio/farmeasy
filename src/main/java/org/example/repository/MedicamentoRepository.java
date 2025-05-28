package org.example.repository;

import org.example.model.Medicamento;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MedicamentoRepository extends MongoRepository<Medicamento, String> {

    // Puedes agregar métodos personalizados aquí si lo deseas11
    List<Medicamento> findByNombreContainingIgnoreCase(String nombre);
    List<Medicamento> findByFarmacia(String farmacia);
}
