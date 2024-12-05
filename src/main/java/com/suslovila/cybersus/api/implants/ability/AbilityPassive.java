package com.suslovila.cybersus.api.implants.ability;

import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.api.fuel.FuelComposite;
import com.suslovila.cybersus.utils.KhariumSusNBTHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.living.LivingEvent;

public abstract class AbilityPassive extends Ability {
    private final String IS_ABILITY_ENABLED_NBT = Cybersus.prefixAppender.doAndGet(this.name + ":isEnabled");

    public AbilityPassive(String name) {
        super(name);
    }

    @Override
    public void onEnableButtonClicked(EntityPlayer player, int index, ItemStack implant) {
        if (player.worldObj.isRemote) return;
        switchAbilityStatus(player, index, implant);
        notifyClient(player, index, implant);
    }

    protected void switchAbilityStatus(EntityPlayer player, int index, ItemStack implant) {
        NBTTagCompound tag = KhariumSusNBTHelper.getOrCreateTag(implant);
        if (isOnCooldown(implant)) return;
        boolean isAlreadyActive = KhariumSusNBTHelper.getOrCreateBoolean(tag, IS_ABILITY_ENABLED_NBT, false);
        if (!isAlreadyActive) {
            FuelComposite fuelConsumeOnActivation = getFuelConsumeOnActivation(player, index, implant);
            if (!fuelConsumeOnActivation.tryTakeFuelFromPlayer(player)) {
                return;
            }
        }
        tag.setBoolean(IS_ABILITY_ENABLED_NBT, !isAlreadyActive);
        onAbilityStatusSwitched(player, index, implant);
    }

    @Override
    public boolean isActive(ItemStack implant) {
        return KhariumSusNBTHelper.getOrCreateBoolean(KhariumSusNBTHelper.getOrCreateTag(implant), IS_ABILITY_ENABLED_NBT, false);
    }

    public void onAbilityStatusSwitched(EntityPlayer player, int index, ItemStack implant) {}

    @Override
    public void onPlayerUpdateEvent(LivingEvent.LivingUpdateEvent event, EntityPlayer player, int index, ItemStack implant) {
        super.onPlayerUpdateEvent(event, player, index, implant);
        if (player.worldObj.isRemote || player.ticksExisted % getFuelConsumePeriod(player, index, implant) != 0 || !isActive(implant)) return;
        FuelComposite fuelConsumePerSecond = getFuelConsumePerCheck(player, index, implant);
        if (fuelConsumePerSecond != null && !fuelConsumePerSecond.tryTakeFuelFromPlayer(player)) {
            sendToCooldown(player, index, implant);
            notifyClient(player, index, implant);
        }
    }

    @Override
    public void onUnequipped(EntityPlayer player, int index, ItemStack implant) {
        if(isActive(implant)) {
            sendToCooldown(player, index, implant);
        }
    }

    @Override
    public void sendToCooldown(EntityPlayer player, int index, ItemStack implant) {
        super.sendToCooldown(player, index, implant);
        NBTTagCompound tag = KhariumSusNBTHelper.getOrCreateTag(implant);
        tag.setBoolean(IS_ABILITY_ENABLED_NBT, false);
    }

    public int getFuelConsumePeriod(EntityPlayer player, int index, ItemStack implant) {
        return 20;
    }

    public abstract FuelComposite getFuelConsumePerCheck(EntityPlayer player, int index, ItemStack implant);

    @Override
    public boolean hasFuel(EntityPlayer player, int index, ItemStack implant) {
        if (isActive(implant)) {
            return getFuelConsumePerCheck(player, index, implant).hasPlayerEnough(player);
        }
        else {
            return getFuelConsumeOnActivation(player, index, implant).hasPlayerEnough(player);

        }
    }
}