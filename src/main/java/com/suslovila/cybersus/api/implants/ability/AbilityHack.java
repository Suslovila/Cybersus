package com.suslovila.cybersus.api.implants.ability;

import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.utils.KhariumSusNBTHelper;
import com.suslovila.cybersus.utils.SusGraphicHelper;
import com.suslovila.cybersus.utils.SusVec3;
import com.suslovila.cybersus.utils.SusWorldHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;

public abstract class AbilityHack extends Ability {
    public static final String HACK_TIME_LEFT_NBT = Cybersus.prefixAppender.doAndGet("hack");
    public static final String HACK_VICTIM_UUID_NBT = Cybersus.prefixAppender.doAndGet("victim_uuid");
    public static final String HACK_VICTIM_ID_NBT = Cybersus.prefixAppender.doAndGet("victim_id");

    public static final String TARGET_LOST_TIME_LEFT = Cybersus.prefixAppender.doAndGet("time_until_target_lost");

    public static final ArrayList<ResourceLocation> textureOuterCircles = new ArrayList<>();
    public static final ResourceLocation textureInnerCircle = new ResourceLocation(Cybersus.MOD_ID, "textures/gui/implants/ffhack_circle_inner.png");

    static {
        for (int i = 0; i < 3; i++) {
            int realIndex = i + 1;
            textureOuterCircles.add(new ResourceLocation(Cybersus.MOD_ID, "textures/gui/implants/fhack_circle_outer_" + realIndex + ".png"));
        }
    }

    public AbilityHack(String name) {
        super(name);
    }

    @Override
    public boolean isActive(ItemStack implant) {
        return isHacking(implant);
    }

    /**
     * time required to hack entity in ticks.
     *
     * @return time in ticks
     */
    public abstract int getRequiredHackTime();

    public boolean canHackEntity(EntityPlayer hacker, Entity victim, int slotIndex, ItemStack implant) {
        return true;
    }

    public void onEntityBeingHacked(EntityPlayer hacker, Entity victim, int slotIndex, ItemStack implant) {
        hackEntity(hacker, victim, slotIndex, implant);
        sendToCooldown(hacker, slotIndex, implant);
        notifyClient(hacker, slotIndex, implant);
    }

    /**
     * fired when entity was successfully hacked
     *
     * @param hacker
     * @param victim
     * @param slotIndex
     * @param implant
     */
    public abstract void hackEntity(EntityPlayer hacker, Entity victim, int slotIndex, ItemStack implant);

    @Override
    public void onEnableButtonClicked(EntityPlayer player, int index, ItemStack implant) {
        // only server here
        NBTTagCompound tag = KhariumSusNBTHelper.getOrCreateTag(implant);
        if (isHacking(implant) && !player.worldObj.isRemote) {
            sendToCooldown(player, index, implant);
            notifyClient(player, index, implant);
            return;
        }
        if (!isOnCooldown(implant) && !isHacking(implant) && !player.worldObj.isRemote) {

            MovingObjectPosition hitResult = SusWorldHelper.raytraceEntities(player.worldObj, player, getLockDistance(player, index, implant));
            Entity hitEntity = hitResult != null ? hitResult.entityHit : null;
            if (hitResult == null || hitResult.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY || hitEntity == null) {
                return;
            }
            if (!canHackEntity(player, hitEntity, index, implant) || !player.canEntityBeSeen(hitEntity)) {
                return;
            }
            if (getFuelConsumeOnActivation(player, index, implant).tryTakeFuelFromPlayer(player)) {
                tag.setInteger(HACK_TIME_LEFT_NBT, getRequiredHackTime());
                KhariumSusNBTHelper.setUUID(tag, HACK_VICTIM_UUID_NBT, hitEntity.getPersistentID());
                // sync memes
                tag.setInteger(HACK_VICTIM_ID_NBT, hitEntity.getEntityId());

                notifyClient(player, index, implant);
            }
        }
    }

