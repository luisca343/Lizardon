package es.allblue.lizardon.net;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.objects.requests.EnviarDinero;
import es.allblue.lizardon.objects.requests.GimnasioJSON;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class GimnasioQuery implements IMessage {
    public GimnasioQuery() {
    }

    public GimnasioJSON gimnasioJSON;
    Gson gson;

    public GimnasioQuery(GimnasioJSON gimnasioJSON) {
        this.gimnasioJSON = gimnasioJSON;
        gson = new Gson();
        Lizardon.getLogger().info(new TextComponentString("DATOS RECIBIDOS, A PROCESAR"));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        String str = gson.toJson(gimnasioJSON);
        buf.writeCharSequence(str, Charsets.UTF_8);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        String str = buf.toString(Charsets.UTF_8);
        gimnasioJSON = gson.fromJson(str, GimnasioJSON.class);
        //str = buf.toString(Charsets.UTF_8);
    }
}