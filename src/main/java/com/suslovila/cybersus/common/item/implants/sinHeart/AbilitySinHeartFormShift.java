package com.suslovila.cybersus.common.item.implants.sinHeart;

import com.suslovila.cybersus.api.fuel.FuelComposite;
import com.suslovila.cybersus.api.fuel.impl.fuel.essentia.FuelEssentia;
import com.suslovila.cybersus.api.implants.ability.AbilityPassive;
import com.suslovila.cybersus.client.RenderHelper;
import com.suslovila.cybersus.utils.KhariumSusNBTHelper;
import com.suslovila.cybersus.utils.SusGraphicHelper;
import cpw.mods.fml.common.eventhandler.EventPriority;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.Thaumcraft;

import java.util.Arrays;
import java.util.List;

import static com.suslovila.cybersus.Cybersus.random;

public class AbilitySinHeartFormShift extends AbilityPassive {
    Aspect aspect;

    public AbilitySinHeartFormShift(Aspect aspect) {
        super("sin_mode", true, true);
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
    public void onAbilityStatusSwitched(EntityPlayer player, int index, ItemStack implant) {
        super.onAbilityStatusSwitched(player, index, implant);
        for (int a = 0; a < 60; a++) {
            float he = Math.max(1.0F, player.height * (150 - 100) / 150.0F);
            Thaumcraft.proxy.smokeSpiral(player.worldObj, player.posX, player.boundingBox.minY + (he / 2.0F), player.posZ, he, random.nextInt(360), MathHelper.floor_double(player.boundingBox.minY) - 1, aspect.getColor());

        }
    }

    @Override
    public void onRenderPlayerSpecialPost(RenderPlayerEvent.Specials.Post event, EntityPlayer player, int index, ItemStack implant, RenderHelper.RenderType type, EventPriority priority) {

//        SusGraphicHelper.drawGuideArrows();
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
            if (player.isSneaking()) {
                GL11.glTranslated(0.0F, -0.05F, 0.0F);
            }
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
//            SusGraphicHelper.drawGuideArrows();
//            RenderHelper.Helper.translateToHeadLevel(player);
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

    @Override
    public void sendToCooldown(EntityPlayer player, int index, ItemStack implant) {
        super.sendToCooldown(player, index, implant);
        player.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 20 * 10, 9));
        player.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, 20 * 10, 9));
        player.addPotionEffect(new PotionEffect(Potion.weakness.id, 20 * 10, 9));

    }
}