    /**
     * returns the raytrace distance of hack. Note that if infinity returned, Minecraft will be freezed ;)
     *
     * @param player
     * @param index
     * @param implant
     * @return
     */
    public abstract double getLockDistance(EntityPlayer player, int index, ItemStack implant);

    public abstract double getLoseDistance(EntityPlayer player, int index, ItemStack implant);

    public abstract int getTargetTimeLose(EntityPlayer player, int index, ItemStack implant);

    @Override
    public void onPlayerUpdateEvent(LivingEvent.LivingUpdateEvent event, EntityPlayer player, int index, ItemStack implant) {
        NBTTagCompound tag = KhariumSusNBTHelper.getOrCreateTag(implant);
        int hackTime = KhariumSusNBTHelper.getOrCreateInteger(tag, HACK_TIME_LEFT_NBT, 0);
        if (isHacking(implant)) {
            if (!player.worldObj.isRemote) {
                Entity focusedEntityServer = verifyEntityAndCheckForRelock(player, index, implant);
                if (focusedEntityServer == null) {
                    sendToCooldown(player, index, implant);
                    notifyClient(player, index, implant);
                    return;
                }
                if (focusedEntityServer.getEntityId() != tag.getInteger(HACK_VICTIM_ID_NBT)) {
                    tag.setInteger(HACK_VICTIM_ID_NBT, focusedEntityServer.getEntityId());
                    notifyClient(player, index, implant);
                }
            }
            if (!isRelockingTarget(implant)) {
                tag.setInteger(HACK_TIME_LEFT_NBT, hackTime - 1);
                // if after decreasing we are done:
                if (!isHacking(implant)) {
                    Entity focusedEntity = player.worldObj.getEntityByID(tag.getInteger(HACK_VICTIM_ID_NBT));
                    if (focusedEntity != null) {
                        onEntityBeingHacked(player, focusedEntity, index, implant);
                    }
                }
            } else {
                tag.setInteger(TARGET_LOST_TIME_LEFT, tag.getInteger(TARGET_LOST_TIME_LEFT) - 1);
                if (!isRelockingTarget(implant)) {
                    sendToCooldown(player, index, implant);
                    notifyClient(player, index, implant);
                }
            }
        } else {
            if (isOnCooldown(implant)) {
                super.onPlayerUpdateEvent(event, player, index, implant);
            }
        }
    }

    protected Entity verifyEntityAndCheckForRelock(EntityPlayer player, int index, ItemStack implant) {
        NBTTagCompound tag = KhariumSusNBTHelper.getOrCreateTag(implant);
        UUID previousFocusedEntityUUID = KhariumSusNBTHelper.getUUIDOrNull(tag, HACK_VICTIM_UUID_NBT);
        if (previousFocusedEntityUUID == null) {
            return null;
        }

        double radius = getLoseDistance(player, index, implant);
        Entity entity = findEntityInRadius(player, index, implant, previousFocusedEntityUUID, radius);
        if (entity == null || !entity.isEntityAlive()) {
            return null;
        }

        SusVec3 posDelta = SusVec3.getEntityPos(entity).subtract(SusVec3.getEntityPos(player));
        SusVec3 lookVec = new SusVec3(player.getLookVec().xCoord, player.getLookVec().yCoord, player.getLookVec().zCoord);
        double angle = SusVec3.angleBetweenVec3(posDelta, lookVec);

        boolean isTargetAboutToBeLost = !player.canEntityBeSeen(entity) || player.getDistanceToEntity(entity) > getLockDistance(player, index, implant) || angle > (Math.PI / 2);
        if (isTargetAboutToBeLost) {
            if (!isRelockingTarget(implant)) {
                tag.setInteger(TARGET_LOST_TIME_LEFT, getTargetTimeLose(player, index, implant));
                notifyClient(player, index, implant);
            }
            return entity;
        }

        if (isRelockingTarget(implant)) {
            tag.setInteger(TARGET_LOST_TIME_LEFT, 0);
            notifyClient(player, index, implant);
        }

        return entity;
    }

//    private Entity getEntityByUUID(World world, UUID uniqueId) {
//        for (Entity entity : world.enti) {
//            for (Entity entity : chunk.getEntities()) {
//                if (entity.getUniqueId().equals(uniqueId))
//                    return entity;
//            }
//        }
//
//        return null;
//    }

