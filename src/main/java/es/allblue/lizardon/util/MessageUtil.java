package es.allblue.lizardon.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;

import java.util.UUID;

public class MessageUtil {

    public static void enviarMensaje(PlayerEntity player, String mensaje) {
        player.sendMessage(new StringTextComponent(mensaje), UUID.randomUUID());
    }
}
