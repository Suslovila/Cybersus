package com.suslovila.cybersus.client.gui;

import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.common.item.ItemPortableMultiAspectContainer;
import com.suslovila.cybersus.utils.SusGraphicHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.client.lib.UtilsFX;

import java.awt.Color;

public class GuiSynthesizerButtonAspect extends GuiButton {
    public static final ResourceLocation textureSelected = new ResourceLocation(Cybersus.MOD_ID, "textures/gui/gravitas.png");
    private GuiItemPortableContainer gui;

    public GuiSynthesizerButtonAspect(GuiItemPortableContainer gui, int id, int x, int y, int xSize, int ySize, String text) {
        super(id, x, y, xSize, ySize, text);
        this.gui = gui;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        GL11.glPushMatrix();
        Aspect aspect = GuiItemPortableContainer.buttonAssociations.get(this.id);
        float alpha = 1.0f;
        UtilsFX.bindTexture(aspect.getImage());
        SusGraphicHelper.bindColor(aspect.getColor(), alpha, 1.0f);
        GL11.glTranslated(
                this.xPosition + this.width / 2.0,
                this.yPosition + this.height / 2.0,
                0.0
        );
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glRotated(-90.0, 0.0, 0.0, 1.0);
        SusGraphicHelper.drawFromCenter(12.0);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        if (aspect == gui.currentAspect) {
            GL11.glPushMatrix();
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glTranslated(0.0, 0.0, -1.0);
            SusGraphicHelper.bindTexture(new ResourceLocation(Cybersus.MOD_ID, "textures/gui/aspect_outer.png"));
            SusGraphicHelper.drawFromCenter(16);
            GL11.glPopMatrix();
        }

        GL11.glRotated(90.0, 0.0, 0.0, 1.0);

        GL11.glPushMatrix();
        ItemPortableMultiAspectContainer portableAspectContainer = (ItemPortableMultiAspectContainer) gui.portableContainer.getItem();
        String aspectAmount = Integer.toString(portableAspectContainer.getStoredAspects(gui.portableContainer).getAmount(aspect));
        GL11.glTranslated((-4.0 * aspectAmount.length()) / 2, 0.0, 1.0);
        GL11.glScaled(0.7, 0.7, 0.7);
        Minecraft.getMinecraft().fontRendererObj.drawString(aspectAmount, 0, 0, Color.white.getRGB());
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();

        GL11.glPopMatrix();
    }
}

