package com.suslovila.cybersus.client;

import com.suslovila.cybersus.api.implants.ability.Ability;
import com.suslovila.cybersus.api.process.WorldProcess;
import com.suslovila.cybersus.common.item.implants.ImplantShadowSkin;
import com.suslovila.cybersus.common.processes.ProcessGravityTrap;
import com.suslovila.cybersus.extendedData.CustomWorldData;
import com.suslovila.cybersus.extendedData.CybersusPlayerExtendedData;
import com.suslovila.cybersus.utils.KhariumSusNBTHelper;
import com.suslovila.cybersus.utils.SusObjectWrapper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovementInput;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.entity.living.LivingEvent;

import java.util.*;

import static com.suslovila.cybersus.common.item.implants.ImplantShadowSkin.PREPARATION_TIMER;


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