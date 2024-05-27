package es.boffmedia.teras.pixelmon.battle;

import com.pixelmonmod.pixelmon.api.events.battles.BattleEndEvent;
import com.pixelmonmod.pixelmon.api.pokemon.stats.BattleStatsType;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.BattleController;
import com.pixelmonmod.pixelmon.battles.controller.log.MoveResults;
import com.pixelmonmod.pixelmon.battles.controller.log.action.BattleAction;
import com.pixelmonmod.pixelmon.battles.controller.log.action.type.*;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.GlobalStatusBase;
import com.pixelmonmod.pixelmon.battles.status.StatusBase;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.battles.status.Weather;
import es.boffmedia.teras.Teras;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TerasBattleLog {

    public static HashMap<Integer, String> logs = new HashMap<>();
    //public static HashMap<Integer, HashMap<String, List<PixelmonWrapper>>> activePokemon = new HashMap<>();

    public static void initLog(int battleIndex){
        logs.put(battleIndex, "");
    }

    public static String getBattleLog(int battleIndex){
        if(!logs.containsKey(battleIndex)) {
            initLog(battleIndex);
        }
        return logs.get(battleIndex);
    }

    public static void appendLine(int battleIndex, String log){
        //Teras.getLogger().warn("LOGGING: " + log);
        logs.put(battleIndex, logs.get(battleIndex) + log + System.lineSeparator());
    }

    public static void printLog(int battleIndex){
        Teras.LOGGER.info("LOG DEL COMBATE " + battleIndex);
        Teras.LOGGER.info(logs.get(battleIndex));
    }

    public static void logEvent(BattleAction action, TerasBattle battle) {
        Teras.LOGGER.warn("=== Log event: " + action.toString() + " ===");
        /*

        if (action instanceof TurnBeginAction) turnBeginAction((TurnBeginAction) action, bc);
        else if (action instanceof StatChangeAction) statChangeAction((StatChangeAction) action, bc);
        else if (action instanceof AttackAction) attackAction((AttackAction) action, bc);
        else if (action instanceof WeatherChangeAction) weatherChangeAction((WeatherChangeAction) action, bc);
        else if (action instanceof StatusAddAction) statusAddAction((StatusAddAction) action, bc);
        else if (action instanceof SwitchAction) switchAction((SwitchAction) action, bc);


        else if (action instanceof BattleMessageAction) battleMessageAction((BattleMessageAction) action, bc);
        else if (action instanceof TurnEndAction) turnEndAction((TurnEndAction) action, bc);
        else if (action instanceof BattleEndAction) { battleEndAction((BattleEndAction) action, bc); }
        else Teras.LOGGER.info("Unknown action: " + action.getClass().getSimpleName());

        */


        /* ====== TO DO ======
         * Añadir |faint|p1a: Mega Salamanca
         * Arreglar el switch cuando entra un pokemon después de debilitar a otro (es un switch normal)
         * ====== TO DO ======
         */
    }

    /* Actions */
    private static void battleEndAction(BattleEndAction action, BattleController bc) {
        Teras.LOGGER.error("===Battle End===");
        Teras.LOGGER.info("\n"+getBattleLog(bc.battleIndex));

    }



    public static void turnBeginAction(TurnBeginAction action, BattleController bc){
        int turn = (int) getProtectedProperty("turn", action);
        if(turn == 0) appendStartBattle(bc);


        appendLine(bc.battleIndex,"|turn|" + (turn+1));
        //appendLine(bc.battleIndex,"|callback|decision");
        appendLine(bc.battleIndex,"|");
        appendLine(bc.battleIndex, "|t:|" + System.currentTimeMillis() / 1000);

    }

    public static PokemonPosition getPosition(PixelmonWrapper pokemon, BattleController bc){
        int participantIndex = 0;
        for (BattleParticipant participant : bc.participants) {
            int index = participant.getTeamPokemon().indexOf(pokemon);
            if(index != -1){
                return new PokemonPosition(participantIndex, index);
            }
            participantIndex++;
        }

        return null;
    }

    public static void turnEndAction(TurnEndAction action, BattleController bc){

    }

    public static void battleMessageAction(BattleMessageAction action, BattleController bc){
        String message = (String) getProtectedProperty("message", action);
        Teras.getLogger().error(message);
    }

    public static void attackAction(AttackAction action, BattleController bc){
        PixelmonWrapper pokemon = (PixelmonWrapper) getProtectedProperty("pokemon", action);
        Attack attack = (Attack) getProtectedProperty("attack", action);
        boolean wildPokemon = (boolean) getProtectedProperty("wildPokemon", action);
        String pokemonName = (String) getProtectedProperty("pokemonName", action);
        String[] targets = (String[]) getProtectedProperty("targets", action);
        MoveResults[] moveResults = (MoveResults[]) getProtectedProperty("moveResults", action);

        // The attack action is a bit more complex, as it can target multiple pokemon
        // move|p2a: Calyrex|Astral Barrage|p1a: Kyogren't|[spread] p1a,p1b

        String move = attack.getActualMove().getAttackName();
        String source = getPositionString(pokemon, bc);

        String attackStr =  "|move|" + getPositionAndNameString(pokemon, bc) + "|" + move + "|";
        String mainTargetStr = getPositionAndNameString(moveResults[0].target, bc);

        attackStr += mainTargetStr + "|";

        if(moveResults.length > 1){
            attackStr += "[spread] ";
            attackStr += Arrays.stream(moveResults).map((target) -> getPositionString(target.target, bc)).reduce((a, b) -> a + "," + b).get();
        }

        appendLine(bc.battleIndex, attackStr);

        for (MoveResults moveResult : moveResults) {
            PixelmonWrapper target = moveResult.target;
            String targetName = target.getNickname();

            int currentHealth = moveResult.getTarget().getHealth();
            int maxHealth = moveResult.getTarget().getMaxHealth();

            if(currentHealth == 0){
                appendLine(bc.battleIndex, "|-damage|" + getPositionString(target, bc) + ": " + targetName + "|0 fnt");
            }else{
                appendLine(bc.battleIndex, "|-damage|" + getPositionString(target, bc) + ": " + targetName + "|" + currentHealth + "\\/" + maxHealth);
            }


        }
    }

    public static void switchAction(SwitchAction action, BattleController bc){
        Teras.LOGGER.info("------------------ Switch action ------------------");
        PixelmonWrapper pokemon = (PixelmonWrapper) getProtectedProperty("pokemon", action);
        PixelmonWrapper switchingTo = (PixelmonWrapper) getProtectedProperty("switchingTo", action);
        //|switch|p1a: Suicum|Walking Wake, L50|100\/100
        appendLine(bc.battleIndex, "|switch|" + getPositionString(pokemon, bc) + ": " +switchingTo.getNickname()  + "|" + switchingTo.getSpecies().getName() + ", L" + switchingTo.getPokemonLevel() + "|" + switchingTo.getHealth() + "\\/" + switchingTo.getMaxHealth());

    }


    public static void weatherChangeAction(WeatherChangeAction action, BattleController bc){
        //|-weather|SunnyDay|[from] ability: Drought|[of] p1a: Kyogren't
        GlobalStatusBase newGlobalStatus = (GlobalStatusBase) getProtectedProperty("newWeather", action);
        GlobalStatusBase oldGlobalStatus = (GlobalStatusBase) getProtectedProperty("oldWeather", action);
        PixelmonWrapper pokemon = (PixelmonWrapper) getProtectedProperty("pokemon", action);

        if(newGlobalStatus instanceof Weather){
            Weather newWeather = (Weather) newGlobalStatus;
            String weatherName;
            switch(newWeather.type) {
                case Sunny:
                    weatherName = "SunnyDay";
                    break;
                default:
                    weatherName = "UnknownWeather";
                    break;
            }
            appendLine(bc.battleIndex, "|-weather|" + weatherName + "|");
        }
    }

    public static void statusAddAction(StatusAddAction action, BattleController bc){
        StatusBase status = (StatusBase) getProtectedProperty("status", action);
        PixelmonWrapper pokemon = (PixelmonWrapper) getProtectedProperty("pokemon", action);

        if(status.type == StatusType.ParadoxBoost){
            appendLine(bc.battleIndex,"|-activate|" + getPositionAndNameString(pokemon, bc) + "|ability: " + pokemon.getAbility().getName());
        }
    }

    /*
    public static void globalStatusAddAction(GlobalStatusAddAction action, BattleController bc){
        //|-status|p1a: Kyogren't|confusion
        PixelmonWrapper pokemon = (PixelmonWrapper) getProtectedProperty("pokemon", action);
        GlobalStatusBase status = (GlobalStatusBase) getProtectedProperty("status", action);
        String statusName = status.getClass().getSimpleName().toLowerCase();
        appendLine(bc.battleIndex, "|-status|" + getPositionAndNameString(pokemon, bc) + "|" + statusName);
    }*/

    public static void statChangeAction(StatChangeAction action, BattleController bc){
        int[] oldStats = (int[]) getProtectedProperty("oldStats", action);
        int[] newStats = (int[]) getProtectedProperty("newStats", action);
        String pokemonName = (String) getProtectedProperty("pokemonName", action);

        //Teras.getLogger().error("+++Pokemon: " + pokemon.getSpecies().getName() +" " + pokemon.getPokemonName() +" " + pokemon.getPokemonUUID());

        int[] delta = new int[oldStats.length];
        for (int i = 0; i < oldStats.length; i++) {
            delta[i] = newStats[i] - oldStats[i];
        }

        for (int i = 0; i < delta.length; i++) {
            if (delta[i] == 0) continue;

            BattleStatsType stat = BattleStatsType.fromIndex(i);
            Teras.LOGGER.info(pokemonName + " " + stat.name() + " " + delta[i]);

            String boostType = delta[i] > 0 ? "|-boost|" : "|-unboost|";
            String statName = stat.name().toLowerCase();



        }


    }

    // Helpers
    public static String getPositionAndNameString(PixelmonWrapper pokemon, BattleController bc){
        return getPositionString(pokemon, bc) + ": " + pokemon.getNickname();
    }

    public static String getPositionString(PixelmonWrapper pokemon, BattleController bc){
        // Returns the position of the pokemon in the team
        // The position consists of the letter p, the participant number and the position in the team (a, b, c, d, e, f...)

        for (BattleParticipant participant : bc.participants) {
            int index = participant.getTeamPokemon().indexOf(pokemon);
            if(index != -1){
                return "p" + (bc.participants.indexOf(participant) + 1) + getPositionLetter(index);
            }
        }
        return null;
    }

    public static char getPositionLetter(int index){
        final char[] letters = {'a', 'b', 'c', 'd', 'e', 'f'};

        return letters[index];
    }

    public static void appendStartBattle(BattleController bc){
        initLog(bc.battleIndex);
        ArrayList<String> pokemonInit = new ArrayList<>();
        ArrayList<String> switchInit = new ArrayList<>();

        final int[] index = {1};
        final char[] teamPos = {'a', 'b', 'c', 'd', 'e', 'f'};

        /*
        activePokemon = new HashMap<>();
        HashMap<String, List<PixelmonWrapper>> active = activePokemon.get(bc.battleIndex);
        if(active == null){
            active = new HashMap<>();
            activePokemon.put(bc.battleIndex, active);
        }

        HashMap<String, List<PixelmonWrapper>> finalActive = active;

        HashMap<String, List<PixelmonWrapper>> finalActive1 = active;
        */

        bc.participants.forEach((participant) -> {
            Teras.LOGGER.error("===========================");
            Teras.LOGGER.error(participant.getDisplayName());
            Teras.LOGGER.error(participant.getTeamPokemon().size());
            Teras.LOGGER.error(participant.numControlledPokemon);

            for(int i = 0; i < participant.allPokemon.length; i++){
                PixelmonWrapper pokemon = participant.allPokemon[i];
                Teras.LOGGER.warn("INIT: " + pokemon.getNickname() + " " + pokemon.getPokemonLevel().getPokemonLevel());
                if(pokemon != null) pokemonInit.add(bc.battleIndex, "|poke|p" + index[0] + "|" + pokemon.getSpecies().getName()  + ", L"+pokemon.getPokemonLevel().getPokemonLevel()+"|");
            }


            /*
            participant.getTeamPokemon().forEach((pokemon) -> {
                Teras.LOGGER.warn("INIT: " + pokemon.getNickname() + " " + pokemon.getPokemonLevel().getPokemonLevel());
                if(pokemon != null) pokemonInit.add(bc.battleIndex, "|poke|p" + index[0] + "|" + pokemon.getSpecies().getName()  + ", L"+pokemon.getPokemonLevel().getPokemonLevel()+"|");
            });
            */

            final int[] positionIndex = {0};
            int max = participant.numControlledPokemon;


            for(int i = 0; i < max; i++){
                PixelmonWrapper pw = (PixelmonWrapper) participant.allPokemon[i];
                if(pw != null) switchInit.add(bc.battleIndex, "|switch|p" + index[0] + teamPos[positionIndex[0]]
                        + "|" + pw.getNickname() + "|"+pw.getSpecies().getName()
                        +", L"+pw.getPokemonLevel().getPokemonLevel()+"|"+pw.getHealth()+"\\/"+pw.getMaxHealth());
                positionIndex[0]++;

                //finalActive.put(participant.getDisplayName(), Arrays.asList(participant.allPokemon));
            }

            /*
            participant.controlledPokemon.forEach((pokemon) -> {
                PixelmonWrapper pw = (PixelmonWrapper) pokemon;
                switchInit.add(bc.battleIndex, "|switch|p" + index[0] + teamPos[positionIndex[0]]
                        + "|" + pw.getNickname() + "|"+pw.getSpecies().getName()
                        +", L"+pw.getPokemonLevel().getPokemonLevel()+"|"+pw.getHealth()+"\\/"+pw.getMaxHealth());
                positionIndex[0]++;
            });*/

            index[0]++;
        });




        pokemonInit.forEach((line) -> appendLine(bc.battleIndex, line));
        appendLine(bc.battleIndex, "|start");
        switchInit.forEach((line) -> appendLine(bc.battleIndex, line));



    }

    // Reflection
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
