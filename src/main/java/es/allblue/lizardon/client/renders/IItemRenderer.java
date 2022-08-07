package es.allblue.lizardon.client.renders;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.item.ItemStack;

public interface IItemRenderer {

    void render(MatrixStack stack, ItemStack is, float handSideSign, float swingProgress, float equipProgress, IRenderTypeBuffer buffer, int packedLight);

}
