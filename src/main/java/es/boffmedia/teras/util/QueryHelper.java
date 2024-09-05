package es.boffmedia.teras.util;

import com.google.gson.Gson;
import es.boffmedia.teras.Teras;
import es.boffmedia.teras.client.ClientProxy;
import es.boffmedia.teras.net.Messages;
import es.boffmedia.teras.net.server.SMessageCheckSpawns;
import es.boffmedia.teras.net.server.SMessageDatosServer;
import es.boffmedia.teras.net.server.SMessageDarCaja;
import es.boffmedia.teras.net.serverOld.SMessageFinalizarLlamada;
import es.boffmedia.teras.net.serverOld.SMessageIniciarLlamada;
import es.boffmedia.teras.net.serverOld.SMessageVerMisiones;
import es.boffmedia.teras.objects.post.PokedexEventResponse;
import es.boffmedia.teras.objects_old.serverdata.UserData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.montoyo.mcef.api.IBrowser;
import net.montoyo.mcef.api.IJSQueryCallback;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collection;

public class QueryHelper {
    final static String SUCCESS = "{\"status\": \"ok\"}";
    final static String ERROR = "{\"status\": \"error\"}";

    final static Gson gson = new Gson();

    public static boolean handleQuery(IBrowser iBrowser, long l, String query, boolean b, IJSQueryCallback callback) {
        Teras.LOGGER.info("Query recibida: "+query);
        ClientProxy.callbackMCEF = callback;
        /* Requests 'GET' */
        if(query.contains("getUserData")){
            Messages.INSTANCE.sendToServer(new SMessageDatosServer("query"));
            ClientProxy.callbackMisiones = callback;
            return true;
        }
        if(query.contains("getPlayers")){
            Collection<NetworkPlayerInfo> jugadores = Minecraft.getInstance().getConnection().getOnlinePlayers();
            ArrayList<UserData> usuarios = new ArrayList<>();
            for (NetworkPlayerInfo jugador : jugadores) {
                UserData userData = new UserData(jugador.getProfile().getId().toString(), jugador.getProfile().getName());
                usuarios.add(userData);
            }
            String respuesta = gson.toJson(usuarios);
            callback.success(respuesta);
            return true;
        }
        if(query.contains("getSpawns")){
            Messages.INSTANCE.sendToServer(new SMessageCheckSpawns("getSpawns"));
            return true;
        }
        if(query.contains("getMisiones")){
            ClientProxy.callbackMisiones = callback;
            Messages.INSTANCE.sendToServer(new SMessageVerMisiones(query));
            return true;
        }
        if(query.contains("darCaja")){
            Messages.INSTANCE.sendToServer(new SMessageDarCaja(query));
            callback.success(SUCCESS);
            return true;
        }
        /*
        * ===============================
        *  Requests 'POST'
        * ===============================
        */
        if(query.contains("setCall")){
            Messages.INSTANCE.sendToServer(new SMessageIniciarLlamada(query));
            return true;
        }
        if(query.contains("leaveCall")){
            Messages.INSTANCE.sendToServer(new SMessageFinalizarLlamada(query));
            return true;
        }
        return false;
    }

    public static void handlePOST(StringBuilder response, HttpURLConnection con) throws IOException {
        Teras.LOGGER.info("Se ha recibido una respuesta a una petición POST: ");
        Teras.LOGGER.info(response.toString());
        Teras.LOGGER.info("WingullAPI: " + con.getResponseCode());

        String responseString = response.toString();

        if(responseString.contains("pokedex_event")){
            PokedexEventResponse pokedexEventResponse = gson.fromJson(responseString, PokedexEventResponse.class);
            pokedexEventResponse.sendMessage();
        }
    }

}
