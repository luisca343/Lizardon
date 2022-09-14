package es.allblue.lizardon.init;

import es.allblue.lizardon.items.LizardonItemGroup;
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
            () -> new SmartRotom(new Item.Properties().stacksTo(1).tab(LizardonItemGroup.LIZARDON_GROUP)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}