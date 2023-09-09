package es.allblue.lizardon.objects.pixelmon;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.boss.BossTier;
import com.pixelmonmod.pixelmon.api.pokemon.boss.BossTierRegistry;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.api.rules.BattleRuleRegistry;
import com.pixelmonmod.pixelmon.battles.api.rules.BattleRules;
import com.pixelmonmod.pixelmon.battles.api.rules.value.BooleanValue;
import com.pixelmonmod.pixelmon.battles.controller.BattleController;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.TrainerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.WildPixelmonParticipant;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.util.MessageUtil;
import es.allblue.lizardon.util.Scoreboard;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TextFormatting;

import java.awt.*;
import java.util.List;

public class Combate {
    private int idCombate;
    private ServerPlayerEntity player;
    private ConfigCombate configCombate;

    private MobEntity entidad;



    public Combate(ServerPlayerEntity player, ConfigCombate configCombate) {
        this.player = player;
        this.configCombate = configCombate;
    }

    public int nivelMaximoJugador(){
        return getPlayerParty().getHighestLevel();
    }

    public int getNivelEquipoRival(){
        return configCombate.getNivelEquipo(nivelMaximoJugador());
    }

    public void iniciarCombate(){
        System.out.println("Iniciando combate");

        configCombate.setNivelEquipo(nivelMaximoJugador());

        if(configCombate.curar()){
            System.out.println("Curando equipo");
            getPlayerParty().heal();
        }

        Scoreboard.getOrCreateObjective(player, configCombate.getNombreArchivo());

        System.out.println("Creando reglas");
        BattleRules br = new BattleRules();


        br.setNewClauses(configCombate.getNormas());
        br = br.set(BattleRuleRegistry.TEAM_PREVIEW, new BooleanValue(true));

        System.out.println("Iniciando combate");
        BattleController battle = BattleRegistry.startBattle(new BattleParticipant[]{getPartJugador()}, new BattleParticipant[]{getPartRival()}, br);
        this.idCombate = battle.battleIndex;

        System.out.println("Combate iniciado: " + idCombate);

        Lizardon.getLBC().addCombateEspecial(idCombate, this);
        MessageUtil.enviarMensaje(player, TextFormatting.LIGHT_PURPLE + "¡Combate iniciado!");
    }

    public PlayerPartyStorage getPlayerParty(){
        return StorageProxy.getParty(player);
    }

    public PlayerParticipant getPartJugador(){
        List<Pokemon> pokemon = getPlayerParty().findAll(Pokemon::canBattle);
        return new PlayerParticipant(player, pokemon, configCombate.numPokemonJugador());
    }

    public BattleParticipant getPartRival(){
        if (configCombate.esEntrenador()) return getPartRivalEntrenador();
        else return getPartRivalSalvaje();
    }

    private BattleParticipant getPartRivalSalvaje() {
        Pokemon pkm = configCombate.getFirstPokemon();
        PixelmonEntity pixelmon = pkm.getOrCreatePixelmon();
        player.level.addFreshEntity(pixelmon);
        setEntidad(pixelmon);

        System.out.println("Participante: " + new WildPixelmonParticipant(pixelmon));
        return new WildPixelmonParticipant(pixelmon);
    }

    private BattleParticipant getPartRivalEntrenador() {
        NPCTrainer npc = new NPCTrainer(player.level);
        npc.setName("Entrenador");


        npc.setBossTier(BossTierRegistry.NOT_BOSS);
        if(getNivelEquipoRival() > 100){
            int niveles = getNivelEquipoRival() - 100;
            BossTier tier = new BossTier("+"+niveles,"+"+niveles, false, 0, Color.BLACK, 1.0f, false,0.0, 0.0, "PALETA", 1.0, niveles);
            npc.setBossTier(tier);
        }

        System.out.println("IA: " + configCombate.getIA());
        npc.setBattleAIMode(configCombate.getIA());
        if(!npc.isAddedToWorld()){
            npc.setPos(player.getX(), player.getY(), player.getZ());
            player.level.addFreshEntity(npc);
            setEntidad(npc);
        }

        System.out.println("Equipo: " + configCombate.getEquipo());
        List<Pokemon> equipoEntrenador = configCombate.getEquipo();
        if(equipoEntrenador.isEmpty()){
            System.out.println("Equipo vacio");
            return null;
        }
        int i = 0;
        for (Pokemon pkm : equipoEntrenador) {
            npc.getPokemonStorage().set(i, pkm);

            i++;
            if (i == 6) break;
        }

        return new TrainerParticipant(npc, configCombate.numPokemonNPC());
    }

    public ConfigCombate getConfigCombate() {
        return configCombate;
    }

    public void setConfigCombate(ConfigCombate configCombate) {
        this.configCombate = configCombate;
    }

    public ServerPlayerEntity getPlayer() {
        return player;
    }

    public void setPlayer(ServerPlayerEntity player) {
        this.player = player;
    }

    public MobEntity getEntidad() {
        return entidad;
    }

    public void setEntidad(MobEntity entidad) {
        this.entidad = entidad;
    }
}


