package es.allblue.lizardon.items;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.items.templates.ItemBasic;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class PiedraEspiritu extends ItemBasic {
    public PiedraEspiritu(String name) {
        super(name);
        setMaxStackSize(1);


    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ActionResult<ItemStack> result = super.onItemRightClick(worldIn, playerIn, handIn);
        ItemStack stack = result.getResult();

        if (!stack.hasTagCompound() || !stack.getTagCompound().hasKey("almas")) {
            stack.setTagCompound(new NBTTagCompound());
            stack.getTagCompound().setInteger("almas", 0);
        }
        int almas = stack.getTagCompound().getInteger("almas");
        if (almas < 108) {
            if (worldIn.isRemote) {
                playerIn.sendMessage(new TextComponentString(TextFormatting.LIGHT_PURPLE + "Una voz tenebrosa te susurra: " + (108 - almas)));
            }
        } else {
            if (!worldIn.isRemote) {
                playerIn.sendMessage(new TextComponentString(TextFormatting.LIGHT_PURPLE + "La piedra ha liberado su poder"));
                Pokemon pokemon = Pixelmon.pokemonFactory.create(EnumSpecies.Spiritomb);
                pokemon.getOrSpawnPixelmon(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ);
            }
            stack.shrink(1);
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(TextFormatting.DARK_GREEN + "Misteriosa piedra hallada en lo más profundo del subsuelo de Teras");
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("almas")) {
            int almas = stack.getTagCompound().getInteger("almas");
            tooltip.add(TextFormatting.DARK_PURPLE + "La voz tenebrosa resuena: " + (108 - almas) + "...");
        }
    }


}
