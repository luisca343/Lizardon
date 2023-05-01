package es.allblue.lizardon.tileentity;

import de.maxhenkel.voicechat.api.audiochannel.AudioPlayer;
import es.allblue.lizardon.init.TileEntityInit;
import es.allblue.lizardon.util.music.AudioManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;


public class TocadiscosTE extends TileEntity {
    String disco;
    private AudioPlayer player;


    public TocadiscosTE() {
        super(TileEntityInit.TOCADISCOS.get());
    }

    public TocadiscosTE(Direction facing) {
        super(TileEntityInit.TOCADISCOS.get());
    }


    public String getDisco() {
        return disco;
    }

    public void setDisco(String nombre) {
        if(this.level.isClientSide()){
            return;
        }
        System.out.println("TocadiscosTE.setDisco");

        disco = nombre;
        AudioManager manager = new AudioManager();
        player = manager.getPlayer(this.level, this.worldPosition, nombre);

        player.startPlaying();
    }

    public void stopDisco() {
        player.stopPlaying();
    }

    public boolean hasDisco() {
        return disco != null;
    }
}
