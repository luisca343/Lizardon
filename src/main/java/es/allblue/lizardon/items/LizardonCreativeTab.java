package es.allblue.lizardon.items;

import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.init.ItemsInit;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class LizardonCreativeTab extends CreativeTabs {

    public LizardonCreativeTab() {
        super(Lizardon.MODID);
    }

    @Override
    @Nonnull
    public ItemStack getTabIconItem() {
        return new ItemStack(ItemsInit.smart_rotom);
    }

}
