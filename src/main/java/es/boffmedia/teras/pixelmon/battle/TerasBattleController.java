package es.boffmedia.teras.pixelmon.battle;

import java.util.HashMap;

public class TerasBattleController {
    public HashMap<Integer, TerasBattle> terasBattles = new HashMap<>();



    public static enum TipoCombate{
        INDIVIDUAL("INDIVIDUAL"),
        DOBLE("DOBLE"),
        TRIPLE("TRIPLE"),
        MULTIPLE("MULTIPLE"),
        TB_INDIVIDUAL("TB_INDIVIDUAL"),
        TB_DOBLE("TB_DOBLE"),
        TB_TRIPLE("TB_TRIPLE"),
        TB_MULTIPLE("TB_MULTIPLE");

        public final String label;

        private TipoCombate(String label) {
            this.label = label;
        }

    }


    public boolean existsTerasBattle(int id){
        return terasBattles.containsKey(id);
    }

    public TerasBattle getTerasBattle(int id){
        return terasBattles.get(id);
    }

    public void addTerasBattle(int id, TerasBattle combate){
        terasBattles.put(id, combate);
        TerasBattleLog.appendStartBattle(combate);
    }

    public void removeTerasBattle(int id){
        terasBattles.remove(id);
    }
}
