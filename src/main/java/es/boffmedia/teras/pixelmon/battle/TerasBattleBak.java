/*
package es.boffmedia.teras.pixelmon.battle;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.pokemon.boss.BossTier;
import com.pixelmonmod.pixelmon.api.pokemon.boss.BossTierRegistry;
import com.pixelmonmod.pixelmon.api.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StorageProxy;
import com.pixelmonmod.pixelmon.battles.api.rules.BattleRuleRegistry;
import com.pixelmonmod.pixelmon.battles.api.rules.BattleRules;
import com.pixelmonmod.pixelmon.battles.api.rules.teamselection.TeamSelectionRegistry;
import com.pixelmonmod.pixelmon.battles.controller.BattleController;
import com.pixelmonmod.pixelmon.battles.controller.log.action.BattleAction;
import com.pixelmonmod.pixelmon.battles.controller.participants.*;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import es.boffmedia.teras.Teras;
import es.boffmedia.teras.objects.pixelmon.BattleConfig;
import es.boffmedia.teras.objects_old.pixelmon.PosicionEquipo;
import es.boffmedia.teras.util.Scoreboard;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TerasBattleBak {
    BattleController battle;
    public ServerPlayerEntity player;
    public BattleConfig battleConfig;
    public MobEntity entity;
    public String log;
    public PlayerParticipant playerParticipant;
    public BattleParticipant rivalParticipant;
    LogHelper logHelper;
    private ArrayList<PosicionEquipo> fainted = new ArrayList<>();
    boolean teamSelection = false;

    private HashMap<Integer,HashMap<Integer, PixelmonWrapper>> activePokemon = new HashMap<>();

    ArrayList<String> pokemonInit = new ArrayList<>();
    ArrayList<String> switchInit = new ArrayList<>();

    public TerasBattleBak() {
        this.player = player;
        this.battleConfig = new BattleConfig();
        this.log = "";
        logHelper = new LogHelper();
    }

    public TerasBattleBak(ServerPlayerEntity player, BattleConfig battleConfig) {
        this.player = player;
        this.battleConfig = battleConfig;
        this.log = "";
        logHelper = new LogHelper();
    }

    public void logEvent(BattleAction event){
        TerasBattleLog.logEvent(event, this);
    }

    public int getHighestPlayerLevel(){
        return getPlayerParty().getHighestLevel();
    }

    public int getRivalTeamLevel(){
        return battleConfig.calculateTeamLevel(getHighestPlayerLevel());
    }

    public void start() {
        // Register the battle
        battleConfig.setNivelEquipo(getHighestPlayerLevel());

        if (battleConfig.healBeforeStart()) {
            getPlayerParty().heal();
        }


        Scoreboard.getOrCreateObjective(player, battleConfig.getNombreArchivo());

        BattleRules br = new BattleRules();
        br.setNewClauses(battleConfig.getNormas());
        br = br.set(BattleRuleRegistry.TEAM_SELECT, true);
        br = br.set(BattleRuleRegistry.TEAM_PREVIEW, true);

        br = br.set(BattleRuleRegistry.NUM_POKEMON, battleConfig.getPlayerPkmCount());
        //br = br.set(BattleRuleRegistry.TURN_TIME, 45);
        br = br.set(BattleRuleRegistry.BATTLE_TYPE, battleConfig.getBattleType());



        TeamSelectionRegistry.Builder test =
                TeamSelectionRegistry
                        .builder()
                        .members(getPlayerParticipant().getEntity(), getRivalParticipant().getEntity())
                        .battleRules(br)
                        .showOpponentTeam()
                        .closeable()
                        .battleStartConsumer(bc -> {
                            battle = bc;
                            Teras.getLBC().addTerasBattle(bc.battleIndex, this);
                            TerasBattleLog.appendStartBattle(this);

                            entity.remove();
                            entity = null;
                        })
                        .cancelConsumer(ts -> {
                            Teras.LOGGER.error("CANCELADO");
                        });
        test.start();
    }

    public BattleController getBattle(){
        return battle;
    }

    public String getName1(){
        return player.getDisplayName().getString();
    }

    public String getName2(){
        return getRivalParticipant().getDisplayName();
    }

    public PlayerPartyStorage getPlayerParty(){
        return StorageProxy.getParty(player);
    }

    public PlayerParticipant getPlayerParticipant(){
        if(playerParticipant !=null) return playerParticipant;
        //List<Pokemon> pokemon = getPlayerParty().findAll(Pokemon::canBattle);
        return new PlayerParticipant(player, (Pokemon) null);
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
            npc.setName(battleConfig.getNombre());
            npc.setCustomSteveTexture(name);

            npc.setPos(player.getX(), player.getY(), player.getZ());
            player.level.addFreshEntity(npc);


            //npc.addEffect(new EffectInstance(Effects.INVISIBILITY, Integer.MAX_VALUE, 0, true, true));
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
    }

    public String getLog(){
        return log;
    }

    public int getBattleId() {
        return battle.battleIndex;
    }


    public void swapPokemon(int team, int position, PixelmonWrapper newPokemon) {
        if(!activePokemon.containsKey(team)){
            activePokemon.put(team, new HashMap<>());

        }
        activePokemon.get(team).put(position, newPokemon);
    }
    public boolean swapPokemon(PixelmonWrapper pokemon, PixelmonWrapper switchingTo) {
        for (Map.Entry<Integer, HashMap<Integer, PixelmonWrapper>> teamEntry : activePokemon.entrySet()) {
            for (Map.Entry<Integer, PixelmonWrapper> positionEntry : teamEntry.getValue().entrySet()) {
                if (positionEntry.getValue().equals(pokemon)) {
                    teamEntry.getValue().put(positionEntry.getKey(), switchingTo);
                    return true;
                }
            }
        }
        return false;
    }

    public PixelmonWrapper getActivePokemon(int team, int position){
        if(!activePokemon.containsKey(team)){
            return null;
        }
        return activePokemon.get(team).get(position);
    }

    public HashMap<Integer, PixelmonWrapper> getActiveTeam(int team){
        if(!activePokemon.containsKey(team)){
            return new HashMap<>();
        }
        return activePokemon.get(team);
    }


    final char[] letters = {'a', 'b', 'c', 'd', 'e', 'f'};
    public String getPositionString(PixelmonWrapper pkm){
        String result = null;
        for (Map.Entry<Integer, HashMap<Integer, PixelmonWrapper>> teamEntry : activePokemon.entrySet()) {
            for (Map.Entry<Integer, PixelmonWrapper> positionEntry : teamEntry.getValue().entrySet()) {
                if (positionEntry.getValue().equals(pkm)) {
                    result = "p" + teamEntry.getKey() + letters[positionEntry.getKey()];
                    break;
                }
            }
            if (result != null) {
                break;
            }
        }
        return result;
    }

    public ArrayList<String> getPokemonInit() {
        return pokemonInit;
    }

    public void setPokemonInit(ArrayList<String> pokemonInit) {
        this.pokemonInit = pokemonInit;
    }

    public ArrayList<String> getSwitchInit() {
        return switchInit;
    }

    public void setSwitchInit(ArrayList<String> switchInit) {
        this.switchInit = switchInit;
    }
}


*/