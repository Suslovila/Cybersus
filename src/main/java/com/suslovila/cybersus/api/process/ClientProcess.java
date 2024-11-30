package com.suslovila.cybersus.api.process;

import com.suslovila.cybersus.utils.SusVec3;
import cpw.mods.fml.common.gameevent.TickEvent;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;

public abstract class ClientProcess extends WorldProcess implements ISerializableProcess {
    protected double x;
    protected double y;
    protected double z;
    protected int timeLeft;
    protected int totalDuration;

    public ClientProcess(SusVec3 vec3, int duration) {
        this.x = vec3.x;
        this.y = vec3.y;
        this.z = vec3.z;
        this.timeLeft = duration;
        this.totalDuration = duration;
    }

    public ClientProcess() {
    }

    @Override
    public void tickClient(TickEvent.ClientTickEvent event) {
        if(!Minecraft.getMinecraft().isGamePaused()) {
            if (!isExpired(event)) timeLeft -= 1;
        }
    }

    @Override
    public boolean isExpired(TickEvent.WorldTickEvent event) {
        return timeLeft <= 0;
    }
    @Override
    public boolean isExpired(TickEvent.ClientTickEvent event) {
        return timeLeft <= 0;
    }
    @Override
    public void writeTo(ByteBuf buf) {
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);

        buf.writeInt(timeLeft);
        buf.writeInt(totalDuration);

    }
    @Override
    public void readFrom(ByteBuf buf) {
        x = buf.readDouble();
        y = buf.readDouble();
        z = buf.readDouble();

        timeLeft = buf.readInt();
        totalDuration = buf.readInt();
    }
}

