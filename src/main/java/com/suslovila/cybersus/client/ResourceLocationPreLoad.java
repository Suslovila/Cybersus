package com.suslovila.cybersus.client;

import net.minecraft.util.ResourceLocation;

public class ResourceLocationPreLoad extends ResourceLocation {
    public ResourceLocationPreLoad(String path) {
        super(path);
        ClientEvents.INSTANCE.preLoadedResourceLocations.add(this);
    }
    public ResourceLocationPreLoad(String MOD_ID, String path) {
        super(MOD_ID, path);
        ClientEvents.INSTANCE.preLoadedResourceLocations.add(this);
    }
}
