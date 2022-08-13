package es.allblue.lizardon.items.crops;

import es.allblue.lizardon.init.BlocksInit;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class SemillaNeohuana extends ItemSeeds {
    public SemillaNeohuana(String name) {
        super(BlocksInit.CROP_NEOHUANA, Blocks.FARMLAND);
        this.setRegistryName(name);
        this.setUnlocalizedName(name);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(TextFormatting.RED + "- OBJETO ILEGAL -");
        tooltip.add(TextFormatting.DARK_GREEN + "Semillas de planta psicotrópica descubierta en una isla de la costa Africana");
    }
}
