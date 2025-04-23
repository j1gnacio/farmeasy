package org.example;

public class Ubicacion {
    private String idUbicacion;
    private String direccion;
    private String ciudad;
    private double latitud;
    private double longitud;

    public void obtenerCordenada(){
        // Simulación básica. En un caso real se usaría una API externa
        switch (ciudad.toLowerCase()) {
            case "temuco":
                latitud = -38.7359;
                longitud = -72.5904;
                break;
            case "villarrica":
                latitud = -39.2859;
                longitud = -72.2279;
                break;
            case "pucon":
                latitud = -39.2728;
                longitud = -71.9772;
                break;
            case "angol":
                latitud = -37.7972;
                longitud = -72.7147;
                break;
            case "puerto saavedra":
                latitud = -38.7833;
                longitud = -73.3833;
                break;
            default:
                latitud = 0.0;
                longitud = 0.0;
                System.out.println("Ubicación no reconocida, coordenadas por defecto.");
                return;
        }
        System.out.println("Coordenadas para " + ciudad + ": Latitud " + latitud + ", Longitud " + longitud);
    }
    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public String getCiudad() {
        return ciudad;
    }
}
