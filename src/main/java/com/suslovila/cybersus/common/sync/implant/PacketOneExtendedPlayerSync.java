package com.suslovila.cybersus.common.sync.implant;

import com.suslovila.cybersus.api.implants.ImplantStorage;
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

import java.lang.ref.WeakReference;


public class PacketOneExtendedPlayerSync
        implements IMessage {
    private int entityId;
    private ImplantStorage implants;

    public PacketOneExtendedPlayerSync() {
    }

    public PacketOneExtendedPlayerSync(CybersusPlayerExtendedData data) {
        this.entityId = data.player.getEntityId();
        this.implants = data.implantStorage;
    }


    public void toBytes(ByteBuf buffer) {
        buffer.writeInt(this.entityId);
        implants.writeTo(buffer);
    }


    public void fromBytes(ByteBuf buffer) {
        this.entityId = buffer.readInt();
        implants = ImplantStorage.readFrom(buffer);
    }

    public static class Handler
            implements IMessageHandler<PacketOneExtendedPlayerSync, IMessage> {
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(PacketOneExtendedPlayerSync message, MessageContext ctx) {
            Entity entity = Minecraft.getMinecraft().thePlayer.worldObj.getEntityByID(message.entityId);
            if (entity instanceof EntityPlayer) {
                CybersusPlayerExtendedData data = CybersusPlayerExtendedData.get((EntityPlayer) entity);
                if (data != null) {
                    message.implants.player = new WeakReference<>((EntityPlayer) entity);
                    data.implantStorage = (message.implants);
                    data.implantStorage.markDirty();
                }
            }
            return null;
        }
    }
}


