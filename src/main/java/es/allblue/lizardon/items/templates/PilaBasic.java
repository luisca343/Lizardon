package es.allblue.lizardon.items.templates;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PilaBasic extends ItemBasic {
    public PilaBasic(String name) {
        super(name);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {

        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }
}
