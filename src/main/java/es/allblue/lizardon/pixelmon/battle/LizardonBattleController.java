package es.allblue.lizardon.pixelmon.battle;

import es.allblue.lizardon.objects.pixelmon.Combate;

import java.util.HashMap;

public class LizardonBattleController {
    public HashMap<Integer, Combate> combatesEspeciales = new HashMap<>();

    public boolean existeCombateEspecial(int id){
        return combatesEspeciales.containsKey(id);
    }

    public Combate getCombateEspecial(int id){
        return combatesEspeciales.get(id);
    }

    public void addCombateEspecial(int id, Combate combate){
        combatesEspeciales.put(id, combate);
    }

    public void removeCombateEspecial(int id){
        combatesEspeciales.remove(id);
    }


}
