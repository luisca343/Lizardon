package es.allblue.lizardon.util;

import es.allblue.lizardon.Lizardon;
import okhttp3.*;

import java.io.IOException;

public class PostRequest implements Runnable {

    String endpoint;
    String datos;
    String res;

    public PostRequest(String endpoint, String datos) {
        this.endpoint = endpoint;
        this.datos = datos;
    }

    @Override
    public void run() {
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        // put your json here
        RequestBody body = RequestBody.create(JSON, datos);
        Request request = new Request.Builder()
                .url("http://api.lizardon.es/pixelmon/" + endpoint)
                .post(body)
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
