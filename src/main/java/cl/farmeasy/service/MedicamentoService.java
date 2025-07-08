package cl.farmeasy.service;

import java.util.List;
import java.util.Optional;

import cl.farmeasy.repository.MedicamentoRepository;
import cl.farmeasy.model.Medicamento;
import org.springframework.stereotype.Service;

/**
 * Servicio para la logica de negocio relacionada con los medicamentos.
 */
@Service
public class MedicamentoService {

    private final MedicamentoRepository medicamentoRepository;

    /**
     * Constructor para inyectar las dependencias del servicio de medicamentos.
     *
     * @param medicamentoRepository El repositorio para el acceso a datos de medicamentos.
     */
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
     * Busca un medicamento especifico por su ID.
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
     * Busca medicamentos por su nombre, ignorando mayusculas y minusculas.
     *
     * @param nombre El nombre del medicamento a buscar.
     * @return Una lista de medicamentos que coinciden con el nombre.
     */
    public List<Medicamento> buscarPorNombre(String nombre) {
        return medicamentoRepository.findByNombreContainingIgnoreCase(nombre);
    }
}