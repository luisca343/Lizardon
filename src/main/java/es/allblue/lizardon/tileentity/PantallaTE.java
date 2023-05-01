package es.allblue.lizardon.tileentity;

import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.client.gui.PantallaCine;
import es.allblue.lizardon.init.TileEntityInit;
import es.allblue.lizardon.util.Multiblock;
import es.allblue.lizardon.util.vector.Vector3i;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.montoyo.mcef.api.IBrowser;


public class PantallaTE extends TileEntity {
    public IBrowser browser;
    public Direction facing;

    public int ancho;
    public int alto;
    private boolean loaded = true;
    private AxisAlignedBB renderBB;

    public void setDimensions(int x, int y) {
        ancho = x;
        alto = y;
    }

    public float ytVolume = Float.POSITIVE_INFINITY;

    public void updateAABB(BlockPos pos2) {
        AxisAlignedBB box = new AxisAlignedBB(this.getBlockPos());

        if (box == null) renderBB = new AxisAlignedBB(worldPosition);
        else renderBB = Multiblock.getAABB(getBlockPos(), pos2);
    }

    public class PantallaFake{

    }

    public PantallaTE() {
        super(TileEntityInit.TEST_PANTALLA.get());

    }

    public PantallaTE(Direction facing) {
        super(TileEntityInit.TEST_PANTALLA.get());
        this.facing = facing;
    }



    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return renderBB;
    }

    public PantallaCine getPantallaCine() {
        return new PantallaCine(browser);
    }

    public void updateTrackDistance(double d, float masterVolume) {
        final Lizardon wd = Lizardon.INSTANCE;
        boolean needsComputation = true;
        int intPart = 0; //Need to initialize those because the compiler is stupid
        int fracPart = 0;


                    float dist = (float) Math.sqrt(d);
                    float vol;

                    if (dist <= wd.avDist100)
                        vol = masterVolume * wd.ytVolume;
                    else if (dist >= wd.avDist0)
                        vol = 0.0f;
                    else
                        vol = (float) ((1.0f - (dist - wd.avDist100) / (wd.avDist0 - wd.avDist100)) * masterVolume * wd.ytVolume);

                    if (Math.abs(ytVolume - vol) < 0.5f)
                        return; //Delta is too small

                    ytVolume = vol;
                    intPart = (int) vol; //Manually convert to string, probably faster in that case...
                    fracPart = ((int) (vol * 100.0f)) - intPart * 100;
                    needsComputation = false;

       //browser.runJS(VideoType.getVolumeJSQuery(intPart, fracPart), "");
    }
    public void load() {
        loaded = true;
    }

    public void unload() {
            browser.close();
            browser = null;
            loaded = false;

        Lizardon.PROXY.trackScreen(this, false);
    }

    @Override
    public void onLoad() {
        if (level.isClientSide) {
            Lizardon.PROXY.trackScreen(this, true);
        }
    }


    @Override
    public void onChunkUnloaded() {
        if (level.isClientSide()) {
            Lizardon.PROXY.trackScreen(this, false);

            browser.close();
            browser = null;
        }
    }

    public boolean isLoaded() {
        return loaded;
    }

}
