package org.example;

public class Busqueda {
    private String termino;
    private Ubicacion ubicacion;
    private Farmacia farmacia;

    public void realizarBusqueda() {
        System.out.println("Realizando búsqueda de: " + termino);

        if (ubicacion != null) {
            System.out.println("Ubicación de búsqueda: " + ubicacion.getCiudad());
        } else {
            System.out.println("No se especificó ubicación.");
        }

        if (farmacia != null) {
            System.out.println("Buscando en la farmacia: " + farmacia.getNombre());
            for (Medicamento medicamento : farmacia.getListaMedicamentos()) {
                if (medicamento.getNombre().toLowerCase().contains(termino.toLowerCase())) {
                    System.out.println("Medicamento encontrado: " + medicamento.getNombre());
                    medicamento.obtenerDetalles();
                }
            }
        } else {
            System.out.println("No se especificó farmacia.");
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
