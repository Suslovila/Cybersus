package com.suslovila.cybersus.common.sync.implant;

import com.suslovila.cybersus.api.implants.ability.Ability;
import com.suslovila.cybersus.common.item.ItemImplant;
import com.suslovila.cybersus.extendedData.CybersusPlayerExtendedData;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;


public class PacketEnableImplantSync
        implements IMessage {
    private int slotId;
    private int abilityId;


    public PacketEnableImplantSync() {
    }

    public PacketEnableImplantSync(int slotId, int abilityId) {
        this.slotId = slotId;
        this.abilityId = abilityId;
    }


    public void toBytes(ByteBuf buffer) {
        buffer.writeInt(slotId);
        buffer.writeInt(abilityId);
    }


    public void fromBytes(ByteBuf buffer) {
        slotId = buffer.readInt();
        abilityId = buffer.readInt();
    }

    public static class Handler
            implements IMessageHandler<PacketEnableImplantSync, IMessage> {
        public IMessage onMessage(PacketEnableImplantSync message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().playerEntity;
            CybersusPlayerExtendedData data = CybersusPlayerExtendedData.get(player);
            if (data != null) {
                ItemStack implant = data.implantStorage.getStackInSlot(message.slotId);
                if (implant != null) {
                    ItemImplant implantClass = (ItemImplant) implant.getItem();
                    List<Ability> abilities = implantClass.getAbilities(player, message.slotId, implant);
                    if (abilities.size() > message.abilityId) {
                        abilities.get(message.abilityId).onEnableButtonClicked(player, message.slotId, implant);
                    }
                }
            }
            return null;
        }
    }
}


