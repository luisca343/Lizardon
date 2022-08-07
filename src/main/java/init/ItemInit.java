package init;

import es.allblue.lizardon.items.SmartRotom;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemInit {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, "lizardon");

    public static final RegistryObject<Item> SMARTROTOM = ITEMS.register("smartrotom",
            () -> new SmartRotom(new Item.Properties().tab(ItemGroup.TAB_TOOLS)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
