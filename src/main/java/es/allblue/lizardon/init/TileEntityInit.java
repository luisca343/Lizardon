package es.allblue.lizardon.init;

import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.tileentity.FunkoTE;
import es.allblue.lizardon.tileentity.PantallaTE;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntityInit {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES
            = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Lizardon.MOD_ID);


    public static RegistryObject<TileEntityType<FunkoTE>> FUNKO_TE =
            TILE_ENTITIES.register("funko_tile", () -> TileEntityType.Builder.of(FunkoTE::new, BlockInit.FUNKO.get()).build(null));

    public static RegistryObject<TileEntityType<PantallaTE>> TEST_PANTALLA =
            TILE_ENTITIES.register("pantalla_te", () -> TileEntityType.Builder.of(PantallaTE::new, BlockInit.PANTALLA.get()).build(null));


    public static void register(IEventBus eventBus){
        TILE_ENTITIES.register(eventBus);
    }
}
