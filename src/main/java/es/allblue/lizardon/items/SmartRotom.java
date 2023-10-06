package es.allblue.lizardon.items;


import com.pixelmonmod.pixelmon.api.pokedex.PlayerPokedex;
import com.pixelmonmod.pixelmon.api.pokedex.PokedexRegistrationStatus;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.client.ClientProxy;
import es.allblue.lizardon.init.BlockInit;
import es.allblue.lizardon.net.Messages;
import es.allblue.lizardon.net.server.SMessageUpdateDex;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraft.util.*;

public class SmartRotom extends Item {
    public SmartRotom(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType interactLivingEntity(ItemStack item, PlayerEntity player, LivingEntity entity, Hand hand) {
        if(entity instanceof PixelmonEntity){
            PixelmonEntity pixelmon = (PixelmonEntity) entity;
            int smartRotomID = item.getTag().getInt("PadID");
            ClientProxy.PadData smartRotom = Lizardon.PROXY.getPadByID(smartRotomID);

            int idPokemon = pixelmon.getSpecies().getDex();
            smartRotom.view.runJS("abrirDex("+ idPokemon +")", "");
            PlayerPokedex pokedex = new PlayerPokedex(player.getUUID());
            if(pokedex.get(idPokemon) == PokedexRegistrationStatus.UNKNOWN){
                Messages.INSTANCE.sendToServer( new SMessageUpdateDex(idPokemon+""));

            }
            return ActionResultType.FAIL;
        }

        return ActionResultType.SUCCESS;
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        World world = context.getLevel();
        assert Lizardon.PROXY.getPadByID(stack.getTag().getInt("PadID")).view.getURL() != null;

        if(!world.isClientSide()){
            return super.onItemUseFirst(stack, context);
        }
        BlockState bloque = world.getBlockState(context.getClickedPos());
        String url = Lizardon.PROXY.getPadByID(stack.getTag().getInt("PadID")).view.getURL();
        if(url.toLowerCase().contains("rzap")){
            actualizarPad(stack);
            if(bloque.getBlock() == Blocks.JUKEBOX){
                BlockState state = BlockInit.TOCADISCOS.get().defaultBlockState();
                world.setBlock(context.getClickedPos(), state, 2);
                world.playSound(context.getPlayer(), context.getClickedPos(), SoundEvents.ARMOR_EQUIP_LEATHER, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
        }

        return super.onItemUseFirst(stack, context);
    }

    public static void actualizarPad(ItemStack stack){
        if(!stack.hasTag() || !stack.getTag().contains("PadID")){
            CompoundNBT nbt = new CompoundNBT();
            nbt.putString("PadURL", "http://www.google.es");
            int id = Lizardon.PROXY.getNextPadID();
            nbt.putInt("PadID", id);
            stack.setTag(nbt);

            Lizardon.PROXY.updatePad(id, stack.getTag(), true);
        }
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);

        actualizarPad(stack);

        if(world.isClientSide()){
            Lizardon.PROXY.openMinePadGui(stack.getTag().getInt("PadID"));
        }
        return super.use(world, player, hand);
    }
}
