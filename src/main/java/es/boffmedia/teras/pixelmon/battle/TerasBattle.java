package es.boffmedia.teras.pixelmon.battle;

import com.pixelmonmod.pixelmon.api.battles.BattleType;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.boss.BossTier;
import com.pixelmonmod.pixelmon.api.pokemon.boss.BossTierRegistry;
import com.pixelmonmod.pixelmon.api.storage.PartyStorage;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.api.BattleBuilder;
import com.pixelmonmod.pixelmon.battles.api.rules.BattleRuleRegistry;
import com.pixelmonmod.pixelmon.battles.api.rules.BattleRules;
import com.pixelmonmod.pixelmon.battles.api.rules.teamselection.TeamSelection;
import com.pixelmonmod.pixelmon.battles.api.rules.teamselection.TeamSelectionRegistry;
import com.pixelmonmod.pixelmon.battles.controller.BattleController;
import com.pixelmonmod.pixelmon.battles.controller.log.action.BattleAction;
import com.pixelmonmod.pixelmon.battles.controller.participants.*;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.TrainerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.WildPixelmonParticipant;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.npcs.registry.BaseTrainer;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import es.boffmedia.teras.Teras;
import es.boffmedia.teras.objects_old.pixelmon.BattleConfig;
import es.boffmedia.teras.objects_old.pixelmon.PosicionEquipo;
import es.boffmedia.teras.util.MessageHelper;
import es.boffmedia.teras.util.Scoreboard;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

import java.awt.*;
import java.util.*;
import java.util.List;

public class TerasBattle {
    public int battleId;
    public ServerPlayerEntity player;
    public BattleConfig battleConfig;
    public MobEntity entity;
    public String log;
    public PlayerParticipant playerParticipant;
    public BattleParticipant rivalParticipant;
    LogHelper logHelper;

    private ArrayList<PosicionEquipo> fainted = new ArrayList<>();

    BattleController battle;

    boolean teamSelection = false;

    public TerasBattle() {
        this.player = player;
        this.battleConfig = new BattleConfig();
        this.log = "";
        logHelper = new LogHelper();
    }

    public TerasBattle(ServerPlayerEntity player, BattleConfig battleConfig) {
        this.player = player;
        this.battleConfig = battleConfig;
        this.log = "";
        logHelper = new LogHelper();
    }

    public void logEvent(BattleAction event){
        TerasBattleLog.logEvent(event, this);
    }

    public ArrayList<PosicionEquipo> getFainted() {
        return fainted;
    }

    public void setFainted(PosicionEquipo posicion) {
        fainted.add(posicion);
    }

    public int getHighestPlayerLevel(){
        return getPlayerParty().getHighestLevel();
    }

    public int getRivalTeamLevel(){
        return battleConfig.calculateTeamLevel(getHighestPlayerLevel());
    }

