package com.suslovila.cybersus.common.item.implants.reactionIncreaser;

import com.suslovila.cybersus.api.fuel.FuelComposite;
import com.suslovila.cybersus.api.fuel.impl.fuel.essentia.FuelEssentia;
import com.suslovila.cybersus.api.implants.ImplantType;
import com.suslovila.cybersus.api.implants.ability.Ability;
import com.suslovila.cybersus.api.implants.ability.AbilityInstant;
import com.suslovila.cybersus.client.clientProcess.processes.ProcessPlayerAccelerationEffect;
import com.suslovila.cybersus.common.item.implants.ItemCybersusImplant;
import com.suslovila.cybersus.extendedData.CustomWorldData;
import com.suslovila.cybersus.utils.SusVec3;
import com.suslovila.cybersus.utils.SusWorldHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

import java.util.ArrayList;
import java.util.List;

public class ImplantReactionIncreaser extends ItemCybersusImplant {
    public static final ArrayList<Ability> abilities = new ArrayList<>();

    static {
        abilities.add(new AbilityAcceleration("acceleration") {


            @Override
            public FuelComposite getFuelConsumeOnActivation(EntityPlayer player, int index, ItemStack implant) {
                FuelComposite fuelComposite = FuelComposite.allRequired();
                fuelComposite.addRequiredFuel(new FuelEssentia(new AspectList().add(Aspect.MOTION, 16).add(Aspect.ENERGY, 16)));

                return fuelComposite;
            }

            @Override
            public int getCooldownTotal(EntityPlayer player, int index, ItemStack implant) {
                return 7 * 1;
            }

        });
    }

    public ImplantReactionIncreaser() {
        super(ImplantType.NERVOUS_SYSTEM);
    }

    @Override
    public List<Ability> getAbilities(EntityPlayer player, int index, ItemStack implant) {
        return abilities;
    }

    @Override
    public String getName() {
        return "reaction_increaser";
    }

}

