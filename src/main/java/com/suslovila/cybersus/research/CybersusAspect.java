package com.suslovila.cybersus.research;

import com.emoniph.witchery.Witchery;
import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.common.item.CybersusItems;
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


    private static AspectList aspectListOfAllMax = null;
    public static AspectList getAspectListOfAllMax() {
        if(aspectListOfAllMax == null) {
            aspectListOfAllMax = new AspectList();
            for(String key : Aspect.aspects.keySet()) {
                Aspect aspect = Aspect.aspects.get(key);
                aspectListOfAllMax.add(aspect, Integer.MAX_VALUE);
            }
        }
        return aspectListOfAllMax;
    }
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


        addAspectsToItem(new ItemStack(CybersusItems.synthDerm, 1), new AspectList().add(Aspect.FLESH, 16).add(Aspect.MECHANISM, 16));
        addAspectsToItem(new ItemStack(CybersusItems.synthGlass, 1), new AspectList().add(Aspect.SENSES, 16).add(Aspect.CRYSTAL, 16).add(Aspect.MECHANISM, 16));
        addAspectsToItem(new ItemStack(CybersusItems.synthNerv, 1), new AspectList().add(Aspect.SENSES, 24).add(Aspect.MECHANISM, 24));
        addAspectsToItem(new ItemStack(CybersusItems.synaptite, 1), new AspectList().add(Aspect.SENSES, 24).add(Aspect.MAGIC, 24));
        addAspectsToItem(new ItemStack(CybersusItems.icarusBlood, 1), new AspectList().add(SANGUINO, 32).add(Aspect.MECHANISM, 16).add(Aspect.SLIME, 8));
        addAspectsToItem(new ItemStack(CybersusItems.alloyX87, 1), new AspectList().add(Aspect.METAL, 64).add(Aspect.MECHANISM, 32));
        addAspectsToItem(new ItemStack(CybersusItems.gravitationDust, 1), new AspectList().add(Aspect.ORDER, 8).add(GRAVITAS, 4));
        addAspectsToItem(new ItemStack(CybersusItems.gravityIcreaser, 1), new AspectList().add(GRAVITAS, 32).add(Aspect.MECHANISM, 16));
        addAspectsToItem(new ItemStack(CybersusItems.myomass, 1), new AspectList().add(Aspect.ENERGY, 16).add(Aspect.MECHANISM, 32).add(Aspect.MOTION, 16));
        addAspectsToItem(new ItemStack(CybersusItems.phasolite, 1), new AspectList().add(Aspect.EXCHANGE, 16).add(Aspect.METAL, 16).add(Aspect.CRYSTAL, 16).add(Aspect.WATER, 16));
        addAspectsToItem(new ItemStack(CybersusItems.berserkHeart, 1), new AspectList().add(Aspect.WEAPON, 32).add(Aspect.ARMOR, 32).add(Aspect.MECHANISM, 32));
        addAspectsToItem(new ItemStack(CybersusItems.portableMultiAspectContainer, 1), new AspectList().add(HUMILITAS, 16).add(Aspect.WATER, 32).add(Aspect.TRAP, 16));
        addAspectsToItem(new ItemStack(CybersusItems.portablesingleAspectContainer, 1), new AspectList().add(HUMILITAS, 16).add(Aspect.WATER, 32).add(Aspect.TRAP, 16));
        addAspectsToItem(new ItemStack(CybersusItems.exploder, 1), new AspectList().add(Aspect.ENERGY, 16).add(Aspect.MECHANISM, 16));
        addAspectsToItem(new ItemStack(CybersusItems.shadowSkin, 1), new AspectList().add(Aspect.DARKNESS, 32).add(Aspect.FLESH, 16).add(Aspect.MECHANISM, 8));
        addAspectsToItem(new ItemStack(CybersusItems.phoenixHeart, 1), new AspectList().add(SANGUINO, 16).add(Aspect.ENERGY, 16).add(Aspect.MOTION, 16).add(Aspect.MECHANISM, 16).add(Aspect.LIFE, 64).add(Aspect.FIRE, 64));
        addAspectsToItem(new ItemStack(CybersusItems.motherboardBlank, 1), new AspectList().add(Aspect.MIND, 32).add(Aspect.MECHANISM, 32).add(Aspect.SENSES, 16));
        addAspectsToItem(new ItemStack(CybersusItems.heartBlank, 1), new AspectList().add(SANGUINO, 16).add(Aspect.ENERGY, 16).add(Aspect.MOTION, 16).add(Aspect.MECHANISM, 16));
        addAspectsToItem(new ItemStack(CybersusItems.illusionGenerator, 1), new AspectList().add(Aspect.MIND, 64).add(Aspect.TRAP, 64).add(Aspect.MECHANISM, 64));
        addAspectsToItem(new ItemStack(CybersusItems.reactionIncreaser, 1), new AspectList().add(Aspect.SENSES, 64).add(Aspect.MOTION, 64).add(Aspect.MECHANISM, 64));

        if(Cybersus.forbiddenMagicLoaded) {
            addAspectsToItem(new ItemStack(CybersusItems.implantSinHeart, 1), new AspectList().add(Aspect.SOUL, 32).add(Aspect.DEATH, 16));
        }
        if(Cybersus.witcheryLoaded) {
            addAspectsToItem(new ItemStack(CybersusItems.sleepModule, 1), new AspectList().add(Aspect.SOUL, 16).add(Aspect.TRAP, 16).add(Aspect.MECHANISM, 16));
            addAspectsToItem(new ItemStack(CybersusItems.tormentor, 1), new AspectList().add(Aspect.TRAP, 16).add(Aspect.DEATH, 16).add(Aspect.SOUL, 16).add(Aspect.MECHANISM, 16));
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