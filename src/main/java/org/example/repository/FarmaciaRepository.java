package org.example.repository;

import java.util.List;

import org.example.model.Farmacia;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface FarmaciaRepository extends MongoRepository<Farmacia, String> {
    List<Farmacia> findByNombreContainingIgnoreCase(String nombre);

    // Consulta para encontrar farmacias cerca de un punto dado dentro de una distancia (en metros)
    @Query("{'ubicacion': { $nearSphere: { $geometry: { type: 'Point', coordinates: [?0, ?1] }, $maxDistance: ?2 } } }")
    List<Farmacia> findByUbicacionNear(double longitude, double latitude, double maxDistance);
}
