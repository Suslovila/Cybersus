package com.suslovila.cybersus.api.process;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

public interface ISerializableProcess {
    void writeTo(ByteBuf byteBuf);
    void readFrom(ByteBuf byteBuf);

}
