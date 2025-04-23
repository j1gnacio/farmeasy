package org.example;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HistorialPrecioFarmacia {
    private String idHistorial;
    private List<RegistroPrecio> historialPrecios = new ArrayList<>();

    // Clase interna para guardar el precio en una fecha específica
    public static class RegistroPrecio {
        private PrecioFarmacia precioFarmacia;
        private LocalDateTime fecha;

        public RegistroPrecio(PrecioFarmacia precioFarmacia, LocalDateTime fecha) {
            this.precioFarmacia = precioFarmacia;
            this.fecha = fecha;
        }

        public void mostrarRegistro() {
            System.out.println("Fecha: " + fecha);
            precioFarmacia.obtenerPrecio();
        }
    }

    public void guardarPrecio(PrecioFarmacia precioActual) {
        RegistroPrecio nuevoRegistro = new RegistroPrecio(precioActual, LocalDateTime.now());
        historialPrecios.add(nuevoRegistro);
        System.out.println("Precio guardado en historial.");
    }

    public HistorialPrecioFarmacia() {}

    public void mostrarHistorial() {
        System.out.println("Historial de precios:");
        for (RegistroPrecio registro : historialPrecios) {
            registro.mostrarRegistro();
        }
    }

    public List<RegistroPrecio> getHistorialPrecios() {
        return historialPrecios;
    }
}

