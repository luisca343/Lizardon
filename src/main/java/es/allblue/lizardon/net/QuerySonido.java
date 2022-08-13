package es.allblue.lizardon.net;

import com.google.common.base.Charsets;
import es.allblue.lizardon.Lizardon;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class QuerySonido implements IMessage {
    public QuerySonido() {
    }

    public String str;

    public QuerySonido(String str) {
        this.str = str;
    }

    @Override
    public void toBytes(ByteBuf buf) {

        buf.writeCharSequence(str, Charsets.UTF_8);
    }

    @Override
    public void fromBytes(ByteBuf buf) {

        str = buf.toString(Charsets.UTF_8);
    }
}