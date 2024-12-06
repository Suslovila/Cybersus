package com.suslovila.cybersus.common.item.implants;

import com.suslovila.cybersus.api.fuel.FuelComposite;
import com.suslovila.cybersus.api.implants.ImplantType;
import com.suslovila.cybersus.api.implants.ability.Ability;
import com.suslovila.cybersus.api.implants.ability.AbilityHack;
import com.suslovila.cybersus.common.processes.illusion.ProcessIllusion;
import com.suslovila.cybersus.extendedData.CustomWorldData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ImplantIllusionGenerator extends ItemCybersusImplant {
    public static final String name = "mind_breaker";
    public static final ArrayList<Ability> abilities = new ArrayList<>();

    static {
        abilities.add(new AbilityHack("illusions") {

            @Override
            public FuelComposite getFuelConsumeOnActivation(EntityPlayer player, int index, ItemStack implant) {
                return FuelComposite.EMPTY;
            }

            @Override
            public int getCooldownTotal(EntityPlayer player, int index, ItemStack implant) {
                return 20;
            }

            @Override
            public int getRequiredHackTime() {
                return 20;
            }

            @Override
            public void hackEntity(EntityPlayer hacker, Entity victim, int slotIndex, ItemStack implant) {
                if(!(victim instanceof EntityPlayerMP)) return;
                ProcessIllusion processIllusion = new ProcessIllusion(hacker, (EntityPlayerMP) victim, 20 * 20);
                CustomWorldData.syncProcess(processIllusion, (EntityPlayerMP) victim);
            }

            @Override
            public double getLockDistance(EntityPlayer player, int index, ItemStack implant) {
                return 10;
            }

            @Override
            public double getLoseDistance(EntityPlayer player, int index, ItemStack implant) {
                return 20;
            }

            @Override
            public int getTargetTimeLose(EntityPlayer player, int index, ItemStack implant) {
                return 20;
            }

            @Override
            public boolean canHackEntity(EntityPlayer hacker, Entity victim, int slotIndex, ItemStack implant) {
                return victim instanceof EntityPlayer;
            }
        });
    }

    public ImplantIllusionGenerator() {
        super(ImplantType.OCULAR_SYSTEM);
    }

    @Override
    public List<Ability> getAbilities(EntityPlayer player, int index, ItemStack implant) {
        return abilities;
    }

    @Override
    public String getName() {
        return "illusion_generator";
    }
}

