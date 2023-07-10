package es.allblue.lizardon.init;

import es.allblue.lizardon.Lizardon;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class FluidInit {
    public static ResourceLocation WATER_STILL = new ResourceLocation("block/water_still");
    public static ResourceLocation WATER_FLOW = new ResourceLocation("block/water_flow");
    public static ResourceLocation WATER_OVERLAY = new ResourceLocation("block/water_overlay");

    public static final DeferredRegister<Fluid> FLUIDS
            = DeferredRegister.create(ForgeRegistries.FLUIDS, Lizardon.MOD_ID);

    public static final RegistryObject<FlowingFluid> AGUAS_TERMALES_SOURCE
            = FLUIDS.register("aguas_termales_fluid", () -> new ForgeFlowingFluid.Source(FluidInit.AGUAS_TERMALES_PROPERTIES));
    public static final RegistryObject<FlowingFluid> AGUAS_TERMALES_FLOWING
            = FLUIDS.register("aguas_termales_flowing", () -> new ForgeFlowingFluid.Flowing(FluidInit.AGUAS_TERMALES_PROPERTIES));

    public static final RegistryObject<FlowingFluidBlock> AGUAS_TERMALES_BLOCK = BlockInit.BLOCKS.register("aguas_termales",
            () -> new FlowingFluidBlock(() -> FluidInit.AGUAS_TERMALES_SOURCE.get(), AbstractBlock.Properties.of(Material.WATER)));

    private static final ForgeFlowingFluid.Properties AGUAS_TERMALES_PROPERTIES = new ForgeFlowingFluid.Properties(
            () -> AGUAS_TERMALES_SOURCE.get(), () -> AGUAS_TERMALES_FLOWING.get(),
            FluidAttributes.builder(WATER_STILL, WATER_FLOW).overlay(WATER_OVERLAY).color(0xFF40d9f7))
            .block(() -> FluidInit.AGUAS_TERMALES_BLOCK.get()).bucket(() -> ItemInit.CUBO_AGUAS_TERMALES.get());




    public static void register(IEventBus eventBus){
        FLUIDS.register(eventBus);
    }
}
