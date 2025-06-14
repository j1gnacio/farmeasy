package org.example.service;

import java.util.logging.Logger;

import org.example.model.Farmacia;
import org.example.model.Medicamento;
import org.example.model.Ubicacion;
import org.springframework.stereotype.Service;

@Service
public class Busqueda {
    private static final Logger logger = Logger.getLogger(Busqueda.class.getName());

    private String termino;
    private Ubicacion ubicacion;
    private Farmacia farmacia;

    public Busqueda() {
        // Constructor sin argumentos para Spring
    }

    public Busqueda(String termino, Ubicacion ubicacion, Farmacia farmacia) {
        this.termino = termino;
        this.ubicacion = ubicacion;
        this.farmacia = farmacia;
    }

    public void realizarBusqueda() {
        if (logger.isLoggable(java.util.logging.Level.INFO)) {
            logger.info("Realizando búsqueda de: " + termino);
        }

        if (ubicacion != null) {
            if (logger.isLoggable(java.util.logging.Level.INFO)) {
                logger.info("Ubicación de búsqueda: " + ubicacion.getCiudad());
            }
        } else {
            if (logger.isLoggable(java.util.logging.Level.INFO)) {
                logger.info("No se especificó ubicación.");
            }
        }

        if (farmacia != null) {
            if (logger.isLoggable(java.util.logging.Level.INFO)) {
                logger.info("Buscando en la farmacia: " + farmacia.getNombre());
            }
            for (Medicamento medicamento : farmacia.getListaMedicamentos()) {
                if (medicamento.getNombre().toLowerCase().contains(termino.toLowerCase())) {
                    if (logger.isLoggable(java.util.logging.Level.INFO)) {
                        logger.info("Medicamento encontrado: " + medicamento.getNombre());
                    }
                    // El método obtenerDetalles() no existe, se elimina la llamada
                    // medicamento.obtenerDetalles();
                }
            }
        } else {
            if (logger.isLoggable(java.util.logging.Level.INFO)) {
                logger.info("No se especificó farmacia.");
            }
        }
    }

    public String getTermino() {
        return termino;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public Farmacia getFarmacia() {
        return farmacia;
    }
}
