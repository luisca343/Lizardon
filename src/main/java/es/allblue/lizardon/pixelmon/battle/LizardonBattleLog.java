package es.allblue.lizardon.pixelmon.battle;

import com.pixelmonmod.pixelmon.api.pokemon.species.gender.Gender;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.log.MoveResults;
import com.pixelmonmod.pixelmon.battles.controller.log.action.BattleAction;
import com.pixelmonmod.pixelmon.battles.controller.log.action.type.*;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.GlobalStatusBase;
import com.pixelmonmod.pixelmon.battles.status.StatusBase;
import com.pixelmonmod.pixelmon.battles.status.Terrain;
import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.objects.pixelmon.PosicionEquipo;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class LizardonBattleLog {

    public static void logEvent(BattleAction action, Combate combate) {
        if (action instanceof TurnBeginAction) turnBeginAction((TurnBeginAction) action, combate);
        if (action instanceof TurnEndAction) turnEndAction((TurnEndAction) action, combate);
        if (action instanceof StatChangeAction) statChangeAction((StatChangeAction) action, combate);
        if (action instanceof BattleMessageAction) battleMessageAction((BattleMessageAction) action, combate);
        if (action instanceof SelectAttackAction) selectAttackAction((SelectAttackAction) action, combate);
        if (action instanceof AttackAction) attackAction((AttackAction) action, combate);
        if (action instanceof DamagePokemonAction) damagePokemonAction((DamagePokemonAction) action, combate);
        if (action instanceof SwitchAction) switchAction((SwitchAction) action, combate);

        if (action instanceof StatusAddAction) statusAddAction((StatusAddAction) action, combate);
        if (action instanceof GlobalStatusRemoveAction) globalStatusRemoveAction((GlobalStatusRemoveAction) action, combate);
        if (action instanceof GlobalStatusAddAction) globalStatusAddAction((GlobalStatusAddAction) action, combate);
        if (action instanceof TerrainChangeAction) terrainChangeAction((TerrainChangeAction) action, combate);


        Lizardon.LOGGER.info("SE HA LOGEADO UNA ACCION: " + action.getClass().getSimpleName());
    }

    private static void terrainChangeAction(TerrainChangeAction action, Combate combate) {
        Lizardon.LOGGER.warn("========== TERRAIN CHANGE ACTION ==========");
        Terrain newTerrain = (Terrain) getProtectedProperty("newTerrain", action);
        Terrain oldTerrain = (Terrain) getProtectedProperty("oldTerrain", action);

        Lizardon.LOGGER.warn("Nuevo terreno: " + newTerrain);
        Lizardon.LOGGER.warn("Viejo terreno: " + oldTerrain);
    }

    private static void globalStatusAddAction(GlobalStatusAddAction action, Combate combate) {
        Lizardon.LOGGER.warn("========== GLOBAL STATUS ADD ACTION ==========");
        String pokemonName = (String) getProtectedProperty("pokemonName", action);
        GlobalStatusBase status = (GlobalStatusBase) getProtectedProperty("status", action);

        Lizardon.LOGGER.warn("Pokemon: " + pokemonName);
        Lizardon.LOGGER.warn("Status: " + status);
    }

    private static void globalStatusRemoveAction(GlobalStatusRemoveAction action, Combate combate) {
        Lizardon.LOGGER.warn("========== GLOBAL STATUS REMOVE ACTION ==========");
        GlobalStatusBase status = (GlobalStatusBase) getProtectedProperty("status", action);

        Lizardon.LOGGER.warn("Status: " + status);

    }

    private static void statusAddAction(StatusAddAction action, Combate combate) {
        Lizardon.LOGGER.warn("========== STATUS ADD ACTION ==========");
        String pokemonName = (String) getProtectedProperty("pokemonName", action);
        StatusBase status = (StatusBase) getProtectedProperty("status", action);

        Lizardon.LOGGER.warn("Pokemon: " + pokemonName);
        Lizardon.LOGGER.warn("Status: " + status);
    }

    private static void turnEndAction(TurnEndAction action, Combate combate) {
        List<PixelmonWrapper> teams = (List<PixelmonWrapper>) getProtectedProperty("teams", action);
        int turn = (int) getProtectedProperty("turn", action);

    }


    private static void turnBeginAction(TurnBeginAction action, Combate combate) {
        int turn = (int) getProtectedProperty("turn", action);
        //Lizardon.LOGGER.info("========== TURN BEGIN ACTION ["+turn+"] ==========");
        LogHelper helper = combate.logHelper;
        ArrayList<PosicionEquipo> fainted = combate.getFainted();
        Lizardon.LOGGER.info("Fainted: " + fainted);
        for (PosicionEquipo posicionEquipo : fainted) {
            Lizardon.LOGGER.info("Posicion: " + posicionEquipo);
            PixelmonWrapper pk = combate.getPokemonAt(posicionEquipo);
            if(combate.esGrupoSuficiente(posicionEquipo) || posicionEquipo.getPosicion() < (combate.getMaxPokemon(posicionEquipo) - 1)){
                helper.enviarMensajeCambio(posicionEquipo, pk, combate);
            }else{
                Lizardon.LOGGER.info("No quedan suficientes Pokémon en el equipo. Cagaste hijo.");
            }
        }
        combate.resetFainted();

        combate.appendLog("|turn|" + (turn+1) + System.lineSeparator());
        combate.appendLog("|callback|decision" + System.lineSeparator());
        combate.appendLog("|" + System.lineSeparator());


        List<PixelmonWrapper> startingTeams = (List<PixelmonWrapper>) getProtectedProperty("startingTeams", action);

        Lizardon.LOGGER.info("Starting teams: " + startingTeams);


        if(turn==0) return;


        //comprobarSiLosPokemonEstanPutoVivosDeUnaVez(combate);

    }


    private static void switchAction(SwitchAction action, Combate combate) {
        //Lizardon.LOGGER.info("========== SWITCH ACTION ==========");
        PixelmonWrapper pokemon = (PixelmonWrapper) getProtectedProperty("pokemon", action);
        PixelmonWrapper switchingTo = action.switchingTo;

        LogHelper helper = combate.logHelper;
        PosicionEquipo pos = combate.getPosicionv2(pokemon);

        helper.enviarMensajeCambio(pos, switchingTo, combate);
    }

    private static void damagePokemonAction(DamagePokemonAction action, Combate combate) {
        String pokemonName = (String) getProtectedProperty("pokemonName", action);
        int damage = (int) getProtectedProperty("damage", action);
        String source= (String) getProtectedProperty("source", action);
        DamageTypeEnum damageType = (DamageTypeEnum) getProtectedProperty("damageType", action);
        int healthBefore= (int) getProtectedProperty("healthBefore", action);
        int healthAfter= (int) getProtectedProperty("healthAfter", action);

        //combate.appendLog("|-damage|" + pokemonName + "|" + healthAfter + "\\/" + healthBefore + System.lineSeparator());
    }

    private static void attackAction(AttackAction action, Combate combate) {
        //Lizardon.LOGGER.info("========== ATTACK ACTION ==========");
        PixelmonWrapper pokemon = (PixelmonWrapper) getProtectedProperty("pokemon", action);
        PosicionEquipo posPokemon = combate.getPosicionv2(pokemon);
        LogHelper helper = combate.logHelper;
        Attack attack = (Attack) getProtectedProperty("attack", action);
        String[] targets = (String[]) getProtectedProperty("targets", action);

        String target1 = pokemon.getNickname();
        if(targets.length > 0) {
            target1 = targets[0];
        }

        PosicionEquipo pos0 = combate.getPosicionByName(target1);
        if(pos0 == null) pos0 = posPokemon;

        ArrayList<String> acciones = new ArrayList<>();

        combate.appendLog("|move|" + posPokemon.toString() + ": " + pokemon.getNickname()+"|"+attack.getMove().getAttackName()+"|"+pos0+": " + target1 +  System.lineSeparator());

        int index = 0;
        for (MoveResults moveResult : action.getMoveResults()) {
            PixelmonWrapper objetivo = moveResult.target;
            PosicionEquipo posicion = combate.getPosicionv2(objetivo);
            String posObjetivo = posicion.toString();

            index++;
            String vidaRestante = objetivo.getHealth() > 0 ? objetivo.getHealth() + "\\/" + objetivo.getMaxHealth() : "0 fnt";

            //if(objetivo.getHealth() > 0) combate.appendLog("|-damage|" + posObjetivo + ": " + objetivo.getNickname() + "|" + objetivo.getHealth() + "\\/" + objetivo.getMaxHealth() + System.lineSeparator());
            acciones.add("|-damage|" + posObjetivo + ": " + objetivo.getNickname() + "|" + vidaRestante + System.lineSeparator());

            if(moveResult.getDamage() == 0){
                //combate.appendLog("|-fail|"+posObjetivo+": " + objetivo.getNickname() + System.lineSeparator());
                acciones.add("|-fail|"+posObjetivo+": " + objetivo.getNickname() + System.lineSeparator());
            }


            /*
            if(helper.getPokemonRestantes(posicion, combate) == 0){
                int ganador = helper.getPlayerContrario(posicion);
                String nombreGanador = ganador == 1 ? combate.player.getDisplayName().getString() : combate.getPartRival().getDisplayName();
                combate.appendLog("|win|" + nombreGanador + System.lineSeparator());
            }*/

        }

        acciones.forEach(combate::appendLog);
        for (MoveResults moveResult : action.getMoveResults()) {
            PixelmonWrapper objetivo = moveResult.target;
            PosicionEquipo posicion = combate.getPosicionv2(objetivo);
            String posObjetivo = posicion.toString();

            if(objetivo.getHealth() == 0){
                combate.appendLog("|faint|" + posObjetivo + ": " + objetivo.getNickname() + System.lineSeparator());
                combate.setFainted(posicion);
            }

            if(helper.getPokemonRestantes(posicion, combate) == 0){
                int ganador = helper.getPlayerContrario(posicion);
                String nombreGanador = ganador == 1 ? combate.player.getDisplayName().getString() : combate.getPartRival().getDisplayName();
                combate.appendLog("|win|" + nombreGanador + System.lineSeparator());
            }
        }



    }

    private static void selectAttackAction(SelectAttackAction action, Combate combate) {

    }

    private static void battleMessageAction(BattleMessageAction action, Combate combate) {
        String message = (String) getProtectedProperty("message", action);
        Lizardon.LOGGER.warn("BATTLE LOG: " + message);
    }

    private static void statChangeAction(StatChangeAction action, Combate combate) {
        //Lizardon.LOGGER.info("========== STAT CHANGE ACTION ==========");
        PixelmonWrapper pokemon = (PixelmonWrapper) getProtectedProperty("pokemon", action);
        final int[] oldStats = (int[]) getProtectedProperty("oldStats", action);
        final int[] newStats = (int[]) getProtectedProperty("newStats", action);

        LogHelper helper = combate.logHelper;
        //helper.checkFallecimiento(pokemon, combate);

        if(Arrays.equals(oldStats, newStats)){
            Lizardon.LOGGER.info("Los stats de " + pokemon.getNickname() + " no han cambiado");
            return;
        }

    }

    public static void appendEquipo(PixelmonWrapper[] equipo1, Combate combate, int numJugador) {
        for (int i = 0; i < equipo1.length; i++) {
            PixelmonWrapper pk = equipo1[i];
            combate.appendLog(getStartingPkmStr(pk, numJugador));
        }
    }
    public static String getStartingPkmStr(PixelmonWrapper pk, int numJugador){
        char gender = pk.getGender().equals(Gender.FEMALE) ? 'F' :  (pk.getGender().equals(Gender.MALE) ? 'M' : '-');
        String shiny = pk.getRealTextureNoCheck().contains("shiny") ? ", shiny" : "";
        return "|poke|p" + numJugador + "|" + pk.getSpecies().getName() + ", " + gender + shiny + System.lineSeparator();
    }


    public static Object getProtectedProperty(String property, Object obj){
        Field f = FieldUtils.getField(obj.getClass(), property, true);
        f.setAccessible(true);

        try {
            return f.get(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
