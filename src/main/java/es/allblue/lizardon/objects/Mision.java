package es.allblue.lizardon.objects;

import java.util.ArrayList;

public class Mision {
    private Integer id;
    private String nombre;
    private String categoria;
    private int siguienteMision;
    private int tipo;
    private String textoCompletar;
    private String textoLog;
    private String nombreNPC;
    private boolean repetible;
    private ArrayList<RecompensaMision> recompensas;
    private boolean activa;
    private String skin;
    private ArrayList<ObjetivoMision> objetivos;
    private String estado;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getSiguienteMision() {
        return siguienteMision;
    }

    public void setSiguienteMision(int siguienteMision) {
        this.siguienteMision = siguienteMision;
    }

    public String getTextoCompletar() {
        return textoCompletar;
    }

    public void setTextoCompletar(String textoCompletar) {
        this.textoCompletar = textoCompletar;
    }

    public String getTextoLog() {
        return textoLog;
    }

    public void setTextoLog(String textoLog) {
        this.textoLog = textoLog;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getNombreNPC() {
        return nombreNPC;
    }

    public void setNombreNPC(String nombreNPC) {
        this.nombreNPC = nombreNPC;
    }

    public boolean isRepetible() {
        return repetible;
    }

    public void setRepetible(boolean repetible) {
        this.repetible = repetible;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public ArrayList<RecompensaMision> getRecompensas() {
        return recompensas;
    }

    public void setRecompensas(ArrayList<RecompensaMision> recompensas) {
        this.recompensas = recompensas;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ArrayList<ObjetivoMision> getObjetivos() {
        return objetivos;
    }

    public void setObjetivos(ArrayList<ObjetivoMision> objetivos) {
        this.objetivos = objetivos;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

}
