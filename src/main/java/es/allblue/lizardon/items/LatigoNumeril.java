package es.allblue.lizardon.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.pixelmonmod.pixelmon.entities.pixelmon.PixelmonEntity;
import es.allblue.lizardon.util.LizardonDamageSource;
import es.allblue.lizardon.util.music.LizardonSoundEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;


import java.util.UUID;

import static es.allblue.lizardon.util.vector.RayTrace.rayTraceEntities;

public class LatigoNumeril extends Item {
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public LatigoNumeril(Properties properties) {
        super(properties);

        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 1.0D, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", 1.6D, AttributeModifier.Operation.ADDITION));
        //builder.put(ForgeMod.REACH_DISTANCE.get(), new AttributeModifier(UUID.fromString("533e583e-312f-11ee-be56-0242ac120002"), "Weapon modifier", 5.0D, AttributeModifier.Operation.ADDITION));

        this.defaultModifiers = builder.build();
    }



    @Override
    public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity) {
        player.level.playSound(player, player.getX(), player.getY(), player.getZ(), LizardonSoundEvents.LATIGO_NUMERIL.get(), player.getSoundSource(), 1.0F, 1.0F);

        return super.onLeftClickEntity(stack, player, entity);
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

        if (entityRayTraceResult != null) {
            LivingEntity entity = (LivingEntity) entityRayTraceResult.getEntity();

            // Pull entity towards player
            Vector3d playerPos = player.position().add(0, player.getEyeHeight(), 0);
            Vector3d entityPos = entity.position();
            Vector3d pullVec = playerPos.subtract(entityPos).normalize().scale(1);
            entity.setDeltaMovement(pullVec);



        }

        return super.use(world, player, hand);
    }




}
