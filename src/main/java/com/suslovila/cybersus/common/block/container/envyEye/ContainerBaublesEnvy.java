package com.suslovila.cybersus.common.block.container.envyEye;

import baubles.api.BaubleType;
import baubles.common.container.ContainerPlayerExpanded;
import baubles.common.container.InventoryBaubles;
import baubles.common.container.SlotBauble;
import baubles.common.lib.PlayerHandler;
import com.suslovila.cybersus.common.block.container.DefaultContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;

public class ContainerBaublesEnvy extends DefaultContainer {
    EntityPlayer envier;
    EntityPlayer victim;
    public ContainerBaublesEnvy(EntityPlayer envier, EntityPlayer victim) {
        this.envier = envier;
        this.victim = victim;

        InventoryPlayer inventoryPlayer = new InventoryPlayer(victim);
        for(int i = 0; i < 4; ++i) {
            final int armorType = i;
            this.addSlotToContainer(new Slot(inventoryPlayer, inventoryPlayer.getSizeInventory() - 1 - armorType, 8, 8 + armorType * 18) {
                public int getSlotStackLimit() {
                    return 1;
                }

                public boolean isItemValid(ItemStack par1ItemStack) {
                    return par1ItemStack == null ? false : par1ItemStack.getItem().isValidArmor(par1ItemStack, armorType, victim);
                }
            });
        }

//        this.addSlotToContainer(new SlotBlocked(this.baubles, BaubleType.AMULET, 0, 80, 8));
//        this.addSlotToContainer(new SlotBlocked(this.baubles, BaubleType.RING, 1, 80, 26));
//        this.addSlotToContainer(new SlotBlocked(this.baubles, BaubleType.RING, 2, 80, 44));
//        this.addSlotToContainer(new SlotBlocked(this.baubles, BaubleType.BELT, 3, 80, 62));

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new SlotBlocked(inventoryPlayer, j + (i + 1) * 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int i = 0; i < 9; ++i) {
            this.addSlotToContainer(new SlotBlocked(inventoryPlayer, i, 8 + i * 18, 142));
        }

    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }
}
