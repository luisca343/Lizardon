package es.allblue.lizardon.items.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.network.play.server.SPacketEntityProperties;
import net.minecraft.network.play.server.SPacketRemoveEntityEffect;
import net.minecraft.world.WorldServer;

import java.util.Collection;


public class PocionParalisis extends PotionBase {
    public static final PocionParalisis INSTANCE = new PocionParalisis();

    public PocionParalisis() {
        super(false, 16777148, "paralisis");
        setIconIndex(0, 0);
        registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, "677206f4-8f2b-11ec-b909-0242ac120002", -0.80f, 1);
        registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_DAMAGE, "67720a5a-8f2b-11ec-b909-0242ac120002", -0.25f, 1);
        registerPotionAttributeModifier(SharedMonsterAttributes.ARMOR, "67720f78-8f2b-11ec-b909-0242ac120002", -0.25f, 1);
        registerPotionAttributeModifier(SharedMonsterAttributes.FLYING_SPEED, "67720f78-8f2b-11ec-b909-0242ac120001", -0.80f, 1);
    }

    public void removeAttributesModifiersFromEntity(EntityLivingBase entityLivingBaseIn, AbstractAttributeMap attributeMapIn, int amplifier) {
        super.removeAttributesModifiersFromEntity(entityLivingBaseIn, attributeMapIn, amplifier);
        if (/*!(entityLivingBaseIn instanceof EntityPlayer) && */entityLivingBaseIn.isServerWorld()) {
            ((WorldServer) entityLivingBaseIn.world).getEntityTracker().sendToTracking(entityLivingBaseIn, new SPacketRemoveEntityEffect(entityLivingBaseIn.getEntityId(), this));
            ((WorldServer) entityLivingBaseIn.world).getEntityTracker().sendToTracking(entityLivingBaseIn, new SPacketEntityProperties(entityLivingBaseIn.getEntityId(), (Collection<IAttributeInstance>) entityLivingBaseIn.getAttributeMap().getAllAttributes()));
        }
    }

    public void applyAttributesModifiersToEntity(EntityLivingBase entityLivingBaseIn, AbstractAttributeMap attributeMapIn, int amplifier) {
        super.applyAttributesModifiersToEntity(entityLivingBaseIn, attributeMapIn, amplifier);
        if (/*!(entityLivingBaseIn instanceof EntityPlayer) && */entityLivingBaseIn.isServerWorld()) {
            ((WorldServer) entityLivingBaseIn.world).getEntityTracker().sendToTracking(entityLivingBaseIn, new SPacketEntityEffect(entityLivingBaseIn.getEntityId(), entityLivingBaseIn.getActivePotionEffect(this)));
            ((WorldServer) entityLivingBaseIn.world).getEntityTracker().sendToTracking(entityLivingBaseIn, new SPacketEntityProperties(entityLivingBaseIn.getEntityId(), (Collection<IAttributeInstance>) entityLivingBaseIn.getAttributeMap().getAllAttributes()));
        }
    }
}
