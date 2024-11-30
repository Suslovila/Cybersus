package com.suslovila.cybersus.client.gui;

import com.mojang.realmsclient.util.Pair;
import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.api.implants.ability.Ability;
import com.suslovila.cybersus.api.implants.ability.AbilityHack;
import com.suslovila.cybersus.common.item.ItemImplant;
import com.suslovila.cybersus.extendedData.CybersusPlayerExtendedData;
import com.suslovila.cybersus.utils.SusGraphicHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.suslovila.cybersus.utils.SusGraphicHelper.*;

public class GuiImplants {

    public static GuiImplants INSTANCE = new GuiImplants();

    private GuiImplants() {
    }


    private final RenderItem itemRender = new RenderItem();

    public static int currentImplantSlotId = 0;
    public static boolean shouldRenderGui = true;

    private static final ResourceLocation slotActive = new ResourceLocation(Cybersus.MOD_ID, "textures/gui/implants/abilitySlotActive.png");
    private static final ResourceLocation slotDeactivated = new ResourceLocation(Cybersus.MOD_ID, "textures/gui/implants/abilitySlotDeactivated.png");
    private static final ResourceLocation slotInCooldown = new ResourceLocation(Cybersus.MOD_ID, "textures/gui/implants/abilitySlotCooldown.png");

    private static final ResourceLocation spinningCircle = new ResourceLocation(Cybersus.MOD_ID, "textures/misc/radial4.png");


    private static final ResourceLocation hackHotbar = new ResourceLocation(Cybersus.MOD_ID, "textures/gui/implants/hack_hotbar.png");
    private static final ResourceLocation hackHotbarActivated = new ResourceLocation(Cybersus.MOD_ID, "textures/gui/implants/hack_hotbar_activated.png");
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void renderGui(RenderGameOverlayEvent.Post event) {
        if (event.type == RenderGameOverlayEvent.ElementType.ALL) {

            if (!shouldRenderGui) return;
            CybersusPlayerExtendedData data = CybersusPlayerExtendedData.get(Minecraft.getMinecraft().thePlayer);
            if (data == null) return;
            ItemStack stack = data.implantStorage.getStackInSlot(currentImplantSlotId);

            GL11.glPushAttrib(GL11.GL_LIGHTING);
            GL11.glPushAttrib(GL11.GL_CULL_FACE);
            GL11.glPushAttrib(GL11.GL_BLEND);

            if (stack != null) {

                drawImplantWithScale(event, stack, 1.5f);
            }
            ArrayList<Pair<ItemStack, AbilityHack>> activeHackAbilities = new ArrayList<>();
            data.implantStorage.forEachImplant((index, implant) -> {
                List<Ability> implantAbilities = ((ItemImplant) implant.getItem()).getAbilities(Minecraft.getMinecraft().thePlayer, index, implant);
                for (int i = 0; i < implantAbilities.size(); i++) {
                    Ability ability = implantAbilities.get(i);
                    if (ability instanceof AbilityHack && ((AbilityHack) ability).isHacking(implant)) {
                        activeHackAbilities.add(Pair.of(implant, (AbilityHack) ability));
                    }
                }
            });

            renderHackHotbars(event, activeHackAbilities);


            GL11.glPopAttrib();
            GL11.glPopAttrib();
            GL11.glPopAttrib();

            RenderHelper.disableStandardItemLighting();

        }
    }


