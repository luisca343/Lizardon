package es.allblue.lizardon.items.potion;

import es.allblue.lizardon.Lizardon;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class EfectoTaser extends Potion {
    public static final EfectoTaser INSTANCE = new EfectoTaser();

    public EfectoTaser() {
        super(true, 13791173);
        String name = "taser";
        setIconIndex(0, 0);
        setRegistryName(new ResourceLocation(Lizardon.MODID+":"+name));
        setPotionName("effect."+name);
    }


    @SideOnly(Side.CLIENT)
    @Override
    public boolean hasStatusIcon() {
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(Lizardon.MODID,"textures/gui/potion_effects.png"));
        return super.hasStatusIcon();
    }

    @Override
    public boolean isInstant() {
        return false;
    }

    public void aplicar(){
        registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED,"677206f4-8f2b-11ec-b909-0242ac120002",-0.80f,1);
        registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_DAMAGE,"67720a5a-8f2b-11ec-b909-0242ac120002",-0.25f,1);
        registerPotionAttributeModifier(SharedMonsterAttributes.ARMOR,"67720f78-8f2b-11ec-b909-0242ac120002",-0.25f,1);
    }
}
