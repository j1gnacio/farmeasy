package org.example;

import java.util.Date;
import java.util.logging.Logger;
import java.util.logging.Level;

public class HistorialBusqueda {
    private static final Logger logger = Logger.getLogger(HistorialBusqueda.class.getName());

    private Usuario usuario;
    private Date fecha;
    private Busqueda busqueda;

    public HistorialBusqueda(Usuario usuario, Busqueda busqueda) {
        this.usuario = usuario;
        this.busqueda = busqueda;
        this.fecha = new Date(); // Se asigna la fecha actual al momento de crear el historial
    }

    public void registrarBusqueda() {
        logger.info("Registrando búsqueda realizada por: " + usuario.getNombre());
        logger.info("Fecha: " + fecha);
        logger.info("Término buscado: " + busqueda.getTermino());
        if (busqueda.getUbicacion() != null) {
            logger.info("Ubicación: " + busqueda.getUbicacion().getCiudad());
        }
        if (busqueda.getFarmacia() != null) {
            logger.info("Farmacia: " + busqueda.getFarmacia().getNombre());
        }
    }

    // Getters (por si necesitas acceder desde otra clase)
    public Usuario getUsuario() {
        return usuario;
    }

    public Date getFecha() {
        return fecha;
    }

    public Busqueda getBusqueda() {
        return busqueda;
    }
}
