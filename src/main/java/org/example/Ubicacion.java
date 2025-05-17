package org.example;

import java.util.UUID;
import java.util.logging.Logger;
import java.util.logging.Level;

public class Ubicacion {
    private static final Logger logger = Logger.getLogger(Ubicacion.class.getName());

    private String idUbicacion;
    private String direccion;
    private String ciudad;
    private double latitud;
    private double longitud;

    // Constructor que inicializa todos los atributos
    public Ubicacion(String direccion, String ciudad) {
        this.idUbicacion = UUID.randomUUID().toString(); // Generar un ID único
        this.direccion = direccion;
        this.ciudad = ciudad;
        obtenerCoordenadas(); // Obtener coordenadas al crear la ubicación
    }

    // Método para obtener coordenadas basado en la ciudad
    private void obtenerCoordenadas() {
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
                logger.info("Ubicación no reconocida, coordenadas por defecto.");
                return;
        }
        logger.info("Coordenadas para " + ciudad + ": Latitud " + latitud + ", Longitud " + longitud);
    }

    // Métodos de acceso (getters)
    public String getIdUbicacion() {
        return idUbicacion;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    // Método para establecer coordenadas manualmente (opcional)
    public void setCoordenadas(double latitud, double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }
}
