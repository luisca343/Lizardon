package es.allblue.lizardon.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.STitlePacket;
import net.minecraft.util.text.StringTextComponent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class MessageHelper {

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

    public static String formatearQuery(String query, String[] partes){
        String[] parametros = new String[partes.length - 1];
        for (int i = 0; i < parametros.length; i++) {
            parametros[i] = "\""+partes[i]+"\"";
        }

        return String.format("%s(%s)", query, parametros);
    }
}
