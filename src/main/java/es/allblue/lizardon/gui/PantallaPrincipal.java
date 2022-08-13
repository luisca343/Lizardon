package es.allblue.lizardon.gui;

import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.gui.apps.AppBanco;
import es.allblue.lizardon.gui.botones.BotonApp;
import es.allblue.lizardon.objects.App;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;


@SideOnly(Side.CLIENT)
public class PantallaPrincipal extends Pantalla {

    private static final ResourceLocation TEST = new ResourceLocation(Lizardon.MODID, "textures/comunismo.jpg");
    int NUM_APPS_LINEA;
    int MARGIN;
    int ICON_SIZE;

    @Override
    public void initGui() {
        super.initGui();
        FONDO_MOVIL = new ResourceLocation("lizardon", "textures/movil/fondos/rotom1.png");

        if (SCREEN_WIDTH <= 100) {
            NUM_APPS_LINEA = 3;
        } else {
            NUM_APPS_LINEA = 4;
        }

        MARGIN = (int) (SCREEN_WIDTH * .05);
        ICON_SIZE = (SCREEN_WIDTH - (NUM_APPS_LINEA + 1) * MARGIN) / (NUM_APPS_LINEA);

        ArrayList<App> apps = new ArrayList<>();
        //apps.add(new App("Telefono",new ResourceLocation(lizardon.MODID, "textures/icons/llamada.png"),new AppLlamadas(this)));
        apps.add(new App("ChatApp!", new ResourceLocation(Lizardon.MODID, "textures/icons/chatit.png"), new VentanaEmergente(this)));
        //apps.add(new App("Rooker",new ResourceLocation(Lizardon.MODID, "textures/icons/twitter.png"),new BrowserScreen(null)));
        apps.add(new App("StarBank", new ResourceLocation(Lizardon.MODID, "textures/icons/starbank.png"), new AppBanco(this)));
        apps.add(new App("Settings", new ResourceLocation(Lizardon.MODID, "textures/icons/pc.png"), null));
        apps.add(new App("Settings", new ResourceLocation(Lizardon.MODID, "textures/icons/settings.png"), new PantallaPrincipal()));

        for (int ii = 0; ii < apps.size(); ii++) {
            int numIconoX = ii % NUM_APPS_LINEA;
            int numIconoY = ii / NUM_APPS_LINEA;
            BotonApp botonApp = new BotonApp(MARGIN + pantallaX + numIconoX * MARGIN + numIconoX * ICON_SIZE, MARGIN + pantallaY + numIconoY * MARGIN + numIconoY * ICON_SIZE, 0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE, apps.get(ii), fontRenderer);
            buttonList.add(botonApp);
        }


    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        int pantallaX = getPantallaX();
        int pantallaY = getPantallaY();

        ScaledResolution scaled = new ScaledResolution(mc);
        int width = scaled.getScaledWidth();
        int height = scaled.getScaledHeight();
    }
}