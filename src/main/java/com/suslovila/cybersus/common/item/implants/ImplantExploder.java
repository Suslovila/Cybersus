package com.suslovila.cybersus.common.item.implants;

import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.api.fuel.FuelComposite;
import com.suslovila.cybersus.api.fuel.FuelVariation;
import com.suslovila.cybersus.api.fuel.impl.FuelEmpty;
import com.suslovila.cybersus.api.fuel.impl.fuel.essentia.FuelEssentia;
import com.suslovila.cybersus.api.implants.ImplantType;
import com.suslovila.cybersus.api.implants.ability.Ability;
import com.suslovila.cybersus.api.implants.ability.AbilityHack;
import com.suslovila.cybersus.utils.SusCollectionUtils;
import fox.spiteful.forbidden.DarkAspects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

import java.util.ArrayList;
import java.util.List;

public class ImplantExploder extends ItemCybersusImplant {
    public static final String name = "exploser";
    public static final ArrayList<Ability> abilities = new ArrayList<>();

    public ImplantExploder() {
        super(ImplantType.BRAIN);

    }

    @Override
    public List<Ability> getAbilities(EntityPlayer player, int index, ItemStack implant) {
        return abilities;
    }

    static {
        abilities.add(new AbilityHack("explosion") {
            @Override
            public int getRequiredHackTime() {
                return 20 * 5;
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
                return 20 * 3;
            }

            @Override
            public FuelComposite getFuelConsumeOnActivation(EntityPlayer player, int index, ItemStack implant) {
                FuelComposite composite = FuelComposite.allRequired();
                FuelVariation fuelVariation = new FuelVariation();
                if (Cybersus.forbiddenMagicLoaded) {
                    fuelVariation.addSimpleVariant(new FuelEssentia(new AspectList().add(DarkAspects.WRATH, 256)));
                }
                composite.addRequiredFuel(new FuelEssentia(new AspectList().add(Aspect.WEAPON, 256).add(Aspect.FIRE, 256)));
                composite.fuelVariations.add(fuelVariation);


                return composite;
            }

            @Override
            public int getCooldownTotal(EntityPlayer player, int index, ItemStack implant) {
                return 20 * 20;
            }

            @Override
            public void hackEntity(EntityPlayer hacker, Entity victim, int slotIndex, ItemStack implant) {
                hacker.worldObj.createExplosion(hacker, victim.posX, victim.posY, victim.posZ, 4.0f, true);
                if (victim instanceof EntityLivingBase) {
                    EntityLivingBase livingBase = (EntityLivingBase) victim;
                    victim.attackEntityFrom(DamageSource.outOfWorld, livingBase.getMaxHealth() / 2);
                }
            }
        });
    }


    @Override
    public String getName() {
        return name;
    }
}
