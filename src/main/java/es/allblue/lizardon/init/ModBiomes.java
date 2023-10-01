package es.allblue.lizardon.init;

import com.pixelmonmod.pixelmon.world.dimension.ConfiguredCarvers;
import com.pixelmonmod.pixelmon.world.dimension.SurfaceBuilders;
import es.allblue.lizardon.Lizardon;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.structure.StructureFeatures;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilders;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;
import java.util.function.Supplier;

public class ModBiomes {



    public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES, Lizardon.MOD_ID);

    public static final RegistryObject<Biome> ARRECIFE_WINGULL = getPueblo("arrecife_wingull", Biome.RainType.RAIN, new BiomeAmbience.Builder()
                                .fogColor(15658734)
                                .skyColor(7907327)
                                .waterColor(4445678)
                                .waterFogColor(4445678)
                                .build());

    public static final RegistryObject<Biome> PUERTO_WINGULL = getPueblo("puerto_wingull", Biome.RainType.RAIN, new BiomeAmbience.Builder()
            .fogColor(15658734)
            .skyColor(7907327)
            .waterColor(4445678)
            .waterFogColor(4445678)
            .build());

    public static Biome getBiomeByName(String name){
        for (RegistryObject<Biome> biome : BIOMES.getEntries()) {
            if(biome.getId().getPath().equals(name)){
                return biome.get();
            }
        }
        return null;
    }



    //
    public static final RegistryObject<Biome> PUEBLO_TULIPAN = crearPuebloBasico("pueblo_tulipan", Biome.RainType.RAIN);
    public static final RegistryObject<Biome> PUEBLO_SHIROI = crearPuebloBasico("pueblo_shiroi", Biome.RainType.SNOW);
    public static final RegistryObject<Biome> PUEBLO_HAGANE = getPueblo("pueblo_hagane", Biome.RainType.RAIN, new BiomeAmbience.Builder()
            .fogColor(0xcccccc)
            .skyColor(0x6a778f)
            .waterColor(0x525975)
            .waterFogColor(0x1c1c30)
            .grassColorOverride(0x696969)
            .foliageColorOverride(0x3d3d3d)
            .build());
    public static final RegistryObject<Biome> PUEBLO_YUME = getPueblo("pueblo_yume", Biome.RainType.RAIN, new BiomeAmbience.Builder()
            .fogColor(0xeeeeee)
            .skyColor(0x6a778f)
            .waterColor(0x3e4a7a)
            .waterFogColor(0x050533)
            .grassColorOverride(0x4b4d49)
            .foliageColorOverride(0x646e60)
            .build());
    public static final RegistryObject<Biome> PUEBLO_DENTO = crearPuebloBasico("pueblo_dento", Biome.RainType.RAIN);
    public static final RegistryObject<Biome> PUEBLO_IWA = crearPuebloBasico("pueblo_iwa", Biome.RainType.RAIN);
    public static final RegistryObject<Biome> PUEBLO_TSUCHI = crearPuebloBasico("pueblo_tsuchi", Biome.RainType.NONE);
    public static final RegistryObject<Biome> PUEBLO_OASIS = crearPuebloBasico("pueblo_oasis", Biome.RainType.NONE);
    public static final RegistryObject<Biome> PUEBLO_SENSHI = crearPuebloBasico("pueblo_senshi", Biome.RainType.RAIN);
    public static final RegistryObject<Biome> PUEBLO_KINOKO = crearPuebloBasico("pueblo_kinoko", Biome.RainType.RAIN);
    public static final RegistryObject<Biome> PUEBLO_SAKURA = getPueblo("pueblo_sakura", Biome.RainType.RAIN, new BiomeAmbience.Builder()
            .fogColor(15658734)
            .skyColor(7907327)
            .waterColor(0x4fbae0)
            .waterFogColor(0x4fbae0)
            .grassColorOverride(0xd1ffdd)
            .foliageColorOverride(0x97baa0)
            .build());

    public static final RegistryObject<Biome> PUEBLO_TAKAI = crearPuebloBasico("pueblo_takai", Biome.RainType.RAIN);
    public static final RegistryObject<Biome> PUEBLO_DOKU = crearPuebloBasico("pueblo_doku", Biome.RainType.RAIN);
    public static final RegistryObject<Biome> PUEBLO_GAKU = crearPuebloBasico("pueblo_gaku", Biome.RainType.RAIN);
    public static final RegistryObject<Biome> PUEBLO_LAVANDA = getPueblo("pueblo_lavanda", Biome.RainType.RAIN, new BiomeAmbience.Builder()
            .fogColor(15658734)
            .skyColor(0x6a778f)
            .waterColor(0x4fbae0)
            .waterFogColor(0x4fbae0)
            .build());

    public static final RegistryObject<Biome> PUEBLO_DENKI = crearPuebloBasico("pueblo_denki", Biome.RainType.RAIN);
    public static final RegistryObject<Biome> PUEBLO_MIZU = getPueblo("pueblo_mizu", Biome.RainType.RAIN, new BiomeAmbience.Builder()
            .fogColor(15658734)
            .skyColor(7907327)
            .waterColor(0x4fbae0)
            .waterFogColor(0x4fbae0)
            .grassColorOverride(0x2fc28c)
            .foliageColorOverride(0x218f66)
            .build());
    public static final RegistryObject<Biome> PUEBLO_OLIVO = crearPuebloBasico("pueblo_olivo", Biome.RainType.RAIN);

    public static final RegistryObject<Biome> NARUKAMI_AKINA = crearPuebloBasico("narukami_akina", Biome.RainType.RAIN);
    public static final RegistryObject<Biome> FUKITSU_GANSOLIA = crearPuebloBasico("fukitsu_gansolia", Biome.RainType.RAIN);



    public static void generateBiomes() {
        setupBiome(ARRECIFE_WINGULL.get(), BiomeManager.BiomeType.WARM, 0);

        setupBiome(PUEBLO_TULIPAN.get(), BiomeManager.BiomeType.WARM, 0);
        setupBiome(PUEBLO_SHIROI.get(), BiomeManager.BiomeType.WARM, 0);
        setupBiome(PUEBLO_HAGANE.get(), BiomeManager.BiomeType.WARM, 0);
        setupBiome(PUEBLO_YUME.get(), BiomeManager.BiomeType.WARM, 0);
        setupBiome(PUEBLO_DENTO.get(), BiomeManager.BiomeType.WARM, 0);
        setupBiome(PUEBLO_IWA.get(), BiomeManager.BiomeType.WARM, 0);
        setupBiome(PUEBLO_TSUCHI.get(), BiomeManager.BiomeType.WARM, 0);
        setupBiome(PUEBLO_OASIS.get(), BiomeManager.BiomeType.WARM, 0);
        setupBiome(PUEBLO_SENSHI.get(), BiomeManager.BiomeType.WARM, 0);
        setupBiome(PUEBLO_KINOKO.get(), BiomeManager.BiomeType.WARM, 0);
        setupBiome(PUEBLO_SAKURA.get(), BiomeManager.BiomeType.WARM, 0);

        setupBiome(PUEBLO_TAKAI.get(), BiomeManager.BiomeType.WARM, 0);
        setupBiome(PUEBLO_DOKU.get(), BiomeManager.BiomeType.WARM, 0);
        setupBiome(PUEBLO_GAKU.get(), BiomeManager.BiomeType.WARM, 0);
        setupBiome(PUEBLO_LAVANDA.get(), BiomeManager.BiomeType.WARM, 0);
        setupBiome(PUEBLO_DENKI.get(), BiomeManager.BiomeType.WARM, 0);
        setupBiome(PUEBLO_MIZU.get(), BiomeManager.BiomeType.WARM, 0);
        setupBiome(PUEBLO_OLIVO.get(), BiomeManager.BiomeType.WARM, 0);

        setupBiome(NARUKAMI_AKINA.get(), BiomeManager.BiomeType.WARM, 0);
        setupBiome(FUKITSU_GANSOLIA.get(), BiomeManager.BiomeType.WARM, 0);

    }


    public static RegistryObject<Biome> crearPuebloBasico(String nombre, Biome.RainType precipitaciones){
        return BIOMES.register(nombre, () ->
                new Biome.Builder()
                        .biomeCategory(Biome.Category.NONE)
                        .temperature(0.8f)
                        .downfall(.4f)
                        .precipitation(precipitaciones)
                        .specialEffects(new BiomeAmbience.Builder()
                                .fogColor(15658734)
                                .skyColor(7907327)
                                .waterColor(9421531)
                                .waterFogColor(9421531)
                                .build()
                        )
                        .scale(0.01f)
                        .depth(0.01f)
                        .mobSpawnSettings(new MobSpawnInfo.Builder().build())
                        .generationSettings(new BiomeGenerationSettings.Builder().surfaceBuilder(ConfiguredSurfaceBuilders.GRASS).build())
                        .build()
        );
    }


    public static RegistryObject<Biome> getPueblo(String nombre, Biome.RainType precipitaciones, BiomeAmbience specialEffects){
        return BIOMES.register(nombre, () ->
                new Biome.Builder()
                        .biomeCategory(Biome.Category.NONE)
                        .temperature(0.95f)
                        .downfall(.4f)
                        .precipitation(precipitaciones)
                        .specialEffects(specialEffects)
                        .scale(0.01f)
                        .depth(0.01f)
                        .mobSpawnSettings(new MobSpawnInfo.Builder().build())
                        .generationSettings(new BiomeGenerationSettings.Builder().surfaceBuilder(ConfiguredSurfaceBuilders.GRASS).build())
                        .build()
        );
    }

    public static void register(IEventBus modEventBus)
    {
        BIOMES.register(modEventBus);
    }


    private static void setupBiome(Biome biome, BiomeManager.BiomeType biomeType, int weight, BiomeDictionary.Type... types){
        RegistryKey<Biome> key = RegistryKey.create(ForgeRegistries.Keys.BIOMES, Objects.requireNonNull(ForgeRegistries.BIOMES.getKey(biome)));

        BiomeDictionary.addTypes(key, types);
        BiomeManager.addBiome(biomeType, new BiomeManager.BiomeEntry(key, weight));
    }
}
