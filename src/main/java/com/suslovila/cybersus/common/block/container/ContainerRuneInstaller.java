package com.suslovila.cybersus.common.block.container;

import com.suslovila.cybersus.common.block.runeInstaller.TileRuneInstaller;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerRuneInstaller extends DefaultContainer {
    private final TileRuneInstaller tile;
    private final EntityPlayer player;

    public ContainerRuneInstaller(TileRuneInstaller tile, EntityPlayer player) {
        this.tile = tile;
        this.player = player;

        addSlotToContainer(new SlotRuneUsingItem(tile.inventory, 0, 232, 80));
        addPlayerInventory();
    }

    private void addPlayerInventory() {
        for (int playerInvRow = 0; playerInvRow <= 2; playerInvRow++) {
            for (int playerInvCol = 0; playerInvCol <= 8; playerInvCol++) {
                addSlotToContainer(new Slot(player.inventory, playerInvCol + playerInvRow * 9 + 9,
                        161 + 18 * playerInvCol, 179 + playerInvRow * 18));
            }
        }
        for (int hotbarSlot = 0; hotbarSlot <= 8; hotbarSlot++) {
            addSlotToContainer(new Slot(player.inventory, hotbarSlot, 161 + 18 * hotbarSlot, 235));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    public static class SlotRuneUsingItem extends Slot {
        public SlotRuneUsingItem(IInventory inventory, int slotIndex, int x, int y) {
            super(inventory, slotIndex, x, y);
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return inventory.isItemValidForSlot(getSlotIndex(), stack);
        }
    }
}