package es.boffmedia.teras.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import es.boffmedia.mcef.MCEF;
import es.boffmedia.mcef.MCEFBrowser;
import es.boffmedia.mcef.api.*;
import es.boffmedia.teras.Teras;
import es.boffmedia.teras.client.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PantallaSmartRotom extends Screen implements IDisplayHandler, IJSQueryHandler {
    private static final int BROWSER_DRAW_OFFSET = 0;

    private MCEFBrowser browser;
    private ClientProxy.PadData pad;

    public PantallaSmartRotom(ClientProxy.PadData padData) {
        super(new StringTextComponent("Smart Rotom"));
        this.pad = padData;
    }


    @Override
    protected void init() {
        super.init();
        /*
        if (browser == null) {
            String url = Teras.config.getHome();
            boolean transparent = true;
            browser = MCEF.createBrowser(url, transparent);
            resizeBrowser();
        }*/

        if (browser == null) {
            API api = Teras.getInstance().getAPI();
            if(api == null) return;
            Teras.getLogger().warn("API is not null");
            Teras.getLogger().warn("Pad: " + pad);
            Teras.getLogger().warn("Pad view: " + pad.view);
            browser =  pad.view;
            resizeBrowser();
        }

    }

    private int mouseX(double x) {
        return (int) ((x - BROWSER_DRAW_OFFSET) * minecraft.getWindow().getGuiScale());
    }

    private int mouseY(double y) {
        return (int) ((y - BROWSER_DRAW_OFFSET) * minecraft.getWindow().getGuiScale());
    }

    private int scaleX(double x) {
        return (int) ((x - BROWSER_DRAW_OFFSET * 2) * minecraft.getWindow().getGuiScale());
    }

    private int scaleY(double y) {
        return (int) ((y - BROWSER_DRAW_OFFSET * 2) * minecraft.getWindow().getGuiScale());
    }

    private void resizeBrowser() {
        if (width > 100 && height > 100) {
            browser.resize(scaleX(width), scaleY(height));
        }
    }

    @Override
    public void resize(Minecraft minecraft, int i, int j) {
        super.resize(minecraft, i, j);
        resizeBrowser();
    }

    /*
    @Override
    public void onClose() {
        browser.close();
        super.onClose();
    }*/

    @Override
    public void onClose() {
        //showCursor();
        try {
            if (!pad.view.getURL().contains("liga")) {
                this.pad.view.resize((int) 1280, (int) 720);
            }
        } catch (Exception e) {
            Teras.LOGGER.warn("Error al cerrar la pantalla. Ignorando...");
        } finally {
            assert minecraft != null;
            minecraft.setScreen(null);
        }
    }

    @Override
    public void render(MatrixStack matrixStack, int i, int j, float f) {
        if(browser != null) {
            GlStateManager._disableDepthTest();
            GlStateManager._enableTexture();
            browser.draw(matrixStack, .0d, height, width, 0); //Don't forget to flip Y axis.
            GlStateManager._enableDepthTest();
        }

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        browser.sendMousePress(mouseX(mouseX), mouseY(mouseY), button);
        browser.setFocus(true);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        browser.sendMouseRelease(mouseX(mouseX), mouseY(mouseY), button);
        browser.setFocus(true);
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        browser.sendMouseMove(mouseX(mouseX), mouseY(mouseY));
        super.mouseMoved(mouseX, mouseY);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        browser.sendMouseWheel(mouseX(mouseX), mouseY(mouseY), delta, 0);
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        browser.sendKeyPress(keyCode, scanCode, modifiers);
        browser.setFocus(true);
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        browser.sendKeyRelease(keyCode, scanCode, modifiers);
        browser.setFocus(true);
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (codePoint == (char) 0) return false;
        browser.sendKeyTyped(codePoint, modifiers);
        browser.setFocus(true);
        return super.charTyped(codePoint, modifiers);
    }

    public void onUrlChanged(IBrowser b, String nurl) {
        /*
            if (b == browser && url != null) {
                url.setText(nurl);
            }
        */
    }




    /**/

    @Override
    public void onAddressChange(IBrowser browser, String url) {

    }

    @Override
    public void onTitleChange(IBrowser browser, String title) {

    }

    @Override
    public void onTooltip(IBrowser browser, String text) {

    }

    @Override
    public void onStatusMessage(IBrowser browser, String value) {

    }

    @Override
    public boolean handleQuery(IBrowser b, long queryId, String query, boolean persistent, IJSQueryCallback cb) {
        return false;
    }

    @Override
    public void cancelQuery(IBrowser b, long queryId) {

    }
}