    public void start() {
        /* Register the battle */
        battleConfig.setNivelEquipo(getHighestPlayerLevel());

        if (battleConfig.healBeforeStart()) {
            getPlayerParty().heal();
        }


        Scoreboard.getOrCreateObjective(player, battleConfig.getNombreArchivo());

        BattleRules br = new BattleRules();
        br.setNewClauses(battleConfig.getNormas());
        br = br.set(BattleRuleRegistry.TEAM_SELECT, true);
        br = br.set(BattleRuleRegistry.TEAM_PREVIEW, true);

        br = br.set(BattleRuleRegistry.NUM_POKEMON, 2);
        br = br.set(BattleRuleRegistry.TURN_TIME, 45);
        br = br.set(BattleRuleRegistry.BATTLE_TYPE, BattleType.DOUBLE);


      /*
        BattleBuilder.builder()
                .startSync()
                  .teamOne(getPlayerParticipant())
                .teamTwo(getRivalParticipant())
                .teamSelection(true)
                .rules(br)
                .teamSelectionBuilder(TeamSelectionRegistry.builder()
                        .battleRules(br)
                        .showOpponentTeam()
                        .battleStartConsumer(bc -> {
                            Teras.LOGGER.error("Does this work?");
                            battle = bc;
                            battleId = bc.battleIndex;
                            Teras.getLBC().addTerasBattle(battleId, this);
                        })
                        .cancelConsumer(ts -> {
                            Teras.LOGGER.error("CANCELADO");
                        })
                )
                .allowSpectators(true)
                .startHandler((battleStartedEvent, bc) -> {
                    Teras.LOGGER.error("This does not work.");
                })
                .endHandler((battleEndEvent, bc) -> {
                    Teras.LOGGER.error("This works.");
                }).start();
*/



        TeamSelectionRegistry.Builder test =
                TeamSelectionRegistry
                        .builder()
                        .members(getPlayerParticipant().getEntity(), getRivalParticipant().getEntity())
                        .battleRules(br)
                        .showOpponentTeam()
                        .closeable()
                        .battleStartConsumer(bc -> {
                            battle = bc;
                            battleId = bc.battleIndex;
                            Teras.getLBC().addTerasBattle(battleId, this);
                        })
                        .cancelConsumer(ts -> {
                            Teras.LOGGER.error("CANCELADO");
                        });
        test.start();


        /*
        TerasTeamSelectionRegistry.Builder terasSelect =
                TerasTeamSelectionRegistry
                        .builder()
                        .battleRules(br)
                        .showRules()
                        .showOpponentTeam()
                        .closeable()
                        .battleStartConsumer(bc -> {
                            battle = bc;
                            battleId = bc.battleIndex;
                            Teras.getLBC().addTerasBattle(battleId, this);
                        })
                        .cancelConsumer(ts -> {
                            Teras.LOGGER.error("CANCELADO");
                        })
                        .members(getPlayerParticipant().getStorage(), tp.getStorage());*/



        /*
        battle = BattleRegistry.startBattle(new BattleParticipant[]{getPlayerParticipant()}, new BattleParticipant[]{getRivalParticipant()}, br);
        Teras.getLBC().addTerasBattle(battleId, this);
        this.battleId = battle.battleIndex;
        MessageHelper.enviarMensaje(player, TextFormatting.LIGHT_PURPLE + "¡Combate iniciado!");*/






        /*
        battle = BattleRegistry.startBattle(new BattleParticipant[]{getPlayerParticipant()}, new BattleParticipant[]{getRivalParticipant()}, br);
        Teras.getLBC().addTerasBattle(battleId, this);

        for (PixelmonWrapper pixelmonWrapper : battle.getActivePokemon()) {
            int equipo;
            if (pixelmonWrapper.getOwnerName().equals(getRivalParticipant().getDisplayName())) equipo = 2;
            else equipo = 1;

        }

        this.battleId = battle.battleIndex;
        //iniciarLog();

        Teras.LOGGER.info("NUM POKEMOR JUGADOR: " + battleConfig.getNumPkmJugador());
        Teras.LOGGER.info("NUM POKEMOR RIVAL: " + battleConfig.getNumPkmRival());

        //Teras.getLBC().addCombateEspecial(idCombate, this);
        MessageHelper.enviarMensaje(player, TextFormatting.LIGHT_PURPLE + "¡Combate iniciado!");
*/

        /*
        CompletableFuture<BattleController> futureBattleController = BattleBuilder.builder()
                .startSync()
                .teamOne(getPlayerParticipant())
                .teamTwo(getRivalParticipant())
                .teamSelection(teamSelection)
                .rules(br)
                .teamSelectionBuilder(TeamSelectionRegistry.builder()
                        .battleRules(br)
                        .showOpponentTeam()
                )
                .allowSpectators(true).start();

        futureBattleController.thenAccept(bc -> {
            Teras.LOGGER.error("COMBATE INICIADO");
            battle = bc;
            battleId = bc.battleIndex;
            Teras.LOGGER.info("CURRENT battleId 2: " + bc.battleIndex);
            Teras.getLBC().addTerasBattle(battleId, this);
            //postStart();
        });*/

        /*
        futureBattleController.thenRunAsync(() -> {
            Teras.LOGGER.error("COMBATE INICIADO");
            battle = futureBattleController.getNow(null);
            battleId = battle.battleIndex;
            Teras.LOGGER.info("CURRENT battleId 2: " + battleId);
            Teras.getLBC().addTerasBattle(battleId, this);
            postStart();
        });*/
    }

