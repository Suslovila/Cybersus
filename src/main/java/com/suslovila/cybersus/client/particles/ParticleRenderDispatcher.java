package com.suslovila.cybersus.client.particles;

import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.utils.SusGraphicHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayDeque;

public class ParticleRenderDispatcher {

    // Called from onRenderWorldLast since that was already registered.
    public static void dispatch() {
        Tessellator tessellator = Tessellator.instance;
        GL11.glPushMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glDepthMask(false);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569f);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        // rendering smoke-spiral particles

        GL11.glDepthMask(true);

        // rendering black particles
        dispatchQueuedRenders(
            tessellator,
            FXBrokenSoul.queuedRenders,
            FXBrokenSoul.queuedDepthIgnoringRenders,
            FXBrokenSoul.texture
        );

        SusGraphicHelper.bindColor(Color.black.getRGB(), 1.0f, 1.0f);
        dispatchQueuedRenders(
                tessellator,
                FXShadowDrop.queuedRenders,
                FXShadowDrop.queuedDepthIgnoringRenders,
                FXShadowDrop.FXTexture
        );

        dispatchQueuedRenders(
                tessellator,
                FXGravity.queuedRenders,
                FXGravity.queuedDepthIgnoringRenders,
                FXGravity.FXTexture
        );

        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    public static void dispatchQueuedRenders(
    Tessellator tessellator,
    ArrayDeque<FXBase> queuedRenders,
    ArrayDeque<FXBase> queuedDepthIgnoringRenders,
    ResourceLocation texture
    ) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1f);
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        if (!queuedRenders.isEmpty()) {
            tessellator.startDrawingQuads();
            for (FXBase particle : queuedRenders) {
                particle.renderQueued(tessellator);
            }
            tessellator.draw();
        }
        if (!queuedDepthIgnoringRenders.isEmpty()) {
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            tessellator.startDrawingQuads();
            for (FXBase particle : queuedDepthIgnoringRenders) {
                particle.renderQueued(tessellator);
            }
            tessellator.draw();
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        }
        queuedRenders.clear();
        queuedDepthIgnoringRenders.clear();
    }
}

