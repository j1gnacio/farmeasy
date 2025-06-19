package org.example.repository;

import org.example.model.HistorialBusqueda;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface HistorialBusquedaRepository extends MongoRepository<HistorialBusqueda, String> {

    /**
     * Encuentra todas las búsquedas de un usuario específico, ordenadas por fecha descendente.
     * @param usuarioId El ID del usuario.
     * @param sort El criterio de ordenación.
     * @return Una lista del historial de búsqueda.
     */
    List<HistorialBusqueda> findByUsuarioId(String usuarioId, Sort sort);
}