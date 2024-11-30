package com.suslovila.cybersus.api.implants.ability;

import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.api.fuel.FuelComposite;
import com.suslovila.cybersus.api.fuel.FuelVariation;
import com.suslovila.cybersus.utils.KhariumSusNBTHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;

public abstract class AbilityTemporary extends Ability {
    public static final String TIME_LEFT_NBT = Cybersus.prefixAppender.doAndGet("active_timer");

    public AbilityTemporary(String name) {
        super(name);
    }

    @Override
    public void onEnableButtonClicked(EntityPlayer player, int index, ItemStack implant) {
        if (player.worldObj.isRemote) return;
        activateAbility(player, index, implant);
        notifyClient(player, index, implant);
    }

    protected abstract int getEffectDuration();

    public boolean activateAbility(EntityPlayer player, int index, ItemStack implant) {
        if (isOnCooldown(implant) || isActive(implant)) return false;
        FuelComposite requiredFuel = getFuelConsumeOnActivation(player, index, implant);
        if (!requiredFuel.tryTakeFuelFromPlayer(player)) {
            return false;
        }

        KhariumSusNBTHelper.getOrCreateTag(implant).setInteger(TIME_LEFT_NBT, getEffectDuration());
        return true;
    }

    @Override
    public void onPlayerUpdateEvent(LivingEvent.LivingUpdateEvent event, EntityPlayer player, int index, ItemStack implant) {
        int timeLeftPrevious = KhariumSusNBTHelper.getOrCreateInteger(KhariumSusNBTHelper.getOrCreateTag(implant), TIME_LEFT_NBT, getEffectDuration());
        if (timeLeftPrevious > 0) {
            onTickEnabled(event, player, index, implant);
            KhariumSusNBTHelper.getOrCreateTag(implant).setInteger(TIME_LEFT_NBT, timeLeftPrevious - 1);
            if (timeLeftPrevious == 1) {
                sendToCooldown(player, index, implant);
            }
        } else {
            super.onPlayerUpdateEvent(event, player, index, implant);
        }
    }

    public abstract void onTickEnabled(LivingEvent.LivingUpdateEvent event, EntityPlayer player, int index, ItemStack implant);

    @Override
    public boolean isActive(ItemStack implant) {
        return KhariumSusNBTHelper.getOrCreateInteger(KhariumSusNBTHelper.getOrCreateTag(implant), TIME_LEFT_NBT, 0) != 0;
    }
}