package es.allblue.lizardon.items;

import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import es.allblue.lizardon.util.LizardonDamageSource;
import es.allblue.lizardon.util.music.LizardonSoundEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;


import static es.allblue.lizardon.util.vector.RayTrace.rayTraceEntities;

public class LatigoNumeril extends Item {

    public LatigoNumeril(Properties properties) {
        super(properties);
    }



    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        world.playSound(player, player.getX(), player.getY(), player.getZ(), LizardonSoundEvents.LATIGO_NUMERIL.get(), player.getSoundSource(), 1.0F, 1.0F);

        int range = 5;
        Vector3d startVec = player.getEyePosition(1.0F);
        Vector3d lookVec = player.getViewVector(1.0F).scale(range);
        Vector3d endVec = startVec.add(lookVec);
        AxisAlignedBB boundingBox = player.getBoundingBox().expandTowards(lookVec).inflate(1, 1, 1);
        EntityRayTraceResult entityRayTraceResult = rayTraceEntities(player, startVec, endVec, boundingBox, s -> s instanceof LivingEntity, range * range);

        if (entityRayTraceResult != null && !(entityRayTraceResult.getEntity() instanceof PixelmonEntity)) {
            LivingEntity entity = (LivingEntity) entityRayTraceResult.getEntity();
            entity.hurt(LizardonDamageSource.LATIGO_NUMERIL, 1.0F);
            entity.animateHurt();
        }

        return super.use(world, player, hand);
    }




}
