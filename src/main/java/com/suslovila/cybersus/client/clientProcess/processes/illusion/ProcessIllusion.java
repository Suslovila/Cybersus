package com.suslovila.cybersus.client.clientProcess.processes.illusion;

import com.suslovila.cybersus.api.process.ISerializableProcess;
import com.suslovila.cybersus.api.process.WorldProcess;
import com.suslovila.cybersus.client.RenderPlayerCustom;
import com.suslovila.cybersus.utils.SusGraphicHelper;
import com.suslovila.cybersus.utils.SusVec3;
import cpw.mods.fml.common.gameevent.TickEvent;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import thaumcraft.api.aspects.Aspect;

import java.nio.FloatBuffer;

import static net.minecraft.client.renderer.entity.RendererLivingEntity.NAME_TAG_RANGE;
import static net.minecraft.client.renderer.entity.RendererLivingEntity.NAME_TAG_RANGE_SNEAK;
import static org.lwjgl.opengl.GL11.*;

public class ProcessIllusion extends WorldProcess implements ISerializableProcess {

    int duration;
    int timeLeft;
    int illusionAmount = 7;
    int victimId;
    int hackerId;

    public static RenderPlayerCustom renderPlayerCustom = new RenderPlayerCustom();

    static {
        renderPlayerCustom.setRenderManager(RenderManager.instance);
    }

    public ProcessIllusion(EntityPlayer hacker, EntityPlayer victim, int duration) {
        this.duration = duration;
        this.hackerId = hacker.getEntityId();
        this.victimId = victim.getEntityId();
        this.timeLeft = duration;
    }

    public ProcessIllusion() {
    }

    @Override
    public boolean isExpired(TickEvent.WorldTickEvent event) {
        return timeLeft <= 0;
    }

    @Override
    public boolean isExpired(TickEvent.ClientTickEvent event) {
        return timeLeft <= 0;
    }

    @Override
    public void tickServer(TickEvent.WorldTickEvent event) {
        timeLeft -= 1;
        super.tickServer(event);

    }

    @Override
    public void tickClient(TickEvent.ClientTickEvent event) {
        if (!Minecraft.getMinecraft().isGamePaused()) {
            timeLeft -= 1;
        }
        super.tickClient(event);


    }

    @Override
    public void render(RenderWorldLastEvent event) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.7f);
        EntityPlayer victim = Minecraft.getMinecraft().thePlayer;
        if (victim == null) return;
        if (victim.getEntityId() == this.victimId && victim.worldObj.getEntityByID(this.hackerId) instanceof EntityPlayer) {
            glPushAttrib(GL_COLOR_MATERIAL);
//            glEnable(GL_COLOR_MATERIAL);

            glEnable(GL_LIGHTING);
            Minecraft.getMinecraft().entityRenderer.enableLightmap(event.partialTicks);
            RenderHelper.enableStandardItemLighting();


            EntityPlayer hacker = (EntityPlayer) victim.worldObj.getEntityByID(this.hackerId);

            int brightness = hacker.getBrightnessForRender(event.partialTicks);

            if (hacker.isBurning())
            {
                brightness = 15728880;
            }

            int j = brightness % 65536;
            int k = brightness / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j / 1.0F, (float)k / 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);


            //        SusVec3 lookVector = new SusVec3(x, y, z).subtract(SusGraphicHelper.getRenderPos(player, event.partialTicks).subtract(0.0, player.eyeHeight, 0.0));
            // SusGraphicHelper.translateFromPlayerTo(hacker.getPosition(), event.partialTicks);
            SusVec3 vecFromPlayerToHacker = SusGraphicHelper.getRenderPos(hacker, event.partialTicks).subtract(SusGraphicHelper.getRenderPos(victim, event.partialTicks));
            SusVec3 XZProjection = new SusVec3(vecFromPlayerToHacker.x, 0.0, vecFromPlayerToHacker.z);
            SusVec3 zVec3 = new SusVec3(0, 0, 1.0);
            int xSign = vecFromPlayerToHacker.x > 0 ? 1 : -1;
            double angle = (SusVec3.angleBetweenVec3(zVec3, XZProjection) * 180.0 / Math.PI) * xSign;

//            GL11.glEnable(GL_LIGHTING);
            SusGraphicHelper.pushLight();
//            Minecraft.getMinecraft().entityRenderer.enableLightmap(0);
            int light = hacker.worldObj.getLightBrightnessForSkyBlocks((int)hacker.posX, (int)hacker.posY, (int)hacker.posZ, 0);
            int jVal = light % 65536;
            int kVal = light / 65536;
//            Tessellator.instance.setBrightness(0);
//            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.defaultTexUnit, 0.0f, 0.0f);
//            GL11.glDisable(GL_BLEND);
//            RenderHelper.enableStandardItemLighting();
//            RenderHelper.enableGUIStandardItemLighting();
            GL11.glPushMatrix();
