package org.example;

public class FacturaProducto {
    private int idFactura;
    private int idProducto;
    private int cantidad;

    public FacturaProducto(int idFactura, int idProducto, int cantidad) {
        this.idFactura = idFactura;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
    }

    public int getIdFactura() {
        return idFactura;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public int getCantidad() {
        return cantidad;
    }
}
