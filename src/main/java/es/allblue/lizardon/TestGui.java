package es.allblue.lizardon;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;

public class TestGui extends Screen
{
    String text = "Hello world!";


    protected TestGui(ITextComponent tesx) {
        super(tesx);
    }


}