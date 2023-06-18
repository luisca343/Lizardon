package es.allblue.lizardon.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.STitlePacket;
import net.minecraft.util.text.StringTextComponent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class MessageUtil {

    public static void enviarMensaje(PlayerEntity player, String mensaje) {
        player.sendMessage(new StringTextComponent(mensaje), UUID.randomUUID());
    }

    public static void enviarTitulo(ServerPlayerEntity player, String titulo, int entrada, int permanencia, int salida) {
        player.connection.send(new STitlePacket(STitlePacket.Type.TITLE, new StringTextComponent(titulo),entrada,permanencia,salida));
    }


    public static void enviarTitulo(ServerPlayerEntity player, String titulo) {
        enviarTitulo(player, titulo, 0, 20, 0);
    }


    public static String formatearTiempo(long tiempo){
        return new SimpleDateFormat("mm:ss:SSS").format(new Date(tiempo));
    }
}
