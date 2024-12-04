package com.suslovila.cybersus.common.event.customEvents;

import com.suslovila.cybersus.api.implants.ability.Ability;
import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

@Cancelable
public class PlayerActivatedAbilityEvent extends Event {
    public final EntityPlayer entityPlayer;
    public final int index;
    public final ItemStack stack;
    public final Ability ability;
    public PlayerActivatedAbilityEvent(
            EntityPlayer entityPlayerMP,
            int index,
            ItemStack stack,
            Ability ability) {
        this.entityPlayer = entityPlayerMP;
        this.index = index;
        this.stack = stack;
        this.ability = ability;
    }
}
