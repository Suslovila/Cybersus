package com.suslovila.cybersus.api.process;

import net.minecraft.nbt.NBTTagCompound;

public interface ISaveableProcess {
    void writeTo(NBTTagCompound tagCompound);
    void readFrom(NBTTagCompound tagCompound);
}
