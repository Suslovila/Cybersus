package com.suslovila.cybersus.common.block.runeInstaller;

import com.suslovila.cybersus.api.implants.upgrades.rune.RuneUsingItem;
import com.suslovila.cybersus.common.block.container.SimpleInventory;
import com.suslovila.cybersus.common.block.tile.TileCybersus;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TileRuneInstaller extends TileCybersus {
    public SimpleInventory inventory;

    public TileRuneInstaller() {
        this.inventory = new SimpleInventory(1, 1, "Rune Installer", 1) {
            @Override
            public boolean isItemValidForSlot(int index, ItemStack itemstack) {
                return itemstack.getItem() instanceof RuneUsingItem;
            }
        };
        this.inventory.addListener(this);
    }

    @Override
    public void updateEntity() {
        // Реализация метода updateEntity
    }

//    @Override
//    public void writeCustomNBT(NBTTagCompound rootTag) {
//        super.writeCustomNBT(rootTag);
//        inventory.writeToNBT(rootTag);
//    }
//
//    @Override
//    public void readCustomNBT(NBTTagCompound rootTag) {
//        super.readCustomNBT(rootTag);
//        inventory.readFromNBT(rootTag);
//    }
}