//            System.out.println("the angle is: " + angle);
            glRotated(angle, 0.0, 1.0, 0.0);


            glTranslatef(0.0f, (float) (vecFromPlayerToHacker.y), 0.0f);
            double perCheckAngleDelta = 360.0 / (illusionAmount + 1);
            for (int i = 0; i < illusionAmount; i++) {
                glPushMatrix();
                glRotated(perCheckAngleDelta * (i + 1), 0.0, 1.0, 0.0);
                // idk why, but for correct work we need to subtract 1.3 :/
                glTranslated(0.0, 0.0, XZProjection.length());
//                System.out.println(hacker.rotationYaw);
                glRotated(-angle, 0.0, 1.0, 0.0);
//                glRotated(hacker.rotationYaw + (hacker.rotationYaw - hacker.prevRotationYaw) * event.partialTicks, 0.0, 1.0, 0.0);
//                glRotated(90.0 * -xSign, 0.0, 1.0, 0.0);
//                glRotated(90.0, 0.0, 1.0, 0.0);
//                hacker.riddenByEntity = new EntityItem();
                renderPlayerCustom.shouldRenderName = false;
                drawEntityOrthogonalToZAxis(hacker, false, event);

//                glRotated(-angle, 0.0, 1.0, 0.0);
//                SusVec3 hackerPos = SusGraphicHelper.getRenderPos(hacker, event.partialTicks);
//                renderPlayerCustom.shouldRenderName = true;
//                renderLabel();?
//                SusVec3
//                renderPlayerCustom.renderLivingLabel(hacker, hacker.getFormattedCommandSenderName().getFormattedText(), hackerPos.x, hackerPos.y, hackerPos.z, 64);
                // glDisable(GL_CULL_FACE);
                // UtilsFX.bindTexture(SusGraphicHelper.whiteBlank);
                // SusGraphicHelper.cubeModel.renderAll();
                glPopMatrix();
            }

            glPopMatrix();

            renderPlayerCustom.shouldRenderName = true;
            for (int i = 0; i < illusionAmount; i++) {
                glPushMatrix();
                SusVec3 xAxis = new SusVec3(1.0, 0.0, 0.0);
                SusVec3 zAxis = new SusVec3(0.0, 0.0, 1.0);
                double resultAngle = -(angle + perCheckAngleDelta * (i - 1)) / 180.0 * Math.PI;
                SusVec3 resultOffset = zAxis.scale(Math.sin(resultAngle)).add(xAxis.scale(Math.cos(resultAngle))).normalize().scale(XZProjection.length()).add(0.0, vecFromPlayerToHacker.y, 0.0);

//                SusVec3 rotatedVector = XZProjection.yRot((float) (perCheckAngleDelta * (i + 1)));
                renderLabel(hacker, resultOffset.x, resultOffset.y, resultOffset.z);
                glPopMatrix();
            }

            SusGraphicHelper.popLight();

            RenderHelper.disableStandardItemLighting();
            Minecraft.getMinecraft().entityRenderer.disableLightmap(event.partialTicks);
            glDisable(GL_LIGHTING);
            glPopAttrib();

        }

    }


    @Override
    public String getTypeId() {
        return "illusion";
    }

    public void drawEntityOrthogonalToZAxis(
            EntityLivingBase entity,
            boolean renderLabel,
            RenderWorldLastEvent event
    ) {


        glPushMatrix();
//        RenderManager.instance.renderEntitySimple(Minecraft.getMinecraft().thePlayer, event.partialTicks);
        WrappedRenderManager.doRenderEntity(renderPlayerCustom, entity, 0.0, 0.0, 0.0, 0.0f, 1.0f, false);
        glPopMatrix();

//        RenderManager.instance.renderEntityWithPosYaw(entity, 0.0, 0.0, 0.0, 0.0f, 1.0f);

    }

    public void renderLabel(EntityLivingBase entity, double xOffset, double yOffset, double zOffset) {
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);

        if (renderPlayerCustom.canRenderName(entity))
        {
            float f = 1.6F;
            float f1 = 0.016666668F * f;
            double d3 = entity.getDistanceSqToEntity(RenderManager.instance.livingPlayer);
            float f2 = entity.isSneaking() ? NAME_TAG_RANGE_SNEAK : NAME_TAG_RANGE;

            if (d3 < (double)(f2 * f2))
            {
                String s = entity.getFormattedCommandSenderName().getFormattedText();

                if (entity.isSneaking())
                {
                    FontRenderer fontrenderer = renderPlayerCustom.getFontRendererFromRenderManager();
                    GL11.glPushMatrix();
                    GL11.glTranslatef((float)xOffset + 0.0F, (float)yOffset + entity.height + 0.5F, (float)zOffset);
                    GL11.glNormal3f(0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(-RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(RenderManager.instance.playerViewX, 1.0F, 0.0F, 0.0F);
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
                    renderPlayerCustom.renderOffsetLivingLabel(entity, xOffset, yOffset, zOffset, s, f1, d3);
                }
            }
        }
    }

    @Override
    public void writeTo(ByteBuf byteBuf) {

        byteBuf.writeInt(hackerId);
        byteBuf.writeInt(victimId);
        byteBuf.writeInt(duration);
        byteBuf.writeInt(illusionAmount);
        byteBuf.writeInt(timeLeft);
    }

    @Override
    public void readFrom(ByteBuf byteBuf) {
        hackerId = byteBuf.readInt();
        victimId = byteBuf.readInt();
        duration = byteBuf.readInt();
        illusionAmount = byteBuf.readInt();
        timeLeft = byteBuf.readInt();

    }
}
