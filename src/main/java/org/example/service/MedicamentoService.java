package org.example.service;

import java.util.List;
import java.util.Optional;

import org.example.model.Medicamento;
import org.example.repository.MedicamentoRepository;
import org.springframework.stereotype.Service;

/**
 * Servicio para la lógica de negocio relacionada con los medicamentos.
 */
@Service
public class MedicamentoService {

    private final MedicamentoRepository medicamentoRepository;

    // Se elimina la dependencia de FarmaciaService para simplificar el proyecto.
    public MedicamentoService(MedicamentoRepository medicamentoRepository) {
        this.medicamentoRepository = medicamentoRepository;
    }

    /**
     * Obtiene todos los medicamentos del repositorio.
     *
     * @return Una lista de todos los medicamentos.
     */
    public List<Medicamento> obtenerTodos() {
        return medicamentoRepository.findAll();
    }

    /**
     * Busca un medicamento específico por su ID.
     *
     * @param id El ID del medicamento a buscar.
     * @return Un Optional que puede contener el medicamento si se encuentra.
     */
    public Optional<Medicamento> findById(String id) {
        return medicamentoRepository.findById(id);
    }

    /**
     * Guarda un medicamento en el repositorio.
     *
     * @param medicamento El medicamento a guardar.
     * @return El medicamento guardado.
     */
    public Medicamento guardar(Medicamento medicamento) {
        return medicamentoRepository.save(medicamento);
    }

    /**
     * Busca medicamentos por su nombre, ignorando mayúsculas y minúsculas.
     *
     * @param nombre El nombre del medicamento a buscar.
     * @return Una lista de medicamentos que coinciden con el nombre.
     */
    public List<Medicamento> buscarPorNombre(String nombre) {
        return medicamentoRepository.findByNombreContainingIgnoreCase(nombre);
    }

}