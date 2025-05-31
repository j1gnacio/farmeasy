package org.example.repository;

import org.example.model.Farmacia;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FarmaciaRepository extends MongoRepository<Farmacia, String> {
    List<Farmacia> findByNombreContainingIgnoreCase(String nombre);
}
