package com.suslovila.cybersus.common.item.implants;

import com.suslovila.cybersus.api.fuel.FuelComposite;
import com.suslovila.cybersus.api.fuel.impl.fuel.essentia.FuelEssentia;
import com.suslovila.cybersus.api.implants.ImplantType;
import com.suslovila.cybersus.api.implants.ability.Ability;
import com.suslovila.cybersus.api.implants.ability.AbilityHack;
import com.suslovila.cybersus.api.implants.ability.AbilityInstant;
import com.suslovila.cybersus.common.processes.ProcessGravityTrap;
import com.suslovila.cybersus.common.processes.ProcessPortal;
import com.suslovila.cybersus.extendedData.CustomWorldData;
import com.suslovila.cybersus.research.CybersusAspect;
import com.suslovila.cybersus.utils.KhariumSusNBTHelper;
import com.suslovila.cybersus.utils.SusVec3;
import com.suslovila.cybersus.utils.SusWorldHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

import java.util.ArrayList;
import java.util.List;

public class ImplantPortalPlacer extends ItemCybersusImplant {
    public static final ArrayList<Ability> abilities = new ArrayList<>();

    public ImplantPortalPlacer() {
        super(ImplantType.HAND);

    }

    @Override
    public String getName() {
        return "portal_placer";
    }

    @Override
    public List<Ability> getAbilities(EntityPlayer player, int index, ItemStack implant) {
        return abilities;
    }

    static {
        abilities.add(new AbilityInstant("place_portal") {


            @Override
            protected void onActivated(EntityPlayer player, int index, ItemStack implant) {
                if (player.worldObj.isRemote) return;

//                SusVec3 facingVec = new SusVec3(player.getLookVec()).normalize();
//                double maxDistance = getMaxReachDistance(player, index, implant);
//                MovingObjectPosition hitMOP = SusWorldHelper.raytraceBlocks(player.worldObj, player, false, maxDistance);
//                SusVec3 currentPos = null;
//                if (hitMOP != null) {
//                    currentPos = new SusVec3(hitMOP.blockX, hitMOP.blockY + 1, hitMOP.blockZ).add(0.0, 0.3, 0.0);
//                } else {
//                    currentPos = SusVec3.getEntityPos(player)
//                            .add(facingVec.scale(maxDistance));
//                }

                SusVec3 currentPos = SusVec3.getEntityPos(player);
                    if(KhariumSusNBTHelper.getOrCreateTag(implant).hasKey("portal_first_pos")) {
                        NBTTagCompound tagWithPreviousPos = KhariumSusNBTHelper.getOrCreateTag(implant).getCompoundTag("portal_first_pos");
                        SusVec3 firstPosition = SusVec3.readFrom(tagWithPreviousPos);
                        int dimension = tagWithPreviousPos.getInteger("dimensionId");
                        ProcessPortal processPortalSecond = new ProcessPortal(currentPos, 20* 2 * 600, dimension, firstPosition);
                        ProcessPortal processPortalFirst = new ProcessPortal(firstPosition, 20 * 2 * 600, player.worldObj.provider.dimensionId, currentPos);

                        CustomWorldData.getCustomData(player.worldObj).addProcess(processPortalSecond);
                        CustomWorldData.syncProcess(processPortalSecond);

                        CustomWorldData.getCustomData(DimensionManager.getWorld(dimension)).addProcess(processPortalFirst);
                        CustomWorldData.syncProcess(processPortalFirst);


                        KhariumSusNBTHelper.getOrCreateTag(implant).removeTag("portal_first_pos");
                        sendToCooldown(player, index, implant);
                        notifyClient(player, index, implant);
                    }
                    else {
                        NBTTagCompound tagWithPreviousPos = new NBTTagCompound();
                        tagWithPreviousPos.setInteger("dimensionId", player.worldObj.provider.dimensionId);
                        currentPos.writeTo(tagWithPreviousPos);
                        KhariumSusNBTHelper.getOrCreateTag(implant).setTag("portal_first_pos", tagWithPreviousPos);

                    }



            }

            @Override
            public FuelComposite getFuelConsumeOnActivation(EntityPlayer player, int index, ItemStack implant) {
                return FuelComposite.allRequired(new FuelEssentia(new AspectList().add(CybersusAspect.GRAVITAS, 64).add(CybersusAspect.DIMENSIO, 16)));
//                return FuelComposite.EMPTY;
            }

            @Override
            public int getCooldownTotal(EntityPlayer player, int index, ItemStack implant) {
                return 15 * 20;
            }


            public double getMaxReachDistance(EntityPlayer player, int index, ItemStack implant) {
                return 50.0f;
            }


            public void onRenderWorldLastEventIndividually(RenderWorldLastEvent event, EntityPlayer player, int index, ItemStack implant) {
                if (KhariumSusNBTHelper.getOrCreateTag(implant).hasKey("portal_first_pos")) {
                    NBTTagCompound tagWithPreviousPos = KhariumSusNBTHelper.getOrCreateTag(implant).getCompoundTag("portal_first_pos");
                    SusVec3 firstPosition = SusVec3.readFrom(tagWithPreviousPos);
                    int dimension = tagWithPreviousPos.getInteger("dimensionId");
                    if(player.worldObj.provider.dimensionId == dimension) {

                    }
                }
            }
        });


//        abilities.add(new AbilityInstant("place_portal_2") {
//
//
//            @Override
//            protected void onActivated(EntityPlayer player, int index, ItemStack implant) {
//                if (player.worldObj.isRemote) return;
//
//                SusVec3 facingVec = new SusVec3(player.getLookVec()).normalize();
//                double maxDistance = getMaxReachDistance(player, index, implant);
//                MovingObjectPosition hitMOP = SusWorldHelper.raytraceBlocks(player.worldObj, player, false, maxDistance);
//                if (hitMOP != null) {
//                    ProcessGravityTrap processGravityTrap = new ProcessGravityTrap(new SusVec3(hitMOP.blockX, hitMOP.blockY + 1, hitMOP.blockZ), 20 * 10, 10.0f);
//                    CustomWorldData.getCustomData(player.worldObj).addProcess(processGravityTrap);
//                    CustomWorldData.syncProcess(processGravityTrap);
//                    sendToCooldown(player, index, implant);
//                    notifyClient(player, index, implant);
//                } else {
//                    SusVec3 position = SusVec3.getEntityPos(player)
//                            .add(facingVec.scale(maxDistance));
//                    ProcessGravityTrap processGravityTrap = new ProcessGravityTrap(position, 20 * 10, 10.0f);
//                    CustomWorldData.getCustomData(player.worldObj).addProcess(processGravityTrap);
//                    CustomWorldData.syncProcess(processGravityTrap);
//                    sendToCooldown(player, index, implant);
//                    notifyClient(player, index, implant);
//                }
//            }
//
//            public double getMaxReachDistance(EntityPlayer player, int index, ItemStack implant) {
//                return 50.0f;
//            }
//
//
//            @Override
//            public FuelComposite getFuelConsumeOnActivation(EntityPlayer player, int index, ItemStack implant) {
//                return FuelComposite.allRequired(new FuelEssentia(new AspectList().add(CybersusAspect.GRAVITAS, 64).add(Aspect.TRAP, 16)));
////                return FuelComposite.EMPTY;
//            }
//
//            @Override
//            public int getCooldownTotal(EntityPlayer player, int index, ItemStack implant) {
//                return 15 * 20;
//            }
//
//
//        });
    }
}