package es.allblue.lizardon.objects;

public class ItemTienda {
    String nombre;
    int compra;
    int venta;

    public ItemTienda(String nombre, int compra, int venta) {
        this.nombre = nombre;
        this.compra = compra;
        this.venta = venta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCompra() {
        return compra;
    }

    public void setCompra(int compra) {
        this.compra = compra;
    }

    public int getVenta() {
        return venta;
    }

    public void setVenta(int venta) {
        this.venta = venta;
    }
}
