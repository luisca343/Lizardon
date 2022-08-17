package es.allblue.lizardon.util;

import com.google.gson.Gson;
import de.maxhenkel.voicechat.api.Group;
import de.maxhenkel.voicechat.api.VoicechatConnection;
import de.maxhenkel.voicechat.api.VoicechatServerApi;
import es.allblue.lizardon.ExampleVoicechatPlugin;
import es.allblue.lizardon.net.Messages;
import es.allblue.lizardon.net.server.SMessageTest;
import es.allblue.lizardon.objects.UserData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.montoyo.mcef.api.IBrowser;
import net.montoyo.mcef.api.IJSQueryCallback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QueryHelper {

    public static boolean handleQuery(IBrowser iBrowser, long l, String query, boolean b, IJSQueryCallback callback) {
        System.out.println("ESTO ES UNA PRUEBA");
        System.out.println(query);
        Gson gson = new Gson();

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
        /* Queries JSON */
        if(query.contains("llamar")){
            Messages.INSTANCE.sendToServer(new SMessageTest(query));

            System.out.println("FIN");
            return true;
        }
        return false;
    }

}
