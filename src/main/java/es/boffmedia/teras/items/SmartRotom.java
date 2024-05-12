package es.boffmedia.teras.items;


import com.pixelmonmod.pixelmon.api.pokedex.PlayerPokedex;
import com.pixelmonmod.pixelmon.api.pokedex.PokedexRegistrationStatus;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import com.pixelmonmod.pixelmon.entities.pixelmon.StatueEntity;
import es.boffmedia.teras.Teras;
import es.boffmedia.teras.client.ClientProxy;
import es.boffmedia.teras.net.Messages;
import es.boffmedia.teras.net.serverOld.SMessageUpdateDex;
import es.boffmedia.teras.util.TerasDamageSource;
import es.boffmedia.teras.util.math.vector.RayTrace;
import es.boffmedia.teras.util.music.TerasSoundEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.util.*;

public class SmartRotom extends Item {
    public SmartRotom(Properties properties) {
        super(properties);
    }

    /*
    @Override
    public ActionResultType interactLivingEntity(ItemStack item, PlayerEntity player, LivingEntity entity, Hand hand) {
        if(entity instanceof PixelmonEntity){

            PixelmonEntity pixelmon = (PixelmonEntity) entity;
            int smartRotomID = item.getTag().getInt("PadID");
            ClientProxy.PadData smartRotom = Teras.PROXY.getPadByID(smartRotomID);

            int dex = pixelmon.getSpecies().getDex();
            smartRotom.view.runJS("openDex("+ dex +")", "");
            PlayerPokedex pokedex = new PlayerPokedex(player.getUUID());
            if(pokedex.get(dex) == PokedexRegistrationStatus.UNKNOWN){
                Messages.INSTANCE.sendToServer( new SMessageUpdateDex(dex+""));

            }
            return ActionResultType.FAIL;
        }

        return ActionResultType.SUCCESS;
    }
*/
    public LivingEntity getRayTracedEntities(World world, PlayerEntity player, Hand hand, int range){
        System.out.println("getRayTracedEntities");
        Vector3d startVec = player.getEyePosition(1.0F);
        Vector3d lookVec = player.getViewVector(1.0F).scale(range);
        Vector3d endVec = startVec.add(lookVec);
        AxisAlignedBB boundingBox = player.getBoundingBox().expandTowards(lookVec).inflate(1, 1, 1);
        EntityRayTraceResult entityRayTraceResult = RayTrace.rayTraceEntities(player, startVec, endVec, boundingBox, s -> s instanceof LivingEntity, range * range);
        if(entityRayTraceResult != null){
            LivingEntity entity = (LivingEntity) entityRayTraceResult.getEntity();
            if(!(entity instanceof PixelmonEntity) && !(entity instanceof StatueEntity)) {
                Teras.getLogger().info("Entidad incorrecta");
                return null;
            } else {
                Teras.getLogger().info("Entidad correcta");
                return entity;
            }
        } else {
            Teras.getLogger().info("EntityRayTraceResult: null");
        }

        return null;
    }
    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getItemInHand(hand);

        actualizarPad(stack);

        LivingEntity entity = getRayTracedEntities(world, player, hand, 50);

        if(entity !=null){
            PixelmonEntity pixelmon = (PixelmonEntity) entity;
            int smartRotomID = stack.getTag().getInt("PadID");
            ClientProxy.PadData smartRotom = Teras.PROXY.getPadByID(smartRotomID);

            int dex = pixelmon.getSpecies().getDex();
            String form = pixelmon.getForm().getName();
            String palette = pixelmon.getPalette().getName();

            smartRotom.view.runJS("openDex("+ dex +", '"+ form +"')", "");
            PlayerPokedex pokedex = new PlayerPokedex(player.getUUID());
            if(pokedex.get(dex) == PokedexRegistrationStatus.UNKNOWN){
                Messages.INSTANCE.sendToServer( new SMessageUpdateDex(dex, form, palette));
                return super.use(world, player, hand);
            }
        }


        if(world.isClientSide()){
            Teras.PROXY.openMinePadGui(stack.getTag().getInt("PadID"));
        }

        return super.use(world, player, hand);
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        checkPad(stack);
        World world = context.getLevel();
        assert Teras.PROXY.getPadByID(stack.getTag().getInt("PadID")).view.getURL() != null;

        if(!world.isClientSide()){
            return super.onItemUseFirst(stack, context);
        }


        BlockState bloque = world.getBlockState(context.getClickedPos());
        String url = Teras.PROXY.getPadByID(stack.getTag().getInt("PadID")).view.getURL();
        if(url.toLowerCase().contains("rzap")){
            actualizarPad(stack);
            if(bloque.getBlock() == Blocks.JUKEBOX){
                /*BlockState state = BlockInit.TOCADISCOS.get().defaultBlockState();
                world.setBlock(context.getClickedPos(), state, 2);
                world.playSound(context.getPlayer(), context.getClickedPos(), SoundEvents.ARMOR_EQUIP_LEATHER, SoundCategory.BLOCKS, 1.0F, 1.0F);*/
            }
        }

        return super.onItemUseFirst(stack, context);
    }

    public static void checkPad(ItemStack stack){
        if(!stack.hasTag() || !stack.getTag().contains("PadID")){
            CompoundNBT nbt = new CompoundNBT();
            int id = Teras.PROXY.getNextPadID();
            nbt.putInt("PadID", id);
            stack.setTag(nbt);

            Teras.PROXY.updatePad(id, stack.getTag(), true);
        }
    }

    public static void actualizarPad(ItemStack stack){
        if(!stack.hasTag() || !stack.getTag().contains("PadID")){
            CompoundNBT nbt = new CompoundNBT();
            nbt.putString("PadURL", "http://www.google.es");
            int id = Teras.PROXY.getNextPadID();
            nbt.putInt("PadID", id);
            stack.setTag(nbt);

            Teras.PROXY.updatePad(id, stack.getTag(), true);
        }
    }

}
