package org.example.service;

import org.example.model.Medicamento;
import org.example.repository.MedicamentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MedicamentoServiceTest {

    @Mock
    private MedicamentoRepository medicamentoRepository;

    @InjectMocks
    private MedicamentoService medicamentoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testObtenerTodosMedicamentos() {
        Medicamento med1 = new Medicamento("id1", "Med1", "desc1", "marca1", 10.0, 12.0, "cat1", "pres1");
        Medicamento med2 = new Medicamento("id2", "Med2", "desc2", "marca2", 20.0, 22.0, "cat2", "pres2");

        when(medicamentoRepository.findAll()).thenReturn(List.of(med1, med2));

        List<Medicamento> resultado = medicamentoService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(medicamentoRepository, times(1)).findAll();
    }

    @Test
    public void testObtenerMedicamentoPorId() {
        Medicamento med = new Medicamento("id1", "Med1", "desc1", "marca1", 10.0, 12.0, "cat1", "pres1");

        when(medicamentoRepository.findById("id1")).thenReturn(Optional.of(med));

        Optional<Medicamento> resultado = medicamentoService.findById("id1");

        assertTrue(resultado.isPresent());
        assertEquals("Med1", resultado.get().getNombre());
        verify(medicamentoRepository, times(1)).findById("id1");
    }

    @Test
    public void testBuscarPorNombreConResultadoVacio() {
        when(medicamentoRepository.findByNombreContainingIgnoreCase("noexiste")).thenReturn(List.of());

        List<Medicamento> resultado = medicamentoService.buscarPorNombre("noexiste");

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(medicamentoRepository, times(1)).findByNombreContainingIgnoreCase("noexiste");
    }

    @Test
    public void testGuardarMedicamentoNulo() {
        assertThrows(NullPointerException.class, () -> {
            medicamentoService.guardar(null);
        });
    }
}
