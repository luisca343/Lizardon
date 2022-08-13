package es.allblue.lizardon.net;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.objects.requests.DarObjeto;
import es.allblue.lizardon.objects.requests.RecompensasMina;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MsgQuery implements IMessage {
    public MsgQuery() {
    }

    public String str;

    public MsgQuery(String str) {
        this.str = str;
        Lizardon.getLogger().info(new TextComponentString("DATOS RECIBIDOS, A PROCESAR"));
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