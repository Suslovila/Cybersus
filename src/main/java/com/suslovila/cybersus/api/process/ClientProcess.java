package com.suslovila.cybersus.api.process;

import com.suslovila.cybersus.utils.SusVec3;
import cpw.mods.fml.common.gameevent.TickEvent;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;

public abstract class ClientProcess extends WorldProcess implements ISerializableProcess {
    public SusVec3 position;
    public int timeLeft;
    public int totalDuration;

    public ClientProcess(SusVec3 vec3, int duration) {
        position = vec3;

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
        position.writeTo(buf);

        buf.writeInt(timeLeft);
        buf.writeInt(totalDuration);

    }
    @Override
    public void readFrom(ByteBuf buf) {
        position = SusVec3.readFrom(buf);

        timeLeft = buf.readInt();
        totalDuration = buf.readInt();
    }
}

