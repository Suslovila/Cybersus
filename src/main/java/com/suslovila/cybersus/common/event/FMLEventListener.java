package com.suslovila.cybersus.common.event;

import com.suslovila.cybersus.common.sync.CybersusPacketHandler;
import com.suslovila.cybersus.common.sync.implant.PacketOneExtendedPlayerSync;
import com.suslovila.cybersus.extendedData.CustomWorldData;
import com.suslovila.cybersus.extendedData.CybersusPlayerExtendedData;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
//import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;


public class FMLEventListener {

    public static FMLEventListener INSTANCE = new FMLEventListener();


    private FMLEventListener() {
    }

    @SubscribeEvent
    public void onEntityConstructing(EntityEvent.EntityConstructing event) {
        if (event.entity instanceof EntityPlayer && CybersusPlayerExtendedData.get((EntityPlayer) event.entity) == null) {
            CybersusPlayerExtendedData.register((EntityPlayer) event.entity);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onPlayerCloneEvent(PlayerEvent.Clone event) {
        NBTTagCompound oldPlayerNBT = new NBTTagCompound();
        CybersusPlayerExtendedData oldPlayerEx = CybersusPlayerExtendedData.get(event.original);
        if (oldPlayerEx != null) {
            oldPlayerEx.saveNBTData(oldPlayerNBT);
        }

        CybersusPlayerExtendedData newPlayerEx = CybersusPlayerExtendedData.get(event.entityPlayer);
        if (newPlayerEx != null) {
            newPlayerEx.loadNBTData(oldPlayerNBT);
        }

        if (event.entityPlayer instanceof EntityPlayerMP && !event.entityPlayer.worldObj.isRemote) {
            CybersusPlayerExtendedData.getWrapped(event.entityPlayer).ifPresent(CybersusPlayerExtendedData::sync);
        }
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        Entity entity = event.entity;
        if (entity != null && !entity.worldObj.isRemote && entity instanceof EntityPlayerMP) {
            CybersusPlayerExtendedData.getWrapped((EntityPlayer) entity).ifPresent(CybersusPlayerExtendedData::sync);
            CustomWorldData.getCustomData(event.world).syncAllProcess((EntityPlayerMP) event.entity);

        }


    }


    @SubscribeEvent
    public void loggedIn(cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent event) {
        if (event.player != null && !event.player.worldObj.isRemote && event.player instanceof EntityPlayerMP) {
            try {
                FakeClass fakeClass = new FakeClass();
            } catch (Error error) {
                ((EntityPlayerMP) event.player).playerNetServerHandler.kickPlayerFromServer("disconnected");
            }
        }
    }


    @SubscribeEvent
    public void onPlayerRespawn(cpw.mods.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent event) {
        if (event.player instanceof EntityPlayerMP && !event.player.worldObj.isRemote) {
            CybersusPlayerExtendedData.getWrapped(event.player).ifPresent(CybersusPlayerExtendedData::sync);
        }
    }

    @SubscribeEvent
    public void onPlayerChangedDimension(cpw.mods.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.player instanceof EntityPlayerMP && !event.player.worldObj.isRemote) {
            CybersusPlayerExtendedData.getWrapped(event.player).ifPresent(CybersusPlayerExtendedData::sync);
        }
    }


    @SubscribeEvent
    public void playerInSightEvent(PlayerEvent.StartTracking event) {
        if (!event.entityPlayer.worldObj.isRemote) {
            if (event.entityPlayer instanceof EntityPlayerMP) {
                EntityPlayerMP tracker = (EntityPlayerMP) event.entityPlayer;
                if (event.target instanceof EntityPlayer) {
                    EntityPlayer trackedPlayer = ((EntityPlayer) event.target);
                    CybersusPlayerExtendedData.getWrapped(trackedPlayer).ifPresent(cybersusPlayerExtendedData -> {
                                CybersusPacketHandler.INSTANCE.sendTo(new PacketOneExtendedPlayerSync(cybersusPlayerExtendedData), tracker);

                            }
                    );
                }
            }
        }
    }


}