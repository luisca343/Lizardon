package es.allblue.lizardon.gui.mineria;

import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.objects.mineria.GuiItemMina;
import es.allblue.lizardon.objects.mineria.RecompensaMina;
import es.allblue.lizardon.util.NoiseMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import es.allblue.lizardon.sound.SoundRegistrator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@SideOnly(Side.CLIENT)
public class Mineria extends GuiScreen {

    int damage = 0;

    private ResourceLocation barraProgreso = new ResourceLocation(Lizardon.MODID, "textures/subsuelo/barra" + damage + ".png");
    private ResourceLocation barraHerramientas = new ResourceLocation(Lizardon.MODID, "textures/subsuelo/barraherramientas.png");
    private final ResourceLocation fondo = new ResourceLocation(Lizardon.MODID, "textures/subsuelo/fondo.png");

    private CuadranteMineria[][] tablero;
    private Map<String, ResourceLocation> botones = new HashMap<>();

    public final int MARTILLO = 0;
    public final int PICO = 1;
    private int herrramienta = PICO;

    private Boton botonRojo;
    private Boton botonAzul;

    public int getHerrramienta() {
        return herrramienta;
    }

    public Map<String, ResourceLocation> getBotones() {
        return botones;
    }

    public ArrayList<GuiItemMina> recompensas;
    SoundEvent musicaSubsuelo = SoundRegistrator.MUSICA_SUBSUELO;

    int cuadrantesX = 13;
    int cuadrantesY = 10;

    public void cambiarHerramienta() {
        botonAzul.cambiarEstado();
        botonRojo.cambiarEstado();
        if (herrramienta == MARTILLO) {
            herrramienta = PICO;
        } else {
            herrramienta = MARTILLO;
        }
    }

    public void updateDamage() {
        if (herrramienta == PICO) {
            damage++;
        } else {
            damage += 2;
        }

        if (damage >= 50) {
            damage = 49;
            for(GuiItemMina recompensa: recompensas){
                WorldClient world = Minecraft.getMinecraft().world;
                EntityPlayerSP player = Minecraft.getMinecraft().player;
                player.sendMessage(new TextComponentString("TESTESTES"));
                if(recompensa.isMinado()){
                    //ItemStack itemRecompensa = new ItemStack(recompensa.getItem(),1);
                    ItemStack itemRecompensa = new ItemStack(recompensa.getItem(),1);

                    //EntityItem dropItem = new EntityItem(world, player.posX, player.posY, player.posZ, new ItemStack(recompensa.getItem(),1));
                    player.sendMessage(new TextComponentString(TextFormatting.DARK_GREEN+"Has minado "+itemRecompensa.getDisplayName()));
                    player.addItemStackToInventory(itemRecompensa);
                    //world.spawnEntity(dropItem);

                }else{
                }
            }
            Minecraft.getMinecraft().displayGuiScreen(null);
        }
        barraProgreso = new ResourceLocation(Lizardon.MODID, "textures/subsuelo/barra" + damage + ".png");
    }

    public void crearObjetos(){
        recompensas = new ArrayList<>();
        int errores = 0;
        Random rand = new Random();
        int cantidad = 5;

        ArrayList<RecompensaMina> minerales = new ArrayList<>();
        for(int i = 0; i < cantidad; i++){
            minerales.add(RecompensasMina.getRecompensa());
        }

        for(int i = 0; i < cantidad;){
            RecompensaMina recompensaMina = minerales.get(i);
            int posX = rand.nextInt(tablero.length-recompensaMina.getAncho()+1);
            int posY = rand.nextInt(tablero[0].length-recompensaMina.getAlto()+1);
            boolean overlaps = false;
            for (GuiItemMina recompensa: recompensas) {
                if(!overlaps){
                    overlaps = recompensa.overlaps(posX, posY, recompensaMina.getAncho(), recompensaMina.getAlto());
                }
            }

            if(!overlaps){
                ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
                int backgroundHeight = sr.getScaledHeight();
                int backgroundWidth = sr.getScaledWidth();

                int anchoZona1 = backgroundWidth * 7 / 8;
                int altoZona1 = backgroundHeight * 5 / 6;

                int cuadranteX = anchoZona1 / cuadrantesX;
                int cuadranteY = altoZona1 / cuadrantesY;

                int anchoZona = cuadrantesX * cuadranteX;
                int altoZona = cuadrantesY * cuadranteY;

                int margenX = (anchoZona1 - anchoZona) / 2;
                int margenY = (altoZona1 - altoZona) / 2;

                GuiItemMina itemMina = new GuiItemMina(margenX + posX * cuadranteX, margenY + (int) (backgroundHeight / 6.1 + posY * cuadranteY), cuadranteX*recompensaMina.getAncho(), cuadranteY*recompensaMina.getAlto(), posX, posY, recompensaMina);
                recompensas.add(itemMina);
                i++;
            }else{
                if(errores++ >=100){
                    i++;
                }
            }
        }
    }


