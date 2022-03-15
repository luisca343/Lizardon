package es.allblue.lizardon.objects.requests;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.util.RestApi;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;

public class EnviarDinero {
    String enviador;
    String recibidor;
    String descripcion;
    int valor;

    public EnviarDinero(String enviador, String recibidor, String descripcion, int valor) {
        this.enviador = enviador;
        this.recibidor = recibidor;
        this.valor = valor;
        this.descripcion = descripcion;
    }

    public String getEnviador() {
        return enviador;
    }

    public void setEnviador(String enviador) {
        this.enviador = enviador;
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
        this.valor = valor;
    }

    public void ejecutar(EntityPlayerMP a) {
        EntityPlayerMP pagador = Lizardon.server.getPlayerList().getPlayerByUsername(enviador);
        EntityPlayerMP destinatario = Lizardon.server.getPlayerList().getPlayerByUsername(recibidor);


        if (destinatario != null) {
            PlayerPartyStorage storagePagador = Pixelmon.storageManager.getParty(pagador.getUniqueID());


            PlayerPartyStorage storageDestinatario = Pixelmon.storageManager.getParty(destinatario.getUniqueID());
            int balanceDestinatario = storageDestinatario.getMoney();

            Transaccion transaccionEnvio = new Transaccion(enviador, recibidor, "Pago", descripcion, "https://minotar.net/avatar/" + recibidor + "/70.png", -valor, storagePagador.getMoney() - valor, true);
            RestApi.post("transaccion", transaccionEnvio);

            Transaccion transaccionRecibo = new Transaccion(recibidor, enviador, "Ingreso", descripcion, "https://minotar.net/avatar/" + enviador + "/70.png", valor, storageDestinatario.getMoney() + valor, true);
            RestApi.post("transaccion", transaccionRecibo);

            storagePagador.changeMoney(-valor);
            storageDestinatario.changeMoney(valor);

            storagePagador.updatePlayer();
            storageDestinatario.updatePlayer();

            pagador.sendMessage(new TextComponentString("Has enviado " + valor + " a " + recibidor));
        } else {
            PlayerPartyStorage storagePagador = Pixelmon.storageManager.getParty(pagador.getUniqueID());

            Transaccion transaccionEnvio = new Transaccion(enviador, recibidor, "Pago", descripcion, "https://minotar.net/avatar/" + recibidor + "/70.png", -valor, storagePagador.getMoney() - valor, true);
            RestApi.post("transaccion", transaccionEnvio);

            Transaccion transaccionRecibo = new Transaccion(recibidor, enviador, "Ingreso", descripcion, "https://minotar.net/avatar/" + enviador + "/70.png", valor, 0, false);
            RestApi.post("transaccion", transaccionRecibo);

            storagePagador.changeMoney(-valor);
            pagador.sendMessage(new TextComponentString("Has enviado " + valor + " a " + recibidor));

            storagePagador.updatePlayer();
        }

    }
}
