package com.suslovila.cybersus.api.process;

import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import java.util.UUID;

public abstract class WorldProcess {
    public UUID uuid;

    public WorldProcess() {
        uuid = UUID.randomUUID();
    }

    public void tickServer(WorldTickEvent event) {
    }

    public void tickClient(TickEvent.ClientTickEvent event) {
    }

    public boolean isExpired(TickEvent.WorldTickEvent event) {
        return false;
    }

    public boolean isExpired(TickEvent.ClientTickEvent event) {
        return false;
    }

    public void onExpired(TickEvent.ClientTickEvent event) {
    }

    public void onExpired(TickEvent.WorldTickEvent event) {
    }

    public void render(RenderWorldLastEvent event) {
    }

    public abstract String getTypeId();

}

