package org.example;

import java.util.logging.Logger;

public class PrecioFarmacia {
    private static final Logger logger = Logger.getLogger(PrecioFarmacia.class.getName());

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
        logger.info("Farmacia: " + farmacia.getNombre());
        logger.info("Medicamento: " + medicamento.getNombre());
        logger.info("Precio normal: $" + precioNormal);
        logger.info("Precio oferta: $" + precioOferta);
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
