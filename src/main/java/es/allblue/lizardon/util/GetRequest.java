package es.allblue.lizardon.util;

import okhttp3.*;

import java.io.IOException;

public class GetRequest implements  Runnable{

    String endpoint;
    String datos;
    String res;

    public GetRequest(String endpoint) {
        this.endpoint = endpoint;
        this.datos = datos;
    }

    @Override
    public void run() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://api.lizardon.es/pixelmon/"+endpoint)
                .get()
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            String resStr = response.body().string();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getRes() {
        return res;
    }
}
