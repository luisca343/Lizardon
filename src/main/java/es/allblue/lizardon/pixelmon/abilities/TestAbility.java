package es.allblue.lizardon.pixelmon.abilities;

import com.pixelmonmod.pixelmon.api.pokemon.ability.AbstractAbility;
import com.pixelmonmod.pixelmon.api.pokemon.stats.BattleStatsType;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import es.allblue.lizardon.Lizardon;

public class TestAbility extends AbstractAbility {
    public int[] modifyStats(PixelmonWrapper user, int[] stats) {
        // Gets the Attack stat of the Pokemon and returns it doubled
        stats[BattleStatsType.ATTACK.getStatIndex()] = stats[BattleStatsType.ATTACK.getStatIndex()] * 2;
        return stats;
    }


}
