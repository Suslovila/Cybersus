package com.suslovila.cybersus.client;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SimpleTexture;

import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.event.TextureStitchEvent;


import java.util.*;



public class ClientEvents {
    public List<ResourceLocation> preLoadedResourceLocations = new ArrayList<>();

    public static ClientEvents INSTANCE = new ClientEvents();

    private ClientEvents() {
    }

    @SubscribeEvent
    public void onTextureStitch(TextureStitchEvent.Post event) {
        for (ResourceLocation resourceLocation : preLoadedResourceLocations) {
            Minecraft.getMinecraft().getTextureManager().loadTexture(resourceLocation, new SimpleTexture(resourceLocation));
        }
    }

}