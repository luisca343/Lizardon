package es.allblue.lizardon.gui.apps;


import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.gui.Pantalla;
import es.allblue.lizardon.gui.botones.Boton;
import es.allblue.lizardon.gui.botones.BotonTrans;
import es.allblue.lizardon.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

@SideOnly(Side.CLIENT)
public class AppBancoCambioDivisa extends Pantalla {
    int NUM_CONTACTOS_LINEA;
    int MARGIN;
    int ICON_SIZE;
    int money;
    String textoCambio = "";

    GuiTextField decem;

    public AppBancoCambioDivisa(Pantalla pantallaAnteior) {
        setPantallaAnteior(pantallaAnteior);
    }

    @Override
    public void initGui() {
        super.initGui();
        FONDO_MOVIL = new ResourceLocation("lizardon", "textures/movil/fondos/banco.png");
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        PlayerPartyStorage storage = Pixelmon.storageManager.getParty(player.getUniqueID());
        money = storage.getMoney();

        if(SCREEN_WIDTH<=100){
            NUM_CONTACTOS_LINEA = 3;
        }else {
            NUM_CONTACTOS_LINEA = 4;
        }

        ArrayList<ItemStack> monedas = new ArrayList<>();
        int unus = 0, quinque = 0, decem = 0, quindecim = 0;

        for(int i = 0; i< player.inventory.getSizeInventory();i++){
            ItemStack item = player.inventory.getStackInSlot(i);
            if(item.getItem().getRegistryName().toString().equals("sc:unus")){
                monedas.add(item);
                unus += item.getCount();
            }
            if(item.getItem().getRegistryName().toString().equals("sc:quinque")){
                monedas.add(item);
                quinque += item.getCount();
            }
            if(item.getItem().getRegistryName().toString().equals("sc:decem")){
                monedas.add(item);
                decem += item.getCount();
            }
            if(item.getItem().getRegistryName().toString().equals("sc:quindecim")){
                monedas.add(item);
                quindecim += item.getCount();
            }
        }
        int anchoBoton = (int) (SCREEN_WIDTH*.7);
        buttonList.add(new Boton(getPosX(.49, anchoBoton), getPosY(.55,anchoBoton/2.5), 0, 0, anchoBoton, (int) (anchoBoton/2.5),anchoBoton, (int) (anchoBoton/2.5), new AppBancoCambioDivisa(this),new ResourceLocation("lizardon", "textures/movil/botones/vender_monedas.png")));
        textoCambio = "Recibirás "+(unus*100+quinque*500+decem*1000+quindecim*5000)+" ¥";
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        posX = (width - DEVICE_WIDTH) / 2;
        posY = (height - DEVICE_HEIGHT) / 2;
        int ancho = (int) (SCREEN_WIDTH*.4);

        float escala = 1.5f;
        String texto = Util.getNumeroConPuntitos(money)+" ¥";
        int textWidth = (int) (Minecraft.getMinecraft().fontRenderer.getStringWidth(texto)*escala);
        renderText(texto,escala, getPosX(.5, textWidth), getPosY(0.3, 0),284124);


        escala = 1.0f;
        textWidth = (int) (Minecraft.getMinecraft().fontRenderer.getStringWidth(textoCambio)*escala);
        renderText(textoCambio,escala, getPosX(.5, textWidth), getPosY(0.7, 0),284124);




        /*
        String numUnus = "0";
        textWidth = (int) (Minecraft.getMinecraft().fontRenderer.getStringWidth(numUnus));
        fontRenderer.drawString(numUnus, getPosX(.2, textWidth), getPosY(0.5, 0), 284124,false);

        String numQuinque = "1";
        textWidth = (int) (Minecraft.getMinecraft().fontRenderer.getStringWidth(numUnus));
        fontRenderer.drawString(numQuinque, getPosX(.4, textWidth), getPosY(0.5, 0), 284124,false);

        String numDecem = "2";
        textWidth = (int) (Minecraft.getMinecraft().fontRenderer.getStringWidth(numUnus));
        fontRenderer.drawString(numDecem, getPosX(.6, textWidth), getPosY(0.5, 0), 284124,false);

        String numQuindecim = "3";
        textWidth = (int) (Minecraft.getMinecraft().fontRenderer.getStringWidth(numUnus));
        fontRenderer.drawString(numQuindecim, getPosX(.8, textWidth), getPosY(0.5, 0), 284124,false);
*/




        //decem.drawTextBox();

    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        escala = sr.getScaleFactor();
        Minecraft.getMinecraft().player.sendMessage(new TextComponentString("Escala: "+escala));
        Minecraft.getMinecraft().player.sendMessage(new TextComponentString("X: "+mouseX +" Y:"+mouseY));
    }


}
