
package com.suslovila.cybersus;

import com.suslovila.cybersus.client.gui.CybersusGuiHandler;
import com.suslovila.cybersus.common.CommonProxy;
import com.suslovila.cybersus.common.item.ModItems;
import com.suslovila.cybersus.research.CybersusResearchRegistry;
import com.suslovila.cybersus.utils.NbtKeyNameHelper;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

import java.util.Random;

@Mod(name = Cybersus.NAME, modid = Cybersus.MOD_ID, version = Cybersus.VERSION, dependencies = "after:AWWayofTime;after:Botania;after:witchery;after:Thaumcraft;after:ForbiddenMagic;after:TravellersGear")
public class Cybersus {
    public static final String NAME = "cybersus";
    public static final String MOD_ID = "cybersus";
    public static final String VERSION = "1.0";

    public static boolean thaumcraftLoaded = false;
    public static boolean witcheryLoaded = false;
    public static boolean bloodMagicLoaded = false;
    public static boolean botaniaLoaded = false;
    public static boolean forbiddenMagicLoaded = false;
    public static boolean travellersGearLoaded = false;

    public static Random random = new Random();

    public static final NbtKeyNameHelper prefixAppender = new NbtKeyNameHelper(MOD_ID);


    public static final CreativeTabs tab = new CreativeTabs(NAME) {
        @Override
        public Item getTabIconItem() {
            if (Cybersus.thaumcraftLoaded) {
                return ModItems.heartBlank;
            }
            return Items.redstone;
        }
    };

    @Mod.Instance(MOD_ID)
    public static Cybersus instance;

    @SidedProxy(clientSide = "com.suslovila.cybersus.client.ClientProxy", serverSide = "com.suslovila.cybersus.common.CommonProxy")
    public static CommonProxy proxy;


    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        witcheryLoaded = Loader.isModLoaded("witchery");
        botaniaLoaded = Loader.isModLoaded("Botania");
        thaumcraftLoaded = Loader.isModLoaded("Thaumcraft");
        bloodMagicLoaded = Loader.isModLoaded("AWWayofTime");
        forbiddenMagicLoaded = Loader.isModLoaded("ForbiddenMagic");
        travellersGearLoaded = Loader.isModLoaded("TravellersGear");

        proxy.preInit(event);
    }


    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
        proxy.registerRenderers();

        NetworkRegistry.INSTANCE.registerGuiHandler(this, new CybersusGuiHandler());

    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);

        if (thaumcraftLoaded) {
            CybersusResearchRegistry.integrateInfusion();
            CybersusResearchRegistry.integrateCrucibleRecipe();
            CybersusResearchRegistry.integrateResearch();
        }
    }
}
