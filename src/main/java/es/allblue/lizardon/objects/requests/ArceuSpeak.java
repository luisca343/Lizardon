package es.allblue.lizardon.objects.requests;

import es.allblue.lizardon.Lizardon;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class ArceuSpeak {
    String enviador;
    String mensaje;

    public ArceuSpeak(String enviador, String mensaje) {
        this.enviador = enviador;
        this.mensaje = mensaje;
    }

    public String getEnviador() {
        return enviador;
    }

    public void setEnviador(String enviador) {
        this.enviador = enviador;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public void ejecutar() {
        Lizardon.getLogger().info("ARCEUS ");
        FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().sendMessage(new TextComponentString(mensaje));
    }
}
