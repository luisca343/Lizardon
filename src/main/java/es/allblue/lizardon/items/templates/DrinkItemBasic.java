package es.allblue.lizardon.items.templates;


import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.misc.CustomDamageSources;
import es.allblue.lizardon.objects.EfectoPocion;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class DrinkItemBasic extends ItemFood {
    String tooltip;
    ArrayList<EfectoPocion> effects;
    String nombre;

    public DrinkItemBasic(String name) {
        super(0, 0, false);
        setUnlocalizedName(name);
        setRegistryName(name);
        setAlwaysEdible();
        setCreativeTab(Lizardon.CREATIVE_TAB);
        effects = new ArrayList<>();

        this.nombre = name;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.DRINK;
    }


    public DrinkItemBasic addLore(String tooltip) {
        this.tooltip = tooltip;
        return this;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if (this.tooltip != null)
            tooltip.add(TextFormatting.DARK_GREEN + this.tooltip);
    }


    public DrinkItemBasic addPotionEffect(PotionEffect effect, float probability) {
        effects.add(new EfectoPocion(effect, probability));
        return this;
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        for (EfectoPocion efecto : effects) {
            if (new Random().nextFloat() <= efecto.getChance())
                entityLiving.addPotionEffect(efecto.getEffect());
        }

        if (nombre.equals("leyiax")) {
            entityLiving.attackEntityFrom(CustomDamageSources.LEYIAX, 999);
        }


        if (nombre.equals("ay_mi_madre")) {
            int num = new Random().nextInt() * (100 - 1) + 1;
            if (num == 1)
                entityLiving.attackEntityFrom(CustomDamageSources.AY_MI_MADRE, 999);
        }

        ItemStack stackFinal = stack;
        stackFinal.setCount(stack.getCount() - 1);
        return stackFinal;
    }


}
