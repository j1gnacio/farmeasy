package org.example;

import java.util.Date;

public class HistorialBusqueda {
    private Usuario usuario;
    private Date fecha;
    private Busqueda busqueda;

    public HistorialBusqueda(Usuario usuario, Busqueda busqueda) {
        this.usuario = usuario;
        this.busqueda = busqueda;
        this.fecha = new Date(); // Se asigna la fecha actual al momento de crear el historial
    }

    public void registrarBusqueda() {
        System.out.println("Registrando búsqueda realizada por: " + usuario.getNombre());
        System.out.println("Fecha: " + fecha);
        System.out.println("Término buscado: " + busqueda.getTermino());
        if (busqueda.getUbicacion() != null) {
            System.out.println("Ubicación: " + busqueda.getUbicacion().getCiudad());
        }
        if (busqueda.getFarmacia() != null) {
            System.out.println("Farmacia: " + busqueda.getFarmacia().getNombre());
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
