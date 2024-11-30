package com.suslovila.cybersus.common.block.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import thaumcraft.api.TileThaumcraft;

public class TileCybersus extends TileEntity {

//    @Override
//    public Packet getDescriptionPacket() {
//        NBTTagCompound nbttagcompound = new NBTTagCompound();
//        writeCustomNBT(nbttagcompound);
//        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, nbttagcompound);
//    }
//
//    public void markForSaveAndSync() {
//        markForSave();
//        markForSync();
//    }
//
//    // note: no neighbour update
//    public void markForSave() {
//        worldObj.markTileEntityChunkModified(xCoord, yCoord, zCoord, this);
//    }
//
//    public void markForSync() {
//        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
//    }
//
//    public void markForSaveAndNotifyOthers() {
//        markDirty();
//    }
//
//    public void markForSaveSyncNotify() {
//        markForSync();
//        markForSaveAndNotifyOthers();
//    }
}

