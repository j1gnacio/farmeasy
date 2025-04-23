package org.example;

public class AlertaPrecio {
    private Usuario usuario;
    private Medicamento medicamento;
    private double precioObjetivo;
    private boolean alertaActiva;

    public AlertaPrecio(Usuario usuario, Medicamento medicamento, double precioObjetivo) {
        this.usuario = usuario;
        this.medicamento = medicamento;
        this.precioObjetivo = precioObjetivo;
        this.alertaActiva = false;
    }

    // Método para verificar si el precio ha bajado del precio objetivo
    public void verificarPrecio(double precioActual) {
        if (precioActual < precioObjetivo && !alertaActiva) {
            alertaActiva = true;
            System.out.println("¡Alerta! El precio de " + medicamento.getNombre() + " ha bajado de " +
                    precioObjetivo + " a " + precioActual + ". ¡Es momento de comprar!");
        }
    }

    // Método para crear la alerta
    public void crearAlerta() {
        // Agregar la alerta a una lista de alertas (si existe una en Usuario u otro sistema)
        usuario.agregarAlerta(this);
        System.out.println("Alerta de precio creada para el medicamento: " + medicamento.getNombre());
    }

    // Método para desactivar la alerta (en caso de que el usuario desee desactivarla)
    public void desactivarAlerta() {
        alertaActiva = false;
        System.out.println("La alerta de precio para el medicamento " + medicamento.getNombre() + " ha sido desactivada.");
    }

    // Métodos getter y setter
    public Usuario getUsuario() {
        return usuario;
    }

    public Medicamento getMedicamento() {
        return medicamento;
    }

    public double getPrecioObjetivo() {
        return precioObjetivo;
    }

    public boolean isAlertaActiva() {
        return alertaActiva;
    }
}

