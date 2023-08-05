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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import static es.allblue.lizardon.util.vector.RayTrace.rayTraceEntities;

public class Porra extends Item {
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public Porra(Properties properties) {

        super(properties);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 1.0D, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", 1.6D, AttributeModifier.Operation.ADDITION));

        this.defaultModifiers = builder.build();
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity) {
        player.level.playSound(player, player.getX(), player.getY(), player.getZ(), LizardonSoundEvents.BONK.get(), player.getSoundSource(), 1.0F, 1.0F);

        return super.onLeftClickEntity(stack, player, entity);
    }
}
