package com.suslovila.cybersus.common.item.implants.reactionIncreaser;

import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.api.fuel.FuelComposite;
import com.suslovila.cybersus.api.implants.ability.AbilityInstant;
import com.suslovila.cybersus.client.clientProcess.processes.ProcessPlayerAccelerationEffect;
import com.suslovila.cybersus.extendedData.CustomWorldData;
import com.suslovila.cybersus.utils.SusUtils;
import com.suslovila.cybersus.utils.SusVec3;
import com.suslovila.cybersus.utils.SusWorldHelper;
import jdk.jfr.internal.test.WhiteBox;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import static com.emoniph.witchery.infusion.infusions.InfusionOtherwhere.doCustomRayTrace;

public class AbilityAcceleration extends AbilityInstant {

    public AbilityAcceleration(String name) {
        super(name);
    }

    @Override
    public void onEnableButtonClicked(EntityPlayer player, int index, ItemStack implant) {

    }
    
    @Override
    protected void onActivated(EntityPlayer player, int index, ItemStack implant) {

    }

    @Override
    public FuelComposite getFuelConsumeOnActivation(EntityPlayer player, int index, ItemStack implant) {
        return null;
    }

    @Override
    public int getCooldownTotal(EntityPlayer player, int index, ItemStack implant) {
        return 0;
    }

    public void onEnableButtonClickedCustom(EntityPlayer player, int index, ItemStack implant, SusVec3 displacementDestination) {
        if (isOnCooldown(implant)) return;
        FuelComposite requiredFuel = getFuelConsumeOnActivation(player, index, implant);
        if(displacementDestination.x == 0 && displacementDestination.y == 0 && displacementDestination.z == 0) return;
        if (requiredFuel.tryTakeFuelFromPlayer(player)) {
            
            if(player.worldObj.isRemote) return;

            double dashLength = getDashDistance(player, index, implant);
            SusVec3 previousPosition = SusVec3.getEntityPos(player);
            SusVec3 displacementDestinationResult = displacementDestination.normalize();

            MovingObjectPosition hitMOP = SusWorldHelper.raytraceBlocks(player.worldObj, player, displacementDestinationResult, false, dashLength);

            SusVec3 endPosition;
            if (hitMOP != null) {
                // If we hit something, teleport the player to the hit position
                 this.teleportEntity(player, hitMOP);
                endPosition = new SusVec3(hitMOP.hitVec.xCoord, hitMOP.hitVec.yCoord, hitMOP.hitVec.zCoord);
            } else {
                // Otherwise, compute a fallback position at max distance in the facing direction
                SusVec3 targetPos = SusVec3.getEntityPos(player)
                        .add(displacementDestinationResult.scale(dashLength));

                SusWorldHelper.teleportEntity(player, targetPos);
                endPosition = new SusVec3(targetPos.x, targetPos.y, targetPos.z);
            }

            double resultLength = endPosition.subtract(previousPosition).length();
            for (int i = 0; i < resultLength * 2; i++) {

                SusVec3 processPosition = previousPosition.add(displacementDestinationResult.scale(i / 2.0));
                double color = i / resultLength / 2.0;
                int duration = (int) (10 * i / resultLength / 2);
                ProcessPlayerAccelerationEffect processPlayerCopy = new ProcessPlayerAccelerationEffect(player, processPosition, duration, color);
                CustomWorldData.syncProcess(processPlayerCopy, player.dimension);

                World world = player.worldObj;
                if (world instanceof WorldServer) {
                    WorldServer worldServer = (WorldServer) world;
                    worldServer.playSoundAtEntity(
                            player,
                            Cybersus.MOD_ID + ":sandevistan_blast",
                            0.3f,
                            1.4f + worldServer.rand.nextFloat() * 0.2f
                    );

                }
            }

            sendToCooldown(player, index, implant);
            notifyClient(player, index, implant);

        }
    }

    public static void teleportEntity(EntityPlayer entityPlayer, MovingObjectPosition hitMOP) {
        if (hitMOP != null && entityPlayer instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP)entityPlayer;
            if (!isConnectionClosed(player)) {
                switch (hitMOP.typeOfHit) {
                    case ENTITY:
                        player.setPositionAndUpdate(hitMOP.hitVec.xCoord, hitMOP.hitVec.yCoord, hitMOP.hitVec.zCoord);
                        break;
                    case BLOCK:
                        double hitx = hitMOP.hitVec.xCoord;
                        double hity = hitMOP.hitVec.yCoord;
                        double hitz = hitMOP.hitVec.zCoord;
                        switch (hitMOP.sideHit) {
                            case 0:
                                hity -= (double)2.0F;
                            case 1:
                            default:
                                break;
                            case 2:
                                hitz -= (double)0.5F;
                                break;
                            case 3:
                                hitz += (double)0.5F;
                                break;
                            case 4:
                                hitx -= (double)0.5F;
                                break;
                            case 5:
                                hitx += (double)0.5F;
                                break;
                        }

//                        if(entityPlayer.worldObj.isAirBlock((int)hitx, (int) hity, (int) hitz)) {
//                            hity += 1.0;
//                        }
                        player.fallDistance = 0.0F;
                        player.setPositionAndUpdate(hitx, hity, hitz);
                }
            }
        }

    }


    private static boolean isConnectionClosed(EntityPlayerMP player) {
        return !player.playerNetServerHandler.netManager.isChannelOpen();
    }


    public double getDashDistance(EntityPlayer player, int index, ItemStack implant) {
        return 20;
    }
    
}