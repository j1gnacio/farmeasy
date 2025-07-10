package org.example.service;

import org.example.model.HistorialBusqueda;
import org.example.repository.HistorialBusquedaRepository;
import org.example.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class HistorialBusquedaServiceTest {

    @Mock
    private HistorialBusquedaRepository historialBusquedaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private HistorialBusquedaService historialBusquedaService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(usuarioRepository.findByUsername(anyString())).thenReturn(java.util.Optional.of(new org.example.model.Usuario()));
    }

    @Test
    public void testObtenerHistorialPorUsuario() {
        String usuarioId = "usuario1";

        List<HistorialBusqueda> mockHistorial = List.of(
                new HistorialBusqueda("busqueda1", null),
                new HistorialBusqueda("busqueda2", null)
        );

        when(historialBusquedaRepository.findByUsuarioId(eq(usuarioId), org.mockito.ArgumentMatchers.any(org.springframework.data.domain.Sort.class))).thenReturn(mockHistorial);

        List<HistorialBusqueda> resultado = historialBusquedaService.obtenerHistorialPorUsuario(usuarioId);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(historialBusquedaRepository, times(1)).findByUsuarioId(eq(usuarioId), org.mockito.ArgumentMatchers.any());
    }
}
