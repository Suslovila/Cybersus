package com.suslovila.cybersus.common.sync.implant;

import com.suslovila.cybersus.api.implants.ImplantStorage;
import com.suslovila.cybersus.extendedData.CybersusDataForSync;
import com.suslovila.cybersus.extendedData.CybersusPlayerExtendedData;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class PacketAllExtendedPlayerSync
        implements IMessage {

    private List<CybersusDataForSync> allData;

    public PacketAllExtendedPlayerSync() {
    }

    public PacketAllExtendedPlayerSync(List<CybersusPlayerExtendedData> data) {
        this.allData = data.stream().map(oneData -> new CybersusDataForSync(
                oneData.player.getEntityId(),
                oneData.implantStorage
        )).collect(Collectors.toList());
    }


    public void toBytes(ByteBuf buffer) {
        buffer.writeInt(allData.size());
        for (CybersusDataForSync oneData : allData) {
            buffer.writeInt(oneData.playerId);
            oneData.implantStorage.writeTo(buffer);
        }
    }


    public void fromBytes(ByteBuf buffer) {
        int length = buffer.readInt();
        this.allData = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            int id = buffer.readInt();
            ImplantStorage storage = ImplantStorage.readFrom(buffer);
            this.allData.add(new CybersusDataForSync(id, storage));
        }
    }

    public static class Handler
            implements IMessageHandler<PacketAllExtendedPlayerSync, IMessage> {
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(PacketAllExtendedPlayerSync message, MessageContext ctx) {
            World world = Minecraft.getMinecraft().thePlayer.worldObj;
            for (CybersusDataForSync data : message.allData) {
                Entity entity = world.getEntityByID(data.playerId);
                if (entity instanceof EntityPlayer) {
                    CybersusPlayerExtendedData clientData = CybersusPlayerExtendedData.get((EntityPlayer) entity);
                    if (clientData != null) {
                        data.implantStorage.player = new WeakReference<>((EntityPlayer) entity);
                        clientData.implantStorage = data.implantStorage;
                    }
                }
            }
            return null;
        }
    }
}


