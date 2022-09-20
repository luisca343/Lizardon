package es.allblue.lizardon.pixelmon.abilities;

import com.pixelmonmod.pixelmon.api.pokemon.ability.AbstractAbility;
import com.pixelmonmod.pixelmon.api.pokemon.stats.BattleStatsType;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import es.allblue.lizardon.Lizardon;

public class TestAbility extends AbstractAbility {
    public int[] modifyStats(PixelmonWrapper user, int[] stats) {
        stats[BattleStatsType.ATTACK.getStatIndex()] = stats[BattleStatsType.ATTACK.getStatIndex()] * 2;
        Lizardon.getLogger().info("SE HA ACTIVADO LA PINCHE HABILIDAD, HIJO DE REMIL");
        return stats;
    }

    // EffectTypeAdapter.EFFECTS.put("TestAttack", TestAttack.class);
    //EnumHeldItems.other
    //StatusType.None
}
