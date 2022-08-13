package es.allblue.lizardon.items.templates;


import es.allblue.lizardon.Lizardon;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class FoodItemBasic extends ItemFood {
    String tooltip;

    public FoodItemBasic(String name, int amount, float saturation, boolean isWolfFood) {
        super(amount, saturation, isWolfFood);
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(Lizardon.CREATIVE_TAB);
    }

    public FoodItemBasic addLore(String tooltip) {
        this.tooltip = tooltip;
        return this;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if (this.tooltip != null)
            tooltip.add(TextFormatting.DARK_GREEN + this.tooltip);
    }
}