    public Entity findEntityInRadius(EntityPlayer player, int index, ItemStack implant, UUID uuid, double radius) {
        World world = player.worldObj;
        List<Entity> entityList = world.getEntitiesWithinAABBExcludingEntity(
                null,
                AxisAlignedBB.getBoundingBox(
                        player.posX - radius,
                        player.posY - radius,
                        player.posZ - radius,

                        player.posX + radius,
                        player.posY + radius,
                        player.posZ + radius
                )
        );

        for (Entity entity : entityList) {
            if (entity.getPersistentID().equals(uuid)) {
                return entity;
            }
        }
        return null;
    }

    @Override
    public void onRenderWorldLastEventIndividually(RenderWorldLastEvent event, EntityPlayer player, int index, ItemStack implant) {
        if (isHacking(implant)) {
            glPushMatrix();
            Entity victim = getVictimByID(player, index, implant);
            if (victim == null) return;


            SusVec3 ownerPos = SusGraphicHelper.getRenderPos(player, event.partialTicks);
            SusVec3 victimPos = SusGraphicHelper.getRenderPos(victim, event.partialTicks).add(0.0, victim.height / 2, 0.0);
            SusGraphicHelper.translateFromPlayerTo(victimPos, event.partialTicks);
            SusVec3 distanceVector = ownerPos.add(0.0, player.eyeHeight, 0.0).subtract(victimPos);
            SusVec3 lookVec = new SusVec3(-player.getLookVec().xCoord, -player.getLookVec().yCoord, -player.getLookVec().zCoord);

            glPushAttrib(GL_BLEND);
            glPushAttrib(GL_CULL_FACE);
            glPushAttrib(GL_LIGHTING);
            glEnable(GL_BLEND);
            glDisable(GL_CULL_FACE);

            GL11.glDisable(GL11.GL_DEPTH_TEST);


            glDisable(GL_LIGHTING);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            double distanceFactor = distanceVector.length();
            float timeProgress = (getRequiredHackTime() - getHackTimeLeft(implant) + event.partialTicks);
//            double circlesAppearanceProgress = Math.min(1.2, Math.min(1.0, timeProgress / 20.0) * distanceFactor) / 1.3;
            double circlesAppearanceProgress = Math.min(1.0, timeProgress / 20.0) * distanceFactor / 3;

            float alpha = 0.8f;
            if (isRelockingTarget(implant)) {
                alpha = (float) Math.max(0.05, alpha * Math.abs(Math.sin(SusGraphicHelper.getRenderGlobalTime(event.partialTicks) / 7)));
            }
            Color color = new Color(229f / 255f, 0.0f, 0.0f, alpha);


            SusGraphicHelper.makeSystemOrthToVectorAndHandle(
                    lookVec, 0f,
                    () -> {
                        GL11.glRotated(180.0, 2.0, 0.0, 0.0);

                        double translation = 0.3 * circlesAppearanceProgress;
                        GL11.glTranslated(-translation, -translation / 2, 0.0);
//                        SusGraphicHelper.drawGuideArrows();
                        GL11.glScaled(0.014 * circlesAppearanceProgress / 1.3, 0.014 * circlesAppearanceProgress / 1.3, 0.014 * circlesAppearanceProgress / 1.3);
                        Minecraft.getMinecraft().fontRendererObj.drawString("target: " + victim.getCommandSenderName(), 0, 0, color.getRGB());
                        if (victim instanceof EntityLivingBase) {
                            Minecraft.getMinecraft().fontRendererObj.drawString("health: " + ((EntityLivingBase) victim).getHealth(), 0, 10, color.getRGB());
                        }
                    }
            );

            GL11.glColor4f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, alpha);


            for (int i = 0; i < 3; i++) {
                final int copyI = i;
                final int sign = ((i + 1) % 2 == 0) ? 1 : -1;
                SusGraphicHelper.renderTextureOrth(
                        textureOuterCircles.get(i),
                        circlesAppearanceProgress,
                        circlesAppearanceProgress,
                        lookVec, 0,
                        () -> {

                            GL11.glRotated(sign * (((SusGraphicHelper.getRenderGlobalTime(event.partialTicks) + copyI * 25) * (copyI * 2 + 3) / 2) % 360), 0.0, 0.0, 1.0);
                        }
                );
            }
            SusGraphicHelper.renderTextureOrth(
                    textureInnerCircle,
                    circlesAppearanceProgress,
                    circlesAppearanceProgress,
                    lookVec, 0,
                    () -> {
                        GL11.glRotated(-90.0, 0.0, 0.0, 1.0);

                    }
            );

            GL11.glEnable(GL_DEPTH_TEST);
//            GL11.glDepthMask(false);
            glPopAttrib();
            glPopAttrib();
            glPopAttrib();

            glPopMatrix();

        }
    }


