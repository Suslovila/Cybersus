package com.suslovila.cybersus.api.process;

import com.suslovila.cybersus.utils.SusVec3;
import cpw.mods.fml.common.gameevent.TickEvent;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

public abstract class CommonProcess extends ClientProcess implements ISaveableProcess {

    public CommonProcess(SusVec3 vec3, int duration) {
        this.x = vec3.x;
        this.y = vec3.y;
        this.z = vec3.z;
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
        tagCompound.setDouble("x", x);
        tagCompound.setDouble("y", y);
        tagCompound.setDouble("z", z);

        tagCompound.setInteger("timeLeft", timeLeft);
        tagCompound.setInteger("totalDuration", totalDuration);

    }

    @Override
    public void readFrom(NBTTagCompound tagCompound) {
        x = tagCompound.getDouble("x");
        y = tagCompound.getDouble("y");
        z = tagCompound.getDouble("z");

        timeLeft = tagCompound.getInteger("timeLeft");
        totalDuration = tagCompound.getInteger("totalDuration");
    }


}

