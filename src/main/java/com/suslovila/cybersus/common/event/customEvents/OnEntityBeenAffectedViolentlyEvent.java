package com.suslovila.cybersus.common.event.customEvents;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

@Cancelable
public class OnEntityBeenAffectedViolentlyEvent extends Event {
    public final Entity entity;

    public OnEntityBeenAffectedViolentlyEvent(
            Entity entity) {
        this.entity = entity;
    }
}
