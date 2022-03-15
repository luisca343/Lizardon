package es.allblue.lizardon.net;

import com.google.gson.Gson;
import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.objects.requests.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class GimnasioHandler implements IMessageHandler<GimnasioQuery, IMessage> {

    @Override
    public IMessage onMessage(GimnasioQuery message, MessageContext ctx) {
        EntityPlayerMP serverPlayer = ctx.getServerHandler().player;

        GimnasioJSON gimnasioJSON = message.gimnasioJSON;


        serverPlayer.getServerWorld().addScheduledTask(() -> {

        });
        return null;
    }
}