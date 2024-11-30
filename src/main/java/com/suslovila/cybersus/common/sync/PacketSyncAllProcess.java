package com.suslovila.cybersus.common.sync;

import com.suslovila.cybersus.api.process.ISerializableProcess;
import com.suslovila.cybersus.api.process.WorldProcess;
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

import java.util.HashMap;
import java.util.UUID;

public class PacketSyncAllProcess implements IMessage {
    HashMap<String, HashMap<UUID, WorldProcess>> processesByType;

    public PacketSyncAllProcess(HashMap<String, HashMap<UUID, WorldProcess>> process) {
        this.processesByType = process;
    }

    public PacketSyncAllProcess() {

    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(processesByType.size());
        for (String type : processesByType.keySet()) {
            HashMap<UUID, WorldProcess> processesOfType = processesByType.get(type);
            ByteBufUtils.writeUTF8String(buf, type);
            int amountOfSerializable = 0;
            for (UUID uuid : processesOfType.keySet()) {
                WorldProcess process = processesOfType.get(uuid);
                if((process instanceof ISerializableProcess)) amountOfSerializable++;
            }

            buf.writeInt(amountOfSerializable);
            for (UUID uuid : processesOfType.keySet()) {
                WorldProcess process = processesOfType.get(uuid);
                if(!(process instanceof ISerializableProcess)) continue;
                KhariumSusNBTHelper.writeUUID(buf, process.uuid);
                ((ISerializableProcess) process).writeTo(buf);
            }
        }
    }


    @Override
    public void fromBytes(ByteBuf buf) {
        int amountOfTypes = buf.readInt();
        processesByType = new HashMap<>();
        for (int i = 0; i < amountOfTypes; i++) {
            String type = ByteBufUtils.readUTF8String(buf);
            Class<? extends WorldProcess> clazz = ProcessRegistry.getClassType(type);

            HashMap<UUID, WorldProcess> map = new HashMap<>();
            int amountOfProcesses = buf.readInt();
            for (int j = 0; j < amountOfProcesses; j++) {
                WorldProcess process = null;
                Class[] objects = new Class[0];
                try {
                    process = clazz.getDeclaredConstructor(objects).newInstance();
                } catch (Exception e) {
                    System.out.println("error reading process");
                }
                UUID uuid = KhariumSusNBTHelper.readUUID(buf);
                assert process != null;
                ((ISerializableProcess) process).readFrom(buf);
                process.uuid = uuid;

                map.put(uuid, process);
            }

            processesByType.put(type, map);
        }
    }

    public static class Handler implements IMessageHandler<PacketSyncAllProcess, IMessage> {
        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(PacketSyncAllProcess packet, MessageContext ctx) {
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            if (player == null) return null;

            CustomWorldData worldData = CustomWorldData.getCustomData(player.worldObj);
            worldData.processesMapsByType = packet.processesByType;
            worldData.markDirty();

            return null;
        }
    }
}


