package org.example.repository;

import org.example.model.Medicamento;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Repositorio de Spring Data MongoDB para la entidad Medicamento.
 */
public interface MedicamentoRepository extends MongoRepository<Medicamento, String> {

    /**
     * Busca medicamentos por nombre, ignorando mayúsculas y minúsculas.
     *
     * @param nombre El nombre del medicamento a buscar.
     * @return Una lista de medicamentos que coinciden con el nombre.
     */
    List<Medicamento> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Busca medicamentos por el nombre de la farmacia, ignorando mayúsculas y minúsculas.
     *
     * @param farmacia El nombre de la farmacia.
     * @return Una lista de medicamentos de esa farmacia.
     */
    List<Medicamento> findByFarmaciaIgnoreCase(String farmacia);
}