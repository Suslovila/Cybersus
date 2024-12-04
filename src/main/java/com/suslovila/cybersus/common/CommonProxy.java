package com.suslovila.cybersus.common;

import com.suslovila.cybersus.api.process.ProcessRegistry;
import com.suslovila.cybersus.client.clientProcess.processes.shadowGates.ProcessShadowGates;
import com.suslovila.cybersus.common.block.ModBlocks;
import com.suslovila.cybersus.common.event.*;
import com.suslovila.cybersus.common.item.ModItems;
import com.suslovila.cybersus.common.processes.ProcessGravityTrap;
import com.suslovila.cybersus.common.processes.ProcessIllusion;
import com.suslovila.cybersus.common.processes.ProcessSoulBreak;
import com.suslovila.cybersus.common.sync.CybersusPacketHandler;
import com.suslovila.cybersus.research.CybersusAspect;
import com.suslovila.cybersus.utils.config.ConfigImlants;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        CybersusPacketHandler.init();

        ConfigImlants.registerServerConfig(event.getSuggestedConfigurationFile());

        FMLCommonHandler.instance().bus().register(FMLEventListener.INSTANCE);
        MinecraftForge.EVENT_BUS.register(FMLEventListener.INSTANCE);

        FMLCommonHandler.instance().bus().register(Icons.INSTANCE);
        MinecraftForge.EVENT_BUS.register(Icons.INSTANCE);

        FMLCommonHandler.instance().bus().register(ImplantEvents.INSTANCE);
        MinecraftForge.EVENT_BUS.register(ImplantEvents.INSTANCE);

        FMLCommonHandler.instance().bus().register(CommonProcessHandler.INSTANCE);
        MinecraftForge.EVENT_BUS.register(CommonProcessHandler.INSTANCE);


        ModBlocks.register();
        ModItems.register();

        MinecraftForge.EVENT_BUS.register(ModItems.portableMultiAspectContainer);

        CybersusAspect.initAspects();

        ProcessRegistry.registerProcess(ProcessSoulBreak.class, "soul_break");
        ProcessRegistry.registerProcess(ProcessGravityTrap.class, "gravity_trap");
        ProcessRegistry.registerProcess(ProcessShadowGates.class, "shadow_gates");
        ProcessRegistry.registerProcess(ProcessIllusion.class, "illusion");



    }


    public void init(FMLInitializationEvent event) {

    }

    public void postInit(FMLPostInitializationEvent event) {
        CybersusAspect.initItemsAspects();

    }

    public void registerRenderers() {}
}

