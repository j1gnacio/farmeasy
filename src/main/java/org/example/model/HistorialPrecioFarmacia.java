package org.example.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID; // Importar para generar un ID único
import java.util.logging.Logger;

public class HistorialPrecioFarmacia {
    private static final Logger logger = Logger.getLogger(HistorialPrecioFarmacia.class.getName());

    private String idHistorial; // ID único para el historial
    private List<RegistroPrecio> historialPrecios = new ArrayList<>();

    // Clase interna para guardar el precio en una fecha específica
    public static class RegistroPrecio {
        private static final Logger logger = Logger.getLogger(RegistroPrecio.class.getName());

        private PrecioFarmacia precioFarmacia;
        private LocalDateTime fecha;

        public RegistroPrecio(PrecioFarmacia precioFarmacia, LocalDateTime fecha) {
            this.precioFarmacia = precioFarmacia;
            this.fecha = fecha;
        }

        public void mostrarRegistro() {
            logger.info("Fecha: " + fecha);
            precioFarmacia.obtenerPrecio();
        }
    }

    // Constructor donde se asigna el ID único
    public HistorialPrecioFarmacia() {
        this.idHistorial = UUID.randomUUID().toString(); // Generar un ID único
    }

    public void guardarPrecio(PrecioFarmacia precioActual) {
        RegistroPrecio nuevoRegistro = new RegistroPrecio(precioActual, LocalDateTime.now());
        historialPrecios.add(nuevoRegistro);
        logger.info("Precio guardado en historial.");
    }
    
    public void mostrarHistorial() {
        logger.info("Historial de precios (ID: " + idHistorial + "):");
        for (RegistroPrecio registro : historialPrecios) {
            registro.mostrarRegistro();
        }
    }

    public List<RegistroPrecio> getHistorialPrecios() {
        return historialPrecios;
    }

    public String getIdHistorial() { // Método getter para idHistorial
        return idHistorial;
    }
}
