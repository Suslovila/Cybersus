package com.suslovila.cybersus.extendedData;

import com.suslovila.cybersus.api.implants.ImplantStorage;
import com.suslovila.cybersus.common.sync.CybersusPacketHandler;
import com.suslovila.cybersus.common.sync.implant.PacketOneExtendedPlayerSync;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

import java.util.Optional;

public class CybersusPlayerExtendedData implements IExtendedEntityProperties {
    private static final String EXT_PROP_NAME = "CybersusExtendedPlayer";
    public final EntityPlayer player;
    public ImplantStorage implantStorage;

    public CybersusPlayerExtendedData(EntityPlayer player) {
        this.player = player;
        this.implantStorage = new ImplantStorage();
    }

    public static CybersusPlayerExtendedData get(EntityPlayer player) {
        return (CybersusPlayerExtendedData) player.getExtendedProperties(EXT_PROP_NAME);
    }
    public static Optional<CybersusPlayerExtendedData> getWrapped(EntityPlayer player) {
        return Optional.ofNullable((CybersusPlayerExtendedData) player.getExtendedProperties(EXT_PROP_NAME));
    }
    public static void register(EntityPlayer player) {
        player.registerExtendedProperties(EXT_PROP_NAME, new CybersusPlayerExtendedData(player));
    }

    public static void loadProxyData(EntityPlayer player) {
        if (player != null) {
            CybersusPlayerExtendedData playerEx = get(player);
            if (playerEx != null) {
                playerEx.sync();
            }
        }
    }

    @Override
    public void init(Entity entity, World world) {
    }

    @Override
    public void saveNBTData(NBTTagCompound rootTag) {
        NBTTagCompound properties = new NBTTagCompound();
        implantStorage.writeToNBT(rootTag);
        rootTag.setTag(EXT_PROP_NAME, properties);
    }

    @Override
    public void loadNBTData(NBTTagCompound rootTag) {
        if (rootTag.hasKey(EXT_PROP_NAME)) {
            implantStorage.readFromNBT(rootTag);
        }
    }

    public void writeTo(ByteBuf buf) {
        implantStorage.writeTo(buf);
    }

    public void sync() {
        if (!player.worldObj.isRemote) {
            CybersusPacketHandler.INSTANCE.sendToAll(new PacketOneExtendedPlayerSync(this));
        }
    }

    // Getters and setters
}


