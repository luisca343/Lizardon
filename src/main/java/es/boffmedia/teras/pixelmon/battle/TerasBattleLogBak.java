package es.boffmedia.teras.pixelmon.battle;

import com.pixelmonmod.pixelmon.api.pokemon.species.gender.Gender;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.log.BattleLog;
import com.pixelmonmod.pixelmon.battles.controller.log.MoveResults;
import com.pixelmonmod.pixelmon.battles.controller.log.action.BattleAction;
import com.pixelmonmod.pixelmon.battles.controller.log.action.type.*;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.*;
import es.boffmedia.teras.Teras;
import es.boffmedia.teras.objects_old.pixelmon.PosicionEquipo;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class TerasBattleLogBak {

    public static void parseLog(BattleLog log, Combate combate) {
        Teras.LOGGER.warn("=================== Log del combate ===================");
        log.getAllActions().forEach(action -> logEvent(action, combate));
    }

    public static void logEvent(BattleAction action, Combate combate) {
        /*
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

*/
        Teras.LOGGER.info("SE HA LOGEADO UNA ACCION: " + action.getClass().getSimpleName());
        Teras.LOGGER.warn(action.toString());
    }

    private static void terrainChangeAction(TerrainChangeAction action, Combate combate) {
        Teras.LOGGER.warn("========== TERRAIN CHANGE ACTION ==========");
        Terrain newTerrain = (Terrain) getProtectedProperty("newTerrain", action);
        Terrain oldTerrain = (Terrain) getProtectedProperty("oldTerrain", action);
        PixelmonWrapper pokemon = (PixelmonWrapper) getProtectedProperty("pokemon", action);

        Teras.LOGGER.warn("Nuevo terreno: " + newTerrain);
        Teras.LOGGER.warn("Viejo terreno: " + oldTerrain);



        if(oldTerrain instanceof  NoTerrain && !(newTerrain instanceof NoTerrain)){
            if(pokemon == null) return;
            String nombreTerreno = newTerrain.getClass().getSimpleName();
            combate.appendLog("|fieldstart| " + "move: "+nombreTerreno+"|[of] " + combate.getPosicionv2(pokemon) + ": " + pokemon.getNickname() + System.lineSeparator());
        }

        if(!(oldTerrain instanceof  NoTerrain) && newTerrain instanceof NoTerrain){
            combate.appendLog("|-fieldend|move: Test" + System.lineSeparator());
        }

    }

    private static void globalStatusAddAction(GlobalStatusAddAction action, Combate combate) {
        Teras.LOGGER.warn("========== GLOBAL STATUS ADD ACTION ==========");
        String pokemonName = (String) getProtectedProperty("pokemonName", action);
        GlobalStatusBase status = (GlobalStatusBase) getProtectedProperty("status", action);

        Teras.LOGGER.warn("Pokemon: " + pokemonName);
        Teras.LOGGER.warn("Status: " + status);
    }

    private static void globalStatusRemoveAction(GlobalStatusRemoveAction action, Combate combate) {
        Teras.LOGGER.warn("========== GLOBAL STATUS REMOVE ACTION ==========");
        GlobalStatusBase status = (GlobalStatusBase) getProtectedProperty("status", action);

        Teras.LOGGER.warn("Status: " + status);

    }

    private static void statusAddAction(StatusAddAction action, Combate combate) {
        Teras.LOGGER.warn("========== STATUS ADD ACTION ==========");
        String pokemonName = (String) getProtectedProperty("pokemonName", action);
        StatusBase status = (StatusBase) getProtectedProperty("status", action);

        Teras.LOGGER.warn("Pokemon: " + pokemonName);
        Teras.LOGGER.warn("Status: " + status);
    }

    private static void turnEndAction(TurnEndAction action, Combate combate) {
        List<PixelmonWrapper> teams = (List<PixelmonWrapper>) getProtectedProperty("teams", action);
        Terrain terrain = (Terrain) getProtectedProperty("terrain", action);
        Weather weather = (Weather) getProtectedProperty("weather", action);

        int turn = (int) getProtectedProperty("turn", action);


    }


    private static void turnBeginAction(TurnBeginAction action, Combate combate) {
        int turn = (int) getProtectedProperty("turn", action);
        //Teras.LOGGER.info("========== TURN BEGIN ACTION ["+turn+"] ==========");
        LogHelper helper = combate.logHelper;
        ArrayList<PosicionEquipo> fainted = combate.getFainted();
        Teras.LOGGER.info("Fainted: " + fainted);
        for (PosicionEquipo posicionEquipo : fainted) {
            Teras.LOGGER.info("Posicion: " + posicionEquipo);
            PixelmonWrapper pk = combate.getPokemonAt(posicionEquipo);
            if(combate.esGrupoSuficiente(posicionEquipo) || posicionEquipo.getPosicion() < (combate.getMaxPokemon(posicionEquipo) - 1)){
                helper.enviarMensajeCambio(posicionEquipo, pk, combate);
            }else{
                Teras.LOGGER.info("No quedan suficientes Pokémon en el equipo. Cagaste hijo.");
            }
        }
        combate.resetFainted();

        combate.appendLog("|turn|" + (turn+1) + System.lineSeparator());
        combate.appendLog("|callback|decision" + System.lineSeparator());
        combate.appendLog("|" + System.lineSeparator());


        List<PixelmonWrapper> startingTeams = (List<PixelmonWrapper>) getProtectedProperty("startingTeams", action);

        Teras.LOGGER.info("Starting teams: " + startingTeams);


        if(turn==0) return;


        //comprobarSiLosPokemonEstanPutoVivosDeUnaVez(combate);

    }


    private static void switchAction(SwitchAction action, Combate combate) {
        //Teras.LOGGER.info("========== SWITCH ACTION ==========");
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
        //Teras.LOGGER.info("========== ATTACK ACTION ==========");
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
        if(message.contains("used")) return;
        if(message.contains("sent out")) return;
        if(message.contains("fainted")) return;

        combate.appendLog("|-message|" + message + System.lineSeparator());
    }

    private static void statChangeAction(StatChangeAction action, Combate combate) {
        /*
        //Teras.LOGGER.info("========== STAT CHANGE ACTION ==========");
        PixelmonWrapper pokemon = (PixelmonWrapper) getProtectedProperty("pokemon", action);
        LogHelper logHelper = combate.logHelper;
        ArrayList<Integer> oldBoosts = logHelper.getBoosts(pokemon.getPokemonUUID());
        if(oldBoosts == null){
            logHelper.initBoosts(pokemon);
            Teras.LOGGER.info("Se ha inicializado los boosts de " + pokemon.getNickname());
            return;
        }

        ArrayList<Integer> currentBoosts = logHelper.getBoostsActuales(pokemon);

        // Compare oldBoosts with currentBoosts
        for (int i = 0; i < oldBoosts.size(); i++) {
            int oldBoost = oldBoosts.get(i);
            int currentBoost = currentBoosts.get(i);
            if(oldBoost != currentBoost){
                int cambio = currentBoost - oldBoost;
                String stat = LogHelper.BattleBoostsType.fromIndex(i).getName();
                String tipo = cambio > 0 ? "|-boost|" : "|-unboost|";
                PosicionEquipo pos = combate.getPosicionv2(pokemon);
                cambio = Math.abs(cambio);
                combate.appendLog(tipo + pos.toString() + ": " + pokemon.getNickname() + "|" + stat + "|" + cambio + System.lineSeparator() );
            }
        }

        logHelper.setBoosts(pokemon, currentBoosts);*/
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
