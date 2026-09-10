package com.suslovila.cybersus.common.sync.implant;

import com.suslovila.cybersus.api.implants.ability.Ability;
import com.suslovila.cybersus.common.item.ItemImplant;
import com.suslovila.cybersus.common.item.implants.reactionIncreaser.AbilityAcceleration;
import com.suslovila.cybersus.common.item.implants.reactionIncreaser.ImplantReactionIncreaser;
import com.suslovila.cybersus.extendedData.CybersusPlayerExtendedData;
import com.suslovila.cybersus.utils.SusVec3;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

import java.util.List;

public class PacketReactionIncreaserActivated extends PacketEnableImplantSync {
    SusVec3 directionVector;
    public PacketReactionIncreaserActivated() {
    }

    public PacketReactionIncreaserActivated(int slotId, int abilityId, SusVec3 directionVector) {
        super(slotId, abilityId);
        this.directionVector = directionVector;
    }


    public void toBytes(ByteBuf buffer) {
        super.toBytes(buffer);
        directionVector.writeTo(buffer);
    }


    public void fromBytes(ByteBuf buffer) {
        super.fromBytes(buffer);
        directionVector = SusVec3.readFrom(buffer);
    }

    public static class Handler
            implements IMessageHandler<PacketReactionIncreaserActivated, IMessage> {
        public IMessage onMessage(PacketReactionIncreaserActivated message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().playerEntity;
            CybersusPlayerExtendedData data = CybersusPlayerExtendedData.get(player);
            if (data != null) {
                ItemStack implant = data.implantStorage.getStackInSlot(message.slotId);
                if (implant != null) {
                    ItemImplant implantClass = (ItemImplant) implant.getItem();
                    List<Ability> abilities = implantClass.getAbilities(player, message.slotId, implant);
                    if (abilities.size() > message.abilityId && implantClass instanceof ImplantReactionIncreaser) {
                        AbilityAcceleration abilityAcceleration = (AbilityAcceleration) abilities.get(0);
                        abilityAcceleration.onEnableButtonClickedCustom(player, message.slotId, implant, message.directionVector);
                    }
                }
            }
            return null;
        }
    }
}


