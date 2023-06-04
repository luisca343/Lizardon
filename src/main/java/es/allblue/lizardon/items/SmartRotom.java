package es.allblue.lizardon.items;


import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.client.ClientProxy;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class SmartRotom extends Item {
    public SmartRotom(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType interactLivingEntity(ItemStack item, PlayerEntity player, LivingEntity entity, Hand hand) {
        if(entity instanceof PixelmonEntity){
            System.out.println("Interact with pixelmon");
            PixelmonEntity pixelmon = (PixelmonEntity) entity;

            int smartRotomID = item.getTag().getInt("PadID");
            ClientProxy.PadData smartRotom = Lizardon.PROXY.getPadByID(smartRotomID);

            //smartRotom.view.loadURL(Lizardon.config.getHome()+"/smartrotom/dex/datos/"+pixelmon.getSpecies().getDex()+"/base");
            smartRotom.view.runJS("abrirDex("+ pixelmon.getSpecies().getDex() +")", null);


            int num = pixelmon.getSpecies().getDex();


            return ActionResultType.SUCCESS;
        }

        return ActionResultType.FAIL;
    }

    
    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if(!stack.hasTag()){
            stack.setTag(new CompoundNBT());
            stack.getTag().putString("PadURL", "http://www.google.es");
            int id = Lizardon.PROXY.getNextPadID();
            stack.getTag().putInt("PadID", id);

            Lizardon.PROXY.updatePad(id, stack.getTag(), true);
        }

        if(world.isClientSide()){
            Lizardon.PROXY.openMinePadGui(stack.getTag().getInt("PadID"));
        }
        return super.use(world, player, hand);
    }
}
