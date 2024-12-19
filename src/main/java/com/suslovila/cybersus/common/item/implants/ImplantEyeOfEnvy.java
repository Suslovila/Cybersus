package com.suslovila.cybersus.common.item.implants;

import baubles.api.BaublesApi;
import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.api.fuel.FuelComposite;
import com.suslovila.cybersus.api.fuel.FuelVariation;
import com.suslovila.cybersus.api.fuel.impl.FuelEmpty;
import com.suslovila.cybersus.api.implants.ImplantType;
import com.suslovila.cybersus.api.implants.ability.Ability;
import com.suslovila.cybersus.api.implants.ability.AbilityHack;
import com.suslovila.cybersus.client.gui.CybersusGui;
import com.suslovila.cybersus.utils.CollectionUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ImplantEyeOfEnvy extends ItemCybersusImplant {
    public static final String name = "eye_of_envy";
    public static final ArrayList<Ability> abilities = new ArrayList<>();

    public ImplantEyeOfEnvy() {
        super(ImplantType.OCULAR_SYSTEM);

    }

    @Override
    public List<Ability> getAbilities(EntityPlayer player, int index, ItemStack implant) {
        return abilities;
    }

    static {
        abilities.add(new AbilityHack("look_into_soul") {
            @Override
            public int getRequiredHackTime() {
                return 30;
            }

            @Override
            public double getLockDistance(EntityPlayer player, int index, ItemStack implant) {
                return 30;
            }

            @Override
            public double getLoseDistance(EntityPlayer player, int index, ItemStack implant) {
                return 60;
            }

            @Override
            public int getTargetTimeLose(EntityPlayer player, int index, ItemStack implant) {
                return 80;
            }

            @Override
            public FuelComposite getFuelConsumeOnActivation(EntityPlayer player, int index, ItemStack implant) {
//                FuelVariation fuelKit = new FuelVariation(new ArrayList<>());
//                if(Cybersus.forbiddenMagicLoaded) {
//                    fuelKit.fuelComposites.add(new FuelComposite(CollectionUtils.arrayListOf(new FuelEssentia(new AspectList().add(DarkAspects.WRATH, 1)))));
//                }
//                fuelKit.fuelComposites.add(new FuelComposite(CollectionUtils.arrayListOf(new FuelEssentia(new AspectList().add(Aspect.ENTROPY, 1)))));

//                fuelKit.fuelComposites.add(new FuelComposite(CollectionUtils.arrayListOf(FuelEmpty.INSTANCE)));
                return FuelComposite.EMPTY;
            }

            @Override
            public int getCooldownTotal(EntityPlayer player, int index, ItemStack implant) {
                return 60;
            }

            @Override
            public void hackEntity(EntityPlayer hacker, Entity victim, int slotIndex, ItemStack implant) {
                if (victim instanceof EntityPlayer) {
                    hacker.openGui(Cybersus.instance, CybersusGui.BAUBLES_ENVY.ordinal(), hacker.worldObj, (int)hacker.posX, (int)hacker.posY, (int)hacker.posZ);

//                    (hacker).displayGUIChest((BaublesApi.getBaubles((EntityPlayer)victim)));

                }
            }
        });
    }


    @Override
    public String getName() {
        return name;
    }
}