    /*
    public void postStart(){
        Teras.LOGGER.error("ID COMBATE: " + battleId);

        this.battleId = battle.battleIndex;
        //iniciarLog();

        Teras.LOGGER.info("NUM POKEMOR JUGADOR: " + battleConfig.getNumPkmJugador());
        Teras.LOGGER.info("NUM POKEMOR RIVAL: " + battleConfig.getNumPkmRival());

        Teras.getLBC().addTerasBattle(battleId, this);
        //Teras.getLBC().addCombateEspecial(idCombate, this);
        MessageHelper.enviarMensaje(player, TextFormatting.LIGHT_PURPLE + "¡Combate iniciado!");

    }*/

    public PosicionEquipo getPosicionByName(String name) {
        for (int i = 0; i < battle.getActivePokemon().size(); i++) {
            PixelmonWrapper p = battle.getActivePokemon().get(i);
            if(p.getNickname().equals(name)) {
                if (i < battleConfig.getNumPkmJugador()) {
                    return new PosicionEquipo(1, i);
                } else {
                    return new PosicionEquipo(2, i - battleConfig.getNumPkmJugador());
                }

            }
        }
        return null;
    }
    public int getPokemonRestantes(int equipo){
        return equipo == 1 ? getPlayerParticipant().countAblePokemon() : getRivalParticipant().countAblePokemon();
    }

    public int getMaxPokemon(PosicionEquipo posicionEquipo) {
        int equipo = posicionEquipo.getEquipo();
        return equipo == 1 ? battleConfig.getNumPkmJugador() : battleConfig.getNumPkmRival();
    }

    public boolean esGrupoSuficiente(PosicionEquipo posicionEquipo) {
        int equipo = posicionEquipo.getEquipo();
        int restantes = getPokemonRestantes(equipo);
        int numPokemon = equipo == 1 ? battleConfig.getNumPkmJugador() : battleConfig.getNumPkmRival();
        return restantes >= numPokemon;
    }
    public BattleController getBattle(){
        return battle;
    }

    public PixelmonWrapper getPokemonAt(PosicionEquipo pos){
        //int offset = pos.getEquipo() == 1 ? 0 : configCombate.getNumPkmJugador();
        List<PixelmonWrapper> equipo = pos.getEquipo() == 1 ? getPlayerParticipant().controlledPokemon : getRivalParticipant().controlledPokemon;
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
            if(i < battleConfig.getNumPkmJugador()){
                return new PosicionEquipo(1, i);
            }
            else{
                return new PosicionEquipo(2, i - battleConfig.getNumPkmJugador());
            }
        }

