package com.suslovila.cybersus.common.block.container;

import com.suslovila.cybersus.api.implants.ImplantStorage;
import com.suslovila.cybersus.api.implants.ImplantType;
import com.suslovila.cybersus.extendedData.CybersusPlayerExtendedData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ContainerImplantHolder extends DefaultContainer {

    private final EntityPlayer player;

    public ContainerImplantHolder(EntityPlayer player) {
        this.player = player;

        CybersusPlayerExtendedData data = CybersusPlayerExtendedData.get(player);
        if (data != null) {
            addImplantSlots(data.implantStorage);
        }
        addPlayerInventory();
    }

    private void addImplantSlots(ImplantStorage storage) {
        ImplantType[] implantTypes = ImplantType.values();
        int index = 0;

        int yOffset = 10;
        for (int implantTypeIndex = 0; implantTypeIndex < implantTypes.length; implantTypeIndex++) {
            ImplantType type = implantTypes[implantTypeIndex];
            boolean isLeftSide = (implantTypeIndex % 2 == 0);
            int offsetForTypeSlots = oneSlotStep * ImplantType.getSlotAmount(type) * (isLeftSide ? -1 : 0);
            for (int typeIndex = 0; typeIndex < ImplantType.getSlotAmount(type); typeIndex++) {
                addSlotToContainer(new ImplantSlot(storage, index++,
                        140 + (isLeftSide ? 0 : 200) + typeIndex * oneSlotStep + offsetForTypeSlots,
                        yOffset));
            }
            if (implantTypeIndex % 2 != 0) yOffset += 26;
        }
    }

    private void addPlayerInventory() {
        for (int playerInvRow = 0; playerInvRow <= 2; playerInvRow++) {
            for (int playerInvCol = 0; playerInvCol <= 8; playerInvCol++) {
                addSlotToContainer(new Slot(player.inventory,
                        playerInvCol + playerInvRow * 9 + 9,
                        161 + 18 * playerInvCol,
                        179 + playerInvRow * 18));
            }
        }
        for (int hotbarSlot = 0; hotbarSlot <= 8; hotbarSlot++) {
            addSlotToContainer(new Slot(player.inventory, hotbarSlot,
                    161 + 18 * hotbarSlot, 235));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }
}

class ImplantSlot extends Slot {
    public ImplantSlot(IInventory inventory, int slotIndex, int x, int y) {
        super(inventory, slotIndex, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return inventory.isItemValidForSlot(this.getSlotIndex(), stack);
    }

    @Override
    public IIcon getBackgroundIconIndex() {
        ImplantType type = ImplantType.getTypeForSlotWithIndex(this.getSlotIndex());
        if(type == null) return null;
        return ImplantType.getIcon(type);
    }
}
