package com.suslovila.cybersus.research;

import com.emoniph.witchery.Witchery;
import com.suslovila.cybersus.Cybersus;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.config.ConfigItems;

import java.awt.*;
import java.util.Iterator;

public class CybersusAspect {
    public static Aspect DIMENSIO;
    public static Aspect SANGUINO;

    public static Aspect HUMILITAS;

    public static Aspect GRAVITAS;


    public static void initAspects() {
        HUMILITAS = new Aspect("Humilitas", 16727457, new Aspect[]{Aspect.TRAP, Aspect.VOID}, new ResourceLocation(Cybersus.MOD_ID, "textures/aspect/humilitas.png"), 1) {

        };

            DIMENSIO = new Aspect("Dimensio", new Color(79, 255, 169).getRGB(), new Aspect[]{Aspect.VOID, Aspect.TRAVEL}, new ResourceLocation(Cybersus.MOD_ID, "textures/gui/dimensio.png"), 1) {
        };

        SANGUINO = new Aspect("Sanguino", new Color(240, 0, 0).getRGB(), new Aspect[]{Aspect.LIFE, Aspect.METAL}, new ResourceLocation(Cybersus.MOD_ID, "textures/gui/sanguine.png"), 1) {
        };

        GRAVITAS = new Aspect("Gravitas", new Color(71, 132, 255).getRGB(), new Aspect[]{Aspect.ENERGY, Aspect.EARTH}, new ResourceLocation(Cybersus.MOD_ID, "textures/gui/gravitas.png"), 1) {
        };
    }

    public static void initItemsAspects() {

        Iterator<Block> blockIterator = GameData.getBlockRegistry().iterator();
        while (blockIterator.hasNext()) {
            Block block = blockIterator.next();
            if(block instanceof BlockFalling) {
                addAspectsToItem(new ItemStack(block), new AspectList().add(GRAVITAS, 1));

            }
        }

        addAspectsToItem(new ItemStack(Items.porkchop), new AspectList().add(SANGUINO, 1));
        addAspectsToItem(new ItemStack(Items.beef), new AspectList().add(SANGUINO, 1));
        addAspectsToItem(new ItemStack(Items.ender_pearl), new AspectList().add(DIMENSIO, 2));
        addAspectsToItem(new ItemStack(Items.ender_eye), new AspectList().add(DIMENSIO, 2));

        addAspectsToItem(new ItemStack(Items.nether_star), new AspectList().add(HUMILITAS, 8));
        addAspectsToItem(new ItemStack(ConfigItems.itemEldritchObject, 1, 3), new AspectList().add(HUMILITAS, 16));

        if(Cybersus.witcheryLoaded) {
//            addAspectsToItem(new ItemStack(Witchery.Items.BLOOD_GOBLET, 1,1), new AspectList().add(SANGUINO, 12));
            addAspectsToItem(Witchery.Items.GENERIC.itemEnderDew.createStack(), new AspectList().add(DIMENSIO, 3));

        }
    }

    private static void addAspectsToItem(ItemStack itemStack, AspectList aspectsToAdd) {
        AspectList list = getAspectList(itemStack);
        list.add(aspectsToAdd);
        ThaumcraftApi.registerObjectTag(itemStack, list);
    }

    public static AspectList getAspectList(ItemStack stack) {
        AspectList list = ThaumcraftApiHelper.getObjectAspects(stack);
        if(list == null) return new AspectList();
        return list;
    }
}