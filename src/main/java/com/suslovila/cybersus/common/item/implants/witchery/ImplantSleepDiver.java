package com.suslovila.cybersus.common.item.implants.witchery;

import com.emoniph.witchery.dimension.WorldProviderDreamWorld;
import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.api.fuel.FuelComposite;
import com.suslovila.cybersus.api.fuel.FuelVariation;
import com.suslovila.cybersus.api.fuel.impl.fuel.essentia.FuelEssentia;
import com.suslovila.cybersus.api.implants.ImplantType;
import com.suslovila.cybersus.api.implants.ability.Ability;
import com.suslovila.cybersus.api.implants.ability.AbilityInstant;
import com.suslovila.cybersus.common.item.ItemImplant;
import com.suslovila.cybersus.common.item.implants.ItemCybersusImplant;
import com.suslovila.cybersus.common.item.implants.ItemMagicImplant;
import cpw.mods.fml.common.registry.GameRegistry;
import fox.spiteful.forbidden.DarkAspects;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

import java.util.ArrayList;
import java.util.List;

public class ImplantSleepDiver extends ItemCybersusImplant {
    public static final ArrayList<Ability> abilities = new ArrayList<>();

    public ImplantSleepDiver() {
        super(ImplantType.BRAIN);
    }

    @Override
    public String getName() {
        return "sleep_diver";
    }

    @Override
    public List<Ability> getAbilities(EntityPlayer player, int index, ItemStack implant) {
        return abilities;
    }

    static {
        abilities.add(new AbilityInstant("sleep") {
            @Override
            protected void onActivated(EntityPlayer player, int index, ItemStack implant) {
                if (player.dimension == 0 && !player.worldObj.isRemote && !WorldProviderDreamWorld.getPlayerIsGhost(player)) {
                    sendToCooldown(player, index, implant);
                    WorldProviderDreamWorld.sendPlayerToSpiritWorld(player, 0.5D);
                    player.worldObj.playSoundAtEntity(player, "random.burp", 0.5F, player.worldObj.rand.nextFloat() * 0.1F + 0.9F);
                    notifyClient(player, index, implant);
                }
            }

            @Override
            public FuelComposite getFuelConsumeOnActivation(EntityPlayer player, int index, ItemStack implant) {
                return FuelComposite.allRequired(
                        new FuelEssentia(new AspectList().add(Aspect.FIRE, 1))
                ).addSimpleVariationOfFuels(
                        new FuelEssentia(new AspectList().add(Aspect.TRAP, 1).add(Aspect.SOUL, 1)),
                        new FuelEssentia(new AspectList().add(DarkAspects.SLOTH, 1))
                );
            }

            @Override
            public int getCooldownTotal(EntityPlayer player, int index, ItemStack implant) {
                return 120;
            }

        });
    }
}