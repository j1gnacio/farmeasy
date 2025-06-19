package org.example.repository;

import org.example.model.Favorito;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface FavoritoRepository extends MongoRepository<Favorito, String> {

    // Encuentra todos los favoritos de un usuario específico
    List<Favorito> findByUsuarioId(String usuarioId);

    // Verifica si ya existe un favorito para un usuario y un medicamento
    Optional<Favorito> findByUsuarioIdAndMedicamentoId(String usuarioId, String medicamentoId);

    // Para eliminar un favorito
    void deleteByUsuarioIdAndMedicamentoId(String usuarioId, String medicamentoId);
}