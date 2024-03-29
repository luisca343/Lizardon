package es.boffmedia.teras.net.server;

import com.google.common.base.Charsets;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.PokedexEvent;
import com.pixelmonmod.pixelmon.api.pokedex.PlayerPokedex;
import com.pixelmonmod.pixelmon.api.pokedex.PokedexRegistrationStatus;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonFactory;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.montoyo.mcef.api.IJSQueryCallback;

import java.util.function.Supplier;

public class SMessageUpdateDex implements Runnable{
    private String str;
    private ServerPlayerEntity player;
    private IJSQueryCallback callback;

    public SMessageUpdateDex(String str){
        this.str = str;
    }

    @Override
    public void run() {
        int id = Integer.parseInt(str);
        Pokemon pokemon = PokemonFactory.create(PixelmonSpecies.fromDex(id).get());
        PokedexEvent.Post event = new PokedexEvent.Post(player.getUUID(), PokedexRegistrationStatus.UNKNOWN, pokemon, PokedexRegistrationStatus.SEEN, "SmartRotom");
        Pixelmon.EVENT_BUS.post(event);

        PlayerPokedex pokedex = new PlayerPokedex(player.getUUID());

        pokedex.set(id, PokedexRegistrationStatus.SEEN);
        pokedex.update();
    }

    public static SMessageUpdateDex decode(PacketBuffer buf) {
        SMessageUpdateDex message = new SMessageUpdateDex(buf.toString(Charsets.UTF_8));
        return message;
    }

    public void encode(PacketBuffer buf) {
        buf.writeCharSequence(str, Charsets.UTF_8);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        player = contextSupplier.get().getSender();
        contextSupplier.get().enqueueWork((Runnable) this);
        contextSupplier.get().setPacketHandled(true);
    }
}
