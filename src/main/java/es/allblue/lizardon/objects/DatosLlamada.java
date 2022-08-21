package es.allblue.lizardon.objects;

public class DatosLlamada {
    private String query;
    private String idLlamada;

    public DatosLlamada(String query, String idLlamada) {
        this.query = query;
        this.idLlamada = idLlamada;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getIdLlamada() {
        return idLlamada;
    }

    public void setIdLlamada(String idLlamada) {
        this.idLlamada = idLlamada;
    }
}