//    public Entity getVictimByUUID(EntityPlayer player, int index, ItemStack implant) {
//        NBTTagCompound tagCompound = KhariumSusNBTHelper.getOrCreateTag(implant);
//        if (!tagCompound.hasKey(HACK_VICTIM_UUID_NBT)) return null;
//        WorldServer worldServer = (WorldServer) player.worldObj;
//        worldServer.entit
//        return worldServer.getPlayerEntityByUUID(tagCompound.getInteger(HACK_VICTIM_UUID_NBT));
//    }

    public Entity getVictimByID(EntityPlayer player, int index, ItemStack implant) {
        NBTTagCompound tagCompound = KhariumSusNBTHelper.getOrCreateTag(implant);
        if (!tagCompound.hasKey(HACK_VICTIM_ID_NBT)) return null;
        return player.worldObj.getEntityByID(tagCompound.getInteger(HACK_VICTIM_ID_NBT));
    }


    @Override
    public void sendToCooldown(EntityPlayer player, int index, ItemStack implant) {
        super.sendToCooldown(player, index, implant);
        NBTTagCompound tag = KhariumSusNBTHelper.getOrCreateTag(implant);
        tag.setInteger(HACK_TIME_LEFT_NBT, 0);
        tag.removeTag(HACK_VICTIM_UUID_NBT);
        tag.removeTag(HACK_VICTIM_ID_NBT);
    }

    public boolean isHacking(ItemStack implant) {
        NBTTagCompound tagCompound = KhariumSusNBTHelper.getOrCreateTag(implant);
        return KhariumSusNBTHelper.getOrCreateInteger(tagCompound, HACK_TIME_LEFT_NBT, 0) > 0;
    }

    public int getHackTimeLeft(ItemStack implant) {
        NBTTagCompound tagCompound = KhariumSusNBTHelper.getOrCreateTag(implant);
        return KhariumSusNBTHelper.getOrCreateInteger(tagCompound, HACK_TIME_LEFT_NBT, 0);
    }

    @Override
    public boolean isOnCooldown(ItemStack implant) {
        NBTTagCompound tag = KhariumSusNBTHelper.getOrCreateTag(implant);
        return KhariumSusNBTHelper.getOrCreateInteger(tag, COOLDOWN_NBT, 0) != 0;
    }

    public boolean isRelockingTarget(ItemStack implant) {
        NBTTagCompound tag = KhariumSusNBTHelper.getOrCreateTag(implant);
        return KhariumSusNBTHelper.getOrCreateInteger(tag, TARGET_LOST_TIME_LEFT, 0) > 0;
    }

    public int getTimeBeforeTargetLost(ItemStack implant) {
        NBTTagCompound tag = KhariumSusNBTHelper.getOrCreateTag(implant);
        return KhariumSusNBTHelper.getOrCreateInteger(tag, TARGET_LOST_TIME_LEFT, 0);
    }


}

