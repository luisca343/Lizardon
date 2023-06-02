package es.allblue.lizardon.util;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class WingullAPI {
    private String LABOON_URL = "http://79.116.61.79:34301/laboon/";
    private static String WINGULL_URL = "http://79.116.61.79:34301/";
    public static String wingullGET(String str) {
        try {
            URL url = new URL(str);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");


            con.setDoOutput(true);
            con.addRequestProperty("User-Agent", "Mozilla/4.0");
            con.setRequestProperty("Content-Type", "application/json");


            InputStream inputStream = getConnectionStream(url);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();
            System.out.println(response.toString());
            System.out.println("WingullAPI: " + con.getResponseCode());
            return response.toString();



        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void getFromLaboon(URL url, String json) throws IOException {
        WingullAPI.post(LABOON_URL + "circuito", json);
    }


    public static void wingullPOST( String str, String json) {
        post(WINGULL_URL + str, json);
    }


    // SSend post request
    public static void post(String str, String json) {
        new Thread(() -> {
            try {
                URL url = new URL(str);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");

                con.setDoOutput(true);
                con.addRequestProperty("User-Agent", "Mozilla/4.0");
                con.setRequestProperty("Content-Type", "application/json");

                OutputStream os = con.getOutputStream();
                os.write(json.getBytes());
                os.flush();
                os.close();

                InputStream inputStream = con.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                br.close();
                System.out.println(response.toString());
                System.out.println("WingullAPI: " + con.getResponseCode());

            } catch (ProtocolException e) {
                throw new RuntimeException(e);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                System.out.println("WingullAPI: " + e.getMessage());
            }
        }).start();
    }



    private static InputStream getConnectionStream(URL url) {
        try {
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.addRequestProperty("User-Agent", "Mozilla/4.0");

            return con.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


}
