package com.suslovila.cybersus.api.implants.ability;

import com.suslovila.cybersus.api.fuel.FuelComposite;
import com.suslovila.cybersus.api.fuel.FuelVariation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public abstract class AbilityInstant extends Ability {
    public AbilityInstant(String name) {
        super(name);
    }

    @Override
    public void onEnableButtonClicked(EntityPlayer player, int index, ItemStack implant) {
        if(isOnCooldown(implant)) return;
        FuelComposite requiredFuel = getFuelConsumeOnActivation(player, index, implant);
        if (requiredFuel.tryTakeFuelFromPlayer(player) && canActivate(player, index, implant)) {
            onActivated(player, index, implant);
        }
    }

    /**
     * additional checks if ability can be used. Fuel checks should not be included here
     *
     * @param player
     * @param index
     * @param implant
     * @return
     */
    public boolean canActivate(EntityPlayer player, int index, ItemStack implant) {
        return true;
    }

    /**
     * is fired when all checks for possibility of activation were passed
     *
     * @param player
     * @param index
     * @param implant
     */
    protected abstract void onActivated(EntityPlayer player, int index, ItemStack implant);

    @Override
    public boolean isActive(ItemStack implant) {
        return false;
    }
}