package cl.farmeasy.repository;

import cl.farmeasy.model.Favorito;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio de Spring Data MongoDB para la entidad Favorito.
 */
public interface FavoritoRepository extends MongoRepository<Favorito, String> {

    /**
     * Encuentra todos los favoritos de un usuario específico.
     *
     * @param usuarioId El ID del usuario.
     * @return Una lista de favoritos del usuario.
     */
    List<Favorito> findByUsuarioId(String usuarioId);

    /**
     * Verifica si ya existe un favorito para un usuario y un medicamento.
     *
     * @param usuarioId El ID del usuario.
     * @param medicamentoId El ID del medicamento.
     * @return Un Optional que contiene el favorito si existe.
     */
    Optional<Favorito> findByUsuarioIdAndMedicamentoId(String usuarioId, String medicamentoId);

    /**
     * Elimina un favorito por el ID del usuario y el ID del medicamento.
     *
     * @param usuarioId El ID del usuario.
     * @param medicamentoId El ID del medicamento.
     */
    void deleteByUsuarioIdAndMedicamentoId(String usuarioId, String medicamentoId);
}