package es.allblue.lizardon.util;

import com.google.gson.Gson;
import com.pixelmonmod.pixelmon.api.storage.PCStorage;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.client.ClientProxy;
import es.allblue.lizardon.net.Messages;
import es.allblue.lizardon.net.server.*;
import es.allblue.lizardon.objects.UserData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.montoyo.mcef.api.IBrowser;
import net.montoyo.mcef.api.IJSQueryCallback;

import java.util.ArrayList;
import java.util.Collection;

public class QueryHelper {

    public static boolean handleQuery(IBrowser iBrowser, long l, String query, boolean b, IJSQueryCallback callback) {
        Gson gson = new Gson();
        System.out.println("Query recibida: "+query);
        /* Requests 'GET' */
        if(query.equals("getUserData")){
            Messages.INSTANCE.sendToServer(new SMessageDatosServer("query"));
            ClientProxy.callbackMisiones = callback;
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
        if(query.contains("cerrarRotom")){
            Lizardon.PROXY.closeSmartRotom();
            callback.success("");
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
        if(query.contains("darObjetos")){
            Messages.INSTANCE.sendToServer(new SMessageDarObjetos(query));
            callback.success("test");
            return true;
        }
        if(query.contains("abrirPC")){
            PCStorage pcStorage = ClientStorageManager.openPC;

            Messages.INSTANCE.sendToServer(new SMessageEncenderPC(pcStorage.uuid.toString()));
            callback.success("test");
            return true;
        }
        if(query.contains("getMisiones")){
            ClientProxy.callbackMisiones = callback;
            Messages.INSTANCE.sendToServer(new SMessageVerMisiones(query));
            return true;
        }
        if(query.contains("entrarCarrera")){
            ClientProxy.callbackMisiones = callback;
            Messages.INSTANCE.sendToServer(new SMessageEntrarCarrera(query));
            return true;
        }
        if(query.contains("getPC")){
            ClientProxy.callbackMCEF = callback;
            Messages.INSTANCE.sendToServer(new SMessageGetPC(query));
            return true;
        }
        if(query.contains("setPC")){
            ClientProxy.callbackMCEF = callback;
            Messages.INSTANCE.sendToServer(new SMessageSetPC(query));
            return true;
        }
        return false;
    }

}
