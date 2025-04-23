package org.example;

public class PrecioFarmacia {
    private Farmacia farmacia;
    private Medicamento medicamento;
    private double precioNormal;
    private double precioOferta;

    public PrecioFarmacia(Farmacia farmacia, Medicamento medicamento, double precioNormal, double precioOferta){
        this.farmacia=farmacia;
        this.medicamento=medicamento;
        this.precioNormal=precioNormal;
        this.precioOferta=precioOferta;
    }

    public void obtenerPrecio(){
        System.out.println("Farmacia: " + farmacia.getNombre());
        System.out.println("Medicamento: " + medicamento.getNombre());
        System.out.println("Precio normal: $" + precioNormal);
        System.out.println("Precio oferta: $" + precioOferta);
    }
    public Farmacia getFarmacia() {
        return farmacia;
    }

    public void setFarmacia(Farmacia farmacia) {
        this.farmacia = farmacia;
    }

    public Medicamento getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }

    public double getPrecioNormal() {
        return precioNormal;
    }

    public void setPrecioNormal(double precioNormal) {
        this.precioNormal = precioNormal;
    }

    public double getPrecioOferta() {
        return precioOferta;
    }

    public void setPrecioOferta(double precioOferta) {
        this.precioOferta = precioOferta;
    }
}
