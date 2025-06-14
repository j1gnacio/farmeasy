package org.example.service;

import org.example.model.Farmacia;
import org.example.model.Medicamento;
import org.example.model.Ubicacion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class Busqueda {
    private static final Logger logger = LoggerFactory.getLogger(Busqueda.class);

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
        logger.info("Realizando búsqueda de: {}", termino);

        if (ubicacion != null) {
            logger.info("Ubicación de búsqueda: {}", ubicacion.getCiudad());
        } else {
            logger.info("No se especificó ubicación.");
        }

        if (farmacia != null) {
            logger.info("Buscando en la farmacia: {}", farmacia.getNombre());
            for (Medicamento medicamento : farmacia.getListaMedicamentos()) {
                if (medicamento.getNombre().toLowerCase().contains(termino.toLowerCase())) {
                    logger.info("Medicamento encontrado: {}", medicamento.getNombre());
                }
            }
        } else {
            logger.info("No se especificó farmacia.");
        }
    }

    public String getTermino() {
        return termino;
    }

    public void setTermino(String termino) {
        this.termino = termino;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Farmacia getFarmacia() {
        return farmacia;
    }

    public void setFarmacia(Farmacia farmacia) {
        this.farmacia = farmacia;
    }
}
