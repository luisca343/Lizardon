package es.allblue.lizardon.items.templates;


import es.allblue.lizardon.Lizardon;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ItemBasic extends Item {

    List<String> tooltip = new ArrayList<>();


    public ItemBasic(String name) {
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(Lizardon.CREATIVE_TAB);
    }

    public ItemBasic(String name, List<String> tooltip) {
        this.tooltip = tooltip;
    }


    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if (!this.tooltip.isEmpty()) {
            tooltip = this.tooltip;
        }
    }

    public Item setIlegal(String descripción) {
        tooltip.add(TextFormatting.RED + "- OBJETO ILEGAL -");
        tooltip.add(TextFormatting.DARK_GREEN + descripción);
        return this;
    }


    public Item setDescripción(String descripción) {
        tooltip.add(TextFormatting.DARK_GREEN + descripción);
        return this;
    }
}
