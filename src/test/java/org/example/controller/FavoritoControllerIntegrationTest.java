package org.example.controller;

import org.example.model.Favorito;
import org.example.model.Medicamento;
import org.example.model.Usuario;
import org.example.service.FavoritoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FavoritoControllerIntegrationTest {

    @Mock
    private FavoritoService favoritoService;

    @InjectMocks
    private FavoritoController favoritoController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testVerMisFavoritos() {
        String username = "usuario1";
        Usuario usuario = new Usuario();
        Medicamento medicamento = new Medicamento("idMed", "nombre", "desc", "marca", 0.0, 0.0, "categoria", "presentacion");
        List<Favorito> mockFavoritos = List.of(new Favorito(usuario, medicamento), new Favorito(usuario, medicamento));

        when(favoritoService.obtenerFavoritosPorUsuario(username)).thenReturn(mockFavoritos);

        Model model = mock(Model.class);
        Principal principal = () -> username;

        String viewName = favoritoController.verMisFavoritos(model, principal);

        assertEquals("favoritos/lista", viewName);
    }
}
