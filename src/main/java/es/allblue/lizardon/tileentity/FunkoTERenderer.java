package es.allblue.lizardon.tileentity;


import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.blocks.Funko;
import net.minecraft.block.AbstractSkullBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SkullBlock;
import net.minecraft.block.WallSkullBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.GenericHeadModel;
import net.minecraft.client.renderer.entity.model.HumanoidHeadModel;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.model.DragonHeadModel;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.SkullTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.ModelLoader;

import javax.annotation.Nullable;
import java.util.Map;

// TileEntityRendererDispatcher
@OnlyIn(Dist.CLIENT)
public class FunkoTERenderer extends TileEntityRenderer<FunkoTE> {
    public FunkoTERenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    public void render(FunkoTE funkoTE, float p_225616_2_, MatrixStack stack, IRenderTypeBuffer renderTypeBuffer, int p_225616_5_, int p_225616_6_) {
        float f = 0;
        BlockState blockstate = funkoTE.getBlockState();
        boolean flag = blockstate.getBlock() instanceof WallSkullBlock;
        Direction direction = flag ? blockstate.getValue(WallSkullBlock.FACING) : null;
        float f1 = 0;
        // renderSkull(direction, f1, SkullBlock.Types.PLAYER, funkoTE.getOwnerProfile(), f, stack, renderTypeBuffer, p_225616_5_);

        stack.pushPose();

        IVertexBuilder ivertexbuilder = renderTypeBuffer.getBuffer(getRenderType( funkoTE.getOwnerProfile()));

        ResourceLocation modeloRes = new ResourceLocation("lizardon:block/funko");
        IUnbakedModel test = ModelLoader.instance().getModelOrMissing(modeloRes);


        stack.popPose();
    }

    public static void renderCosa(){

    }

    /*
    public static void renderSkull(@Nullable Direction p_228879_0_, float p_228879_1_, SkullBlock.ISkullType p_228879_2_, @Nullable GameProfile p_228879_3_, float p_228879_4_, MatrixStack p_228879_5_, IRenderTypeBuffer p_228879_6_, int p_228879_7_) {
        p_228879_5_.pushPose();
        if (p_228879_0_ == null) {
            p_228879_5_.translate(0.5D, 0.0D, 0.5D);
        } else {
            float f = 0.25F;
            p_228879_5_.translate((double) (0.5F - (float) p_228879_0_.getStepX() * 0.25F), 0.25D, (double) (0.5F - (float) p_228879_0_.getStepZ() * 0.25F));
        }

        p_228879_5_.scale(-1.0F, -1.0F, 1.0F);
        IVertexBuilder ivertexbuilder = p_228879_6_.getBuffer(getRenderType(p_228879_2_, p_228879_3_));
        genericheadmodel.setupAnim(p_228879_4_, p_228879_1_, 0.0F);
        genericheadmodel.renderToBuffer(p_228879_5_, ivertexbuilder, p_228879_7_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        p_228879_5_.popPose();
    }
*/
    private static RenderType getRenderType(@Nullable GameProfile profile) {
        /*p_209263_0_.put(SkullBlock.Types.SKELETON, new ResourceLocation("textures/entity/skeleton/skeleton.png"));
        p_209263_0_.put(SkullBlock.Types.WITHER_SKELETON, new ResourceLocation("textures/entity/skeleton/wither_skeleton.png"));
        p_209263_0_.put(SkullBlock.Types.ZOMBIE, new ResourceLocation("textures/entity/zombie/zombie.png"));
        p_209263_0_.put(SkullBlock.Types.CREEPER, new ResourceLocation("textures/entity/creeper/creeper.png"));
        p_209263_0_.put(SkullBlock.Types.DRAGON, new ResourceLocation("textures/entity/enderdragon/dragon.png"));*/
        ResourceLocation resourcelocation =  DefaultPlayerSkin.getDefaultSkin();
        if ( profile != null) {
            Minecraft minecraft = Minecraft.getInstance();
            Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = minecraft.getSkinManager().getInsecureSkinInformation(profile);
            return map.containsKey(MinecraftProfileTexture.Type.SKIN) ? RenderType.entityTranslucent(minecraft.getSkinManager().registerTexture(map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN)) : RenderType.entityCutoutNoCull(DefaultPlayerSkin.getDefaultSkin(PlayerEntity.createPlayerUUID(profile)));
        } else {
            return RenderType.entityCutoutNoCullZOffset(resourcelocation);
        }
    }
}
