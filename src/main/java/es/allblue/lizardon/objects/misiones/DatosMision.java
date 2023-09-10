package es.allblue.lizardon.objects.misiones;

import java.util.ArrayList;

public class DatosMision {
    private int id;
    private String nombre;
    private String skin;
    private double x;
    private double y;
    private double z;
    private String nombreNPC;
    private String categoria;
    private int siguienteMision;
    private int tipo;
    private String textoCompletar;
    private String textoLog;
    private boolean repetible;
    private ArrayList<RecompensaMision> recompensas;


    private boolean activa;
    private ArrayList<ObjetivoMision> objetivos;
    private String estado;

    public DatosMision() {
        this.recompensas = new ArrayList<>();
        this.siguienteMision = -1;
        this.tipo = 0;
        this.textoCompletar = "";
        this.textoLog = "";
        this.repetible = false;
        this.categoria = "";
        this.nombreNPC = "";
        this.skin = "";
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.nombre = "";
        this.id = -1;

        /*
        this.activa = false;
        this.objetivos = new ArrayList<>();
        this.estado = "";
        */
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getSiguienteMision() {
        return siguienteMision;
    }

    public void setSiguienteMision(int siguienteMision) {
        this.siguienteMision = siguienteMision;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
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

    public boolean isRepetible() {
        return repetible;
    }

    public void setRepetible(boolean repetible) {
        this.repetible = repetible;
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


    public String getNombreNPC() {
        return nombreNPC;
    }

    public void setNombreNPC(String nombreNPC) {
        this.nombreNPC = nombreNPC;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof DatosMision){
            DatosMision mision = (DatosMision) obj;
            return mision.getId() == this.id && mision.getNombreNPC().equals(this.nombreNPC)
                    && mision.getNombre().equals(this.nombre) && mision.getSkin().equals(this.skin)
                    && mision.getX() == this.x && mision.getY() == this.y && mision.getZ() == this.z
                    && mision.getCategoria().equals(this.categoria) && mision.getSiguienteMision() == this.siguienteMision
                    && mision.getTipo() == this.tipo && mision.getTextoCompletar().equals(this.textoCompletar)
                    && mision.getTextoLog().equals(this.textoLog) && mision.isRepetible() == this.repetible;

        }
        return false;
    }
}
