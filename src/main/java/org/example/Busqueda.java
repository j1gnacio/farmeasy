package org.example;

import java.util.logging.Logger;

public class Busqueda {
    private static final Logger logger = Logger.getLogger(Busqueda.class.getName());

    private String termino;
    private Ubicacion ubicacion;
    private Farmacia farmacia;

    public void realizarBusqueda() {
        logger.info("Realizando búsqueda de: " + termino);

        if (ubicacion != null) {
            logger.info("Ubicación de búsqueda: " + ubicacion.getCiudad());
        } else {
            logger.info("No se especificó ubicación.");
        }

        if (farmacia != null) {
            logger.info("Buscando en la farmacia: " + farmacia.getNombre());
            for (Medicamento medicamento : farmacia.getListaMedicamentos()) {
                if (medicamento.getNombre().toLowerCase().contains(termino.toLowerCase())) {
                    logger.info("Medicamento encontrado: " + medicamento.getNombre());
                    medicamento.obtenerDetalles();
                }
            }
        } else {
            logger.info("No se especificó farmacia.");
        }
    }

    public Busqueda(String termino, Ubicacion ubicacion, Farmacia farmacia) {
        this.termino = termino;
        this.ubicacion = ubicacion;
        this.farmacia = farmacia;
    }





    // Getters y setters si son necesarios


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
