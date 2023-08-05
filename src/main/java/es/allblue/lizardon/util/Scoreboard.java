package es.allblue.lizardon.util;

import lekavar.lma.drinkbeer.DrinkBeer;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreCriteria;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.controllers.data.DialogCategory;

import java.util.Collection;
import java.util.UUID;

public class Scoreboard {

    public static void init(ServerPlayerEntity player, String objective){
        set(player, objective, 0);
    }

    public static void set(ServerPlayerEntity player, String objective, int value){

        String tag = objective.replace("/","_");
        ServerScoreboard scoreboard = player.server.getScoreboard();
        ScoreObjective objetivo = scoreboard.getObjective(tag);

        if(objetivo == null){
            objetivo = scoreboard.addObjective(tag, ScoreCriteria.DUMMY, new StringTextComponent(tag), ScoreCriteria.RenderType.INTEGER);
        }

        Score score = player.getScoreboard().getOrCreatePlayerScore(player.getName().getString(), objetivo);
        score.setScore(value);
    }
}
