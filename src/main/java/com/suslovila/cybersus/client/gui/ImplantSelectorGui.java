package com.suslovila.cybersus.client.gui;

import com.mojang.realmsclient.util.Pair;
import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.api.implants.ImplantStorage;
import com.suslovila.cybersus.client.CybersusKeyHandler;
import com.suslovila.cybersus.client.ResourceLocationPreLoad;

import java.util.ArrayList;

import com.suslovila.cybersus.extendedData.CybersusPlayerExtendedData;
import com.suslovila.cybersus.utils.SusGraphicHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.GuiIngameForge;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;


// the whole idea ws taken from Vampirism mod by teamlapen

public class ImplantSelectorGui
        extends GuiScreen {
    private static final ResourceLocation backgroundTex = new ResourceLocationPreLoad(Cybersus.MOD_ID, "textures/gui/implant_selector_background.png");
    private static final ResourceLocation centerTex = new ResourceLocationPreLoad(Cybersus.MOD_ID, "textures/gui/implant_selector_center.png");
    protected ArrayList<Pair<Integer, ItemStack>> indexesToImplants;
    private int selectedElement = -1;
    private int elementCount;
    private double radDiff;
    protected final float bgred;
    protected final float bgblue;
    protected final float bggreen;
    protected final float bgalpha;
    protected final String name;

    private final RenderItem itemRender = new RenderItem();

    private EntityPlayer player;

    public ImplantSelectorGui() {
        long backgroundColor = 2298478591L;
        this.allowUserInput = true;
        this.bgred = (float) (backgroundColor >> 16L & 0xFFL) / 255.0F;
        this.bgblue = (float) (backgroundColor >> 8L & 0xFFL) / 255.0F;
        this.bggreen = (float) (backgroundColor & 0xFFL) / 255.0F;
        this.bgalpha = (float) (backgroundColor >> 24L & 0xFFL) / 255.0F;
        this.name = "select_implant";
    }


    public boolean doesGuiPauseGame() {
        return false;
    }

    private void drawBackground(float cX, float cY) {
        float scale = (this.height / 2.0F + 16.0F + 16.0F) / 300.0F;

        SusGraphicHelper.setStandartColors();

        glEnable(GL_BLEND);
        GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        GL11.glPushMatrix();
        GL11.glTranslatef(cX, cY, this.zLevel);
        GL11.glScalef(scale, scale, 1.0F);


        this.mc.getTextureManager().bindTexture(backgroundTex);
        GL11.glColor4f(this.bgred, this.bggreen, this.bgblue, this.bgalpha);
        GL11.glBegin(7);
        GL11.glTexCoord2f(1.0F, 1.0F);
        GL11.glVertex3f(150.0F, 150.0F, this.zLevel);
        GL11.glTexCoord2f(1.0F, 0.0F);
        GL11.glVertex3f(150.0F, -150.0F, this.zLevel);
        GL11.glTexCoord2f(0.0F, 0.0F);
        GL11.glVertex3f(-150.0F, -150.0F, this.zLevel);
        GL11.glTexCoord2f(0.0F, 1.0F);
        GL11.glVertex3f(-150.0F, 150.0F, this.zLevel);
        GL11.glEnd();


        if (this.elementCount > 1) {
            for (int i = 0; i < this.elementCount; i++) {
                double rad = i * this.radDiff + this.radDiff / 2.0D;
                double cos = Math.cos(rad);
                double sin = Math.sin(rad);
                drawLine(cos * 60.0D, sin * 60.0D, cos * 300.0D / 2.0D, sin * 300.0D / 2.0D);
            }
        }
        GL11.glPopMatrix();
    }


    protected void drawLine(double x1, double y1, double x2, double y2) {
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glColor4f(0.0F, 0.0F, 0.0F, 1.0F);
        GL11.glLineWidth(2.0F);
        GL11.glBegin(1);
        GL11.glVertex3d(x1, y1, this.zLevel);
        GL11.glVertex3d(x2, y2, this.zLevel);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }


    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.mc.mcProfiler.startSection(this.name);

        int cX = this.width / 2;
        int cY = this.height / 2;
        double radius = (this.height / 4);

        drawBackground(cX, cY);

        double mouseRad = updateMouse(mouseX, mouseY, cX, cY, radius / 2.0D);
        boolean center = (((mouseX - cX) * (mouseX - cX) + (mouseY - cY) * (mouseY - cY)) < radius / 4.0D * radius / 4.0D);
        if (center) {
            this.selectedElement = -1;
        }
        for (int i = 0; i < this.elementCount; i++) {
            Pair<Integer, ItemStack> indexToImplant = this.indexesToImplants.get(i);


            double rad = this.radDiff * i;
            boolean selected = false;
            if (!center && mouseRad > rad - this.radDiff / 2.0D && mouseRad < rad + this.radDiff / 2.0D) {
                selected = true;
            } else if (!center && rad == 0.0D && mouseRad > 6.283185307179586D - this.radDiff / 2.0D) {
                selected = true;
            }
            int x = (int) (cX + Math.cos(rad) * radius) - 8;
            int y = (int) (cY - Math.sin(rad) * radius) - 8;

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            if (selected) {
                this.selectedElement = i;
                drawSelectedCenter(cX, cY, rad);
            }

            GL11.glPushMatrix();
            float scale = i == selectedElement ? 1.3f : 1.0f;
            GL11.glScalef(scale, scale, scale);

            GL11.glTranslated(x / scale, y / scale, 0f);
            drawStack(Minecraft.getMinecraft(), indexToImplant.second(), (int) (1 / scale), (int) (1 / scale), 0f);
            GL11.glPopMatrix();
        }

        if (this.selectedElement == -1) {
            drawUnselectedCenter(cX, cY);
        } else {
            String name = StatCollector.translateToLocal(this.indexesToImplants.get(this.selectedElement).second().getDisplayName());
            int tx = cX - this.mc.fontRendererObj.getStringWidth(name) / 2;
            int ty = this.height / 7;
            this.mc.fontRendererObj.drawStringWithShadow(name, tx, ty, 16777215);
        }
        this.mc.mcProfiler.endSection();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }


    private void drawSelectedCenter(double cX, double cY, double rad) {
        double deg = Math.toDegrees(-rad);
        float scale = this.height / 4.0F / 100.0F;

        GL11.glPushMatrix();

        GL11.glTranslated(cX, cY, this.zLevel);
        GL11.glScalef(scale, scale, 1.0F);
        GL11.glRotated(deg, 0.0D, 0.0D, 1.0D);

        GL11.glEnable(GL_BLEND);
        GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        this.mc.getTextureManager().bindTexture(centerTex);
        GL11.glBegin(7);
        GL11.glTexCoord2f(0.5F, 1.0F);
        GL11.glVertex3d(50.0D, 50.0D, this.zLevel);
        GL11.glTexCoord2f(0.5F, 0.0F);
        GL11.glVertex3d(50.0D, -50.0D, this.zLevel);
        GL11.glTexCoord2f(0.0F, 0.0F);
        GL11.glVertex3d(-50.0D, -50.0D, this.zLevel);
        GL11.glTexCoord2f(0.0F, 1.0F);
        GL11.glVertex3d(-50.0D, 50.0D, this.zLevel);
        GL11.glEnd();

        GL11.glPopMatrix();
    }


    private void drawUnselectedCenter(double cX, double cY) {
        float scale = this.height / 4.0F / 100.0F;

        GL11.glPushMatrix();

        GL11.glTranslated(cX, cY, this.zLevel);
        GL11.glScalef(scale, scale, 1.0F);

        GL11.glEnable(GL_BLEND);
        GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        this.mc.getTextureManager().bindTexture(centerTex);
        GL11.glBegin(7);
        GL11.glTexCoord2f(1.0F, 1.0F);
        GL11.glVertex3d(50.0D, 50.0D, this.zLevel);
        GL11.glTexCoord2f(1.0F, 0.0F);
        GL11.glVertex3d(50.0D, -50.0D, this.zLevel);
        GL11.glTexCoord2f(0.5F, 0.0F);
        GL11.glVertex3d(-50.0D, -50.0D, this.zLevel);
        GL11.glTexCoord2f(0.5F, 1.0F);
        GL11.glVertex3d(-50.0D, 50.0D, this.zLevel);
        GL11.glEnd();

        GL11.glPopMatrix();
    }


    public void initGui() {
        onGuiInit();
        this.elementCount = this.indexesToImplants.size();
        this.radDiff = 6.283185307179586D / this.elementCount;

        try {
            Mouse.setNativeCursor(new Cursor(1, 1, 0, 0, 1, BufferUtils.createIntBuffer(1), null));
        } catch (LWJGLException e) {
        }
        GuiIngameForge.renderCrosshairs = false;
    }


    protected void onElementSelected(int id) {
        GuiImplants.currentImplantSlotId = id;
    }


    public void onGuiClosed() {
        GuiIngameForge.renderCrosshairs = true;

        try {
            Mouse.setNativeCursor(null);
        } catch (LWJGLException e) {
        }
    }


    protected void onGuiInit() {
        this.player = ((EntityPlayer) this.mc.thePlayer);
        indexesToImplants = new ArrayList<>();
        ImplantStorage implantStorage = CybersusPlayerExtendedData.get(player).implantStorage;
        if (implantStorage == null) return;
        int inventorySize = implantStorage.getSizeInventory();
        for (int i = 0; i < inventorySize; i++) {
            ItemStack implant = implantStorage.getStackInSlot(i);
            if (implant == null) continue;

            indexesToImplants.add(Pair.of(i, implant));
        }

    }


    private void setAbsoluteMouse(double x, double y) {
        x = x * this.mc.displayWidth / this.width;
        y = -(y + 1.0D - this.height) * this.mc.displayHeight / this.height;
        Mouse.setCursorPosition((int) x, (int) y);
    }


    private double updateMouse(int x, int y, int cX, int cY, double r) {
        int dx = x - cX;
        int dy = y - cY;
        double rad = Math.atan2(dy, -dx) + Math.PI;

        if (Math.abs(dx) > Math.abs(Math.cos(rad) * r) + 8.0D || Math.abs(dy) > Math.abs(Math.sin(rad) * r) + 8.0D) {
            setAbsoluteMouse(dx / 1.5D + cX + 4.0D, dy / 1.5D + cY);
        }
        return rad;
    }


    public void updateScreen() {
        super.updateScreen();
        this.mc.thePlayer.movementInput.updatePlayerMoveState();
        if (!isKeyDown(CybersusKeyHandler.implantSelectorGui.getKeyCode())) {
            if (this.selectedElement >= 0) {
                onElementSelected(this.indexesToImplants.get(selectedElement).first());
            }

            this.mc.displayGuiScreen(null);
        }
    }

    public static boolean isKeyDown(int k) {
        if (k >= 0) {
            return Keyboard.isKeyDown(k);
        }
        return Mouse.isButtonDown(k + 100);

    }

    public void drawStack(Minecraft mc, ItemStack item, int x, int y, float zLevel) {
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glPushMatrix();

        GL11.glPushAttrib(GL11.GL_TRANSFORM_BIT);
        GL11.glPushAttrib(GL12.GL_RESCALE_NORMAL);
        GL11.glPushAttrib(GL11.GL_LIGHTING);

        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        if (item != null) {
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glPushAttrib(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            float prevZ = itemRender.zLevel;
            itemRender.zLevel = zLevel;
            itemRender.renderWithColor = true;
            itemRender.renderItemAndEffectIntoGUI(mc.fontRendererObj, mc.renderEngine, item, x, y);
            itemRender.renderItemOverlayIntoGUI(mc.fontRendererObj, mc.renderEngine, item, x, y);
            itemRender.zLevel = prevZ;
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glPopAttrib();
        }

        GL11.glPopAttrib();
        GL11.glPopAttrib();
        GL11.glPopAttrib();

        GL11.glPopMatrix();

        RenderHelper.disableStandardItemLighting();

    }

}
