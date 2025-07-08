package org.example.service;

import org.example.model.HistorialBusqueda;
import org.example.model.Usuario;
import org.example.repository.HistorialBusquedaRepository;
import org.example.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio para gestionar el historial de búsqueda de los usuarios.
 */
@Service
public class HistorialBusquedaService {

    @Autowired
    private HistorialBusquedaRepository historialRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Guarda un nuevo registro de búsqueda para un usuario.
     *
     * @param termino El texto que buscó el usuario.
     * @param username El nombre de usuario del usuario logueado.
     */
    public void guardarBusqueda(String termino, String username) {
        // No guardamos búsquedas vacías
        if (termino == null || termino.trim().isEmpty()) {
            return;
        }

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        HistorialBusqueda nuevaBusqueda = new HistorialBusqueda(termino, usuario);
        historialRepository.save(nuevaBusqueda);
    }

    /**
     * Obtiene el historial de búsqueda de un usuario.
     *
     * @param username El nombre del usuario.
     * @return Una lista con su historial, ordenado del más reciente al más antiguo.
     */
    public List<HistorialBusqueda> obtenerHistorialPorUsuario(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // Ordenamos por fecha de búsqueda de forma descendente
        return historialRepository.findByUsuarioId(usuario.getId(), Sort.by(Sort.Direction.DESC, "fechaBusqueda"));
    }
}