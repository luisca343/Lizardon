/*
 * Copyright (C) 2019 BARBOTIN Nicolas
 */

package es.allblue.lizardon.smartrotom.gui;

import es.allblue.lizardon.Lizardon;
import es.allblue.lizardon.init.ItemsInit;
import es.allblue.lizardon.proxy.ClientProxy;
import es.allblue.lizardon.smartrotom.entity.TileEntityScreen;
import es.allblue.lizardon.smartrotom.gui.controls.Button;
import es.allblue.lizardon.smartrotom.gui.controls.TextField;
import es.allblue.lizardon.smartrotom.gui.loading.FillControl;
import es.allblue.lizardon.smartrotom.utilities.BlockSide;
import es.allblue.lizardon.smartrotom.utilities.Util;
import es.allblue.lizardon.smartrotom.utilities.Vector3i;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

import java.util.Map;

public class GuiSetURL2 extends WDScreen {

    //Screen data
    private TileEntityScreen tileEntity;
    private BlockSide screenSide;
    private Vector3i remoteLocation;

    //Pad data
    private final boolean isPad;

    //Common
    private final String screenURL;

    @FillControl
    private TextField tfURL;

    @FillControl
    private Button btnShutDown;

    @FillControl
    private Button btnCancel;

    @FillControl
    private Button btnOk;

    public GuiSetURL2(TileEntityScreen tes, BlockSide side, String url, Vector3i rl) {
        tileEntity = tes;
        screenSide = side;
        remoteLocation = rl;
        isPad = false;
        screenURL = url;
    }

    public GuiSetURL2(String url) {
        isPad = true;
        screenURL = url;
    }

    @Override
    public void initGui() {
        super.initGui();
        loadFrom(new ResourceLocation("webdisplays", "gui/seturl.json"));
        tfURL.setText(screenURL);
    }

    @Override
    protected void addLoadCustomVariables(Map<String, Double> vars) {
        vars.put("isPad", isPad ? 1.0 : 0.0);
    }

    @GuiSubscribe
    public void onButtonClicked(Button.ClickEvent ev) {
        if(ev.getSource() == btnCancel)
            mc.displayGuiScreen(null);
        else if(ev.getSource() == btnOk)
            validate(tfURL.getText());
        else if(ev.getSource() == btnShutDown) {
            if(isPad) {
                Lizardon.NET_HANDLER.sendToServer(new SMessagePadCtrl(""));
            }
            mc.displayGuiScreen(null);
        }
    }

    @GuiSubscribe
    public void onEnterPressed(TextField.EnterPressedEvent ev) {
        validate(ev.getText());
    }

    private void validate(String url) {
        if(!url.isEmpty()) {
            url = Util.addProtocol(url);
            url = ((ClientProxy) Lizardon.getProxy()).getMCEF().punycode(url);

            if(isPad) {
                //Lizardon.NET_HANDLER.sendToServer(new SMessagePadCtrl(url));
                ItemStack held = mc.player.getHeldItem(EnumHand.MAIN_HAND);

                if(held.getItem() == ItemsInit.smart_rotom && held.getTagCompound() != null && held.getTagCompound().hasKey("PadID")) {
                    ClientProxy.PadData pd = ((ClientProxy) Lizardon.getProxy()).getPadByID(held.getTagCompound().getInteger("PadID"));

                    if(pd != null && pd.view != null)
                        pd.view.loadURL(Lizardon.applyBlacklist(url));
                }
            } else{}
                //Lizardon.NET_HANDLER.sendToServer(SMessageScreenCtrl.setURL(tileEntity, screenSide, url, remoteLocation));
        }

        mc.displayGuiScreen(null);
    }

    @Override
    public boolean isForBlock(BlockPos bp, BlockSide side) {
        return (remoteLocation != null && remoteLocation.equalsBlockPos(bp)) || (bp.equals(tileEntity.getPos()) && side == screenSide);
    }

}
