package es.allblue.lizardon.items;

import es.allblue.lizardon.init.ItemInit;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class LizardonItemGroup {
    public static final ItemGroup LIZARDON_GROUP = new ItemGroup("lizardonTab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ItemInit.SMARTROTOM.get());
        }
    };
}
