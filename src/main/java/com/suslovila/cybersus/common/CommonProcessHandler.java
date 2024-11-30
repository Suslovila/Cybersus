package com.suslovila.cybersus.common;

import com.suslovila.cybersus.api.process.WorldProcess;
import com.suslovila.cybersus.extendedData.CustomWorldData;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;


public class CommonProcessHandler {

    public static CommonProcessHandler INSTANCE = new CommonProcessHandler();

    private CommonProcessHandler() {
    }

    @SubscribeEvent
    public void worldTick(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            CustomWorldData customWorldData = CustomWorldData.getCustomData(event.world);
            customWorldData.foreachProcess((uuid, process) -> {
                process.tickServer(event);
            });
            Iterator<Map.Entry<String, HashMap<UUID, WorldProcess>>> iteratorInWhole = customWorldData.processesMapsByType.entrySet().iterator();
            while (iteratorInWhole.hasNext()) {
                Map.Entry<String, HashMap<UUID, WorldProcess>> entry = iteratorInWhole.next();
                Iterator<Map.Entry<UUID, WorldProcess>> oneTypeIterator = entry.getValue().entrySet().iterator();
                while (oneTypeIterator.hasNext()) {
                    WorldProcess process = oneTypeIterator.next().getValue();
                    if (process.isExpired(event)) {
                        oneTypeIterator.remove();
                        process.onExpired(event);
                        customWorldData.markDirty();

                    }
                }

            }
        }
    }
}