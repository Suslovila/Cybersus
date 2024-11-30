package com.suslovila.cybersus.client.clientProcess;

import com.suslovila.cybersus.api.process.ClientProcess;
import com.suslovila.cybersus.client.RenderHelper;
import com.suslovila.cybersus.client.particles.ParticleRenderDispatcher;
import com.suslovila.cybersus.common.item.ItemImplant;
import com.suslovila.cybersus.extendedData.CybersusPlayerExtendedData;
import com.suslovila.cybersus.utils.SusGraphicHelper;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.profiler.Profiler;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ClientImplantEvents {
    public static ClientImplantEvents INSTANCE = new ClientImplantEvents();


    private ClientImplantEvents() {}
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void renderWorldLast(RenderWorldLastEvent event) {
        EntityPlayer currentPlayer = Minecraft.getMinecraft().thePlayer;
        if (currentPlayer == null) return;
        World world = currentPlayer.worldObj;
        List<EntityPlayer> players = world.playerEntities;
        players.forEach(player -> {
            GL11.glPushMatrix();
            SusGraphicHelper.translateFromPlayerTo(SusGraphicHelper.getRenderPos(player, event.partialTicks), event.partialTicks);
            CybersusPlayerExtendedData data = CybersusPlayerExtendedData.get(player);
            if (data != null) {
                CybersusPlayerExtendedData.get(player).implantStorage.forEachImplant((index, stack) -> {
                    if (stack.getItem() instanceof ItemImplant) {
                        ((ItemImplant) stack.getItem()).onRenderWorldLastEvent(event, player, index, stack);
                    }
                });
            }
            GL11.glPopMatrix();
        });

        CybersusPlayerExtendedData data = CybersusPlayerExtendedData.get(currentPlayer);
        if (data != null) {
            data.implantStorage.forEachImplant((index, stack) -> {
                if (stack.getItem() instanceof ItemImplant) {
                    ((ItemImplant) stack.getItem()).onRenderWorldLastEventIndividually(event, currentPlayer, index, stack);
                }
            });
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void renderPlayerEvent(RenderPlayerEvent.Pre event) {
        EntityPlayer currentPlayer = event.entityPlayer;
        if (currentPlayer == null) return;

        CybersusPlayerExtendedData data = CybersusPlayerExtendedData.get(currentPlayer);
        if (data != null) {
            data.implantStorage.forEachImplant((index, stack) -> {
                if (stack.getItem() instanceof ItemImplant) {
                    ((ItemImplant) stack.getItem()).onRenderPlayerEvent(event, currentPlayer, index, stack);
                }
            });
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onRenderHandEvent(RenderHandEvent event) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (player == null) return;
        CybersusPlayerExtendedData.get(player).implantStorage.forEachImplant((index, stack) -> {
            if (stack.getItem() instanceof ItemImplant) {
                ((ItemImplant) stack.getItem()).onRenderHandEvent(event, player, index, stack);
            }
        });
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPlayerRenderEarly(RenderPlayerEvent.Specials.Post event) {

        EntityPlayer player = event.entityPlayer;
        CybersusPlayerExtendedData data = CybersusPlayerExtendedData.get(event.entityPlayer);
        if (data != null) {
            data.implantStorage.forEachImplant((index, stack) -> {
                if (stack.getItem() instanceof ItemImplant) {
                    ((ItemImplant) stack.getItem()).onRenderPlayerEventEquipment(event, player, index, stack, RenderHelper.RenderType.BODY, EventPriority.HIGHEST);
                }
            });


            float yaw = player.prevRotationYawHead + (player.rotationYawHead - player.prevRotationYawHead) * event.partialRenderTick;
            float yawOffset = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * event.partialRenderTick;
            float pitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * event.partialRenderTick;

            GL11.glPushMatrix();
            GL11.glRotatef(yawOffset, 0, -1, 0);
            GL11.glRotatef(yaw - 270, 0, 1, 0);
            GL11.glRotatef(pitch, 0, 0, 1);

            GL11.glRotated(180.0, 1.0, 0.0, 0.0);
            data.implantStorage.forEachImplant((index, stack) -> {
                if (stack.getItem() instanceof ItemImplant) {
                    ((ItemImplant) stack.getItem()).onRenderPlayerEventEquipment(event, player, index, stack, RenderHelper.RenderType.HEAD, EventPriority.HIGHEST);
                }
            });

            GL11.glPopMatrix();
        }

    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onPlayerRenderNormalPriority(RenderPlayerEvent.Specials.Post event) {

        EntityPlayer player = event.entityPlayer;
        CybersusPlayerExtendedData data = CybersusPlayerExtendedData.get(event.entityPlayer);
        if (data != null) {
            data.implantStorage.forEachImplant((index, stack) -> {
                if (stack.getItem() instanceof ItemImplant) {
                    ((ItemImplant) stack.getItem()).onRenderPlayerEventEquipment(event, player, index, stack, RenderHelper.RenderType.BODY, EventPriority.NORMAL);
                }
            });


            float yaw = player.prevRotationYawHead + (player.rotationYawHead - player.prevRotationYawHead) * event.partialRenderTick;
            float yawOffset = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * event.partialRenderTick;
            float pitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * event.partialRenderTick;

            GL11.glPushMatrix();
            GL11.glRotatef(yawOffset, 0, -1, 0);
            GL11.glRotatef(yaw - 270, 0, 1, 0);
            GL11.glRotatef(pitch, 0, 0, 1);

            GL11.glRotated(180.0, 1.0, 0.0, 0.0);
            data.implantStorage.forEachImplant((index, stack) -> {
                if (stack.getItem() instanceof ItemImplant) {
                    ((ItemImplant) stack.getItem()).onRenderPlayerEventEquipment(event, player, index, stack, RenderHelper.RenderType.HEAD, EventPriority.NORMAL);
                }
            });

            GL11.glPopMatrix();
        }

    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void renderParticles(RenderWorldLastEvent event) {
        handleParticles();
    }

    private void handleParticles() {
        Profiler profiler = Minecraft.getMinecraft().mcProfiler;
        profiler.startSection("particles");
        ParticleRenderDispatcher.dispatch();
        profiler.endSection();
    }
}

