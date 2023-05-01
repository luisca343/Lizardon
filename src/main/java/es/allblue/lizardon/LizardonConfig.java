package es.allblue.lizardon;

import net.minecraftforge.common.ForgeConfigSpec;

public class LizardonConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<String> test;

    static {
        BUILDER.push("General");
        test = BUILDER.comment("Test config").define("Esto es una prueba", "Prueba");

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
