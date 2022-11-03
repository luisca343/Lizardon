package es.allblue.lizardon.objects;
/*
*               mision.setSiguienteMision(quest.getNextQuest());
                mision.setTipo(quest.getType());
                mision.setTextoFinal(quest.getCompleteText());
                mision.setTextoLog(quest.getLogText());
*
* */


public class Mision {
    private String nombre;
    private int siguienteMision;
    private int tipo;
    private String textoCompletar;
    private String textoLog;

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
}
