/*package es.boffmedia.teras.pixelmon.battle;

import com.pixelmonmod.pixelmon.api.pokemon.species.gender.Gender;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.log.BattleLog;
import com.pixelmonmod.pixelmon.battles.controller.log.MoveResults;
import com.pixelmonmod.pixelmon.battles.controller.log.action.BattleAction;
import com.pixelmonmod.pixelmon.battles.controller.log.action.type.*;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import es.boffmedia.teras.pixelmon.battle.LogHelper;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.Arrays;

public class TerasBattleLog2 {

    public static void logEvent(BattleAction action, Combate combate) {
        if (action instanceof TurnBeginAction) turnBeginAction((TurnBeginAction) action, combate);
        if (action instanceof StatChangeAction) statChangeAction((StatChangeAction) action, combate);
        if (action instanceof BattleMessageAction) battleMessageAction((BattleMessageAction) action, combate);
        if (action instanceof SelectAttackAction) selectAttackAction((SelectAttackAction) action, combate);
        if (action instanceof AttackAction) attackAction((AttackAction) action, combate);
        if (action instanceof DamagePokemonAction) damagePokemonAction((DamagePokemonAction) action, combate);
        if (action instanceof SwitchAction) switchAction((SwitchAction) action, combate);
    }

    public static void parseLog(BattleLog battleLog, Combate combate){
        int index = 0;
        for (BattleAction action : battleLog.getAllActions()) {
            if (action instanceof TurnBeginAction) turnBeginAction((TurnBeginAction) action, combate);
            if (action instanceof StatChangeAction) statChangeAction((StatChangeAction) action, combate);
            if (action instanceof BattleMessageAction) battleMessageAction((BattleMessageAction) action, combate);
            if (action instanceof SelectAttackAction) selectAttackAction((SelectAttackAction) action, combate);
            if (action instanceof AttackAction) attackAction((AttackAction) action, combate);
            if (action instanceof DamagePokemonAction) damagePokemonAction((DamagePokemonAction) action, combate);
            if (action instanceof SwitchAction) switchAction((SwitchAction) action, combate);
            index++;
        }
    }

    public static void switchAction(SwitchAction action, Combate combate){
        System.out.println("------------------------------------");
        System.out.println(combate.activos);
        PixelmonWrapper pokemon = (PixelmonWrapper) getProtectedProperty("pokemon", action);
        PixelmonWrapper switchingTo = action.switchingTo;

        LogHelper helper = combate.logHelper;

        String posPokemon = combate.getPosicion(pokemon);
        if(posPokemon == null){
            posPokemon = combate.getPosicion(switchingTo);
        }
        combate.setPosicion(posPokemon, switchingTo);
        combate.appendLog("|switch|" + posPokemon + ": " + helper.getPkmGenderShiny(switchingTo) + "|" + helper.getCurrentTotalHealth(switchingTo) + System.lineSeparator());
    }

    static  String letras ="abcdefghijklmnopqrstuvwxyz";
    public static void turnBeginAction(TurnBeginAction action, Combate combate){
        int turn = (int) getProtectedProperty("turn", action);
        System.out.println("==========Turno: "+ turn+ "==========");
        if(turn == 0) {

            LogHelper logHelper = new LogHelper();
            List<PixelmonWrapper> startingTeams = (List<PixelmonWrapper>) getProtectedProperty("startingTeams", action);
            System.out.println("Iniciando combate");
            System.out.println(startingTeams.size());
            System.out.println(startingTeams);

            String nombre1 = combate.player.getDisplayName().getString();
            String nombre2 = combate.getPartRival().getDisplayName();

            combate.appendLog("|join|"+nombre1 + System.lineSeparator());
            combate.appendLog("|join|"+nombre2 + System.lineSeparator());
            combate.appendLog("|player|p1|"+nombre1 + System.lineSeparator());
            combate.appendLog("|player|p2|"+nombre2 + System.lineSeparator());
            combate.appendLog("|gametype|Singles" + System.lineSeparator());
            combate.appendLog("|gen|9" + System.lineSeparator());
            combate.appendLog("|tier|Circuito de Gimnasios de Teras" + System.lineSeparator());
            // Dividimos a la mitad
            PixelmonWrapper[] equipo1 = combate.getPartJugador().allPokemon;
            PixelmonWrapper[] equipo2 = combate.getPartRival().allPokemon;

            logHelper.setEquipo1(equipo1);
            logHelper.setEquipo2(equipo2);

            appendEquipo(equipo1, combate, 1);
            appendEquipo(equipo2, combate, 2);
            combate.appendLog("|teampreview" + System.lineSeparator());
            combate.appendLog("|" + System.lineSeparator());
            combate.appendLog("|start" + System.lineSeparator());

            ArrayList<PixelmonWrapper> playerPokemon = new ArrayList<>();
            ArrayList<PixelmonWrapper> opponentPokemon = new ArrayList<>();

            playerPokemon.add(equipo1[0]);
            opponentPokemon.add(equipo2[0]);


            for (int i = 0; i < playerPokemon.size(); i++) combate.appendLog(getSwitchPkmStr(playerPokemon.get(i), 1, i, true, logHelper));
            for (int i = 0; i < opponentPokemon.size(); i++) combate.appendLog(getSwitchPkmStr(opponentPokemon.get(i), 2, i, true, logHelper));

            logs.put(combate.idCombate, logHelper);
        }

        combate.appendLog("|turn|" + turn + System.lineSeparator());
        combate.appendLog("|callback|decision" + System.lineSeparator());
        combate.appendLog("|" + System.lineSeparator());
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

    public static String getSwitchPkmStr(PixelmonWrapper pk, int player, int pos, boolean first, Combate combate){
        System.out.println("1. Switching to: " + pk.getNickname());
        String gender = pk.getGender().equals(Gender.FEMALE) ? ", F" :  (pk.getGender().equals(Gender.MALE) ? ", M" : "");
        System.out.println("2. Switching to: " + pk.getNickname());
        String shiny = pk.getRealTextureNoCheck().contains("shiny") ? ", shiny" : "";
        System.out.println("3. Switching to: " + pk.getNickname());
        String nickname = pk.getNickname();
        System.out.println("4. Switching to: " + pk.getNickname());

        String nombre = pk.getSpecies().getName();
        int health = first ? pk.getMaxHealth() : pk.getHealth();
        System.out.println("5. Switching to: " + pk.getNickname());

        String datosPoke = nickname + "|" + nombre + gender + shiny + "|" + health + "/" + pk.getMaxHealth() + System.lineSeparator();

        System.out.println("6. Switching to: " + pk.getNickname());
        combate.setPosicion(player ,letras.charAt(pos), pk);
        System.out.println("7. Switching to: " + pk.getNickname());


        return "|switch|p" +player + letras.charAt(pos) + ": " + datosPoke;
    }

    public static String getSwitchPkmStr(PixelmonWrapper pk, int player, int pos, LogHelper logHelper){
        return getSwitchPkmStr(pk, player, pos, logHelper);
    }

    public static void statChangeAction(StatChangeAction action, Combate combate){
        final int[] oldStats = (int[]) getProtectedProperty("oldStats", action);
        final int[] newStats = (int[]) getProtectedProperty("newStats", action);

        if(Arrays.stream(oldStats).anyMatch(i -> i == 0)){
            return;
        }

        if(Arrays.equals(oldStats, newStats)){
            return;
        }

        //System.out.println("Stats antiguos: " + oldStats[0] + " " + oldStats[1] + " " + oldStats[2] + " " + oldStats[3] + " " + oldStats[4] + " " + oldStats[5]);
        //System.out.println("Stats nuevos: " + newStats[0] + " " + newStats[1] + " " + newStats[2] + " " + newStats[3] + " " + newStats[4] + " " + newStats[5]);
    }

    public static void battleMessageAction(BattleMessageAction action, Combate combate){
        //System.out.println("Es un mensaje");

    }

    public static void selectAttackAction(SelectAttackAction action, Combate combate){
        //System.out.println("Es un ataque seleccionado");
    }

    public static void attackAction(AttackAction action, Combate combate){
        PixelmonWrapper pokemon = (PixelmonWrapper) getProtectedProperty("pokemon", action);
        LogHelper helper = combate.logHelper;

        String posAtaque = combate.getPosicion(pokemon);

        Attack attack = (Attack) getProtectedProperty("attack", action);
        String[] targets = (String[]) getProtectedProperty("targets", action);

        String posObjetivo = combate.getPosicion(targets[0]);


        combate.appendLog("|move|" + posAtaque + ": " + pokemon.getNickname()+"|"+attack.getMove().getAttackName()+"|"+posObjetivo+": " + targets[0] +  System.lineSeparator());

        for (MoveResults moveResult : action.getMoveResults()) {
            PixelmonWrapper target = moveResult.getTarget();
            String posTarget = combate.getPosicion(target);

            combate.appendLog("|-damage|" + posTarget + ": " + target.getNickname() + "|" + target.getHealth() + "\\/" + (target.getHealth() + moveResult.damage) + System.lineSeparator());

            if(target.getHealth() == 0){
                combate.appendLog("|faint|" + posTarget + ": " + target.getNickname() + System.lineSeparator());
                combate.activos.remove(posTarget);
            }

            if(moveResult.getDamage() == 0){
                combate.appendLog("|-fail|:" + target.getNickname() + System.lineSeparator());
            }


            if(helper.getPokemonRestantes(target) == 0){
                String ganador = helper.getPlayerContrario(target);
                String nombreGanador = ganador.equals("p1") ? combate.player.getDisplayName().getString() : combate.getPartRival().getDisplayName();
                combate.appendLog("|win|" + nombreGanador + System.lineSeparator());
            }
        }
    }

    public static void damagePokemonAction(DamagePokemonAction action, Combate combate){
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

}*/
