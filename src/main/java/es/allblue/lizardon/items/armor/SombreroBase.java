package es.allblue.lizardon.items.armor;

import es.allblue.lizardon.items.templates.ItemBasic;
import net.minecraft.client.util.ITooltipFlag;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class SombreroBase extends ItemBasic {
    private String nombre;
    public SombreroBase(String nombre) {
        super(nombre);
    }

    @Override
    public boolean isValidArmor(ItemStack stack, EntityEquipmentSlot armorType, Entity entity) {
        if (armorType.equals(EntityEquipmentSlot.HEAD)) {
            return true;
        }
        return false;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(TextFormatting.DARK_GREEN + "Accesorio - Equipable en cabeza.");
    }
}