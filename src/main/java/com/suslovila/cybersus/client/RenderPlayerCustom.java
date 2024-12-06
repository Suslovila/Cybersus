package com.suslovila.cybersus.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;

public class RenderPlayerCustom extends RenderPlayer {
    public boolean shouldRenderName = false;
    @Override
    protected boolean canRenderName(EntityLivingBase targetEntity)
    {
        return Minecraft.isGuiEnabled() && targetEntity != this.renderManager.livingPlayer && !targetEntity.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer) && targetEntity.riddenByEntity == null && shouldRenderName;
    }

    @Override
    public void renderLivingLabel(Entity entity, String name, double x, double y, double z, int maxDistance) {
        super.renderLivingLabel(entity, name, x, y, z, maxDistance);
    }

    @Override
    public void passSpecialRender(EntityLivingBase p_77033_1_, double p_77033_2_, double p_77033_4_, double p_77033_6_)
    {
        if (MinecraftForge.EVENT_BUS.post(new RenderLivingEvent.Specials.Pre(p_77033_1_, this, p_77033_2_, p_77033_4_, p_77033_6_))) return;
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);

        if (this.canRenderName(p_77033_1_))
        {
            float f = 1.6F;
            float f1 = 0.016666668F * f;
            double d3 = p_77033_1_.getDistanceSqToEntity(this.renderManager.livingPlayer);
            float f2 = p_77033_1_.isSneaking() ? NAME_TAG_RANGE_SNEAK : NAME_TAG_RANGE;

            if (d3 < (double)(f2 * f2))
            {
                String s = p_77033_1_.getFormattedCommandSenderName().getFormattedText();

                if (p_77033_1_.isSneaking())
                {
                    FontRenderer fontrenderer = this.getFontRendererFromRenderManager();
                    GL11.glPushMatrix();
                    GL11.glTranslatef((float)p_77033_2_ + 0.0F, (float)p_77033_4_ + p_77033_1_.height + 0.5F, (float)p_77033_6_);
                    GL11.glNormal3f(0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
                    GL11.glScalef(-f1, -f1, f1);
                    GL11.glDisable(GL11.GL_LIGHTING);
                    GL11.glTranslatef(0.0F, 0.25F / f1, 0.0F);
                    GL11.glDepthMask(false);
                    GL11.glEnable(GL11.GL_BLEND);
                    OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                    Tessellator tessellator = Tessellator.instance;
                    GL11.glDisable(GL11.GL_TEXTURE_2D);
                    tessellator.startDrawingQuads();
                    int i = fontrenderer.getStringWidth(s) / 2;
                    tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
                    tessellator.addVertex((double)(-i - 1), -1.0D, 0.0D);
                    tessellator.addVertex((double)(-i - 1), 8.0D, 0.0D);
                    tessellator.addVertex((double)(i + 1), 8.0D, 0.0D);
                    tessellator.addVertex((double)(i + 1), -1.0D, 0.0D);
                    tessellator.draw();
                    GL11.glEnable(GL11.GL_TEXTURE_2D);
                    GL11.glDepthMask(true);
                    fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 0, 553648127);
                    GL11.glEnable(GL11.GL_LIGHTING);
                    GL11.glDisable(GL11.GL_BLEND);
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    GL11.glPopMatrix();
                }
                else
                {
                    this.renderOffsetLivingLabel(p_77033_1_, p_77033_2_, p_77033_4_, p_77033_6_, s, f1, d3);
                }
            }
        }
        MinecraftForge.EVENT_BUS.post(new RenderLivingEvent.Specials.Post(p_77033_1_, this, p_77033_2_, p_77033_4_, p_77033_6_));
    }
}
