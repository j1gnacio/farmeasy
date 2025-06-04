package org.example;

public class Medicamento {
    private String idMedicamento;
    private String nombre;
    private String dosis;
    private String descripcion;

    private String precioNormal;
    private String precioInternet;
    private String url;
    private String farmacia;

    public Medicamento(String idMedicamento, String nombre, String dosis, String descripcion,
                       String precioNormal, String precioInternet, String url, String farmacia) {
        this.idMedicamento = idMedicamento;
        this.nombre = nombre;
        this.dosis = dosis;
        this.descripcion = descripcion;
        this.precioNormal = precioNormal;
        this.precioInternet = precioInternet;
        this.url = url;
        this.farmacia = farmacia;
    }

    public void obtenerDetalles() {
        System.out.println("Nombre: " + (nombre != null ? nombre : ""));
        System.out.println("Descripción: " + (descripcion != null ? descripcion : ""));
        System.out.println("Precio: " + (precioNormal != null ? precioNormal : "No disponible"));
        System.out.println("Precio: " + (precioInternet != null ? precioInternet : "No disponible"));
        System.out.println("URL: " + (url != null ? url : "No disponible"));
        System.out.println("Farmacia: " + (farmacia != null ? farmacia : "No disponible"));
    }

    // Getters si los necesitas...

    public String getIdMedicamento() {
        return idMedicamento;
    }

    public void setIdMedicamento(String idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDosis() {
        return dosis;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPrecioNormal() {
        return precioNormal;
    }

    public void setPrecioNormal(String precioNormal) {
        this.precioNormal = precioNormal;
    }

    public String getPrecioInternet() {
        return precioInternet;
    }

    public void setPrecioInternet(String precioInternet) {
        this.precioInternet = precioInternet;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFarmacia() {
        return farmacia;
    }

    public void setFarmacia(String farmacia) {
        this.farmacia = farmacia;
    }
}
