package com.suslovila.cybersus.common.item.implants.witchery;

import com.emoniph.witchery.Witchery;
import com.emoniph.witchery.WitcheryRecipes;
import com.emoniph.witchery.dimension.WorldProviderDreamWorld;
import com.emoniph.witchery.dimension.WorldProviderTorment;
import com.emoniph.witchery.infusion.Infusion;
import com.emoniph.witchery.infusion.infusions.InfusionOtherwhere;
import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.api.fuel.FuelComposite;
import com.suslovila.cybersus.api.fuel.FuelVariation;
import com.suslovila.cybersus.api.fuel.impl.fuel.FuelInfusion;
import com.suslovila.cybersus.api.fuel.impl.fuel.essentia.FuelEssentia;
import com.suslovila.cybersus.api.implants.ImplantType;
import com.suslovila.cybersus.api.implants.ability.Ability;
import com.suslovila.cybersus.api.implants.ability.AbilityHack;
import com.suslovila.cybersus.api.implants.ability.AbilityInstant;
import com.suslovila.cybersus.common.item.ItemImplant;
import com.suslovila.cybersus.common.item.implants.ItemCybersusImplant;
import fox.spiteful.forbidden.DarkAspects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

import java.util.ArrayList;
import java.util.List;

public class ImplantTormentor extends ItemCybersusImplant {
    public static final ArrayList<Ability> abilities = new ArrayList<>();

    public ImplantTormentor() {
        super(ImplantType.BRAIN);
    }

    @Override
    public String getName() {
        return "tormentor";
    }

    @Override
    public List<Ability> getAbilities(EntityPlayer player, int index, ItemStack implant) {
        return abilities;
    }

    static {
        abilities.add(new AbilityHack("torment") {
            @Override
            public int getRequiredHackTime() {
                return 120;
            }

            @Override
            public void hackEntity(EntityPlayer hacker, Entity victim, int slotIndex, ItemStack implant) {
                sendToCooldown(hacker, slotIndex, implant);
                if (victim instanceof EntityPlayer) {
                    EntityPlayer player = (EntityPlayer) victim;
                    WorldProviderTorment.setPlayerMustTorment(player, 1, -1);
                } else {
                    if (victim instanceof EntityLivingBase && !(victim instanceof net.minecraft.entity.boss.IBossDisplayData)) {
                        EntityLivingBase hitLiving = (EntityLivingBase) victim;
                        hitLiving.setDead();
                    }
                }

                notifyClient(hacker, slotIndex, implant);

            }

            @Override
            public double getLockDistance(EntityPlayer player, int index, ItemStack implant) {
                return 10;
            }

            @Override
            public double getLoseDistance(EntityPlayer player, int index, ItemStack implant) {
                return 30;
            }

            @Override
            public int getTargetTimeLose(EntityPlayer player, int index, ItemStack implant) {
                return 40;
            }

            @Override
            public FuelComposite getFuelConsumeOnActivation(EntityPlayer player, int index, ItemStack implant) {
                FuelComposite fuelComposite = FuelComposite.allRequired(
                        new FuelEssentia(
                                new AspectList()
                                        .add(Aspect.TRAVEL, 16)
                                        .add(Aspect.TRAP, 16)
                                        .add(Aspect.SOUL, 16)
                        )
                );

                FuelVariation fuelVariation = new FuelVariation().addSimpleVariant(new FuelInfusion(Witchery.Recipes.infusionBeast, 10));
                if (Cybersus.forbiddenMagicLoaded) {
                    fuelVariation.addSimpleVariant(new FuelEssentia(new AspectList().add(DarkAspects.WRATH, 16)));
                }
                fuelVariation.addSimpleVariant(new FuelEssentia(new AspectList().add(Aspect.FIRE, 16).add(Aspect.WEAPON, 16)));

                fuelComposite.fuelVariations.add(fuelVariation);
                return FuelComposite.EMPTY;
            }

            @Override
            public int getCooldownTotal(EntityPlayer player, int index, ItemStack implant) {
                return 120;
            }

        });
    }
}