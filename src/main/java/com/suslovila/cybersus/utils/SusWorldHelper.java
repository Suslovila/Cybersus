package com.suslovila.cybersus.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.List;

public class SusWorldHelper {
    public static SusVec3 getPosDouble(TileEntity tileEntity) {
        return new SusVec3(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
    }

    public static void teleportEntity(Entity entity, MovingObjectPosition hitMOP) {
        if (entity instanceof EntityPlayerMP && !((EntityPlayerMP) entity).playerNetServerHandler.netManager.isChannelOpen())
            return;
        if (hitMOP != null) {
            double hitx;
            double hity;
            double hitz;
            switch (hitMOP.typeOfHit) {
                case BLOCK:
                    hitx = hitMOP.hitVec.xCoord;
                    hity = hitMOP.hitVec.yCoord;
                    hitz = hitMOP.hitVec.zCoord;
                    switch (hitMOP.sideHit) {
                        case 0:
                            hity -= 2.0;
                            break;
                        case 2:
                            hitz -= 0.5;
                            break;
                        case 3:
                            hitz += 0.5;
                            break;
                        case 4:
                            hitx -= 0.5;
                            break;
                        case 5:
                            hitx += 0.5;
                            break;
                    }
                    entity.fallDistance = 0.0f;
                    entity.setPosition(hitx, hity, hitz);
                    break;

                default:
                    entity.setPosition(
                            hitMOP.hitVec.xCoord,
                            hitMOP.hitVec.yCoord,
                            hitMOP.hitVec.zCoord
                    );
                    break;
            }
        }
    }

    public static void teleportEntity(Entity entity, SusVec3 pos) {
        if (entity instanceof EntityPlayerMP) {
            if (((EntityPlayerMP) entity).playerNetServerHandler.netManager.isChannelOpen()) {
                ((EntityPlayerMP) entity).setPositionAndUpdate(pos.x, pos.y, pos.z);
                return;
            }
        }
        entity.setPosition(pos.x, pos.y, pos.z);
    }


    public static MovingObjectPosition raytraceBlocks(
            World world,
            EntityPlayer player,
            boolean collisionFlag,
            double reachDistance
    ) {
        Vec3 playerPosition = Vec3.createVectorHelper(player.posX, player.posY + player.getEyeHeight(), player.posZ);
        Vec3 playerLook = player.getLookVec();
        Vec3 playerViewOffset = Vec3.createVectorHelper(
                playerPosition.xCoord + playerLook.xCoord * reachDistance,
                playerPosition.yCoord + playerLook.yCoord * reachDistance,
                playerPosition.zCoord + playerLook.zCoord * reachDistance
        );
        return world.rayTraceBlocks(playerPosition, playerViewOffset, collisionFlag, !collisionFlag, false);
    }

    public static MovingObjectPosition raytraceEntities(
            World world,
            EntityPlayer player,
            double reachDistance
    ) {
        MovingObjectPosition pickedEntity = null;
        Vec3 playerPosition = Vec3.createVectorHelper(player.posX, player.posY + player.getEyeHeight(), player.posZ);
        Vec3 playerLook = player.getLookVec();
        Vec3 playerViewOffset = Vec3.createVectorHelper(
                playerPosition.xCoord + playerLook.xCoord * reachDistance,
                playerPosition.yCoord + playerLook.yCoord * reachDistance,
                playerPosition.zCoord + playerLook.zCoord * reachDistance
        );
        double playerBorder = 1.1 * reachDistance;
        AxisAlignedBB boxToScan = player.boundingBox.expand(playerBorder, playerBorder, playerBorder);
        List<Entity> entitiesHit = world.getEntitiesWithinAABBExcludingEntity(player, boxToScan);
        double closestEntity = reachDistance;

        if (entitiesHit == null || entitiesHit.isEmpty()) {
            return null;
        }

        for (Entity entityHit : entitiesHit) {
            if (entityHit.canBeCollidedWith() && entityHit.boundingBox != null) {
                double border = entityHit.getCollisionBorderSize();
                AxisAlignedBB aabb = entityHit.boundingBox.expand(border, border, border);
                MovingObjectPosition hitMOP = aabb.calculateIntercept(playerPosition, playerViewOffset);
                if (hitMOP != null) {
                    if (aabb.isVecInside(playerPosition)) {
                        if (0.0 < closestEntity || closestEntity == 0.0) {
                            pickedEntity = new MovingObjectPosition(entityHit);
                            if (pickedEntity != null) {
                                pickedEntity.hitVec = hitMOP.hitVec;
                                closestEntity = 0.0;
                            }
                        }
                        continue;
                    }
                    double distance = playerPosition.distanceTo(hitMOP.hitVec);
                    if (distance < closestEntity || closestEntity == 0.0) {
                        pickedEntity = new MovingObjectPosition(entityHit);
                        pickedEntity.hitVec = hitMOP.hitVec;
                        closestEntity = distance;
                    }
                }
            }
        }
        return pickedEntity;
    }

    public static AxisAlignedBB boundingBoxFromTwoVec(SusVec3 pos1, SusVec3 pos2) {
        return AxisAlignedBB.getBoundingBox(
                Math.min(pos1.x, pos2.x),
                Math.min(pos1.y, pos2.y),
                Math.min(pos1.z, pos2.z),
                Math.max(pos1.x, pos2.x),
                Math.max(pos1.y, pos2.y),
                Math.max(pos1.z, pos2.z)
        );
    }
}


