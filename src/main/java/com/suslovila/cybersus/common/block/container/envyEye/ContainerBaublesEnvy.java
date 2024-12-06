package com.suslovila.cybersus.common.block.container.envyEye;

import baubles.common.container.ContainerPlayerExpanded;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerBaublesEnvy extends ContainerPlayerExpanded {
    public ContainerBaublesEnvy(InventoryPlayer playerInv, boolean par2, EntityPlayer player) {
        super(playerInv, par2, player);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return false;
    }
}
