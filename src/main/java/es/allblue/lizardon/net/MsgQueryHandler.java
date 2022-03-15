package es.allblue.lizardon.net;

import com.google.gson.Gson;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.storage.PCStorage;
import com.pixelmonmod.pixelmon.comm.packetHandlers.OpenScreen;
import com.pixelmonmod.pixelmon.comm.packetHandlers.clientStorage.newStorage.pc.ClientChangeOpenPC;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.objects.requests.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MsgQueryHandler implements IMessageHandler<MsgQuery, IMessage> {

    @Override
    public IMessage onMessage(MsgQuery message, MessageContext ctx) {
        Lizardon.getLogger().info("POR FAVOR, PROCESA");
        EntityPlayerMP serverPlayer = ctx.getServerHandler().player;

        String datos = message.str;


        Lizardon.getLogger().info(datos);

        serverPlayer.getServerWorld().addScheduledTask(() -> {
            Gson gson = new Gson();
            if (datos.contains("darObjeto")) {
                DarObjeto darObjeto = gson.fromJson(datos, DarObjeto.class);
                darObjeto.ejecutar(serverPlayer);
            } else if (datos.contains("enviarDinero")) {
                EnviarDinero enviarDinero = gson.fromJson(datos, EnviarDinero.class);
                enviarDinero.ejecutar(serverPlayer);
            } else if (datos.contains("ejecutarPago")) {
                EjecutarPago ejecutarPago = gson.fromJson(datos, EjecutarPago.class);
                ejecutarPago.ejecutar(serverPlayer);
            } else if (datos.contains("recompensasMina")) {
                RecompensasMina recompensasMina = gson.fromJson(datos, RecompensasMina.class);
                recompensasMina.ejecutar(serverPlayer);
            } else if (datos.contains("arceuSpeak")) {
                ArceuSpeak arceuSpeak = gson.fromJson(datos, ArceuSpeak.class);
                arceuSpeak.ejecutar();
            } else if (datos.contains("encenderPC")) {
                Jugador jugador = gson.fromJson(datos, Jugador.class);
                EntityPlayerMP player = Lizardon.server.getPlayerList().getPlayerByUsername(jugador.getJugador());
                try {
                    PCStorage pcStorage = Pixelmon.storageManager.getPCForPlayer(player.getUniqueID());
                    Pixelmon.network.sendTo(new ClientChangeOpenPC(pcStorage.uuid), player);
                    OpenScreen.open(player, EnumGuiScreen.PC, new int[0]);
                } catch (Exception e) {
                    player.sendMessage(new TextComponentString("Parece que el teléfono no funciona..."));
                }
            }
        });
        return null;
    }
}