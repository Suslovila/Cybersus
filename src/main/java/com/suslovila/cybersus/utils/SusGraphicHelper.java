package com.suslovila.cybersus.utils;

import com.suslovila.cybersus.Cybersus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.client.model.obj.WavefrontObject;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class SusGraphicHelper {
    public static final double v1 = 0.0;
    public static final double v2 = 1.0;
    public static final double u1 = 0.0;
    public static final double u2 = 1.0;

    public static final ResourceLocation whiteBlank = new ResourceLocation(Cybersus.MOD_ID, "textures/whiteBlank.png");
    public static float savedLightY = 0.0f;
    public static float savedLightX = 0.0f;
    public static int tessellatorBrightness = 0;

    static {
    }

    public enum BasicDirection {
        NORTH(new SusVec3(0, 0, -1)),
        SOUTH(new SusVec3(0, 0, 1)),
        EAST(new SusVec3(1, 0, 0)),
        WEST(new SusVec3(-1, 0, 0)),
        UP(new SusVec3(0, 1, 0)),
        DOWN(new SusVec3(0, -1, 0));

        public final SusVec3 vec3;

        BasicDirection(SusVec3 vec3) {
            this.vec3 = vec3;
        }
    }

    public static void drawGuideArrows() {
        Tessellator tessellator = Tessellator.instance;
        glDisable(GL_TEXTURE_2D);
        glLineWidth(2F);

        tessellator.startDrawing(GL_LINES);
        tessellator.setColorRGBA_F(0F, 0F, 1F, 1F);
        tessellator.addVertex(0.0, 0.0, 0.0);
        tessellator.addVertex(0.0, 0.0, 2.0);
        tessellator.draw();

        tessellator.startDrawing(GL_LINES);
        tessellator.setColorRGBA_F(0F, 1F, 0F, 1F);
        tessellator.addVertex(0.0, 0.0, 0.0);
        tessellator.addVertex(0.0, 2.0, 0.0);
        tessellator.draw();

        tessellator.startDrawing(GL_LINES);
        tessellator.setColorRGBA_F(1F, 0F, 0F, 1F);
        tessellator.addVertex(0.0, 0.0, 0.0);
        tessellator.addVertex(2.0, 0.0, 0.0);
        tessellator.draw();

        glLineWidth(1F);
        glEnable(GL_TEXTURE_2D);
    }

    public static void bindColor(Tessellator tessellator, int color, float alpha, float fadeFactor) {
        Color co = new Color(color);
        float r = co.getRed() / 255.0f;
        float g = co.getGreen() / 255.0f;
        float b = co.getBlue() / 255.0f;
        tessellator.setColorRGBA_F(r * fadeFactor, g * fadeFactor, b * fadeFactor, alpha);
    }

    public static void bindColor(int color, float alpha, float fadeFactor) {
        Color co = new Color(color);
        float r = co.getRed() / 255.0f;
        float g = co.getGreen() / 255.0f;
        float b = co.getBlue() / 255.0f;
        glColor4f(r * fadeFactor, g * fadeFactor, b * fadeFactor, alpha);
    }

    public static void translateFromPlayerTo(SusVec3 pos, float partialTicks) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        double destX = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
        double destY = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
        double destZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;
        glTranslated(pos.x - destX, pos.y - destY, pos.z - destZ);
    }


    public static SusVec3 getRenderPos(Entity entity, float partialTicks) {
        double destX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
        double destY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks;
        double destZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;

        return new SusVec3(destX, destY, destZ);
    }

    // draws line from specified graphic cords position to zero of cord system

    public static void pushLight() {
        savedLightX = OpenGlHelper.lastBrightnessX;
        savedLightY = OpenGlHelper.lastBrightnessY;
    }

    public static void popLight() {
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, savedLightX, savedLightY);
    }

    public static void pushBrightness(Tessellator tessellator) {
        tessellatorBrightness = tessellator.brightness;
    }

    public static void popBrightness(Tessellator tessellator) {
        tessellator.brightness = tessellatorBrightness;
    }

    public static float getRenderGlobalTime(float partialTicks) {
        return Minecraft.getMinecraft().renderViewEntity.ticksExisted + partialTicks;
    }

    public static void setMaxBrightness() {
        int j = 15728880;
        int k = j % 65536;
        int l = j / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, k / 1.0f, l / 1.0f);
    }

    public static void setStandartColors() {
        glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public static void setDefaultBlend() {
        glDisable(GL_LIGHTING);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE);
    }

    public static void disableBlend() {
        glEnable(GL_LIGHTING);
        glDisable(GL_BLEND);
    }

    public static void drawStack(RenderItem itemRender, ItemStack item, int x, int y, float zLevel) {
        glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        RenderHelper.enableGUIStandardItemLighting();
        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null) return;
        glPushMatrix();
        glPushAttrib(GL_TRANSFORM_BIT);
        glEnable(GL12.GL_RESCALE_NORMAL);
        if (item != null) {
            glEnable(GL_LIGHTING);
            glEnable(GL_DEPTH_TEST);
            float prevZ = itemRender.zLevel;
            itemRender.zLevel = zLevel;
            itemRender.renderWithColor = false;
            itemRender.renderItemAndEffectIntoGUI(mc.fontRendererObj, mc.renderEngine, item, x, y);
            itemRender.renderItemOverlayIntoGUI(mc.fontRendererObj, mc.renderEngine, item, x, y);
            itemRender.zLevel = prevZ;
            itemRender.renderWithColor = true;
            glDisable(GL_DEPTH_TEST);
            glDisable(GL_LIGHTING);
        }
        glPopAttrib();
        glPopMatrix();
    }


    public static void drawFromCenter(double radius) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(-radius, radius, 0.0, u2, v2);
        tessellator.addVertexWithUV(radius, radius, 0.0, u2, v1);
        tessellator.addVertexWithUV(radius, -radius, 0.0, u1, v1);
        tessellator.addVertexWithUV(-radius, -radius, 0.0, u1, v2);
        tessellator.draw();
    }

    public static void drawFromXYAxis(double radius) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(-radius + radius, radius + radius, 0.0, u2, v2);
        tessellator.addVertexWithUV(radius + radius, radius + radius, 0.0, u2, v1);
        tessellator.addVertexWithUV(radius + radius, -radius + radius, 0.0, u1, v1);
        tessellator.addVertexWithUV(-radius + radius, -radius + radius, 0.0, u1, v2);
        tessellator.draw();
    }

    public void alignZAxisWithVector(SusVec3 vec3) {
        // Implementation here
    }

    public static void renderTexture(ResourceLocation texture, double scaleX, double scaleY, double scaleZ) {
        bindTexture(texture);
        glPushMatrix();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glColor4f(1.0f, 0.0f, 1.0f, 1.0f);
        if (Minecraft.getMinecraft().renderViewEntity instanceof EntityPlayer) {
            Tessellator tessellator = Tessellator.instance;
            double arX = ActiveRenderInfo.rotationX;
            double arZ = ActiveRenderInfo.rotationZ;
            double arYZ = ActiveRenderInfo.rotationYZ;
            double arXY = ActiveRenderInfo.rotationXY;
            double arXZ = ActiveRenderInfo.rotationXZ;
            tessellator.startDrawingQuads();
            tessellator.setBrightness(220);
            tessellator.setColorRGBA_F(1.0f, 1.0f, 1.0f, 1.0f);
            Vec3 v1 = Vec3.createVectorHelper((-arX - arYZ), (-arXZ), (-arZ - arXY));
            Vec3 v2 = Vec3.createVectorHelper((-arX + arYZ), arXZ, (-arZ + arXY));
            Vec3 v3 = Vec3.createVectorHelper((arX + arYZ), arXZ, (arZ + arXY));
            Vec3 v4 = Vec3.createVectorHelper((arX - arYZ), (-arXZ), (arZ - arXY));
            double f2 = 0.0;
            double f3 = 1.0;
            double f4 = 0.0;
            double f5 = 1.0;
            tessellator.setNormal(0.0f, 0.0f, -1.0f);
            tessellator.addVertexWithUV(v1.xCoord * scaleX, v1.yCoord * scaleY, v1.zCoord * scaleZ, f2, f5);
            tessellator.addVertexWithUV(v2.xCoord * scaleX, v2.yCoord * scaleY, v2.zCoord * scaleZ, f3, f5);
            tessellator.addVertexWithUV(v3.xCoord * scaleX, v3.yCoord * scaleY, v3.zCoord * scaleZ, f3, f4);
            tessellator.addVertexWithUV(v4.xCoord * scaleX, v4.yCoord * scaleY, v4.zCoord * scaleZ, f2, f4);
            tessellator.draw();
        }
        glDisable(GL_BLEND);
        glPopMatrix();
    }

    public static void renderTextureOrth(ResourceLocation texture, double scaleX, double scaleY, SusVec3 lookVector, double zOffset, Runnable additionalPreparations
    ) {
        bindTexture(texture);
        glPushMatrix();
        glPushAttrib(GL_BLEND);
        glPushAttrib(GL_CULL_FACE);

        glEnable(GL_BLEND);
        glDisable(GL_CULL_FACE);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        if (Minecraft.getMinecraft().renderViewEntity instanceof EntityPlayer) {
            SusVec3 lookVecXZProjection = new SusVec3(lookVector.x, 0.0, lookVector.z);
            double angleAroundY = (SusVec3.angleBetweenVec3(new SusVec3(0, 0, 1), lookVecXZProjection) * 180.0 / Math.PI) * Math.signum(lookVecXZProjection.x);
            double angleAroundX = (SusVec3.angleBetweenVec3(lookVecXZProjection, lookVector) * 180.0 / Math.PI) * (-Math.signum(lookVector.y));
            glRotated(angleAroundY, 0.0, 1.0, 0.0);
            glRotated(angleAroundX, 1.0, 0.0, 0.0);

            glTranslated(0.0f, 0.0f, zOffset);
            additionalPreparations.run();
            glScaled(scaleX, 1.0, 1.0);
            glScaled(1.0, scaleY, 1.0);


            drawFromCenter(1.0);
        }
        glPopAttrib();
        glPopAttrib();

        glPopMatrix();
    }

    public static void makeSystemOrthToVectorAndHandle(SusVec3 lookVector, double zOffset, Runnable additionalPreparations
    ) {
        glPushMatrix();

        glDisable(GL_CULL_FACE);
        if (Minecraft.getMinecraft().renderViewEntity instanceof EntityPlayer) {
            if(lookVector.x == 0.0 && lookVector.z == 0.0 && lookVector.y != 0.0) {
                glRotated(lookVector.y > 0 ? 90.0f : -90.0f, 1.0, 0.0, 0.0);
            }
            else {
                SusVec3 lookVecXZProjection = new SusVec3(lookVector.x, 0.0, lookVector.z);
                double angleAroundY = (SusVec3.angleBetweenVec3(new SusVec3(0, 0, 1), lookVecXZProjection) * 180.0 / Math.PI) * Math.signum(lookVecXZProjection.x);
                double angleAroundX = (SusVec3.angleBetweenVec3(lookVecXZProjection, lookVector) * 180.0 / Math.PI) * (-Math.signum(lookVector.y));
                glRotated(angleAroundY, 0.0, 1.0, 0.0);
                glRotated(angleAroundX, 1.0, 0.0, 0.0);
            }
            glTranslated(0.0f, 0.0f, zOffset);

            additionalPreparations.run();

        }

        glPopMatrix();
    }


    static Map<String, ResourceLocation> boundTextures = new HashMap<String, ResourceLocation>();

    public static void bindTexture(String texture) {
        ResourceLocation rl = null;
        if (boundTextures.containsKey(texture)) {
            rl = boundTextures.get(texture);
        } else {
            rl = new ResourceLocation("thaumcraft", texture);
        }
        (Minecraft.getMinecraft()).renderEngine.bindTexture(rl);
    }

    public static void bindTexture(String mod, String texture) {
        ResourceLocation rl = null;
        if (boundTextures.containsKey(mod + ":" + texture)) {
            rl = boundTextures.get(mod + ":" + texture);
        } else {
            rl = new ResourceLocation(mod, texture);
        }
        (Minecraft.getMinecraft()).renderEngine.bindTexture(rl);
    }


    public static void bindTexture(ResourceLocation resource) {
        (Minecraft.getMinecraft()).renderEngine.bindTexture(resource);
    }
    
    public static void bindCordSystemToPlayerBody(EntityPlayer player, float partialTicks) {

        GL11.glRotatef(-(player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * partialTicks), 0.0F, 1.0F, 0.0F);
//        GL11.glRotatef(player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * partialTicks, 1.0F, 0.0F, 0.0F);
//        GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
//        GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);

    }
}


