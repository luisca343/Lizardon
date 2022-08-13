package es.allblue.lizardon.objects.requests;

import es.allblue.lizardon.Lizardon;

public class Transaccion {
    String conceptoImg;
    String usuario;
    String receptor;
    String concepto;
    String descripcion;
    double cantidad;
    double balance;
    boolean ejecutada;


    String mundo;

    public Transaccion(String usuario, String receptor, String concepto, String descripcion, String conceptoImg, double cantidad, double balance, boolean ejecutada) {
        this.usuario = usuario;
        this.receptor = receptor;
        this.concepto = concepto;
        this.descripcion = descripcion;
        this.conceptoImg = conceptoImg;
        this.cantidad = cantidad;
        this.balance = balance;
        this.ejecutada = ejecutada;
        mundo = Lizardon.NOMBRE_MUNDO;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public String getConceptoImg() {
        return conceptoImg;
    }

    public void setConceptoImg(String conceptoImg) {
        this.conceptoImg = conceptoImg;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getReceptor() {
        return receptor;
    }

    public void setReceptor(String receptor) {
        this.receptor = receptor;
    }

    public boolean isEjecutada() {
        return ejecutada;
    }

    public void setEjecutada(boolean ejecutada) {
        this.ejecutada = ejecutada;
    }

    public String getMundo() {
        return mundo;
    }

    public void setMundo(String mundo) {
        this.mundo = mundo;
    }
}
