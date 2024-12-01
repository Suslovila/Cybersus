package com.suslovila.cybersus.common.item.implants.sinHeart;

import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.api.fuel.FuelComposite;
import com.suslovila.cybersus.api.implants.ability.AbilityPassive;
import com.suslovila.cybersus.client.RenderHelper;
import com.suslovila.cybersus.utils.SusGraphicHelper;
import cpw.mods.fml.common.eventhandler.EventPriority;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderPlayerEvent;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.Aspect;

import java.util.Arrays;
import java.util.List;

public class AbilitySinHeartFormShift extends AbilityPassive {
    public static final List<ModelWrapped> hornModels = Arrays.asList(
            new ModelWrapped("models/zagredHorns.obj"),
            new ModelWrapped("models/simpleHorns.obj"),
            new ModelWrapped("models/liebeHorns.obj")

//            new ModelWrapped(),
//            new ModelWrapped(),
//            new ModelWrapped()
    );

    public static final List<WingTexture> wingModels = Arrays.asList(
            new WingTexture(new ResourceLocation(Cybersus.MOD_ID, "textures/wings/liebeWing.png"), 1.5),
            new WingTexture(new ResourceLocation(Cybersus.MOD_ID, "textures/wings/longWing.png"), 2.5),
            new WingTexture(new ResourceLocation(Cybersus.MOD_ID, "textures/wings/luciferoWing.png"), 0.0),
            new WingTexture(new ResourceLocation(Cybersus.MOD_ID, "textures/wings/megikulaWing.png"), 0.5)
    );
    Aspect aspect;

    public AbilitySinHeartFormShift(Aspect aspect) {
        super("essentia_overdrive");
        this.aspect = aspect;
    }

    @Override
    public FuelComposite getFuelConsumeOnActivation(EntityPlayer player, int index, ItemStack implant) {
//        return FuelComposite.allRequired(new FuelEssentia(new AspectList().add(aspect, 32)));
        return FuelComposite.EMPTY;
    }

    @Override
    public int getCooldownTotal(EntityPlayer player, int index, ItemStack implant) {
        return 2 * 60 * 20;
    }

    @Override
    public FuelComposite getFuelConsumePerCheck(EntityPlayer player, int index, ItemStack implant) {
//        return FuelComposite.allRequired(new FuelEssentia(new AspectList().add(aspect, 4)));
        return FuelComposite.EMPTY;

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
        int hornIndex = (hash) % hornModels.size();
        int wingIndex = (hash2) % wingModels.size();

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
            hornModels.get(hornIndex).render();

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
            WingTexture wingTexture = wingModels.get(wingIndex);
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

    public static int getHash(String input) {
        int hash = 0;
        for (char c : input.toCharArray()) {
            hash += c;
        }
        return hash * 31;
    }
}
