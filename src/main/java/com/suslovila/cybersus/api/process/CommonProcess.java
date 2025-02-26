package com.suslovila.cybersus.api.process;

import com.suslovila.cybersus.utils.SusVec3;
import cpw.mods.fml.common.gameevent.TickEvent;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

public abstract class CommonProcess extends ClientProcess implements ISaveableProcess {

    public CommonProcess(SusVec3 vec3, int duration) {
        position = vec3;

        this.timeLeft = duration;
        this.totalDuration = duration;
    }

    public CommonProcess() {
    }

    @Override
    public void tickServer(TickEvent.WorldTickEvent event) {
        if (timeLeft > 0) timeLeft -= 1;
    }

    @Override
    public void writeTo(NBTTagCompound tagCompound) {
        position.writeTo(tagCompound);

        tagCompound.setInteger("timeLeft", timeLeft);
        tagCompound.setInteger("totalDuration", totalDuration);

    }

    @Override
    public void readFrom(NBTTagCompound tagCompound) {
        position = SusVec3.readFrom(tagCompound);

        timeLeft = tagCompound.getInteger("timeLeft");
        totalDuration = tagCompound.getInteger("totalDuration");
    }


}

