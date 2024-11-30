package com.suslovila.cybersus.common.sync;

import com.suslovila.cybersus.api.process.WorldProcess;
import com.suslovila.cybersus.api.process.ISerializableProcess;
import com.suslovila.cybersus.api.process.ProcessRegistry;
import com.suslovila.cybersus.extendedData.CustomWorldData;
import com.suslovila.cybersus.utils.KhariumSusNBTHelper;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import java.util.UUID;

public class PacketSyncProcess implements IMessage {
    WorldProcess process;

    public PacketSyncProcess(WorldProcess process) {
        if (!(process instanceof ISerializableProcess)) {
            throw new IllegalArgumentException("process must be serialisable!");
        }

        this.process = process;
    }

    public PacketSyncProcess() {

    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, process.getTypeId());
        KhariumSusNBTHelper.writeUUID(buf, process.uuid);
        ((ISerializableProcess) process).writeTo(buf);
    }


    @Override
    public void fromBytes(ByteBuf buf) {
        Class[] objects = new Class[0];
        try {
            Class<? extends WorldProcess> clazz = ProcessRegistry.getClassType(ByteBufUtils.readUTF8String(buf));
            process = clazz.getDeclaredConstructor(objects).newInstance();
        } catch (Exception e) {
            System.out.println("error reading process");
        }
        UUID uuid = KhariumSusNBTHelper.readUUID(buf);
        ((ISerializableProcess) process).readFrom(buf);
        process.uuid = uuid;
    }

    public static class Handler implements IMessageHandler<PacketSyncProcess, IMessage> {
        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(PacketSyncProcess packet, MessageContext ctx) {
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            if (player == null) return null;

            CustomWorldData worldData = CustomWorldData.getCustomData(player.worldObj);
            worldData.addProcess(packet.process);
            return null;
        }
    }
}