    public void drawImplantWithScale(RenderGameOverlayEvent.Post event, ItemStack implant, float scale) {

        double width = event.resolution.getScaledWidth_double();
        double height = event.resolution.getScaledHeight_double();
        double x = 70;
        double y = height - 115;
        GL11.glPushMatrix();


        GL11.glTranslated((x + 8 * scale), (y + 8 * scale), 0.0);
        GL11.glScalef(scale, scale, scale);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        ItemImplant implantClass = (ItemImplant) implant.getItem();

        double radius = 18.0;
        double distanceBetweenAbilities = 10.0;


        GL11.glDisable(GL11.GL_LIGHTING);

        GL11.glPushMatrix();
        double angle = (SusGraphicHelper.getRenderGlobalTime(event.partialTicks) % 360);
        GL11.glRotated(angle, 0.0, 0.0, 1.0);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        bindTexture(spinningCircle);
        SusGraphicHelper.drawFromCenter(radius);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        List<Ability> abilities = implantClass.getAbilities(Minecraft.getMinecraft().thePlayer, currentImplantSlotId, implant);
        double startOffset = Math.max(abilities.size() - 1, 0) * (distanceBetweenAbilities + radius) / 2;
        GL11.glTranslated(-startOffset, 30.0, 0.0);
        for (int index = 0; index < abilities.size(); index++) {
            Ability ability = abilities.get(index);
            GL11.glPushMatrix();

            ResourceLocation slotTexture = ability.isOnCooldown(implant) ? slotInCooldown : (ability.isActive(implant) ? slotActive : slotDeactivated);
            bindTexture(slotTexture);
            SusGraphicHelper.drawFromCenter(radius * 0.7);

            GL11.glTranslated(0.0, 0.0, 1.0);

            GL11.glPushMatrix();
            bindTexture(ability.texture);
            GL11.glRotated(-90.0, 0.0, 0.0, 1.0);
            SusGraphicHelper.drawFromCenter(radius * 0.58);
            GL11.glPopMatrix();

            GL11.glTranslated(0.0, 0.0, 1.0);

            GL11.glPushMatrix();
            float cooldown = ability.getCooldown(implant) / 20.0f;
            String cooldownString = cooldown == 0.0f ? "" : String.format("%.1f", cooldown);
            GL11.glTranslated(-2.0 * cooldownString.length() / 2, 0.0, 0.0);
            GL11.glScaled(0.5, 0.5, 0.5);
            Minecraft.getMinecraft().fontRendererObj.drawString(cooldownString, 0, 0, Color.white.getRGB());
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glPopMatrix();

            GL11.glPushMatrix();
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            if(player == null) return;
            boolean hasPlayerEnough = ability.getFuelConsumeOnActivation(player, currentImplantSlotId, implant).hasPlayerEnough(player);
            double scaleText = 0.4;
            String hasEnoughMessage = hasPlayerEnough ? "fuel enough" : "not enough fuel";
            GL11.glTranslated(-5 * scaleText * hasEnoughMessage.length() / 2, radius * 0.7, 0.0);
            GL11.glScaled(scaleText, scaleText, scaleText);
            Minecraft.getMinecraft().fontRendererObj.drawString(hasEnoughMessage, 0, 0, hasPlayerEnough ? Color.green.getRGB() : Color.red.getRGB());
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glPopMatrix();

            GL11.glPopMatrix();
            GL11.glTranslated(distanceBetweenAbilities + radius, 0.0, 0.0);
        }
        GL11.glPopMatrix();

        GL11.glPopMatrix();

        // drawing implant by itself
        GL11.glPushMatrix();
        GL11.glScalef(scale, scale, scale);
        GL11.glTranslated(x / scale, y / scale, 0f);
        drawStack(Minecraft.getMinecraft(), implant, (int) (1 / scale), (int) (1 / scale), 0f);
        GL11.glPopMatrix();

    }


