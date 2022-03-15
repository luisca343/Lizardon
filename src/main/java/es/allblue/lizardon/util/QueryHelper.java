package es.allblue.lizardon.util;

import com.google.gson.Gson;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.storage.PCStorage;
import com.pixelmonmod.pixelmon.comm.packetHandlers.OpenScreen;
import com.pixelmonmod.pixelmon.comm.packetHandlers.clientStorage.newStorage.pc.ClientChangeOpenPC;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.net.MsgQuery;
import es.allblue.lizardon.objects.DatosUser;
import es.allblue.lizardon.objects.requests.EnviarDinero;
import es.allblue.lizardon.objects.requests.Jugador;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.montoyo.mcef.api.IBrowser;
import net.montoyo.mcef.api.IJSQueryCallback;
import net.montoyo.mcef.utilities.Log;
import net.minecraft.client.Minecraft;
import net.montoyo.wd.client.JSQueryDispatcher;

public class QueryHelper {
    private static Minecraft mc = Minecraft.getMinecraft();

    public static boolean handleQuery(JSQueryDispatcher jsDispatcher, IBrowser browser, long queryId, String query, boolean persistent, IJSQueryCallback cb) {
        Lizardon.getLogger().info(query);
        if (browser != null && query.equalsIgnoreCase("username")) {
            mc.addScheduledTask(() -> {

                try {
                    String name = mc.getSession().getUsername();
                    DatosUser datosUser = new DatosUser(name);
                    Gson gson = new Gson();
                    cb.success(gson.toJson(datosUser));
                } catch (Throwable t) {
                    cb.failure(500, "Internal error.");
                    Log.warning("Could not get username from JavaScript:");
                    t.printStackTrace();
                }
            });

            return true;
        }

        if (browser != null && query.contains("enviarDinero")) {
            Lizardon.NET_HANDLER.sendToServer(new MsgQuery(query));
            browser.loadURL(Lizardon.getProxy().pasarParametros(Lizardon.URL_BASE));
        }

        if (browser != null && query.contains("ejecutarPago")) {
            Lizardon.NET_HANDLER.sendToServer(new MsgQuery(query));
            browser.loadURL(Lizardon.getProxy().pasarParametros(Lizardon.URL_BASE));
        }

        if (browser != null && query.contains("recompensasMina")) {
            Lizardon.NET_HANDLER.sendToServer(new MsgQuery(query));
            browser.loadURL(Lizardon.getProxy().pasarParametros(Lizardon.URL_BASE));
        }

        if (browser != null && query.contains("arceuSpeak")) {
            Lizardon.NET_HANDLER.sendToServer(new MsgQuery(query));
        }

        if (browser != null && query.contains("darObjeto")) {
            Lizardon.NET_HANDLER.sendToServer(new MsgQuery(query));
            browser.loadURL(Lizardon.getProxy().pasarParametros(Lizardon.URL_BASE));
        }

        if (browser != null && query.contains("encenderPC")) {
            browser.loadURL(Lizardon.getProxy().pasarParametros(Lizardon.URL_BASE));
            Lizardon.NET_HANDLER.sendToServer(new MsgQuery(query));
        }

        return false;
    }

    public static void cancelQuery(JSQueryDispatcher jsDispatcher, IBrowser browser, long queryId) {

    }
}
