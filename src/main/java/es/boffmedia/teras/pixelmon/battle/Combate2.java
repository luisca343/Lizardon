/*package es.boffmedia.teras.pixelmon.battle;

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
import es.boffmedia.teras.objects.pixelmon.ConfigCombate;
import es.boffmedia.teras.pixelmon.battle.LogHelper;
import es.boffmedia.teras.objects.pixelmon.PosicionEquipo;
import es.boffmedia.teras.util.MessageHelper;
import es.boffmedia.teras.util.Scoreboard;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TextFormatting;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Combate2 {
    public int idCombate;
    public ServerPlayerEntity player;
    public ConfigCombate configCombate;
    public MobEntity entidad;
    public String log;
    public PlayerParticipant partJugador;
    public BattleParticipant partRival;
    LogHelper logHelper;

    HashMap<PosicionEquipo, PixelmonWrapper> activos = new HashMap<>();


    public Combate2() {
        this.player = player;
        this.configCombate = new ConfigCombate();
        this.log = "";
        logHelper = new LogHelper();
    }

    public Combate2(ServerPlayerEntity player, ConfigCombate configCombate) {
        this.player = player;
        this.configCombate = configCombate;
        this.log = "";
        logHelper = new LogHelper();
    }

    public int nivelMaximoJugador(){
        return getPlayerParty().getHighestLevel();
    }

    public int getNivelEquipoRival(){
        return configCombate.getNivelEquipo(nivelMaximoJugador());
    }

    public void iniciarCombate(){
        System.out.println("1. Iniciando combate");
        configCombate.setNivelEquipo(nivelMaximoJugador());

        if(configCombate.curar()){
            getPlayerParty().heal();
        }

        System.out.println("2. Curando equipo");

        Scoreboard.getOrCreateObjective(player, configCombate.getNombreArchivo());
        BattleRules br = new BattleRules();


        System.out.println("3. Creando reglas");
        br.setNewClauses(configCombate.getNormas());
        br = br.set(BattleRuleRegistry.TEAM_SELECT, new BooleanValue(true));
        br = br.set(BattleRuleRegistry.TEAM_PREVIEW, new BooleanValue(true));

        BattleController battle = BattleRegistry.startBattle(new BattleParticipant[]{getPartJugador()}, new BattleParticipant[]{getPartRival()}, br);

        System.out.println("4. Iniciando combate");
        for (PixelmonWrapper pixelmonWrapper : battle.getActivePokemon()) {
            if (pixelmonWrapper.getOwnerName().equals(getPartRival().getDisplayName())) {
                activos.put(new PosicionEquipo(2, activos.size()), pixelmonWrapper);
            } else {
                activos.put(new PosicionEquipo(1, activos.size()), pixelmonWrapper);
            }
        }

        this.idCombate = battle.battleIndex;
        System.out.println("6. Iniciando Log");
        iniciarLog();

        Teras.getLBC().addCombateEspecial(idCombate, this);
        System.out.println("7. Iniciando combate");
        MessageHelper.enviarMensaje(player, TextFormatting.LIGHT_PURPLE + "¡Combate iniciado!");
        System.out.println("8. Iniciando combate");
    }

    public String getPosicion(String pkmName) {
        Map.Entry<PosicionEquipo, PixelmonWrapper> pokemon = activos.entrySet().stream().filter(entry -> entry.getValue().getPokemonName().equals(pkmName)).findFirst().orElse(null);
        if (pokemon != null) return "p"+pokemon.getKey().getEquipo()+": " + pokemon.getKey().getLetra();

        return "";
    }

    public String getPosicion(PixelmonWrapper pkm){
        Map.Entry<PosicionEquipo, PixelmonWrapper> pokemon = activos.entrySet().stream().filter(entry -> entry.getValue().getPokemonUUID().equals(pkm.getPokemonUUID())).findFirst().orElse(null);
        if (pokemon != null) return "p"+pokemon.getKey().getEquipo()+": " + pokemon.getKey().getLetra();

        return null;
    }


    public void iniciarLog(){
        System.out.println("6.1 Iniciando log");
        String nombre1 = player.getDisplayName().getString();
        String nombre2 = getPartRival().getDisplayName();

        System.out.println("6.2 Iniciando log");

        appendLog("|join|"+nombre1 + System.lineSeparator());
        appendLog("|join|"+nombre2 + System.lineSeparator());
        appendLog("|player|p1|"+nombre1 + System.lineSeparator());
        appendLog("|player|p2|"+nombre2 + System.lineSeparator());
        appendLog("|gametype|Singles" + System.lineSeparator());
        appendLog("|gen|9" + System.lineSeparator());
        appendLog("|tier|Circuito de Gimnasios de Teras" + System.lineSeparator());

        System.out.println("6.3 Iniciando log");
        // Dividimos a la mitad
        PixelmonWrapper[] equipo1 = getPartJugador().allPokemon;
        PixelmonWrapper[] equipo2 = getPartRival().allPokemon;

        System.out.println("6.4 Iniciando log");
        logHelper.setEquipo1(equipo1);
        logHelper.setEquipo2(equipo2);

        System.out.println("6.5 Iniciando log");

        TerasBattleLog.appendEquipo(equipo1, this, 1);
        TerasBattleLog.appendEquipo(equipo2, this, 2);
        appendLog("|teampreview" + System.lineSeparator());
        appendLog("|" + System.lineSeparator());
        appendLog("|start" + System.lineSeparator());

        System.out.println("6.6 Iniciando log");

        ArrayList<PixelmonWrapper> playerPokemon = new ArrayList<>();
        ArrayList<PixelmonWrapper> opponentPokemon = new ArrayList<>();

        System.out.println("6.7 Iniciando log");

        playerPokemon.add(equipo1[0]);
        opponentPokemon.add(equipo2[0]);

        System.out.println("6.8 Iniciando log");


        for (int i = 0; i < playerPokemon.size(); i++) appendLog(TerasBattleLog.getSwitchPkmStr(playerPokemon.get(i), 1, i, true, this));

        System.out.println("6.9 Iniciando log");

        for (int i = 0; i < opponentPokemon.size(); i++) appendLog(TerasBattleLog.getSwitchPkmStr(opponentPokemon.get(i), 2, i, true, this));

        System.out.println("6.10 Iniciando log");

    }

    public PlayerPartyStorage getPlayerParty(){
        return StorageProxy.getParty(player);
    }

    public PlayerParticipant getPartJugador(){
        if(partJugador !=null) return partJugador;
        List<Pokemon> pokemon = getPlayerParty().findAll(Pokemon::canBattle);
        return new PlayerParticipant(player, pokemon, configCombate.numPokemonJugador());
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

    public void setLog(String log){
        this.log = log;
    }

    public void appendLog(String log){
        this.log += log;
    }

    public String getLog(){
        return log;
    }

    public void setPosicion(int equipo, int posPokemon, PixelmonWrapper pkm) {
        int posEquipo = TerasBattleLog.letras.indexOf(equipo);

        System.out.println(activos);
        activos.put(new PosicionEquipo(equipo, posEquipo), pkm);
    }

    public void setPosicion(String posPokemon, PixelmonWrapper pkm) {
        int equipo = Integer.parseInt(String.valueOf(posPokemon.charAt(0)));
        int posEquipo = TerasBattleLog.letras.indexOf(posPokemon.charAt(1));

        System.out.println(activos);
        activos.put(new PosicionEquipo(equipo, posEquipo), pkm);
    }
}


*/