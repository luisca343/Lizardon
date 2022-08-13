package es.allblue.lizardon.gui.apps;



import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.gui.Pantalla;
import es.allblue.lizardon.gui.botones.Boton;
import es.allblue.lizardon.gui.botones.BotonComunista;
import es.allblue.lizardon.gui.botones.BotonTrans;
import es.allblue.lizardon.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import java.io.IOException;
import java.util.Arrays;

@SideOnly(Side.CLIENT)
public class AppBanco extends Pantalla {
    int NUM_CONTACTOS_LINEA;
    int MARGIN;
    int ICON_SIZE;
    int money;

    final int TRANSACCIONES = 0;
    final int INGRESOS = 1;
    final int RETIROS = 2;

    GuiTextField decem;
    int pantalla = TRANSACCIONES;

    public AppBanco(Pantalla pantallaAnteior) {
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

        MARGIN = (int) (SCREEN_WIDTH*.05);
        ICON_SIZE = (SCREEN_WIDTH-(NUM_CONTACTOS_LINEA +1)*MARGIN)/(NUM_CONTACTOS_LINEA);

        int pestanas = 3;
        for(int i = 0; i < pestanas; i++){
            int ancho = SCREEN_WIDTH / pestanas;
            buttonList.add(new BotonComunista(pantallaX + ancho * i, (int) (pantallaY + SCREEN_HEIGHT * .4), 0, 0, ancho, ancho * 9/16,ancho, ancho * 9/16));
        }

        if(pantalla == TRANSACCIONES){
            buttonList.add(new BotonComunista(pantallaX, (int) (pantallaY + SCREEN_HEIGHT * .5), 0, 0, SCREEN_WIDTH, (int) (SCREEN_HEIGHT*.166),SCREEN_WIDTH, (int) (SCREEN_HEIGHT*.166)));
            buttonList.add(new BotonComunista(pantallaX, (int) (pantallaY + SCREEN_HEIGHT * .666), 0, 0, SCREEN_WIDTH, (int) (SCREEN_HEIGHT*.166),SCREEN_WIDTH, (int) (SCREEN_HEIGHT*.166)));
            buttonList.add(new BotonComunista(pantallaX, (int) (pantallaY + SCREEN_HEIGHT * .832), 0, 0, SCREEN_WIDTH, (int) (SCREEN_HEIGHT*.166),SCREEN_WIDTH, (int) (SCREEN_HEIGHT*.166)));
        }



        /* Inicialización de componentes */
        this.decem = new GuiTextField(0,fontRenderer, getPosX(.5,30), getPosY(.6,10), 30, 10);
        decem.setMaxStringLength(23);
        decem.setText("0");
        this.decem.setFocused(true);

        int anchoBoton = (int) (SCREEN_WIDTH*.9);
        //buttonList.add(new Boton(getPosX(.49, anchoBoton), getPosY(.55,anchoBoton/2.5), 0, 0, anchoBoton, (int) (anchoBoton/2.5),anchoBoton, (int) (anchoBoton/2.5), new AppBancoCambioDivisa(this),new ResourceLocation("lizardon", "textures/movil/botones/cambiodivisa.png")));
        //buttonList.add(new Boton(getPosX(.49, anchoBoton), getPosY(.55,anchoBoton/2.5) + anchoBoton/2 , 0, 0, anchoBoton, (int) (anchoBoton/2.5),anchoBoton, (int) (anchoBoton/2.5), new AppBancoCambioDivisa(this),new ResourceLocation("lizardon", "textures/movil/botones/enviardinero.png")));


        Minecraft.getMinecraft();
        MinecraftServer server = mc.world.getMinecraftServer();
        if(server!=null){
            //Lizardon.getInstance().getLogger().info(server.getPlayerList().getPlayers().toString());
        }else{
            Lizardon.getInstance().getLogger().info("No estás en un server");
        }
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
