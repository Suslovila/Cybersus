package com.suslovila.cybersus.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.List;

public class SusWorldHelper {
    public static SusVec3 getPosDouble(TileEntity tileEntity) {
        return new SusVec3(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
    }


    public static void teleportToLocation(World world, double posX, double posY, double posZ, int dimension, Entity entity, boolean presetPosition) {

        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)entity;
            if (entity.dimension != dimension) {
                if (presetPosition) {
                    player.setPosition(posX, posY, posZ);
                }

                travelToDimension(player, dimension);
            }

            player.setPositionAndUpdate(posX, posY, posZ);
        } else if (entity instanceof EntityLiving) {
            if (entity.dimension != dimension) {
                travelToDimension(entity, dimension, posX, posY, posZ);
            } else {
                entity.setLocationAndAngles(posX, posY, posZ, entity.rotationYaw, entity.rotationPitch);
            }
        } else if (entity.dimension != dimension) {
            travelToDimension(entity, dimension, posX, posY, posZ);
        } else {
            entity.setLocationAndAngles(posX, posY, posZ, entity.rotationYaw, entity.rotationPitch);
        }


    }

    public static void travelToDimension(EntityPlayer player, int dimension) {
        if (!player.worldObj.isRemote & player instanceof EntityPlayerMP) {
            MinecraftServer server = MinecraftServer.getServer();
            WorldServer newWorldServer = server.worldServerForDimension(dimension);
            server.getConfigurationManager().transferPlayerToDimension((EntityPlayerMP)player, dimension, new Teleporter2(newWorldServer));
        }

    }

    private static Entity travelToDimension(Entity entityToTeleport, int targetDimensionId, double posX, double posY, double posZ) {
        if (!entityToTeleport.worldObj.isRemote && !entityToTeleport.isDead) {
            entityToTeleport.worldObj.theProfiler.startSection("changeDimension");
            MinecraftServer minecraftserver = MinecraftServer.getServer();
            int currentDimensionId = entityToTeleport.dimension;
            WorldServer worldserver = minecraftserver.worldServerForDimension(currentDimensionId);
            WorldServer worldserver1 = minecraftserver.worldServerForDimension(targetDimensionId);
            entityToTeleport.dimension = targetDimensionId;
            if (currentDimensionId == 1 && targetDimensionId == 1) {
                worldserver1 = minecraftserver.worldServerForDimension(0);
                entityToTeleport.dimension = 0;
            }

            entityToTeleport.worldObj.removeEntity(entityToTeleport);
            entityToTeleport.isDead = false;
            entityToTeleport.worldObj.theProfiler.startSection("reposition");
            minecraftserver.getConfigurationManager().transferEntityToWorld(entityToTeleport, currentDimensionId, worldserver, worldserver1, new Teleporter2(worldserver1));
            entityToTeleport.worldObj.theProfiler.endStartSection("reloading");
            Entity entity = EntityList.createEntityByName(EntityList.getEntityString(entityToTeleport), worldserver1);
            if (entity != null) {
                entity.copyDataFrom(entityToTeleport, true);
                entity.setLocationAndAngles(posX, posY, posZ, entity.rotationYaw, entity.rotationPitch);
                worldserver1.spawnEntityInWorld(entity);
            }

            entityToTeleport.isDead = true;
            entityToTeleport.worldObj.theProfiler.endSection();
            worldserver.resetUpdateEntityTick();
            worldserver1.resetUpdateEntityTick();
            entityToTeleport.worldObj.theProfiler.endSection();
            return entity;
        } else {
            return null;
        }
    }

    private static class Teleporter2 extends Teleporter {
        public Teleporter2(WorldServer server) {
            super(server);
        }

        public boolean makePortal(Entity par1Entity) {
            return false;
        }

        public boolean placeInExistingPortal(Entity par1Entity, double par2, double par4, double par6, float par8) {
            return false;
        }

        public void placeInPortal(Entity par1Entity, double par2, double par4, double par6, float par8) {
        }

        public void removeStalePortalLocations(long par1) {
        }
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
            entity.fallDistance = 0.0f;

        }
    }

    public static void teleportEntity(Entity entity, SusVec3 pos) {
        if (entity instanceof EntityPlayerMP) {
            entity.fallDistance = 0.0f;
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

    public static MovingObjectPosition raytraceBlocks(
            World world,
            EntityPlayer player,
            SusVec3 displacementVector,
            boolean collisionFlag,
            double reachDistance
    ) {
        Vec3 playerPosition = Vec3.createVectorHelper(player.posX, player.posY + player.getEyeHeight(), player.posZ);
        Vec3 playerViewOffset = Vec3.createVectorHelper(
                playerPosition.xCoord + displacementVector.x * reachDistance,
                playerPosition.yCoord + displacementVector.y * reachDistance,
                playerPosition.zCoord + displacementVector.z * reachDistance
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


