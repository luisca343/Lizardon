package es.allblue.lizardon.net;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class QuerySonidoHandler implements IMessageHandler<QuerySonido, IMessage> {

    @Override
    public IMessage onMessage(QuerySonido message, MessageContext ctx) {

        EntityPlayerMP player = ctx.getServerHandler().player;
        String sonidoStr = message.str;

        player.getServerWorld().addScheduledTask(() -> {
            //player.getServerWorld().playSound(player.posX,player.posY,player.posZ, SoundRegistrator.getSonido(sonidoStr), SoundCategory.PLAYERS,1.0f,1.0f,true);
        });
        return null;
    }
}