package org.example.service;

import org.example.model.Medicamento;
import org.example.repository.MedicamentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para MedicamentoService.
 * Verifica la logica de negocio para obtener, buscar y guardar medicamentos.
 */
@ExtendWith(MockitoExtension.class)
class MedicamentoServiceTest {

    @Mock
    private MedicamentoRepository medicamentoRepository;

    @InjectMocks
    private MedicamentoService medicamentoService;

    private Medicamento medicamento1;
    private Medicamento medicamento2;

    /**
     * Prepara datos de prueba antes de cada test.
     */
    @BeforeEach
    void setUp() {
        medicamento1 = new Medicamento();
        medicamento1.setId("med1");
        medicamento1.setNombre("Paracetamol 500mg");

        medicamento2 = new Medicamento();
        medicamento2.setId("med2");
        medicamento2.setNombre("Ibuprofeno 200mg");
    }

    /**
     * Prueba que se devuelve una lista de todos los medicamentos.
     */
    @Test
    void cuandoObtieneTodos_debeDevolverListaDeMedicamentos() {
        when(medicamentoRepository.findAll()).thenReturn(List.of(medicamento1, medicamento2));

        List<Medicamento> resultado = medicamentoService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Paracetamol 500mg", resultado.get(0).getNombre());
        verify(medicamentoRepository).findAll();
    }

    /**
     * Prueba que se devuelve un medicamento cuando se busca por un ID existente.
     */
    @Test
    void cuandoBuscaPorIdExistente_debeDevolverOptionalConMedicamento() {
        when(medicamentoRepository.findById("med1")).thenReturn(Optional.of(medicamento1));

        Optional<Medicamento> resultado = medicamentoService.findById("med1");

        assertTrue(resultado.isPresent());
        assertEquals("Paracetamol 500mg", resultado.get().getNombre());
        verify(medicamentoRepository).findById("med1");
    }

    /**
     * Prueba que se devuelve un Optional vacio al buscar por un ID inexistente.
     */
    @Test
    void cuandoBuscaPorIdInexistente_debeDevolverOptionalVacio() {
        when(medicamentoRepository.findById("id_no_existe")).thenReturn(Optional.empty());

        Optional<Medicamento> resultado = medicamentoService.findById("id_no_existe");

        assertFalse(resultado.isPresent());
        verify(medicamentoRepository).findById("id_no_existe");
    }

    /**
     * Prueba que un medicamento se guarda correctamente.
     */
    @Test
    void cuandoGuardaMedicamento_debeDevolverMedicamentoGuardado() {
        when(medicamentoRepository.save(any(Medicamento.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Medicamento resultado = medicamentoService.guardar(medicamento1);

        assertNotNull(resultado);
        assertEquals("med1", resultado.getId());
        verify(medicamentoRepository).save(medicamento1);
    }

    /**
     * Prueba que la busqueda por nombre devuelve una lista de medicamentos coincidentes.
     */
    @Test
    void cuandoBuscaPorNombre_debeDevolverListaCoincidente() {
        String terminoBusqueda = "parace";
        when(medicamentoRepository.findByNombreContainingIgnoreCase(terminoBusqueda)).thenReturn(List.of(medicamento1));

        List<Medicamento> resultado = medicamentoService.buscarPorNombre(terminoBusqueda);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Paracetamol 500mg", resultado.get(0).getNombre());
        verify(medicamentoRepository).findByNombreContainingIgnoreCase(terminoBusqueda);
    }
}