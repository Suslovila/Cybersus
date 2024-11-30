package com.suslovila.cybersus.common.block.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;

import java.util.LinkedList;

public class SimpleInventory implements IInventory {
    private final ItemStack[] itemStacks;
    private final LinkedList<TileEntity> listeners = new LinkedList<>();
    private final int firstOutPutSlotIndex;
    private final String name;
    private final int stackLimit;

    public SimpleInventory(int size, int firstOutPutSlotIndex, String name, int stackLimit) {
        this.itemStacks = new ItemStack[size];
        this.firstOutPutSlotIndex = firstOutPutSlotIndex;
        this.name = name;
        this.stackLimit = stackLimit;
    }

    @Override
    public int getSizeInventory() {
        return itemStacks.length;
    }

    @Override
    public ItemStack getStackInSlot(int slotId) {
        return itemStacks[slotId];
    }

    @Override
    public ItemStack decrStackSize(int slotId, int count) {
        if (slotId < itemStacks.length && itemStacks[slotId] != null) {
            if (itemStacks[slotId].stackSize > count) {
                ItemStack result = itemStacks[slotId].splitStack(count);
                markDirty();
                return result;
            }
            if (itemStacks[slotId].stackSize < count) {
                return null;
            }
            ItemStack stack = itemStacks[slotId];
            setInventorySlotContents(slotId, null);
            return stack;
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int slotId, ItemStack itemstack) {
        if (slotId >= itemStacks.length) {
            return;
        }
        itemStacks[slotId] = itemstack;
        if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit()) {
            itemstack.stackSize = this.getInventoryStackLimit();
        }
        markDirty();
    }

    @Override
    public String getInventoryName() {
        return name;
    }

    @Override
    public int getInventoryStackLimit() {
        return stackLimit;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        return true;
    }

    @Override
    public void openChest() {
    }

    @Override
    public void closeChest() {
    }

    public void writeToNBT(NBTTagCompound data) {
        NBTTagList slots = new NBTTagList();
        for (int index = 0; index < itemStacks.length; index++) {
            if (itemStacks[index] != null && itemStacks[index].stackSize > 0) {
                NBTTagCompound slot = new NBTTagCompound();
                slots.appendTag(slot);
                slot.setByte(SLOT_NBT, (byte) index);
                itemStacks[index].writeToNBT(slot);
            }
        }
        data.setTag(ITEMS_NBT, slots);
    }

    public void readFromNBT(NBTTagCompound data) {
        if (data.hasKey(ITEMS_NBT)) {
            readFromNBT(data, ITEMS_NBT);
        }
    }

    public void readFromNBT(NBTTagCompound data, String tag) {
        NBTTagList nbttaglist = data.getTagList(tag, Constants.NBT.TAG_COMPOUND);
        for (int j = 0; j < nbttaglist.tagCount(); j++) {
            NBTTagCompound slotNbt = nbttaglist.getCompoundTagAt(j);
            int index;
            if (slotNbt.hasKey(SLOT_INDEX_NBT)) {
                index = slotNbt.getInteger(SLOT_INDEX_NBT);
            } else {
                index = slotNbt.getByte(SLOT_NBT);
            }
            if (index >= 0 && index < itemStacks.length) {
                setInventorySlotContents(index, ItemStack.loadItemStackFromNBT(slotNbt));
            }
        }
    }

    public void addListener(TileEntity listener) {
        listeners.add(listener);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slotId) {
        if (itemStacks[slotId] == null) {
            return null;
        }
        ItemStack stackToTake = itemStacks[slotId];
        setInventorySlotContents(slotId, null);
        return stackToTake;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack itemstack) {
        return index < firstOutPutSlotIndex;
    }

    @Override
    public boolean isCustomInventoryName() {
        return false;
    }

    @Override
    public void markDirty() {
        for (TileEntity handler : listeners) {
            if (handler.getWorld() != null) {
                handler.getWorld().markTileEntityChunkModified(handler.xCoord, handler.yCoord, handler.zCoord, handler);
            }
        }
    }

    private static final String ITEMS_NBT = "Items";
    private static final String SLOT_NBT = "Slot";
    private static final String SLOT_INDEX_NBT = "index";

}
