package es.allblue.lizardon.objects.requests;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.util.RestApi;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;

public class EjecutarPago {
    int id;
    String enviador;
    String descripcion;
    String recibidor;
    int valor;

    public EjecutarPago(String enviador, String descripcion, String recibidor, int valor) {
        this.id = id;
        this.descripcion = descripcion;
        this.enviador = enviador;
        this.recibidor = recibidor;
        this.valor = valor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getRecibidor() {
        return recibidor;
    }

    public void setRecibidor(String recibidor) {
        this.recibidor = recibidor;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = -valor;
    }

    public String getEnviador() {
        return enviador;
    }

    public void setEnviador(String enviador) {
        this.enviador = enviador;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void ejecutar(EntityPlayerMP usuario) {
        PlayerPartyStorage storagePagador = Pixelmon.storageManager.getParty(usuario.getUniqueID());

        if (valor > 0) {
            usuario.sendMessage(new TextComponentString("Has recibido " + valor + " por \"" + descripcion + "\""));
        } else {
            usuario.sendMessage(new TextComponentString("Has pagado " + valor + " por \"" + descripcion + "\""));
        }
        UpdateTrans trans = new UpdateTrans(id, recibidor, storagePagador.getMoney() + valor);
        RestApi.post("updateTrans", trans);

        storagePagador.changeMoney(valor);

        storagePagador.updatePlayer();
    }
}
