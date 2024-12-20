package com.suslovila.cybersus.common.item.implants;

import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.api.fuel.FuelComposite;
import com.suslovila.cybersus.api.fuel.FuelVariation;
import com.suslovila.cybersus.api.fuel.impl.fuel.essentia.FuelEssentia;
import com.suslovila.cybersus.api.implants.ImplantType;
import com.suslovila.cybersus.api.implants.ability.Ability;
import com.suslovila.cybersus.api.implants.ability.AbilityPassive;
import com.suslovila.cybersus.utils.SusCollectionUtils;
import fox.spiteful.forbidden.DarkAspects;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingEvent;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

import java.util.ArrayList;
import java.util.List;

public class ImplantBerserkHeart extends ItemCybersusImplant {
    public static final ArrayList<Ability> abilities = new ArrayList<>();

    public ImplantBerserkHeart() {
        super(ImplantType.HEART);
    }

    @Override
    public List<Ability> getAbilities(EntityPlayer player, int index, ItemStack implant) {
        return abilities;
    }

    static {
        abilities.add(new AbilityPassive("berserk_mode") {
            @Override
            public FuelComposite getFuelConsumePerCheck(EntityPlayer player, int index, ItemStack implant) {

                FuelVariation fuelVariation = new FuelVariation();
                int maxRequiredAspectAmount = 16;
                int currentRequiredAspectAmount = 1 + (int) (maxRequiredAspectAmount * ((player.getMaxHealth() - player.getHealth()) / player.getMaxHealth()));
                if(Cybersus.forbiddenMagicLoaded) {
                    fuelVariation.addSimpleVariant(new FuelEssentia(new AspectList().add(DarkAspects.WRATH, currentRequiredAspectAmount)));
                }
                fuelVariation.addSimpleVariant(new FuelEssentia(new AspectList().add(Aspect.WEAPON, currentRequiredAspectAmount).add(Aspect.FIRE, currentRequiredAspectAmount)));
                FuelComposite fuelComposite = FuelComposite.allRequired();
                fuelComposite.fuelVariations.add(fuelVariation);
                fuelComposite.addRequiredFuel(new FuelEssentia(
                        new AspectList()
                                .add(Aspect.ARMOR, currentRequiredAspectAmount)
                                .add(Aspect.HEAL, currentRequiredAspectAmount)
                                .add(Aspect.BEAST, currentRequiredAspectAmount)

                ));
                return fuelComposite;
//
//                return FuelComposite.EMPTY;
            }

            @Override
            public int getCooldownTotal(EntityPlayer player, int index, ItemStack implant) {
                return 1 * 60 * 20;
            }


            @Override
            public FuelComposite getFuelConsumeOnActivation(EntityPlayer player, int index, ItemStack implant) {
                return getFuelConsumePerCheck(player, index, implant);
            }



            @Override
            public void onPlayerUpdateEvent(LivingEvent.LivingUpdateEvent event, EntityPlayer player, int index, ItemStack implant) {
                super.onPlayerUpdateEvent(event, player, index, implant);
                if (!player.worldObj.isRemote && isActive(implant)) {
                    double healthPercentage = player.getHealth() / player.getMaxHealth();
                    int effectsLevel = 0;
                    if (healthPercentage <= 0.9) {
                        effectsLevel = 0;
                    }
                    if(healthPercentage <= 0.7) {
                        effectsLevel = 1;
                    }
                    if(healthPercentage <= 0.6) {
                        effectsLevel = 2;
                    }
                    if(healthPercentage <= 0.1) {
                        effectsLevel = 3;
                    }
                    if(healthPercentage <= 0.05) {
                        effectsLevel = 4;
                    }
                    List<Potion> givenPotions = SusCollectionUtils.arrayListOf(Potion.resistance, Potion.damageBoost, Potion.regeneration);
                    for(Potion potion : givenPotions) {
                        PotionEffect potionEffect = player.getActivePotionEffect(potion);
                        if(potionEffect == null || potionEffect.getAmplifier() < effectsLevel || potionEffect.getDuration() < 2) {
                            player.addPotionEffect(new PotionEffect(potion.id, potion.id == Potion.regeneration.id ? 30 : 11, effectsLevel));
                        }
                    }
                }
            }

            @Override
            public boolean hasFuel(EntityPlayer player, int index, ItemStack implant) {
                return getFuelConsumePerCheck(player, index, implant).hasPlayerEnough(player);
            }
        });
    }

    @Override
    public String getName() {
        return "berserk_heart";
    }
}

