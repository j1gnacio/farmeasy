package org.example.repository;


import org.example.model.Medicamento;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MedicamentoRepository extends MongoRepository<Medicamento, String> {
}
