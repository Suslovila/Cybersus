package com.suslovila.cybersus.common.item.implants;

import com.suslovila.cybersus.api.fuel.FuelComposite;
import com.suslovila.cybersus.api.fuel.impl.fuel.essentia.FuelEssentia;
import com.suslovila.cybersus.api.implants.ImplantType;
import com.suslovila.cybersus.api.implants.ability.Ability;
import com.suslovila.cybersus.api.implants.ability.AbilityInstant;
import com.suslovila.cybersus.api.implants.ability.AbilityPassive;
import com.suslovila.cybersus.common.processes.ProcessGravityTrap;
import com.suslovila.cybersus.extendedData.CustomWorldData;
import com.suslovila.cybersus.research.CybersusAspect;
import com.suslovila.cybersus.utils.SusVec3;
import com.suslovila.cybersus.utils.SusWorldHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import thaumcraft.api.aspects.AspectList;

import java.util.ArrayList;
import java.util.List;

public class ImplantGravityIcreaser extends ItemCybersusImplant {
    public static final ArrayList<Ability> abilities = new ArrayList<>();

    public ImplantGravityIcreaser() {
        super(ImplantType.HAND);

    }

    @Override
    public String getName() {
        return "gravity_increaser";
    }

    @Override
    public List<Ability> getAbilities(EntityPlayer player, int index, ItemStack implant) {
        return abilities;
    }

    static {
        abilities.add(new AbilityInstant("gravity_trap") {


            @Override
            protected void onActivated(EntityPlayer player, int index, ItemStack implant) {
                if (player.worldObj.isRemote) return;

                SusVec3 facingVec = new SusVec3(player.getLookVec()).normalize();
                double maxDistance = getMaxReachDistance(player, index, implant);
                MovingObjectPosition hitMOP = SusWorldHelper.raytraceBlocks(player.worldObj, player, false, maxDistance);
                if (hitMOP != null) {
                    ProcessGravityTrap processGravityTrap = new ProcessGravityTrap(new SusVec3(hitMOP.blockX, hitMOP.blockY + 1, hitMOP.blockZ), 20 * 10);
                    CustomWorldData.getCustomData(player.worldObj).addProcess(processGravityTrap);
                    CustomWorldData.syncProcess(processGravityTrap);
                    sendToCooldown(player, index, implant);
                    notifyClient(player, index, implant);
                } else {
                    SusVec3 position = SusVec3.getEntityPos(player)
                            .add(facingVec.scale(maxDistance));
                    ProcessGravityTrap processGravityTrap = new ProcessGravityTrap(position, 20 * 10);
                    CustomWorldData.getCustomData(player.worldObj).addProcess(processGravityTrap);
                    CustomWorldData.syncProcess(processGravityTrap);
                    sendToCooldown(player, index, implant);
                    notifyClient(player, index, implant);
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

            @Override
            public void onPlayerHurtEventIfAttacker(LivingHurtEvent event, EntityPlayer player, int index, ItemStack implant) {
//                if (player.worldObj.isRemote) return;
//                ProcessGravityTrap processGravityTrap = new ProcessGravityTrap(SusVec3.getEntityPos(event.entityLiving).add(new SusVec3(0.0, event.entityLiving.height / 2, 0.0)), 20 * 1000);
//                CustomWorldData.getCustomData(player.worldObj).addProcess(processGravityTrap);
//                CustomWorldData.syncProcess(processGravityTrap);
////                FXBrokenSoul fxBrokenSoul = new FXBrokenSoul(player.worldObj, player.posX, player.posY, player.posZ, 0.0, 0.0, 0.0, 1000, 0.2f, true);
////                ParticleEngine.instance.addEffect(player.worldObj, fxBrokenSoul);
            }

            public double getMaxReachDistance(EntityPlayer player, int index, ItemStack implant) {
                return 50.0f;
            }
        });
    }
}