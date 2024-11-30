package com.suslovila.cybersus.common.event;

import com.suslovila.cybersus.common.item.ItemImplant;
import com.suslovila.cybersus.extendedData.CybersusPlayerExtendedData;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.AttackEntityEvent;

import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class ImplantEvents {
    public static ImplantEvents INSTANCE = new ImplantEvents();

    private ImplantEvents() {
    }

    // iterating through all players
//    @SideOnly(Side.CLIENT)
//    @SubscribeEvent
//    public void renderWorldLast(RenderWorldLastEvent event) {
//        EntityPlayer currentPlayer = Minecraft.getMinecraft().thePlayer;
//        if (currentPlayer == null) return;
//        World world = currentPlayer.worldObj;
//        List<EntityPlayer> players = world.playerEntities;
//        players.forEach(player -> {
//            GL11.glPushMatrix();
//            SusGraphicHelper.translateFromPlayerTo(SusGraphicHelper.getRenderPos(player, event.partialTicks), event.partialTicks);
//            CybersusPlayerExtendedData data = CybersusPlayerExtendedData.get(player);
//            if (data != null) {
//                CybersusPlayerExtendedData.get(player).implantStorage.forEachImplant((index, stack) -> {
//                    if (stack.getItem() instanceof ItemImplant) {
//                        ((ItemImplant) stack.getItem()).onRenderWorldLastEvent(event, player, index, stack);
//                    }
//                });
//            }
//            GL11.glPopMatrix();
//        });
//
//        CybersusPlayerExtendedData data = CybersusPlayerExtendedData.get(currentPlayer);
//        if (data != null) {
//            data.implantStorage.forEachImplant((index, stack) -> {
//                if (stack.getItem() instanceof ItemImplant) {
//                    ((ItemImplant) stack.getItem()).onRenderWorldLastEventIndividually(event, currentPlayer, index, stack);
//                }
//            });
//        }
//    }
//
//    @SideOnly(Side.CLIENT)
//    @SubscribeEvent
//    public void renderPlayerEvent(RenderPlayerEvent.Pre event) {
//        EntityPlayer currentPlayer = event.entityPlayer;
//        if (currentPlayer == null) return;
//
//        CybersusPlayerExtendedData data = CybersusPlayerExtendedData.get(currentPlayer);
//        if (data != null) {
//            data.implantStorage.forEachImplant((index, stack) -> {
//                if (stack.getItem() instanceof ItemImplant) {
//                    ((ItemImplant) stack.getItem()).onRenderPlayerEvent(event, currentPlayer, index, stack);
//                }
//            });
//        }
//    }


//    @SubscribeEvent(priority = EventPriority.HIGHEST)
//    public void onPlayerRenderEarly(RenderPlayerEvent.Specials.Post event) {
//
//        EntityPlayer player = event.entityPlayer;
//        CybersusPlayerExtendedData data = CybersusPlayerExtendedData.get(event.entityPlayer);
//        if (data != null) {
//            data.implantStorage.forEachImplant((index, stack) -> {
//                if (stack.getItem() instanceof ItemImplant) {
//                    ((ItemImplant) stack.getItem()).onRenderPlayerEventEquipment(event, player, index, stack, RenderHelper.RenderType.BODY, EventPriority.HIGHEST);
//                }
//            });
//
//
//            float yaw = player.prevRotationYawHead + (player.rotationYawHead - player.prevRotationYawHead) * event.partialRenderTick;
//            float yawOffset = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * event.partialRenderTick;
//            float pitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * event.partialRenderTick;
//
//            GL11.glPushMatrix();
//            GL11.glRotatef(yawOffset, 0, -1, 0);
//            GL11.glRotatef(yaw - 270, 0, 1, 0);
//            GL11.glRotatef(pitch, 0, 0, 1);
//
//            GL11.glRotated(180.0, 1.0, 0.0, 0.0);
//            data.implantStorage.forEachImplant((index, stack) -> {
//                if (stack.getItem() instanceof ItemImplant) {
//                    ((ItemImplant) stack.getItem()).onRenderPlayerEventEquipment(event, player, index, stack, RenderHelper.RenderType.HEAD, EventPriority.HIGHEST);
//                }
//            });
//
//            GL11.glPopMatrix();
//        }
//
//    }

//    @SubscribeEvent(priority = EventPriority.NORMAL)
//    public void onPlayerRenderNormalPriority(RenderPlayerEvent.Specials.Post event) {
//
//        EntityPlayer player = event.entityPlayer;
//        CybersusPlayerExtendedData data = CybersusPlayerExtendedData.get(event.entityPlayer);
//        if (data != null) {
//            data.implantStorage.forEachImplant((index, stack) -> {
//                if (stack.getItem() instanceof ItemImplant) {
//                    ((ItemImplant) stack.getItem()).onRenderPlayerEventEquipment(event, player, index, stack, RenderHelper.RenderType.BODY, EventPriority.NORMAL);
//                }
//            });
//
//
//            float yaw = player.prevRotationYawHead + (player.rotationYawHead - player.prevRotationYawHead) * event.partialRenderTick;
//            float yawOffset = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * event.partialRenderTick;
//            float pitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * event.partialRenderTick;
//
//            GL11.glPushMatrix();
//            GL11.glRotatef(yawOffset, 0, -1, 0);
//            GL11.glRotatef(yaw - 270, 0, 1, 0);
//            GL11.glRotatef(pitch, 0, 0, 1);
//
//            GL11.glRotated(180.0, 1.0, 0.0, 0.0);
//            data.implantStorage.forEachImplant((index, stack) -> {
//                if (stack.getItem() instanceof ItemImplant) {
//                    ((ItemImplant) stack.getItem()).onRenderPlayerEventEquipment(event, player, index, stack, RenderHelper.RenderType.HEAD, EventPriority.NORMAL);
//                }
//            });
//
//            GL11.glPopMatrix();
//        }
//
//    }

//    @SideOnly(Side.CLIENT)
//    @SubscribeEvent
//    public void onRenderHandEvent(RenderHandEvent event) {
//        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
//        if (player == null) return;
//        CybersusPlayerExtendedData.get(player).implantStorage.forEachImplant((index, stack) -> {
//            if (stack.getItem() instanceof ItemImplant) {
//                ((ItemImplant) stack.getItem()).onRenderHandEvent(event, player, index, stack);
//            }
//        });
//    }

//    @SideOnly(Side.CLIENT)
//    @SubscribeEvent
//    public void onRenderPlayerEvent(RenderPlayerEvent.Post event) {
//        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
//        if (player == null) return;
//        CybersusPlayerExtendedData.get(player).implantStorage.forEachImplant((index, stack) -> {
//            if (stack.getItem() instanceof ItemImplant) {
//                ((ItemImplant) stack.getItem()).onRenderPlayerEvent(event, player, index, stack);
//            }
//        });
//    }

    @SubscribeEvent
    public void onPlayerAttackEntityEvent(AttackEntityEvent event) {
        CybersusPlayerExtendedData.get(event.entityPlayer).implantStorage.forEachImplant((index, stack) -> {
            if (stack.getItem() instanceof ItemImplant) {
                ((ItemImplant) stack.getItem()).onPlayerAttackEntityEvent(event, event.entityPlayer, index, stack);
            }
        });
    }

    @SubscribeEvent
    public void onPlayerHealEvent(LivingHealEvent event) {
        if (!(event.entityLiving instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) event.entity;
        if (player == null) return;
        CybersusPlayerExtendedData.get(player).implantStorage.forEachImplant((index, stack) -> {
            if (stack.getItem() instanceof ItemImplant) {
                ((ItemImplant) stack.getItem()).onPlayerHealEvent(event, player, index, stack);
            }
        });
    }

    @SubscribeEvent
    public void onPlayerUpdateEvent(LivingEvent.LivingUpdateEvent event) {
        if (!(event.entityLiving instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) event.entity;
        if (player == null) return;
        CybersusPlayerExtendedData.get(player).implantStorage.forEachImplant((index, stack) -> {
            if (stack.getItem() instanceof ItemImplant) {
                ((ItemImplant) stack.getItem()).onPlayerUpdateEvent(event, player, index, stack);
            }
        });
    }

    @SubscribeEvent
    public void onLivingHurtEventIfVictim(LivingHurtEvent event) {
        if (!(event.entityLiving instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) event.entity;
        if (player == null) return;
        CybersusPlayerExtendedData.get(player).implantStorage.forEachImplant((index, stack) -> {
            if (stack != null && stack.getItem() instanceof ItemImplant) {
                ((ItemImplant) stack.getItem()).onPlayerHurtEventIfVictim(event, player, index, stack);
            }
        });
    }

    @SubscribeEvent
    public void onLivingHurtEventIfAttacker(LivingHurtEvent event) {
        if (!(event.source.getEntity() instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) event.source.getEntity();
        if (player == null) return;
        CybersusPlayerExtendedData.get(player).implantStorage.forEachImplant((index, stack) -> {
            if (stack != null && stack.getItem() instanceof ItemImplant) {
                ((ItemImplant) stack.getItem()).onPlayerHurtEventIfAttacker(event, player, index, stack);
            }
        });
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event) {
        EntityPlayer player = event.entityPlayer;
        if (player == null) return;
        CybersusPlayerExtendedData.get(player).implantStorage.forEachImplant((index, stack) -> {
            if (stack != null && stack.getItem() instanceof ItemImplant) {
                ((ItemImplant) stack.getItem()).onPlayerInteract(event, player, index, stack);
            }
        });
    }

    @SubscribeEvent
    public void onLivingDeathEvent(LivingDeathEvent event) {
        if (!(event.entityLiving instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) event.entity;
        if (player == null) return;
        CybersusPlayerExtendedData.get(player).implantStorage.forEachImplant((index, stack) -> {
            if (stack != null && stack.getItem() instanceof ItemImplant) {
                ((ItemImplant) stack.getItem()).onPlayerDeathEvent(event, player, index, stack);
            }
        });
    }

    @SubscribeEvent
    public void onPlayerBeingAttackedEvent(LivingAttackEvent event) {
        if (!(event.entityLiving instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) event.entity;
        if (player == null) return;
        CybersusPlayerExtendedData.get(player).implantStorage.forEachImplant((index, stack) -> {
            if (stack != null && stack.getItem() instanceof ItemImplant) {
                ((ItemImplant) stack.getItem()).onPlayerBeingAttackedEvent(event, player, index, stack);
            }
        });
    }

    @SubscribeEvent
    public void onLivingSetAttackTargetEvent(LivingSetAttackTargetEvent event) {
        if (!(event.entityLiving instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) event.entity;
        if (player == null) return;
        CybersusPlayerExtendedData.get(player).implantStorage.forEachImplant((index, stack) -> {
            if (stack != null && stack.getItem() instanceof ItemImplant) {
                ((ItemImplant) stack.getItem()).onPlayerSetAttackTargetEvent(event, player, index, stack);
            }
        });
    }


    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        EntityPlayer player = event.getPlayer();
        if (player == null) return;
        CybersusPlayerExtendedData.get(player).implantStorage.forEachImplant((index, stack) -> {
            if (stack != null && stack.getItem() instanceof ItemImplant) {
                ((ItemImplant) stack.getItem()).onBreakBlockEvent(event, player, index, stack);
            }
        });
    }


}
