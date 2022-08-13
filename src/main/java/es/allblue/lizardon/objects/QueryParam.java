package es.allblue.lizardon.objects;

public class QueryParam {
    String param;
    String valor;

    public QueryParam(String param, String valor) {
        this.param = param;
        this.valor = valor;
    }

    public String getParam() {
        return param;
    }

    public String getQueryParam() {
        return param + "=" + valor;
    }
}
