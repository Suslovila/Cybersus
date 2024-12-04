package com.suslovila.cybersus.common.item.implants.sinHeart;

import com.suslovila.cybersus.api.fuel.FuelComposite;
import com.suslovila.cybersus.api.fuel.impl.fuel.essentia.FuelEssentia;
import com.suslovila.cybersus.api.implants.ability.AbilityPassive;
import com.suslovila.cybersus.client.RenderHelper;
import com.suslovila.cybersus.utils.SusGraphicHelper;
import cpw.mods.fml.common.eventhandler.EventPriority;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

import java.util.Arrays;
import java.util.List;

public class AbilitySinHeartFormShift extends AbilityPassive {
    Aspect aspect;

    public AbilitySinHeartFormShift(Aspect aspect) {
        super("sin_mode");
        this.aspect = aspect;
    }

    @Override
    public FuelComposite getFuelConsumeOnActivation(EntityPlayer player, int index, ItemStack implant) {
        return FuelComposite.allRequired(new FuelEssentia(new AspectList().add(aspect, 256)));
//        return FuelComposite.EMPTY;
    }

    @Override
    public int getCooldownTotal(EntityPlayer player, int index, ItemStack implant) {
        return 2 * 60 * 20;
    }

    @Override
    public FuelComposite getFuelConsumePerCheck(EntityPlayer player, int index, ItemStack implant) {
        return FuelComposite.allRequired(new FuelEssentia(new AspectList().add(aspect, 16)));
//        return FuelComposite.EMPTY;

    }

    @Override
    public void onRenderPlayerSpecialPost(RenderPlayerEvent.Specials.Post event, EntityPlayer player, int index, ItemStack implant, RenderHelper.RenderType type, EventPriority priority) {



        if (event.isCanceled()) return;
        if (!isActive(implant)) return;

        GL11.glPushAttrib(GL11.GL_BLEND);
        GL11.glPushAttrib(GL11.GL_LIGHTING);
        GL11.glPushAttrib(GL11.GL_CULL_FACE);

        int hash = getHash(player.getCommandSenderName() + "v");
        int hash2 = getHash(player.getCommandSenderName() + "a");
        int hornIndex = (hash) % SinHeartRenderManager.hornModels.size();
        int wingIndex = (hash2) % SinHeartRenderManager.wingModels.size();

        if (type == RenderHelper.RenderType.HEAD) {
            GL11.glPushMatrix();
            RenderHelper.Helper.translateToHeadLevel(player);
//            SusGraphicHelper.drawGuideArrows();
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_LIGHTING);
            SusGraphicHelper.pushLight();
            SusGraphicHelper.setMaxBrightness();
            SusGraphicHelper.bindColor(aspect.getColor(), 0.5f, 1.0f);
            GL11.glTranslated(0.0, 0.135, 0.0);
            GL11.glScaled(0.25, 0.25, 0.25);
            SinHeartRenderManager.hornModels.get(hornIndex).render();

            SusGraphicHelper.popLight();
            GL11.glPopMatrix();
        }

        if (type == RenderHelper.RenderType.BODY) {
            GL11.glPushMatrix();
            RenderHelper.Helper.translateToHeadLevel(player);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_LIGHTING);
            SusGraphicHelper.pushLight();
            SusGraphicHelper.setMaxBrightness();
            SusGraphicHelper.bindColor(aspect.getColor(), 0.5f, 1.0f);
            GL11.glTranslated(0.0, 0.135, 0.0);
            GL11.glRotated(180.0, 1.0, 0.0, 0.0);
            GL11.glRotated(90.0, 0.0, 0.0, 1.0);
//            GL11.glTranslated(0.0, 1.3, 0.0);
            GL11.glTranslated(-0.9, 0, -0.15);

            GL11.glPushMatrix();

            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glScaled(0.25, 0.25, 0.25);
            WingTexture wingTexture = SinHeartRenderManager.wingModels.get(wingIndex);
            SusGraphicHelper.bindTexture(wingTexture.texture);
//            GL11.glTranslated(wingTexture.xOffset, 0.0, 0.0);
//            SusGraphicHelper.drawGuideArrows();
            if (player.isSneaking()) {
                GL11.glRotatef(-28.64789F, 0.0F, 1.0F, 0.0F);
                GL11.glTranslated(0.0, 0.0, -2.23);
                GL11.glTranslated(-0.35, 0.0, 0.0);
            }
            GL11.glTranslated(wingTexture.xOffset, 0.0, 0.0);
//            SusGraphicHelper.drawGuideArrows();
            GL11.glRotated(-30.0, 1.0, 0.0, 0.0);
            SusGraphicHelper.drawFromXYAxis(4.0f);

            GL11.glPopMatrix();


            GL11.glPushMatrix();
            GL11.glRotated(180, 1.0, 0.0, 0.0);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glScaled(0.25, 0.25, 0.25);
            SusGraphicHelper.bindTexture(wingTexture.texture);
//            GL11.glTranslated(wingTexture.xOffset, 0.0, 0.0);
//            SusGraphicHelper.drawGuideArrows();
            if (player.isSneaking()) {
                GL11.glRotatef(28.64789F, 0.0F, 1.0F, 0.0F);
                GL11.glTranslated(0.0, 0.0, 2.23);
                GL11.glTranslated(-0.35, 0.0, 0.0);
            }
            GL11.glTranslated(wingTexture.xOffset, 0.0, 0.0);
//            SusGraphicHelper.drawGuideArrows();
            GL11.glRotated(30.0, 1.0, 0.0, 0.0);
            SusGraphicHelper.drawFromXYAxis(4.0f);

            GL11.glPopMatrix();


//            GL11.glRotated(180, 1.0, 0.0,0.0);
//            GL11.glDisable(GL11.GL_CULL_FACE);
//            GL11.glScaled(0.25, 0.25, 0.25);
//
//            GL11.glTranslated(wingTexture.xOffset, 0.0, 0.0);
//            if(player.isSneaking()) {
//                GL11.glRotatef(28.64789F, 0.0F, 1.0F, 0.0F);
//                GL11.glTranslated(0.0, 1.0 * wingTexture.xOffset * 5, 1.0 * wingTexture.xOffset * 5);
//            }
//            GL11.glRotated(30.0, 1.0, 0.0,0.0);
//            SusGraphicHelper.drawGuideArrows();
//            SusGraphicHelper.bindTexture(wingTexture.texture);
//            SusGraphicHelper.drawFromXYAxis(4.0f);

            SusGraphicHelper.popLight();
            GL11.glPopMatrix();
        }

        GL11.glPopAttrib();
        GL11.glPopAttrib();
        GL11.glPopAttrib();
    }

    @Override
    public void renderAbility(RenderGameOverlayEvent.Post event, ItemStack implant, float scale, double radius) {
        SusGraphicHelper.bindColor(this.aspect.getColor(), 1.0f, 1.0f);
        super.renderAbility(event, implant, scale, radius);
    }

    public static int getHash(String input) {
        int hash = 0;
        for (char c : input.toCharArray()) {
            hash += c;
        }
        return hash * 31;
    }
}