        return null;
    }

    public void iniciarLog(){
        String nombre1 = player.getDisplayName().getString();
        String nombre2 = getRivalParticipant().getDisplayName();


        appendLog("|join|"+nombre1 + System.lineSeparator());
        appendLog("|join|"+nombre2 + System.lineSeparator());
        appendLog("|player|p1|"+nombre1 + System.lineSeparator());
        appendLog("|player|p2|"+nombre2 + System.lineSeparator());
        appendLog("|gametype|Singles" + System.lineSeparator());
        appendLog("|gen|9" + System.lineSeparator());
        appendLog("|tier|Circuito de Gimnasios de Teras" + System.lineSeparator());

        // Dividimos a la mitad
        PixelmonWrapper[] equipo1 = getPlayerParticipant().allPokemon;
        PixelmonWrapper[] equipo2 = getRivalParticipant().allPokemon;

        /*
        logHelper.setEquipo1(equipo1);
        logHelper.setEquipo2(equipo2);
        */

        /*
        TerasBattleLog.appendEquipo(equipo1, this, 1);
        TerasBattleLog.appendEquipo(equipo2, this, 2);*/

        appendLog("|teampreview" + System.lineSeparator());
        appendLog("|" + System.lineSeparator());
        appendLog("|start" + System.lineSeparator());
        appendLog("|turn|" + 1 + System.lineSeparator());


        /*
        for(int i = 0; i < configCombate.getNumPkmJugador(); i++){
            PixelmonWrapper pkm = equipo1[i];
            logHelper.enviarMensajeCambio(new PosicionEquipo(1, i), pkm, this);
        }

        for(int i = 0; i < configCombate.getNumPkmRival(); i++){
            PixelmonWrapper pkm = equipo2[i];
            logHelper.enviarMensajeCambio(new PosicionEquipo(2, i), pkm, this);
        }*/
    }


    public PlayerPartyStorage getPlayerParty(){
        return StorageProxy.getParty(player);
    }

    public PlayerParticipant getPlayerParticipant(){
        if(playerParticipant !=null) return playerParticipant;
        List<Pokemon> pokemon = getPlayerParty().findAll(Pokemon::canBattle);
        return new PlayerParticipant(player, pokemon, battleConfig.getNumPkmJugador());
    }

    public BattleParticipant getRivalParticipant(){
        if(rivalParticipant !=null) return rivalParticipant;
        if (battleConfig.esEntrenador()) {
            BattleParticipant part = getPartRivalEntrenador();
            rivalParticipant = part;
            return part;
        }
        else {
            BattleParticipant part = getPartRivalSalvaje();
            rivalParticipant = part;
            return part;
        }
    }

    protected BattleParticipant getPartRivalSalvaje() {
        Pokemon pkm = battleConfig.getFirstPokemon();
        PixelmonEntity pixelmon = pkm.getOrCreatePixelmon();
        player.level.addFreshEntity(pixelmon);

        setEntity(pixelmon);

        return new WildPixelmonParticipant(pixelmon);
    }

    protected TrainerParticipant getPartRivalEntrenador() {
        NPCTrainer npc = new NPCTrainer(player.level);
        npc.setBossTier(BossTierRegistry.NOT_BOSS);



        if(getRivalTeamLevel() > 100){
            int niveles = getRivalTeamLevel() - 100;
            BossTier tier = new BossTier("+"+niveles,"+"+niveles, false, 0, Color.BLACK, 1.0f, false,0.0, 0.0, "PALETA", 1.0, niveles);
            npc.setBossTier(tier);
        }

        npc.setBattleAIMode(battleConfig.getIA());
        if(!npc.isAddedToWorld()){
            npc.setTextureIndex(-1);
            String name = "aquaboss";
            npc.setName(name);
            npc.setCustomSteveTexture(name);

            npc.setPos(player.getX(), player.getY(), player.getZ());
            player.level.addFreshEntity(npc);

            npc.addEffect(new EffectInstance(Effects.INVISIBILITY, Integer.MAX_VALUE, 0, true, true));
            setEntity(npc);
        }

        List<Pokemon> equipoEntrenador = battleConfig.getEquipo();
        if(equipoEntrenador.isEmpty()){
            return null;
        }
        int i = 0;
        for (Pokemon pkm : equipoEntrenador) {
            npc.getPokemonStorage().set(i, pkm);

            i++;
            if (i == 6) break;
        }



        return new TrainerParticipant(npc, battleConfig.getNumPkmRival());
    }

    public BattleConfig getBattleConfig() {
        return battleConfig;
    }

    public void setBattleConfig(BattleConfig battleConfig) {
        this.battleConfig = battleConfig;
    }

    public ServerPlayerEntity getPlayer() {
        return player;
    }

    public void setPlayer(ServerPlayerEntity player) {
        this.player = player;
    }

    public MobEntity getEntity() {
        return entity;
    }

    public void setEntity(MobEntity entity) {
        this.entity = entity;
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


