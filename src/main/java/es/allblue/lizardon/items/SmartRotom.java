package es.allblue.lizardon.items;

import com.mojang.realmsclient.gui.ChatFormatting;
import es.allblue.lizardon.items.templates.ItemBasic;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.montoyo.wd.WebDisplays;
import net.montoyo.wd.core.CraftComponent;
import net.montoyo.wd.item.WDItem;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class SmartRotom extends ItemBasic {
    public SmartRotom(String name) {
        super(name);
        setMaxStackSize(1);
        setFull3D();
        setMaxDamage(0);
    }


    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add("Tablet de última generación desarrollada por Lizardon S.L.");
        tooltip.add("Dispone de todo tipo de funciones, excepto la de hacer llamadas.");


        WDItem.addInformation(tooltip);
    }


    private static String getURL(ItemStack is) {
        if (is.getTagCompound() == null || !is.getTagCompound().hasKey("PadURL"))
            return WebDisplays.INSTANCE.homePage;
        else
            return is.getTagCompound().getString("PadURL");
    }

    @Override
    public boolean onEntityItemUpdate(EntityItem ent) {
        if (ent.onGround && !ent.world.isRemote) {
            NBTTagCompound tag = ent.getItem().getTagCompound();

            if (tag != null && tag.hasKey("ThrowHeight")) {
                //Delete it, it touched the ground
                double height = tag.getDouble("ThrowHeight");
                UUID thrower = null;

                if (tag.hasKey("ThrowerMSB") && tag.hasKey("ThrowerLSB"))
                    thrower = new UUID(tag.getLong("ThrowerMSB"), tag.getLong("ThrowerLSB"));

                if (tag.hasKey("PadID") || tag.hasKey("PadURL")) {
                    tag.removeTag("ThrowerMSB");
                    tag.removeTag("ThrowerLSB");
                    tag.removeTag("ThrowHeight");
                } else //We can delete the whole tag
                    ent.getItem().setTagCompound(null);

                if (thrower != null && height - ent.posY >= 20.0) {
                    ent.world.playSound(null, ent.posX, ent.posY, ent.posZ, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 4.0f, 1.0f);
                    ent.world.spawnEntity(new EntityItem(ent.world, ent.posX, ent.posY, ent.posZ, CraftComponent.EXTENSION_CARD.makeItemStack()));
                    ent.setDead();

                    EntityPlayer ply = ent.world.getPlayerEntityByUUID(thrower);
                    if (ply != null && ply instanceof EntityPlayerMP)
                        WebDisplays.INSTANCE.criterionPadBreak.trigger(((EntityPlayerMP) ply).getAdvancements());
                }
            }
        }

        return false;
    }

    @Override
    @Nonnull
    public String getUnlocalizedName(ItemStack stack) {
        String ret = getUnlocalizedName();
        if (stack.getMetadata() > 0)
            ret += "2";

        return ret;
    }
}