    public void renderHackHotbars(RenderGameOverlayEvent.Post event, ArrayList<Pair<ItemStack, AbilityHack>> activeHackAbilities) {
        RenderHelper.enableGUIStandardItemLighting();
        int hackAbilitiesAmount = 0;
        for (int index = 0; index < activeHackAbilities.size(); index++) {
            Pair<ItemStack, AbilityHack> pair = activeHackAbilities.get(index);
            ItemStack implant = pair.first();
            AbilityHack abilityHack = pair.second();

            double width = event.resolution.getScaledWidth_double();
            double height = event.resolution.getScaledHeight_double();

            float hackTime = abilityHack.getHackTimeLeft(implant);
            GL11.glPushMatrix();
            double xHackHotbarRenderer = width - 180;
            double yHackHotbarRenderer = height - 100 - 40 * hackAbilitiesAmount;

            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glTranslated((xHackHotbarRenderer), (yHackHotbarRenderer), 0.0);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glRotated(-90.0, 0.0, 0.0, 1.0);
            SusGraphicHelper.drawGuideArrows();
            GL11.glDisable(GL11.GL_CULL_FACE);
            double scaleHack = 1.25;
            GL11.glPushMatrix();
            GL11.glScaled(scaleHack, scaleHack, 1.0);
            SusGraphicHelper.bindTexture(hackHotbar);
            drawPartFromLeftSide(16, 64, 1.0f);

            float alpha = 0.8f;
            if(abilityHack.isRelockingTarget(implant)) {
                alpha = (float) Math.max(0.05, alpha * Math.abs(Math.sin(SusGraphicHelper.getRenderGlobalTime(event.partialTicks) / 7)));
            }
            Color color = new Color(229f / 255f, 0.0f, 0.0f, alpha);
            GL11.glColor4f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, alpha);

            GL11.glEnable(GL11.GL_BLEND);

            float actualPartialTicks = (abilityHack.isRelockingTarget(implant) ? 0 : event.partialTicks);
            SusGraphicHelper.bindTexture(hackHotbarActivated);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
            float progress = (abilityHack.getRequiredHackTime() - hackTime + actualPartialTicks) / ((float) abilityHack.getRequiredHackTime() + actualPartialTicks);
            GL11.glTranslated(0.0, 0.0, 1.0);
            drawPartFromLeftSide(16, 64, progress);
            GL11.glPopMatrix();
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

            GL11.glPushMatrix();
            StringBuilder hackMessage = new StringBuilder("hacking");
            int dotAmount = (int) ((SusGraphicHelper.getRenderGlobalTime(actualPartialTicks) % 19) / 5);
            for (int i = 0; i < dotAmount; i++) {
                hackMessage.append(".");
            }
            if(abilityHack.isRelockingTarget(implant)) {
                hackMessage = new StringBuilder("lost in: ").append(String.format("%.1f", abilityHack.getTimeBeforeTargetLost(implant) / 20.0f));
            }
            GL11.glRotated(90.0, 0.0, 0.0, 1.0);
            GL11.glTranslated(0.0, -20.0, 0.0);
            GL11.glScaled(scaleHack * 0.65, scaleHack * 0.65, 1.0);


            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPushMatrix();
            GL11.glScaled(0.8, 0.8, 1.0);
            Minecraft.getMinecraft().fontRendererObj.drawString("task: " + abilityHack.name + ".tmc", 0, 5, Color.red.getRGB());
            GL11.glPopMatrix();
            GL11.glEnable(GL11.GL_BLEND);

            float alpha1 = 0.9f;
            if(!abilityHack.isRelockingTarget(implant)) {
                alpha1 = (float) Math.max(0.05, alpha1 * Math.abs(Math.sin(SusGraphicHelper.getRenderGlobalTime(actualPartialTicks) / 7)));
            }

            Color colorChanged = new Color(1.0f, 0.0f, 0.0f, alpha1);
            int progressRenderPercent = (int) (progress * 100);
            Minecraft.getMinecraft().fontRendererObj.drawString(String.valueOf(progressRenderPercent) + "%", 80, 40, Color.red.getRGB());

            Minecraft.getMinecraft().fontRendererObj.drawString(hackMessage.toString(), 0, 40, colorChanged.getRGB());


            SusGraphicHelper.setStandartColors();
            GL11.glPopMatrix();


            GL11.glPopMatrix();
            hackAbilitiesAmount++;

        }
    }

    public static void drawPartFromLeftSide(float height, float width, float picturePart) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        float percent = Math.min(picturePart, 1.0f);
        tessellator.addVertexWithUV(height / 2, 0, 0.0, u1, v1);
        tessellator.addVertexWithUV(height / 2, width * percent, 0.0, u2 * percent, v1);
        tessellator.addVertexWithUV(-height / 2, width * percent, 0.0, u2 * percent, v2);
        tessellator.addVertexWithUV(-height / 2, 0, 0.0, u1, v2);
        tessellator.draw();
    }

    public void drawStack(Minecraft mc, ItemStack item, int x, int y, float zLevel) {
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glPushMatrix();
        GL11.glPushAttrib(GL11.GL_TRANSFORM_BIT);
        GL11.glPushAttrib(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        if (item != null) {
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glPushAttrib(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            float prevZ = itemRender.zLevel;
            itemRender.zLevel = zLevel;
            itemRender.renderWithColor = false;
            itemRender.renderItemAndEffectIntoGUI(mc.fontRendererObj, mc.renderEngine, item, x, y);
            itemRender.renderItemOverlayIntoGUI(mc.fontRendererObj, mc.renderEngine, item, x, y);
            itemRender.zLevel = prevZ;
            itemRender.renderWithColor = true;
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glPopAttrib();
        }
        GL11.glPopAttrib();
        GL11.glPopAttrib();
        GL11.glPopMatrix();
    }

    public void loadOverlayGLSettings() {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution scaledResolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        SusGraphicHelper.drawGuideArrows();
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        SusGraphicHelper.drawGuideArrows();
        GL11.glLoadIdentity();
        GL11.glOrtho(
                0.0,
                scaledResolution.getScaledWidth_double(),
                scaledResolution.getScaledHeight_double(),
                0.0,
                1000.0,
                3000.0
        );
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0f, 0.0f, -2000.0f);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        SusGraphicHelper.drawGuideArrows();
    }



}