package com.suslovila.cybersus.common.block.container.envyEye;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class SlotBlocked extends Slot {
    public SlotBlocked(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_) {
        super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
    }

    public boolean canTakeStack(EntityPlayer p_82869_1_) {
        return false;
    }
}
