package es.allblue.lizardon.pixelmon;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTutor;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BaseStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BaseStatsLoader;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import es.allblue.lizardon.Lizardon;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class LizardonStatsLoader extends BaseStatsLoader {

    public static void cargarStats() {
        if (PixelmonConfig.useExternalJSONFilesStats)
            (new File("./pixelmon/stats/")).mkdirs();
        for (EnumSpecies species : EnumSpecies.values()) {
            Lizardon.getLogger().info(species.getPokemonName());
            try {
                if (!PixelmonConfig.useExternalJSONFilesStats) {
                    BaseStats.allBaseStats.put(species, getBaseStatsFromAssets(species));
                } else {
                    BaseStats stat = null;
                    try {
                        stat = getBaseStatsFromExternal(species, true);
                    } catch (Exception e) {
                        //Pixelmon.LOGGER.error("Could not load external base stat file for {}", species.name());
                        //e.printStackTrace();
                    }
                    if (stat == null) {
                        //Pixelmon.LOGGER.warn("Failed to load external base stat file for {}, Loading internal instead", species.name());
                        stat = getBaseStatsFromAssets(species);
                    }

                    BaseStats.allBaseStats.put(species, stat);
                }
            } catch (Exception e) {
                //Pixelmon.LOGGER.error("Could not load base stats for {}, file: {}.json, external: {}", species.getPokemonName(), PixelmonConfig.useExternalJSONFilesStats ? species.name() : species.getNationalPokedexNumber(), Boolean.valueOf(PixelmonConfig.useExternalJSONFilesStats));
                //e.printStackTrace();
            }
        }
        for (BaseStats stat : BaseStats.allBaseStats.values()) {
            List<BaseStats> stats = Lists.newArrayList((BaseStats[]) new BaseStats[]{stat});
            if (stat.forms != null)
                stats.addAll(stat.forms.values());
            stat.initLevelupMoves();
            if (stat.forms != null)
                for (Map.Entry<Integer, BaseStats> entry : stat.forms.entrySet()) {
                    ((BaseStats) entry.getValue()).initLevelupMoves();
                    if (((BaseStats) entry.getValue()).form == 0)
                        ((BaseStats) entry.getValue()).form = ((Integer) entry.getKey()).intValue();
                }
            for (BaseStats bs : stats) {
                for (Attack a : bs.getTutorMoves()) {
                    AttackBase ab = a.getActualMove();
                    if (!NPCTutor.allTutorMoves.contains(ab))
                        NPCTutor.allTutorMoves.add(ab);
                }
                for (Attack a : bs.getTransferMoves()) {
                    AttackBase ab = a.getActualMove();
                    if (!NPCTutor.allTransferMoves.contains(ab))
                        NPCTutor.allTransferMoves.add(ab);
                }
                bs.calculateMinMaxLevels();
            }
        }
    }

    public static BaseStats getBaseStatsFromAssets(EnumSpecies species) throws IOException {
        String path = "/assets/lizardon/stats/" + species.getNationalPokedexNumber() + ".json";
        try (Reader reader = new InputStreamReader(BaseStats.class.getResourceAsStream(path))) {
            BaseStats bs = (BaseStats) GSON.fromJson(reader, BaseStats.class);
            prepare(species, bs);
            return bs;
        } catch (Exception e) {
            //Pixelmon.LOGGER.error("Couldn't load internal stat JSON: " + path);
            throw e;
        }
    }

    private static void prepare(EnumSpecies species, BaseStats bs) {
        bs.nationalPokedexNumber = species.getNationalPokedexInteger();
        bs.preEvolutions = (bs.preEvolutions == null) ? new String[0] : bs.preEvolutions;
        if (bs.forms != null && !bs.forms.isEmpty())
            for (BaseStats form : bs.forms.values())
                form.expand(bs);
        List<EnumSpecies> legacyPreEvolutions = new ArrayList<>();
        List<PokemonSpec> specPreEvolutions = new ArrayList<>();
        for (String preEvolution : bs.preEvolutions) {
            PokemonSpec spec = new PokemonSpec(preEvolution.split(" "));
            if (spec != null) {
                Optional<EnumSpecies> s = EnumSpecies.getFromName(spec.name);
                if (!s.isPresent() || s.get() == null) {
                    //Pixelmon.LOGGER.error("Found Invalid pokemonSpec in preEvolutions for " + species.name() + ".json matching \"" + spec.toString() + "\"");
                } else {
                    legacyPreEvolutions.add(s.get());
                    specPreEvolutions.add(spec);
                }
            }
        }
        bs.specPreEvolutions = new PokemonSpec[(specPreEvolutions == null) ? 0 : specPreEvolutions.size()];
        specPreEvolutions.toArray(bs.specPreEvolutions);
        bs.legacyPreEvolutions = new EnumSpecies[(legacyPreEvolutions == null) ? 0 : legacyPreEvolutions.size()];
        legacyPreEvolutions.toArray(bs.legacyPreEvolutions);
    }
}


