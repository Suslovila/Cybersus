package com.suslovila.cybersus.common.event.customEvents;

import com.suslovila.cybersus.api.implants.ability.AbilityHack;
import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

// server only
@Cancelable
public class PlayerTriesToStartHackingEvent extends Event {
    public final EntityPlayerMP hacker;
    public final Entity victim;
    public final int index;
    public final ItemStack stack;
    AbilityHack abilityHack;

    public PlayerTriesToStartHackingEvent(
            EntityPlayerMP entityPlayerMP,
            Entity victim,
            int index,
            ItemStack stack,
            AbilityHack abilityHack) {
        this.hacker = entityPlayerMP;
        this.victim = victim;
        this.index = index;
        this.stack = stack;
        this.abilityHack = abilityHack;
    }
}
