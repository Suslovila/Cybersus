package com.suslovila.cybersus.client;

import com.suslovila.cybersus.client.clientProcess.ClientImplantEvents;
import com.suslovila.cybersus.client.gui.GuiImplants;
import com.suslovila.cybersus.common.CommonProxy;
import com.suslovila.cybersus.common.block.ModBlocks;
import com.suslovila.cybersus.common.event.ImplantEvents;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);

        FMLCommonHandler.instance().bus().register(GuiImplants.INSTANCE);
        MinecraftForge.EVENT_BUS.register(GuiImplants.INSTANCE);

        FMLCommonHandler.instance().bus().register(ClientProcessHandler.INSTANCE);
        MinecraftForge.EVENT_BUS.register(ClientProcessHandler.INSTANCE);

        FMLCommonHandler.instance().bus().register(ClientImplantEvents.INSTANCE);
        MinecraftForge.EVENT_BUS.register(ClientImplantEvents.INSTANCE);

    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        ModBlocks.registerRender();
        setupItemRenderers();

        KeyHandler.register();

    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }

    @Override
    public void registerRenderers() {
    }

    private void setupItemRenderers() {
    }
}

