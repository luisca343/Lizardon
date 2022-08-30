package es.allblue.lizardon.util;

import com.google.gson.Gson;
import es.allblue.lizardon.net.Messages;
import es.allblue.lizardon.net.server.SMessageFinalizarLlamada;
import es.allblue.lizardon.net.server.SMessageIniciarLlamada;
import es.allblue.lizardon.objects.UserData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.montoyo.mcef.api.IBrowser;
import net.montoyo.mcef.api.IJSQueryCallback;

import java.util.ArrayList;
import java.util.Collection;

public class QueryHelper {

    public static boolean handleQuery(IBrowser iBrowser, long l, String query, boolean b, IJSQueryCallback callback) {
        System.out.println("ESTO ES UNA PRUEBA");
        System.out.println(query);
        Gson gson = new Gson();

        /* Requests 'GET' */
        if(query.equals("getUserData")){
            String uuid = Minecraft.getInstance().player.getStringUUID();
            String nombre = Minecraft.getInstance().player.getName().getString();
            UserData data = new UserData(uuid, nombre);

            String respuesta = gson.toJson(data);
            callback.success(respuesta);
            return true;
        }
        if(query.equals("getPlayers")){
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
        /* Requests 'POST' */
        if(query.contains("entrarLlamada")){
            System.out.println("entrarLlamada");
            Messages.INSTANCE.sendToServer(new SMessageIniciarLlamada(query));
            callback.success("test");
            return true;
        }
        if(query.contains("colgarLlamada")){
            Messages.INSTANCE.sendToServer(new SMessageFinalizarLlamada(query));
            callback.success("test");
            return true;
        }
        return false;
    }

}
