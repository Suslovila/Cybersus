package com.suslovila.cybersus.common.sync;

import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.api.implants.ability.Ability;
import com.suslovila.cybersus.client.gui.CybersusGui;
import com.suslovila.cybersus.common.item.ItemImplant;
import com.suslovila.cybersus.extendedData.CybersusPlayerExtendedData;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

import java.util.List;


public class PacketOpenImplantGui
        implements IMessage {


    public PacketOpenImplantGui() {
    }

    public PacketOpenImplantGui(int slotId, int abilityId) {
    }


    public void toBytes(ByteBuf buffer) {
    }


    public void fromBytes(ByteBuf buffer) {
    }

    public static class Handler
            implements IMessageHandler<PacketOpenImplantGui, IMessage> {
        public IMessage onMessage(PacketOpenImplantGui message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().playerEntity;
            player.openGui(Cybersus.instance, CybersusGui.IMPLANT_INSTALLER.ordinal(), ctx.getServerHandler().playerEntity.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);

            return null;
        }
    }
}


