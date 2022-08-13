package es.allblue.lizardon.items.rol;

import es.allblue.lizardon.Lizardon;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.PotionEffect;

public class Porra extends ItemSword {
    public Porra(String nombre) {
        super(ToolMaterial.STONE);
        setUnlocalizedName(nombre);
        setRegistryName(nombre);
        setCreativeTab(Lizardon.CREATIVE_TAB);
    }

    @Override
    public float getAttackDamage() {
        return 4;
    }


    @Override
    public boolean isFull3D() {
        return true;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        target.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 40, 2));
        return super.hitEntity(stack, target, attacker);
    }
}