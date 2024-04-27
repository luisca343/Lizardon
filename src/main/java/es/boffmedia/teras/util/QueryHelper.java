package es.boffmedia.teras.util;

import com.google.gson.Gson;
import com.pixelmonmod.pixelmon.api.spawning.AbstractSpawner;
import es.boffmedia.teras.Teras;
import es.boffmedia.teras.client.ClientProxy;
import es.boffmedia.teras.net.Messages;
import es.boffmedia.teras.net.server.SMessageCheckSpawns;
import es.boffmedia.teras.net.server.SMessageDatosServer;
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
import java.util.List;

public class QueryHelper {

    public static boolean handleQuery(IBrowser iBrowser, long l, String query, boolean b, IJSQueryCallback callback) {
        Gson gson = new Gson();
        Teras.LOGGER.info("Query recibida: "+query);
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
            ClientProxy.callbackMCEF = callback;
            Messages.INSTANCE.sendToServer(new SMessageCheckSpawns("getSpawns"));
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
            PokedexEventResponse pokedexEventResponse = new Gson().fromJson(responseString, PokedexEventResponse.class);
            pokedexEventResponse.sendMessage();
        }
    }

}
