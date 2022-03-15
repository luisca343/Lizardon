package es.allblue.lizardon.util;

import com.google.gson.Gson;

public class RestApi {

    public static String get(String endpoint) {
        Gson gson = new Gson();
        GetRequest request = new GetRequest(endpoint);
        Thread t = new Thread();
        t.start();
        return "";
    }


    public static String post(String endpoint, Object datos) {
        Gson gson = new Gson();
        String json = gson.toJson(datos);
        PostRequest request = new PostRequest(endpoint, json);
        Thread t = new Thread(request);
        t.start();
        return "";
    }
}