    @Override
    public void initGui() {
        super.initGui();
        //Minecraft.getMinecraft().player.playSound(musicaSubsuelo,.05f,1f);
        int textureX = 800;
        int textureY = 450;

        float x = (float) width / textureX;
        float y = (float) height / textureY;

        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        int backgroundHeight = sr.getScaledHeight();
        int backgroundWidth = sr.getScaledWidth();

        int anchoZona1 = backgroundWidth * 7 / 8;
        int altoZona1 = backgroundHeight * 5 / 6;

        int cuadranteX = anchoZona1 / cuadrantesX;
        int cuadranteY = altoZona1 / cuadrantesY;

        int anchoZona = cuadrantesX * cuadranteX;
        int altoZona = cuadrantesY * cuadranteY;

        int margenX = (anchoZona1 - anchoZona) / 2;
        int margenY = (altoZona1 - altoZona) / 2;

        tablero = new CuadranteMineria[cuadrantesX][cuadrantesY];

        botones.put("btn_rojo0", new ResourceLocation(Lizardon.MODID, "textures/subsuelo/btn_rojo0.png"));
        botones.put("btn_rojo1", new ResourceLocation(Lizardon.MODID, "textures/subsuelo/btn_rojo1.png"));
        botones.put("btn_azul0", new ResourceLocation(Lizardon.MODID, "textures/subsuelo/btn_azul0.png"));
        botones.put("btn_azul1", new ResourceLocation(Lizardon.MODID, "textures/subsuelo/btn_azul1.png"));
        NoiseMap noise = new NoiseMap(new Random(), 1, cuadrantesX, cuadrantesY);
        noise.initialise();


        float[][] mapa = noise.getGrid();
        for (int i = 0; i < mapa.length; i++) {
            String res = "";
            for (int j = 0; j < mapa[0].length; j++) {
                res += mapa[i][j] + ",";
            }
        }

        int margenBarra = backgroundWidth * 7 / 8;
        int anchoBoton = backgroundWidth / 8;
        int altoBoton = (int) (anchoBoton / 0.82);

        botonRojo = new Boton(margenBarra, altoBoton, 0, 0, anchoBoton, altoBoton, anchoBoton, altoBoton, this, "btn_rojo", 0);
        botonAzul = new Boton(margenBarra, (int) (altoBoton * 2.2), 0, 0, anchoBoton, altoBoton, anchoBoton, altoBoton, this, "btn_azul", 1);

        buttonList.add(botonRojo);
        buttonList.add(botonAzul);

        crearObjetos();

        for (int i = 0; i < cuadrantesX; i++) {
            for (int j = 0; j < cuadrantesY; j++) {
                int estado = 0;
                float valor = mapa[i][j];
                if (valor > 0.5) {
                    estado = 6;
                } else if (valor > -0.2) {
                    estado = 5;
                } else if (valor > -0.4) {
                    estado = 4;
                } else if (valor > -0.7) {
                    estado = 3;
                } else {
                    estado = 2;
                }
                /*
                else {
                    estado = 1;
                }*/

                CuadranteMineria cuadranteMineria = new CuadranteMineria(margenX + i * cuadranteX, margenY + (int) (backgroundHeight / 6.1 + j * cuadranteY), 0, 0, cuadranteX, cuadranteY, cuadranteX, cuadranteY, i, j, this, estado);
                tablero[i][j] = cuadranteMineria;

                buttonList.add(cuadranteMineria);
            }
        }


    }

    public CuadranteMineria[][] getTablero() {
        return tablero;
    }

    public ArrayList<GuiItemMina> getRecompensas() {
        return recompensas;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        int backgroundHeight = sr.getScaledHeight();
        int backgroundWidth = sr.getScaledWidth();

        int anchoBarra = backgroundWidth * 7 / 8;
        int altoBarra = backgroundHeight / 6;

        mc.getTextureManager().bindTexture(fondo);
        drawModalRectWithCustomSizedTexture(0, 0, 0, 0, backgroundWidth, backgroundHeight, backgroundWidth, backgroundHeight);

        mc.getTextureManager().bindTexture(barraProgreso);
        drawModalRectWithCustomSizedTexture(0, 0, 0, 0, anchoBarra, altoBarra, anchoBarra, altoBarra);

        int anchoBarra2 = backgroundWidth * 1 / 8;
        mc.getTextureManager().bindTexture(barraHerramientas);
        drawModalRectWithCustomSizedTexture(anchoBarra, 0, 0, 0, anchoBarra2, backgroundHeight, anchoBarra2, backgroundHeight);

        for(int i = 0; i< recompensas.size(); i++){
            GuiItemMina item = recompensas.get(i);
            mc.getTextureManager().bindTexture(item.getResourceLocation());
            drawModalRectWithCustomSizedTexture(item.getX(),item.getY(),0,0,item.getAnchoX(),item.getAnchoY(),item.getAnchoX(),item.getAnchoY());
            //mc.player.sendChatMessage("Renderizando item en "+item.getX()+","+item.getY()+" ["+item.getAnchoX()+","+item.getAnchoY()+"]");
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void onGuiClosed() {
        Minecraft.getMinecraft().getSoundHandler().stopSounds();
        super.onGuiClosed();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}