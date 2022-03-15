package es.allblue.lizardon.items;

import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.gui.mineria.Mineria;
import es.allblue.lizardon.items.templates.ItemBasic;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class KitMineria extends ItemBasic {
    public KitMineria(String name) {
        super(name);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        float cooldown = player.getCooldownTracker().getCooldown(this, 0);
        if (cooldown == 0) {
            if (world.isRemote) {
                Lizardon.getProxy().abrirVentanaTest("http://lizardon.es/mineria");
            }
        }

        return super.onItemRightClick(world, player, hand);
    }


    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(TextFormatting.DARK_PURPLE + "Kit de minería desarrollado por Lizardon S.L.");
        tooltip.add(TextFormatting.DARK_PURPLE + "Te permite excavar minerales en rocas.");
    }


}
