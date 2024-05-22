package es.boffmedia.teras.pixelmon.battle;

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
import com.pixelmonmod.pixelmon.battles.controller.participants.*;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import es.boffmedia.teras.Teras;
import es.boffmedia.teras.objects_old.pixelmon.ConfigCombate;
import es.boffmedia.teras.objects_old.pixelmon.PosicionEquipo;
import es.boffmedia.teras.util.MessageHelper;
import es.boffmedia.teras.util.Scoreboard;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.TextFormatting;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Combate {
    public int idCombate;
    public ServerPlayerEntity player;
    public ConfigCombate configCombate;
    public MobEntity entidad;
    public String log;
    public PlayerParticipant partJugador;
    public BattleParticipant partRival;
    LogHelper logHelper;

    private ArrayList<PosicionEquipo> fainted = new ArrayList<>();

    BattleController battle;

    public Combate() {
        this.player = player;
        this.configCombate = new ConfigCombate();
        this.log = "";
        logHelper = new LogHelper();
    }

    public Combate(ServerPlayerEntity player, ConfigCombate configCombate) {
        this.player = player;
        this.configCombate = configCombate;
        this.log = "";
        logHelper = new LogHelper();
    }

    public ArrayList<PosicionEquipo> getFainted() {
        return fainted;
    }

    public void setFainted(PosicionEquipo posicion) {
        fainted.add(posicion);
    }

    public int nivelMaximoJugador(){
        return getPlayerParty().getHighestLevel();
    }

    public int getNivelEquipoRival(){
        return configCombate.getNivelEquipo(nivelMaximoJugador());
    }

    public void iniciarCombate(){
        configCombate.setNivelEquipo(nivelMaximoJugador());

        if(configCombate.curar()){
            getPlayerParty().heal();
        }


        Scoreboard.getOrCreateObjective(player, configCombate.getNombreArchivo());
        BattleRules br = new BattleRules();


        br.setNewClauses(configCombate.getNormas());
        br = br.set(BattleRuleRegistry.TEAM_SELECT, new BooleanValue(true));
        br = br.set(BattleRuleRegistry.TEAM_PREVIEW, new BooleanValue(true));

        battle = BattleRegistry.startBattle(new BattleParticipant[]{getPartJugador()}, new BattleParticipant[]{getPartRival()}, br);

        for (PixelmonWrapper pixelmonWrapper : battle.getActivePokemon()) {
            int equipo;
            if (pixelmonWrapper.getOwnerName().equals(getPartRival().getDisplayName())) equipo = 2;
            else equipo = 1;

        }

        this.idCombate = battle.battleIndex;
        //iniciarLog();

        Teras.getLBC().addCombateEspecial(idCombate, this);
        MessageHelper.enviarMensaje(player, TextFormatting.LIGHT_PURPLE + "¡Combate iniciado!");
    }

    public PosicionEquipo getPosicionByName(String name) {
        for (int i = 0; i < battle.getActivePokemon().size(); i++) {
            PixelmonWrapper p = battle.getActivePokemon().get(i);
            if(p.getNickname().equals(name)) {
                if (i < configCombate.getNumPkmJugador()) {
                    return new PosicionEquipo(1, i);
                } else {
                    return new PosicionEquipo(2, i - configCombate.getNumPkmJugador());
                }

            }
        }
        return null;
    }
    public int getPokemonRestantes(int equipo){
        return equipo == 1 ? getPartJugador().countAblePokemon() : getPartRival().countAblePokemon();
    }

    public int getMaxPokemon(PosicionEquipo posicionEquipo) {
        int equipo = posicionEquipo.getEquipo();
        return equipo == 1 ? configCombate.getNumPkmJugador() : configCombate.getNumPkmRival();
    }

    public boolean esGrupoSuficiente(PosicionEquipo posicionEquipo) {
        int equipo = posicionEquipo.getEquipo();
        int restantes = getPokemonRestantes(equipo);
        int numPokemon = equipo == 1 ? configCombate.getNumPkmJugador() : configCombate.getNumPkmRival();
        return restantes >= numPokemon;
    }
    public BattleController getBattle(){
        return battle;
    }

    public PixelmonWrapper getPokemonAt(PosicionEquipo pos){
        //int offset = pos.getEquipo() == 1 ? 0 : configCombate.getNumPkmJugador();
        List<PixelmonWrapper> equipo = pos.getEquipo() == 1 ? getPartJugador().controlledPokemon : getPartRival().controlledPokemon;
        int posicion = pos.getPosicion();
        if(posicion >= equipo.size()) posicion = equipo.size() - 1;

        return equipo.get(posicion);
    }


    public PosicionEquipo getPosicionContraria(PosicionEquipo posPokemon) {
        int equipo = posPokemon.getEquipo();
        int posicion = posPokemon.getPosicion();
        if(equipo == 1) return new PosicionEquipo(2, posicion);
        else return new PosicionEquipo(1, posicion);
    }

    public PosicionEquipo getPosicionv2(PixelmonWrapper pk){
        ArrayList<PixelmonWrapper> list = battle.getActivePokemon();
        for (int i = 0; i < list.size(); i++) {
            PixelmonWrapper p = list.get(i);
            if(!p.getPokemonUUID().equals(pk.getPokemonUUID())){
                continue;
            }
            if(i < configCombate.getNumPkmJugador()){
                return new PosicionEquipo(1, i);
            }
            else{
                return new PosicionEquipo(2, i - configCombate.getNumPkmJugador());
            }
        }

        return null;
    }

    public void iniciarLog(){
        String nombre1 = player.getDisplayName().getString();
        String nombre2 = getPartRival().getDisplayName();


        appendLog("|join|"+nombre1 + System.lineSeparator());
        appendLog("|join|"+nombre2 + System.lineSeparator());
        appendLog("|player|p1|"+nombre1 + System.lineSeparator());
        appendLog("|player|p2|"+nombre2 + System.lineSeparator());
        appendLog("|gametype|Singles" + System.lineSeparator());
        appendLog("|gen|9" + System.lineSeparator());
        appendLog("|tier|Circuito de Gimnasios de Teras" + System.lineSeparator());

        // Dividimos a la mitad
        PixelmonWrapper[] equipo1 = getPartJugador().allPokemon;
        PixelmonWrapper[] equipo2 = getPartRival().allPokemon;

        /*
        logHelper.setEquipo1(equipo1);
        logHelper.setEquipo2(equipo2);
        */

        TerasBattleLog.appendEquipo(equipo1, this, 1);
        TerasBattleLog.appendEquipo(equipo2, this, 2);

        appendLog("|teampreview" + System.lineSeparator());
        appendLog("|" + System.lineSeparator());
        appendLog("|start" + System.lineSeparator());
        appendLog("|turn|" + 1 + System.lineSeparator());

        for(int i = 0; i < configCombate.getNumPkmJugador(); i++){
            PixelmonWrapper pkm = equipo1[i];
            logHelper.enviarMensajeCambio(new PosicionEquipo(1, i), pkm, this);
        }

        for(int i = 0; i < configCombate.getNumPkmRival(); i++){
            PixelmonWrapper pkm = equipo2[i];
            logHelper.enviarMensajeCambio(new PosicionEquipo(2, i), pkm, this);
        }
    }


    public PlayerPartyStorage getPlayerParty(){
        return StorageProxy.getParty(player);
    }

    public PlayerParticipant getPartJugador(){
        if(partJugador !=null) return partJugador;
        List<Pokemon> pokemon = getPlayerParty().findAll(Pokemon::canBattle);
        return new PlayerParticipant(player, pokemon, configCombate.getNumPkmJugador());
    }

    public BattleParticipant getPartRival(){
        if(partRival !=null) return partRival;
        if (configCombate.esEntrenador()) {
            BattleParticipant part = getPartRivalEntrenador();
            partRival = part;
            return part;
        }
        else {
            BattleParticipant part = getPartRivalSalvaje();
            partRival = part;
            return part;
        }
    }

    protected BattleParticipant getPartRivalSalvaje() {
        Pokemon pkm = configCombate.getFirstPokemon();
        PixelmonEntity pixelmon = pkm.getOrCreatePixelmon();
        player.level.addFreshEntity(pixelmon);

        setEntidad(pixelmon);

        return new WildPixelmonParticipant(pixelmon);
    }

    protected BattleParticipant getPartRivalEntrenador() {
        NPCTrainer npc = new NPCTrainer(player.level);
        npc.setName("Entrenador");


        npc.setBossTier(BossTierRegistry.NOT_BOSS);
        if(getNivelEquipoRival() > 100){
            int niveles = getNivelEquipoRival() - 100;
            BossTier tier = new BossTier("+"+niveles,"+"+niveles, false, 0, Color.BLACK, 1.0f, false,0.0, 0.0, "PALETA", 1.0, niveles);
            npc.setBossTier(tier);
        }

        npc.setBattleAIMode(configCombate.getIA());
        if(!npc.isAddedToWorld()){
            npc.setPos(player.getX(), player.getY(), player.getZ());
            player.level.addFreshEntity(npc);
            npc.addEffect(new EffectInstance(Effects.INVISIBILITY, Integer.MAX_VALUE, 0, true, true));
            setEntidad(npc);
        }

        List<Pokemon> equipoEntrenador = configCombate.getEquipo();
        if(equipoEntrenador.isEmpty()){
            return null;
        }
        int i = 0;
        for (Pokemon pkm : equipoEntrenador) {
            npc.getPokemonStorage().set(i, pkm);

            i++;
            if (i == 6) break;
        }

        return new TrainerParticipant(npc, configCombate.getNumPkmRival());
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

    public void setLog(String log){
        this.log = log;
    }

    public void appendLog(String log){
        this.log += log;
        //Teras.LOGGER.info(log);
    }

    public String getLog(){
        return log;
    }


    public void resetFainted() {
        fainted = new ArrayList<>();
    }

}


