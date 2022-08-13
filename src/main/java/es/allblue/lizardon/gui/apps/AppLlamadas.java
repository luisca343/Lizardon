package es.allblue.lizardon.gui.apps;

import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.gui.Pantalla;
import es.allblue.lizardon.gui.botones.BotonComunista;
import es.allblue.lizardon.gui.botones.BotonContacto;
import es.allblue.lizardon.objects.Contacto;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;

@SideOnly(Side.CLIENT)
public class AppLlamadas extends Pantalla {
    int NUM_CONTACTOS_LINEA;
    int MARGIN;
    int ICON_SIZE;


    public AppLlamadas(Pantalla pantallaAnteior) {
        setPantallaAnteior(pantallaAnteior);
    }

    @Override
    public void initGui() {
        if(!Loader.isModLoaded("gvc")){
            Minecraft.getMinecraft().player.sendMessage(new TextComponentString("Para usar esta función debes tener instalado Gliby's Voice Chat Reloaded 1.2.5 o superior"));
            Minecraft.getMinecraft().displayGuiScreen(getPantallaAnteior());
        }
        super.initGui();

        if(SCREEN_WIDTH<=100){
            NUM_CONTACTOS_LINEA = 3;
        }else {
            NUM_CONTACTOS_LINEA = 4;
        }

        MARGIN = (int) (SCREEN_WIDTH*.05);
        ICON_SIZE = (SCREEN_WIDTH-(NUM_CONTACTOS_LINEA +1)*MARGIN)/(NUM_CONTACTOS_LINEA);

        ArrayList<Contacto> contactos = new ArrayList<>();
        contactos.add(new Contacto("Nuevo","A\\u00f1adir", new ResourceLocation(Lizardon.MODID, "textures/nuevo.png")));
        contactos.add(new Contacto("67d9b543-5ac9-41e1-a8a5-20d7689e24a4","Luisca343"));
        contactos.add(new Contacto("ca8c9db0-9375-4a7c-9713-7ce7e591d1ff","Shu_r"));
        contactos.add(new Contacto("e4f3e314-ea7f-4ef6-aa5b-06162c5bf7f6","SrKamina"));


        for(int ii = 0; ii < contactos.size(); ii++){
            int numIconoX = ii% NUM_CONTACTOS_LINEA;
            int numIconoY = ii/ NUM_CONTACTOS_LINEA;
            BotonContacto botonContacto = new BotonContacto(MARGIN+pantallaX+numIconoX*MARGIN+numIconoX*ICON_SIZE,MARGIN+pantallaY+numIconoY*MARGIN+numIconoY*ICON_SIZE,0,0,ICON_SIZE,ICON_SIZE,ICON_SIZE,ICON_SIZE,contactos.get(ii),fontRenderer);
            buttonList.add(botonContacto);


        }


    }
}
