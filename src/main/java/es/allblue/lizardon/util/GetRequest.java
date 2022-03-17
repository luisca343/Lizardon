package es.allblue.lizardon.util;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.objects.requests.CocheJSON;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
            Gson gson = new Gson();
            switch(endpoint){
                case "coches":
                    List<CocheJSON> coches = stringToArray(resStr, CocheJSON[].class);
                    for(int i = 0; i < coches.size(); i++){
                        Lizardon.getLogger().info(coches.get(i).getNombre());
                        Lizardon.getLogger().info(coches.get(i).getDatos());
                    }
                return;
            }
            Lizardon.getLogger().info(resStr);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> List<T> stringToArray(String s, Class<T[]> clazz) {
        T[] arr = new Gson().fromJson(s, clazz);
        return Arrays.asList(arr); //or return Arrays.asList(new Gson().fromJson(s, clazz)); for a one-liner
    }

    public String getRes() {
        return res;
    }
}
