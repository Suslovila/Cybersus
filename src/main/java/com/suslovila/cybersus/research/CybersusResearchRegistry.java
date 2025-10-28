package com.suslovila.cybersus.research;

import com.emoniph.witchery.Witchery;
import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.api.implants.ability.Ability;
import com.suslovila.cybersus.common.item.ItemImplant;
import com.suslovila.cybersus.common.item.CybersusItems;
import com.suslovila.cybersus.common.item.implants.sinHeart.ImplantSinHeart;
import fox.spiteful.forbidden.DarkAspects;
import fox.spiteful.forbidden.items.ForbiddenItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchPage;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;

import java.util.HashMap;
import java.util.List;

public class CybersusResearchRegistry {

    private static HashMap<String, InfusionRecipe> runicMatrixRecipes = new HashMap<>();
    private static HashMap<String, CrucibleRecipe> crucibleRecipes = new HashMap<>();

    // ItemStacks
//    private static final ItemStack essentiaReservoirVoid = ItemStack(ModBlocks.BlockEssentiaReservoirVoid, 1, 0);


    //    public static ResearchItem aspectContainers;
    private static final String cybersusCategory = Cybersus.MOD_ID;
    private static final String basicInfo = "CYBERSUS_BASIC_INFO";

    public static final String blankHeartKey = "BLANK_HEART";
    public static final String shadowSkinKey = "SHADOW_SKIN";

    public static final String gravityIncreaserKey = "GRAVITY_INCREASER";
    public static final String aspectHoldersKey = "PORTABLE_ASPECT_HOLDERS";
    public static final String multiAspectHolderKey = "MULTI_ASPECT_HOLDER";
    public static final String singleAspectHolderKey = "SINGLE_ASPECT_HOLDER";
    public static final String motherboardBlankKey = "BLANK_MOTHERBOARD";
    public static final String phoenixHeartKey = "PHOENIX_HEART";
    public static final String sinHeartKey = "SIN_HEART";
    public static final String sleepModuleKey = "SLEEP_MODULE";
    public static final String tormentorKey = "TORMENTOR";
    public static final String berserkHeartKey = "BERSERK_HEART";
    public static final String blankEyeKey = "BLANK_EYE";
    public static final String mindExploderKey = "MIND_EXPLODER";
    public static final String illusionGeneratorKey = "ILLUSION_GENERATOR";
    public static final String reactionIncreaserKey = "REACTION_INCREASER";
    public static final String alloyX87Key = "ALLOY_X87";
    public static final String synthGlassKey = "SYNTH_GLASS";
    public static final String synthDermKey = "SYNTH_DERM";
    public static final String synthNervKey = "SYNTH_NERV";
    public static final String myomassKey = "MYOMASS";
    public static final String icarusBloodKey = "ICARUS_BLOOD";


    public static void integrateCrucibleRecipe() {
        if (Cybersus.forbiddenMagicLoaded) {
            for (int i = 0; i < ImplantSinHeart.sinAspects.size(); i++) {
                CrucibleRecipe recipe = ThaumcraftApi.addCrucibleRecipe(sinHeartKey, new ItemStack(CybersusItems.implantSinHeart, 1, i), new ItemStack(CybersusItems.implantSinHeart, 1, ImplantSinHeart.sinAspects.size()), (new AspectList()).add(DarkAspects.NETHER, 128).add(ImplantSinHeart.sinAspects.get(i), 256));
                crucibleRecipes.put(sinHeartKey + i, recipe);
            }
        }
    }

    public static void addCrucibleRecipe(Item item, int count, int damage, String key, ItemStack catalyst, AspectList aspects) {
        CrucibleRecipe recipe = ThaumcraftApi.addCrucibleRecipe(key, new ItemStack(item, count, damage), catalyst, aspects);
        crucibleRecipes.put(key, recipe);
    }

    public static void integrateInfusion() {
        runicMatrixRecipes.put(blankHeartKey, ThaumcraftApi.addInfusionCraftingRecipe(
                blankHeartKey,
                new ItemStack(CybersusItems.heartBlank),
                9,
                new AspectList().add(Aspect.LIFE, 64).add(Aspect.ENERGY, 64).add(Aspect.MECHANISM, 256).add(Aspect.MAGIC, 92).add(Aspect.MAN, 16),
                new ItemStack(ConfigItems.itemEldritchObject, 1, 3),
                new ItemStack[]{
                        new ItemStack(ConfigItems.itemResource, 1, 15),
                        new ItemStack(ConfigItems.itemResource, 1, 16), new ItemStack(ConfigItems.itemResource, 1, 16), new ItemStack(ConfigItems.itemResource, 1, 16),
                        new ItemStack(CybersusItems.synthDerm, 1), new ItemStack(CybersusItems.synthDerm, 1), new ItemStack(CybersusItems.synthDerm, 1),
                        new ItemStack(CybersusItems.icarusBlood, 1),
                        new ItemStack(CybersusItems.myomass, 1), new ItemStack(CybersusItems.myomass, 1), new ItemStack(CybersusItems.myomass, 1)
                }
        ));

        runicMatrixRecipes.put(shadowSkinKey, ThaumcraftApi.addInfusionCraftingRecipe(
                shadowSkinKey,
                new ItemStack(CybersusItems.shadowSkin),
                7,
                new AspectList().add(Aspect.DARKNESS, 256).add(Aspect.EXCHANGE, 64).add(Aspect.MECHANISM, 24).add(Aspect.MAGIC, 72).add(Aspect.MAN, 16).add(Aspect.VOID, 128),
                new ItemStack(CybersusItems.synthDerm),
                new ItemStack[]{new ItemStack(ConfigItems.itemResource, 1, 17), new ItemStack(ConfigItems.itemResource, 1, 16), new ItemStack(ConfigItems.itemResource, 1, 17),
                        new ItemStack(ConfigItems.itemResource, 1, 16), new ItemStack(ConfigItems.itemResource, 1, 17), new ItemStack(ConfigItems.itemResource, 1, 16)}
        ));

        runicMatrixRecipes.put(gravityIncreaserKey, ThaumcraftApi.addInfusionCraftingRecipe(
                gravityIncreaserKey,
                new ItemStack(CybersusItems.gravityIcreaser),
                8,
                new AspectList().add(CybersusAspect.GRAVITAS, 512).add(Aspect.MAGIC, 32).add(Aspect.MECHANISM, 12).add(Aspect.TRAP, 64).add(Aspect.ENERGY, 128).add(Aspect.EARTH, 128).add(Aspect.ORDER, 64),
                new ItemStack(CybersusItems.alloyX87),
                new ItemStack[]{new ItemStack(ConfigItems.itemResource, 1, 16), new ItemStack(CybersusItems.gravitationDust, 1),  new ItemStack(CybersusItems.gravitationDust, 1),
                        new ItemStack(ConfigItems.itemFocusPortableHole),
                        new ItemStack(ConfigItems.itemResource, 1, 16),  new ItemStack(CybersusItems.gravitationDust, 1),  new ItemStack(CybersusItems.gravitationDust, 1)}
        ));


        runicMatrixRecipes.put(multiAspectHolderKey, ThaumcraftApi.addInfusionCraftingRecipe(
                aspectHoldersKey,
                new ItemStack(CybersusItems.portableMultiAspectContainer),
                6,
                new AspectList().add(CybersusAspect.GRAVITAS, 64).add(CybersusAspect.HUMILITAS, 64).add(Aspect.MECHANISM, 64).add(Aspect.VOID, 256).add(Aspect.ENERGY, 128).add(Aspect.ORDER, 64).add(Aspect.WATER, 128),
                new ItemStack(ConfigBlocks.blockJar),
                new ItemStack[]{
                        new ItemStack(Items.gold_ingot), new ItemStack(ConfigItems.itemResource, 1, 16), new ItemStack(ConfigBlocks.blockMetalDevice, 1, 3),
                        new ItemStack(ConfigBlocks.blockCosmeticOpaque, 1, 2), new ItemStack(ConfigItems.itemResource, 1, 16), new ItemStack(Items.gold_ingot),
                        new ItemStack(ConfigBlocks.blockCosmeticOpaque, 1, 2), new ItemStack(ConfigItems.itemResource, 1, 16), new ItemStack(ConfigBlocks.blockMetalDevice, 1, 3),
                        new ItemStack(ConfigBlocks.blockCosmeticOpaque, 1, 2), new ItemStack(ConfigItems.itemResource, 1, 16), new ItemStack(ConfigBlocks.blockMetalDevice, 1, 3),
                }
        ));

        runicMatrixRecipes.put(singleAspectHolderKey, ThaumcraftApi.addInfusionCraftingRecipe(
                aspectHoldersKey,
                new ItemStack(CybersusItems.portablesingleAspectContainer),
                6,
                new AspectList().add(CybersusAspect.GRAVITAS, 64).add(CybersusAspect.HUMILITAS, 64).add(Aspect.MECHANISM, 64).add(Aspect.VOID, 256).add(Aspect.ENERGY, 128).add(Aspect.ORDER, 64).add(Aspect.WATER, 128),
                new ItemStack(ConfigBlocks.blockJar),
                new ItemStack[]{
                        new ItemStack(Items.iron_ingot), new ItemStack(ConfigItems.itemResource, 1, 16), new ItemStack(ConfigBlocks.blockMetalDevice, 1, 3),
                        new ItemStack(ConfigBlocks.blockCosmeticOpaque, 1, 2), new ItemStack(ConfigItems.itemResource, 1, 16), new ItemStack(Items.iron_ingot),
                        new ItemStack(ConfigBlocks.blockCosmeticOpaque, 1, 2), new ItemStack(ConfigItems.itemResource, 1, 16), new ItemStack(ConfigBlocks.blockMetalDevice, 1, 3),
                        new ItemStack(ConfigBlocks.blockCosmeticOpaque, 1, 2), new ItemStack(ConfigItems.itemResource, 1, 16), new ItemStack(ConfigBlocks.blockMetalDevice, 1, 3),
                }
        ));

        runicMatrixRecipes.put(motherboardBlankKey, ThaumcraftApi.addInfusionCraftingRecipe(
                motherboardBlankKey,
                new ItemStack(CybersusItems.motherboardBlank),
                6,
                new AspectList().add(Aspect.MECHANISM, 128).add(Aspect.MIND, 256).add(Aspect.ENERGY, 128).add(Aspect.ORDER, 64).add(Aspect.EXCHANGE, 16),
                new ItemStack(CybersusItems.alloyX87, 1),
                new ItemStack[]{
                        new ItemStack(Items.gold_ingot), new ItemStack(ConfigItems.itemResource, 1, 16), new ItemStack(Items.redstone), new ItemStack(CybersusItems.synaptite),
                        new ItemStack(Items.gold_ingot), new ItemStack(ConfigItems.itemResource, 1, 16), new ItemStack(Items.redstone), new ItemStack(CybersusItems.synaptite),
                        new ItemStack(Items.gold_ingot), new ItemStack(ConfigItems.itemResource, 1, 16), new ItemStack(Items.redstone), new ItemStack(CybersusItems.synaptite),
                        new ItemStack(Items.gold_ingot), new ItemStack(ConfigItems.itemResource, 1, 16), new ItemStack(Items.redstone), new ItemStack(CybersusItems.synaptite),
                }
        ));

        runicMatrixRecipes.put(alloyX87Key, ThaumcraftApi.addInfusionCraftingRecipe(
                alloyX87Key,
                new ItemStack(CybersusItems.alloyX87),
                8,
                new AspectList().add(Aspect.LIFE, 64).add(Aspect.MAGIC, 128).add(Aspect.MECHANISM, 128).add(CybersusAspect.HUMILITAS, 8).add(Aspect.METAL, 256),
                new ItemStack(ConfigItems.itemResource, 1, 16),
                new ItemStack[]{
                        new ItemStack(CybersusItems.synaptite), new ItemStack(Items.iron_ingot), new ItemStack(Items.redstone),
                        new ItemStack(CybersusItems.synaptite), new ItemStack(Items.iron_ingot), new ItemStack(Items.redstone),
                }
        ));

        runicMatrixRecipes.put(phoenixHeartKey, ThaumcraftApi.addInfusionCraftingRecipe(
                phoenixHeartKey,
                new ItemStack(CybersusItems.phoenixHeart),
                6,
                new AspectList().add(Aspect.FIRE, 128).add(Aspect.MAGIC, 128).add(Aspect.LIFE, 256).add(Aspect.EXCHANGE, 256).add(Aspect.MECHANISM, 64),
                new ItemStack(CybersusItems.heartBlank),
                new ItemStack[]{
                        new ItemStack(Items.blaze_powder), new ItemStack(Items.feather), new ItemStack(Items.redstone),
                        new ItemStack(Items.blaze_powder), new ItemStack(Items.feather), new ItemStack(Items.redstone),
                }
        ));

        if (Cybersus.forbiddenMagicLoaded) {
            runicMatrixRecipes.put(sinHeartKey, ThaumcraftApi.addInfusionCraftingRecipe(
                    sinHeartKey,
                    new ItemStack(CybersusItems.implantSinHeart, 1, ImplantSinHeart.sinAspects.size()),
                    25,
                    new AspectList().add(DarkAspects.NETHER, 256).add(Aspect.MAGIC, 256).add(Aspect.EXCHANGE, 256).add(Aspect.SOUL, 256),
                    new ItemStack(CybersusItems.heartBlank),
                    new ItemStack[]{
                            new ItemStack(ConfigItems.itemCompassStone), new ItemStack(ForbiddenItems.deadlyShards, 1, 0), new ItemStack(ForbiddenItems.deadlyShards, 1, 1),
                            new ItemStack(ForbiddenItems.deadlyShards, 1, 2), new ItemStack(ForbiddenItems.deadlyShards, 1, 3), new ItemStack(ForbiddenItems.deadlyShards, 1, 4),
                            new ItemStack(ForbiddenItems.deadlyShards, 1, 5), new ItemStack(ForbiddenItems.deadlyShards, 1, 6), new ItemStack(ConfigItems.itemCompassStone),
                            new ItemStack(ConfigItems.itemCompassStone), new ItemStack(ConfigItems.itemCompassStone)

                    }
            ));
        }

        if (Cybersus.witcheryLoaded) {
            if (CybersusItems.sleepModule != null) {
                AspectList aspects = new AspectList().add(Aspect.MAGIC, 64).add(CybersusAspect.DIMENSIO, 64).add(Aspect.SOUL, 36).add(Aspect.TRAVEL, 32).add(Aspect.TRAP, 16);
                if (Cybersus.forbiddenMagicLoaded) {
                    aspects.add(DarkAspects.SLOTH, 64);
                } else {
                    aspects.add(Aspect.SOUL, 64).add(Aspect.TRAP, 64);
                }
                runicMatrixRecipes.put(sleepModuleKey, ThaumcraftApi.addInfusionCraftingRecipe(
                        sleepModuleKey,
                        new ItemStack(CybersusItems.sleepModule, 1, 0),
                        5,
                        aspects,
                        new ItemStack(CybersusItems.motherboardBlank),
                        new ItemStack[]{
                                Witchery.Items.GENERIC.itemBrewOfSleeping.createStack(), Witchery.Items.GENERIC.itemMellifluousHunger.createStack(), new ItemStack(ConfigItems.itemShard, 1, 6),
                                new ItemStack(ConfigItems.itemResource, 1, 15), new ItemStack(ConfigItems.itemResource, 1, 16)

                        }
                ));
            }
            if (CybersusItems.tormentor != null) {
                AspectList aspects = new AspectList().add(Aspect.MAGIC, 64).add(CybersusAspect.DIMENSIO, 32).add(Aspect.SOUL, 16).add(Aspect.TRAVEL, 8).add(Aspect.TRAP, 196);
                if (Cybersus.forbiddenMagicLoaded) {
                    aspects.add(DarkAspects.WRATH, 128);
                } else {
                    aspects.add(Aspect.FIRE, 128).add(Aspect.WEAPON, 128);
                }
                runicMatrixRecipes.put(tormentorKey, ThaumcraftApi.addInfusionCraftingRecipe(
                        tormentorKey,
                        new ItemStack(CybersusItems.tormentor),
                        23,
                        aspects,
                        new ItemStack(CybersusItems.motherboardBlank),
                        new ItemStack[]{
                                Witchery.Items.GENERIC.itemBrewSoulTorment.createStack(), Witchery.Items.GENERIC.itemCondensedFear.createStack(), new ItemStack(ConfigItems.itemShard, 1, 6),
                                new ItemStack(ConfigItems.itemResource, 1, 15), new ItemStack(ConfigItems.itemResource, 1, 16)

                        }

                ));
            }
        }
        if (CybersusItems.berserkHeart != null) {
            AspectList aspects = new AspectList().add(Aspect.MAGIC, 64).add(Aspect.HEAL, 512).add(Aspect.ARMOR, 512).add(Aspect.ENERGY, 512);
            if (Cybersus.forbiddenMagicLoaded) {
                aspects.add(DarkAspects.WRATH, 1024);
            } else {
                aspects.add(Aspect.FIRE, 1024).add(Aspect.WEAPON, 1024);
            }
            runicMatrixRecipes.put(berserkHeartKey, ThaumcraftApi.addInfusionCraftingRecipe(
                    berserkHeartKey,
                    new ItemStack(CybersusItems.berserkHeart),
                    20,
                    aspects,
                    new ItemStack(CybersusItems.heartBlank),
                    new ItemStack[]{
                            new ItemStack(ConfigItems.itemSwordCrimson), new ItemStack(Items.iron_ingot), new ItemStack(Items.redstone),
                            new ItemStack(ConfigItems.itemResource, 1, 15), new ItemStack(Items.blaze_powder),
                            new ItemStack(Items.skull), new ItemStack(ConfigItems.itemResource, 1, 16),
                            new ItemStack(Items.nether_star),
                            new ItemStack(ConfigItems.itemResource, 1, 16),
                            new ItemStack(Items.nether_wart)
                    }

            ));
        }
        if (CybersusItems.exploder != null) {
            AspectList aspects = new AspectList().add(Aspect.MAGIC, 256).add(Aspect.ENTROPY, 512).add(Aspect.MIND, 512);
            runicMatrixRecipes.put(mindExploderKey, ThaumcraftApi.addInfusionCraftingRecipe(
                    mindExploderKey,
                    new ItemStack(CybersusItems.exploder),
                    16,
                    aspects,
                    new ItemStack(CybersusItems.motherboardBlank),
                    new ItemStack[]{
                            new ItemStack(Blocks.tnt), new ItemStack(Items.iron_ingot), new ItemStack(Items.redstone),
                            new ItemStack(Items.blaze_powder),
                            new ItemStack(ConfigItems.itemZombieBrain),
                            new ItemStack(ConfigItems.itemBottleTaint)
                    }

            ));
        }

        if (CybersusItems.eyeBlank != null) {
            AspectList aspects = new AspectList().add(Aspect.MAGIC, 128).add(Aspect.SENSES, 512).add(Aspect.MECHANISM, 256).add(Aspect.CRYSTAL, 256);
            runicMatrixRecipes.put(blankEyeKey, ThaumcraftApi.addInfusionCraftingRecipe(
                    blankEyeKey,
                    new ItemStack(CybersusItems.eyeBlank),
                    16,
                    aspects,
                    new ItemStack(Items.spider_eye),
                    new ItemStack[]{
                            new ItemStack(CybersusItems.synthGlass, 1),
                            new ItemStack(Items.redstone, 1),
                            new ItemStack(CybersusItems.synthGlass, 1),
                            new ItemStack(ConfigItems.itemResource, 1, 16),
                            new ItemStack(CybersusItems.synthGlass, 1),
                            new ItemStack(Items.glowstone_dust, 1),
                    }

            ));
        }
        if (CybersusItems.illusionGenerator != null) {
            AspectList aspects = new AspectList().add(Aspect.TRAP, 512).add(Aspect.MIND, 512).add(Aspect.MECHANISM, 512).add(Aspect.MAN, 512);
            runicMatrixRecipes.put(illusionGeneratorKey, ThaumcraftApi.addInfusionCraftingRecipe(
                    illusionGeneratorKey,
                    new ItemStack(CybersusItems.illusionGenerator),
                    26,
                    aspects,
                    new ItemStack(CybersusItems.eyeBlank),
                    new ItemStack[]{
                            new ItemStack(ConfigItems.itemZombieBrain, 1),
                            new ItemStack(Items.redstone, 1),
                            new ItemStack(ConfigItems.itemResource, 1, 16),

                    }

            ));
        }
        if (CybersusItems.reactionIncreaser != null) {
            AspectList aspects = new AspectList().add(Aspect.MOTION, 512).add(Aspect.SENSES, 512).add(Aspect.MECHANISM, 128);
            runicMatrixRecipes.put(reactionIncreaserKey, ThaumcraftApi.addInfusionCraftingRecipe(
                    reactionIncreaserKey,
                    new ItemStack(CybersusItems.reactionIncreaser),
                    30,
                    aspects,
                    new ItemStack(CybersusItems.alloyX87),
                    new ItemStack[]{
                            new ItemStack(CybersusItems.motherboardBlank, 1),
                            new ItemStack(CybersusItems.synthNerv, 1),
                            new ItemStack(CybersusItems.synthNerv, 1),
                            new ItemStack(CybersusItems.synthNerv, 1),
                            new ItemStack(CybersusItems.synthNerv, 1),
                    }

            ));
        }

        if (CybersusItems.synthGlass != null) {
            AspectList aspects = new AspectList().add(Aspect.CRYSTAL, 128).add(Aspect.SENSES, 128).add(Aspect.MECHANISM, 128);
            runicMatrixRecipes.put(synthGlassKey, ThaumcraftApi.addInfusionCraftingRecipe(
                    synthGlassKey,
                    new ItemStack(CybersusItems.synthGlass),
                    4,
                    aspects,
                    new ItemStack(Blocks.glass_pane),
                    new ItemStack[]{
                            new ItemStack(ConfigBlocks.blockCosmeticOpaque, 1, 2),
                            new ItemStack(CybersusItems.synaptite),
                            new ItemStack(ConfigBlocks.blockCosmeticOpaque, 1, 2),
                            new ItemStack(Items.redstone, 1),
                            new ItemStack(ConfigBlocks.blockCosmeticOpaque, 1, 2),
                            new ItemStack(Items.glowstone_dust, 1),
                    }
            ));
        }

        if (CybersusItems.synthDerm != null) {
            AspectList aspects = new AspectList().add(Aspect.LIFE, 128).add(Aspect.ARMOR, 128).add(Aspect.MECHANISM, 128).add(Aspect.FLESH, 32).add(Aspect.MAGIC, 32).add(CybersusAspect.HUMILITAS, 8);
            runicMatrixRecipes.put(synthDermKey, ThaumcraftApi.addInfusionCraftingRecipe(
                    synthDermKey,
                    new ItemStack(CybersusItems.synthDerm),
                    5,
                    aspects,
                    new ItemStack(Items.leather),
                    new ItemStack[]{
                            new ItemStack(CybersusItems.phasolite, 1),
                            new ItemStack(Items.slime_ball, 1),
                            new ItemStack(CybersusItems.synaptite, 1),
                    }

            ));
        }

        if (CybersusItems.synthNerv != null) {
            AspectList aspects = new AspectList().add(Aspect.SENSES, 256).add(Aspect.MECHANISM, 128).add(CybersusAspect.HUMILITAS, 8);
            runicMatrixRecipes.put(synthNervKey, ThaumcraftApi.addInfusionCraftingRecipe(
                    synthNervKey,
                    new ItemStack(CybersusItems.synthNerv),
                    7,
                    aspects,
                    new ItemStack(Items.rotten_flesh),
                    new ItemStack[]{
                            new ItemStack(CybersusItems.phasolite, 1),
                            new ItemStack(CybersusItems.synaptite, 1),
                            new ItemStack(CybersusItems.synaptite, 1),
                            new ItemStack(CybersusItems.synaptite, 1),
                            new ItemStack(CybersusItems.phasolite, 1),
                            new ItemStack(CybersusItems.synaptite, 1),
                            new ItemStack(CybersusItems.synaptite, 1),
                            new ItemStack(CybersusItems.synaptite, 1),
                            new ItemStack(CybersusItems.phasolite, 1),
                            new ItemStack(CybersusItems.synaptite, 1),
                            new ItemStack(CybersusItems.synaptite, 1),
                            new ItemStack(CybersusItems.synaptite, 1),
                    }

            ));
        }

        if (CybersusItems.myomass != null) {
            AspectList aspects = new AspectList().add(Aspect.LIFE, 64).add(Aspect.ENERGY, 256).add(Aspect.MECHANISM, 128).add(Aspect.MOTION, 128);
            runicMatrixRecipes.put(myomassKey, ThaumcraftApi.addInfusionCraftingRecipe(
                    myomassKey,
                    new ItemStack(CybersusItems.myomass),
                    4,
                    aspects,
                    new ItemStack(Items.rotten_flesh),
                    new ItemStack[]{
                            new ItemStack(Items.slime_ball, 1),
                            new ItemStack(Items.string, 1),
                            new ItemStack(Items.slime_ball, 1),
                            new ItemStack(Items.string, 1),
                            new ItemStack(Items.slime_ball, 1),
                            new ItemStack(Items.string, 1),
                            new ItemStack(Items.slime_ball, 1),
                            new ItemStack(Items.string, 1),
                            new ItemStack(Items.slime_ball, 1),
                            new ItemStack(Items.string, 1),

                    }

            ));
        }

        if (CybersusItems.icarusBlood != null) {
            AspectList aspects = new AspectList().add(CybersusAspect.SANGUINO, 256).add(Aspect.MECHANISM, 256).add(CybersusAspect.HUMILITAS, 64);
            runicMatrixRecipes.put(icarusBloodKey, ThaumcraftApi.addInfusionCraftingRecipe(
                    icarusBloodKey,
                    new ItemStack(CybersusItems.icarusBlood),
                    4,
                    aspects,
                    new ItemStack(Items.slime_ball),
                    new ItemStack[]{
                            new ItemStack(CybersusItems.phasolite, 1),
                            new ItemStack(Items.slime_ball, 1),
                            new ItemStack(Items.slime_ball, 1),
                            new ItemStack(CybersusItems.phasolite, 1),
                            new ItemStack(Items.slime_ball, 1),
                            new ItemStack(Items.slime_ball, 1),
                            new ItemStack(CybersusItems.phasolite, 1),
                            new ItemStack(Items.slime_ball, 1),
                            new ItemStack(Items.slime_ball, 1),

                    }

            ));
        }

    }












    public static void integrateResearch() {
        ResearchCategories.registerCategory(
                cybersusCategory,
                new ResourceLocation(Cybersus.MOD_ID, "textures/misc/mod_logo.png"),
                new ResourceLocation(Cybersus.MOD_ID, "textures/misc/cybersus_background.png")
        );

        new ResearchImplantItem(
                basicInfo,
                cybersusCategory,
                new AspectList().add(Aspect.MECHANISM, 8).add(Aspect.MAGIC, 4).add(Aspect.ENERGY, 6).add(Aspect.MAN, 4).add(Aspect.ORDER, 4),
                0,
                0,
                0,
                new ResourceLocation(Cybersus.MOD_ID, "textures/misc/mod_logo.png")
        ).setPages(new ResearchPage("1"), new ResearchPage("2"), new ResearchPage("3"), new ResearchPage("4"))
                .registerResearchItem();


        new ResearchImplantItem(
                blankHeartKey,
                cybersusCategory,
                new AspectList().add(Aspect.MECHANISM, 8).add(Aspect.MAGIC, 4).add(Aspect.ENERGY, 6),
                3,
                5,
                2,
                new ItemStack(CybersusItems.heartBlank)
        ).setPages(new ResearchPage("1"), new ResearchPage(runicMatrixRecipes.get(blankHeartKey))).setParents(basicInfo, "PRIMPEARL").setParents(aspectHoldersKey).setParentsHidden(synthDermKey).setParentsHidden(myomassKey).setConcealed()
                .registerResearchItem();

        new ResearchImplantItem(
                gravityIncreaserKey,
                cybersusCategory,
                new AspectList().add(CybersusAspect.GRAVITAS, 1).add(Aspect.MAGIC, 1).add(Aspect.MECHANISM, 1).add(Aspect.TRAP, 1).add(Aspect.ENERGY, 1).add(Aspect.EARTH, 1).add(Aspect.ORDER, 1),
                -5,
                0,
                3,
                new ItemStack(CybersusItems.gravityIcreaser)
        ).setPages(injectSimpleImplantResearchInfo(CybersusItems.gravityIcreaser, new ResearchPage(runicMatrixRecipes.get(gravityIncreaserKey)))).setParents(aspectHoldersKey).setConcealed()
                .registerResearchItem();


        new ResearchImplantItem(
                shadowSkinKey,
                cybersusCategory,
                new AspectList().add(Aspect.DARKNESS, 1).add(Aspect.EXCHANGE, 1).add(Aspect.MECHANISM, 1).add(Aspect.MAGIC, 1).add(Aspect.MAN, 1).add(Aspect.VOID, 1),
                5,
                -3,
                3,
                new ItemStack(CybersusItems.shadowSkin)
        ).setPages(injectSimpleImplantResearchInfo(CybersusItems.shadowSkin, new ResearchPage(runicMatrixRecipes.get(shadowSkinKey)))).setParents(aspectHoldersKey).setParentsHidden(synthDermKey).setConcealed()
                .registerResearchItem();


//        new ResearchImplantItem(
//                singleAspectHolderKey,
//                cybersusCategory,
//                new AspectList(),
//                12,
//                13,
//                3,
//                new ItemStack(ModItems.portableMultiAspectContainer)
//        ).setParents(aspectHoldersKey).setVirtual().registerResearchItem();
//
//        new ResearchImplantItem(
//                multiAspectHolderKey,
//                cybersusCategory,
//                new AspectList(),
//                12,
//                12,
//                3,
//                new ItemStack(ModItems.portableMultiAspectContainer)
//        ).setParents(aspectHoldersKey).setVirtual().registerResearchItem();

        new ResearchImplantItem(
                aspectHoldersKey,
                cybersusCategory,
                new AspectList().add(CybersusAspect.HUMILITAS, 16).add(Aspect.VOID, 16).add(Aspect.MAGIC, 16).add(Aspect.WATER, 32),
                3,
                0,
                3,
                new ItemStack(CybersusItems.portableMultiAspectContainer)
        ).setPages(new ResearchPage("1"), new ResearchPage(runicMatrixRecipes.get(singleAspectHolderKey)), new ResearchPage(runicMatrixRecipes.get(multiAspectHolderKey)), new ResearchPage("2")).setParents(basicInfo).setConcealed()
                .registerResearchItem();

        new ResearchImplantItem(
                motherboardBlankKey,
                cybersusCategory,
                new AspectList().add(Aspect.MECHANISM, 1).add(Aspect.MIND, 1).add(Aspect.ENERGY, 1).add(Aspect.ORDER, 1).add(Aspect.EXCHANGE, 1),
                -5,
                5,
                6,
                new ItemStack(CybersusItems.motherboardBlank)
        ).setPages(new ResearchPage("1"), new ResearchPage(runicMatrixRecipes.get(motherboardBlankKey))).setParentsHidden(aspectHoldersKey).setParentsHidden(alloyX87Key).setConcealed()
                .registerResearchItem();

        new ResearchImplantItem(
                phoenixHeartKey,
                cybersusCategory,
                new AspectList().add(Aspect.MECHANISM, 1).add(Aspect.FIRE, 1).add(Aspect.LIFE, 1).add(Aspect.EXCHANGE, 1).add(Aspect.MAGIC, 1),
                5,
                7,
                0,
                new ItemStack(CybersusItems.phoenixHeart)
        ).setPages(injectSimpleImplantResearchInfo(CybersusItems.phoenixHeart, new ResearchPage(runicMatrixRecipes.get(phoenixHeartKey)))).setParents(blankHeartKey).setConcealed()
                .registerResearchItem();

        if (Cybersus.forbiddenMagicLoaded) {
            CrucibleRecipe[] sinHeartRecipes = new CrucibleRecipe[ImplantSinHeart.sinAspects.size()];
            for (int i = 0; i < ImplantSinHeart.sinAspects.size(); i++) {
                sinHeartRecipes[i] = crucibleRecipes.get(sinHeartKey + i);
            }

            new ResearchImplantItem(
                    sinHeartKey,
                    cybersusCategory,
                    new AspectList().add(DarkAspects.NETHER, 1).add(Aspect.MAGIC, 1).add(Aspect.EXCHANGE, 1).add(Aspect.SOUL, 1),
                    6,
                    5,
                    0,
                    new ItemStack(CybersusItems.implantSinHeart, 1, ImplantSinHeart.sinAspects.size())
            ).setPages(new ResearchPage("1"), new ResearchPage(runicMatrixRecipes.get(sinHeartKey)), new ResearchPage(sinHeartRecipes), new ResearchPage("2"), new ResearchPage("3"), new ResearchPage("4")).setConcealed()
                    .setParents(blankHeartKey).registerResearchItem();

        }


//        InfusionRecipe[] infusionRecipesForSleepModule = new InfusionRecipe[Cybersus.forbiddenMagicLoaded ? 2 : 1];
//        infusionRecipesForSleepModule[0] = runicMatrixRecipes.get(sleepModuleKey + "1");
//        if(Cybersus.forbiddenMagicLoaded) {
//            infusionRecipesForSleepModule[1] = runicMatrixRecipes.get(sleepModuleKey + "2");
//        }


        if (Cybersus.witcheryLoaded) {
            new ResearchImplantItem(
                    sleepModuleKey,
                    cybersusCategory,
                    new AspectList().add(Aspect.MAGIC, 1).add(CybersusAspect.DIMENSIO, 1).add(Aspect.SOUL, 1).add(Aspect.TRAVEL, 1).add(Aspect.TRAP, 1),
                    -7,
                    8,
                    2,
                    new ItemStack(CybersusItems.sleepModule)
            ).setPages(injectSimpleImplantResearchInfo(CybersusItems.sleepModule, new ResearchPage(runicMatrixRecipes.get(sleepModuleKey)))).setItemTriggers(Witchery.Items.GENERIC.itemBrewOfSleeping.createStack()).setHidden()
                    .setParents(motherboardBlankKey).registerResearchItem();
//
//        InfusionRecipe[] infusionRecipesForTormentor = new InfusionRecipe[Cybersus.forbiddenMagicLoaded ? 2 : 1];
//        infusionRecipesForTormentor[0] = runicMatrixRecipes.get(tormentorKey + "1");
//        if(Cybersus.forbiddenMagicLoaded) {
//            infusionRecipesForTormentor[1] = runicMatrixRecipes.get(tormentorKey + "2");
//        }

            new ResearchImplantItem(
                    tormentorKey,
                    cybersusCategory,
                    new AspectList().add(Aspect.MAGIC, 1).add(CybersusAspect.DIMENSIO, 1).add(Aspect.SOUL, 1).add(Aspect.TRAVEL, 1).add(Aspect.TRAP, 1).add(Aspect.WEAPON, 1).add(Aspect.FIRE, 1),
                    -8,
                    6,
                    5,
                    new ItemStack(CybersusItems.tormentor)
            ).setPages(injectSimpleImplantResearchInfo(CybersusItems.tormentor, new ResearchPage(runicMatrixRecipes.get(tormentorKey)))).setItemTriggers(Witchery.Items.GENERIC.itemBrewSoulTorment.createStack()).setHidden()
                    .setParents(motherboardBlankKey).registerResearchItem();

        }


        new ResearchImplantItem(
                berserkHeartKey,
                cybersusCategory,
                new AspectList().add(Aspect.MAGIC, 64).add(Aspect.HEAL, 2048).add(Aspect.ARMOR, 2048).add(Aspect.ENERGY, 2048),
                2,
                7,
                2,
                new ItemStack(CybersusItems.berserkHeart)
        ).setPages(new ResearchPage("1"), new ResearchPage(runicMatrixRecipes.get(berserkHeartKey))).setConcealed()
                .setParents(blankHeartKey).registerResearchItem();


        new ResearchImplantItem(
                mindExploderKey,
                cybersusCategory,
                new AspectList().add(Aspect.MAGIC, 256).add(Aspect.SENSES, 2048).add(Aspect.MECHANISM, 1024).add(Aspect.CRYSTAL, 1024),
                -8,
                4,
                2,
                new ItemStack(CybersusItems.exploder)
        ).setPages(injectSimpleImplantResearchInfo(CybersusItems.exploder, new ResearchPage(runicMatrixRecipes.get(mindExploderKey)))).setConcealed()
                .setParents(motherboardBlankKey).registerResearchItem();

        new ResearchImplantItem(
                blankEyeKey,
                cybersusCategory,
                new AspectList().add(Aspect.MAGIC, 256).add(Aspect.ENTROPY, 2048).add(Aspect.MIND, 2048),
                6,
                1,
                2,
                new ItemStack(CybersusItems.eyeBlank)
        ).setPages(new ResearchPage("1"), new ResearchPage(runicMatrixRecipes.get(blankEyeKey))).setConcealed()
                .setParents(aspectHoldersKey).setParentsHidden(synthGlassKey).registerResearchItem();


        new ResearchImplantItem(
                illusionGeneratorKey,
                cybersusCategory,
                new AspectList().add(Aspect.MAGIC, 256).add(Aspect.TRAP, 2048).add(Aspect.MIND, 1024).add(Aspect.MAN, 1024),
                9,
                0,
                2,
                new ItemStack(CybersusItems.illusionGenerator)
        ).setPages(injectSimpleImplantResearchInfo(CybersusItems.illusionGenerator, new ResearchPage(runicMatrixRecipes.get(illusionGeneratorKey)))).setConcealed()
                .setParents(blankEyeKey).registerResearchItem();

        new ResearchImplantItem(
                reactionIncreaserKey,
                cybersusCategory,
                new AspectList().add(Aspect.MOTION, 256).add(Aspect.SENSES, 1024).add(Aspect.MECHANISM, 1024),
                3,
                -5,
                2,
                new ItemStack(CybersusItems.reactionIncreaser)
        ).setPages(injectSimpleImplantResearchInfo(CybersusItems.reactionIncreaser, new ResearchPage(runicMatrixRecipes.get(reactionIncreaserKey)))).setConcealed()
                .setParentsHidden(alloyX87Key).setParentsHidden(motherboardBlankKey).registerResearchItem();

        new ResearchImplantItem(
                alloyX87Key,
                cybersusCategory,
                new AspectList().add(Aspect.LIFE, 64).add(Aspect.MAGIC, 128).add(Aspect.MECHANISM, 128).add(CybersusAspect.HUMILITAS, 8).add(Aspect.METAL, 256),
                2,
                -2,
                2,
                new ItemStack(CybersusItems.alloyX87)
        ).setPages(new ResearchPage("1"), new ResearchPage(runicMatrixRecipes.get(alloyX87Key))).setConcealed()
                .setParents(basicInfo).registerResearchItem();

        new ResearchImplantItem(
                synthGlassKey,
                cybersusCategory,
                new AspectList().add(Aspect.SENSES, 64).add(Aspect.MAGIC, 128).add(Aspect.MECHANISM, 128).add(Aspect.CRYSTAL, 16),
                0,
                -3,
                2,
                new ItemStack(CybersusItems.synthGlass)
        ).setPages(new ResearchPage("1"), new ResearchPage(runicMatrixRecipes.get(synthGlassKey))).setConcealed()
                .setParents(basicInfo).registerResearchItem();

        new ResearchImplantItem(
                synthDermKey,
                cybersusCategory,
                new AspectList().add(Aspect.LIFE, 64).add(Aspect.ARMOR, 128).add(Aspect.MECHANISM, 128).add(Aspect.FLESH, 16),
                -2,
                -2,
                2,
                new ItemStack(CybersusItems.synthDerm)
        ).setPages(new ResearchPage("1"), new ResearchPage(runicMatrixRecipes.get(synthDermKey))).setConcealed()
                .setParents(basicInfo).registerResearchItem();

        new ResearchImplantItem(
                synthNervKey,
                cybersusCategory,
                new AspectList().add(Aspect.SENSES, 256).add(Aspect.MECHANISM, 128),
                -2,
                2,
                2,
                new ItemStack(CybersusItems.synthNerv)
        ).setPages(new ResearchPage("1"), new ResearchPage(runicMatrixRecipes.get(synthNervKey))).setConcealed()
                .setParents(basicInfo).registerResearchItem();

        new ResearchImplantItem(
                myomassKey,
                cybersusCategory,
                new AspectList().add(Aspect.LIFE, 64).add(Aspect.ENERGY, 128).add(Aspect.MECHANISM, 128).add(Aspect.MOTION, 128),
                0,
                3,
                2,
                new ItemStack(CybersusItems.myomass)
        ).setPages(new ResearchPage("1"), new ResearchPage(runicMatrixRecipes.get(myomassKey))).setConcealed()
                .setParents(basicInfo).registerResearchItem();

        new ResearchImplantItem(
                icarusBloodKey,
                cybersusCategory,
                new AspectList().add(CybersusAspect.SANGUINO, 64).add(Aspect.MECHANISM, 128).add(CybersusAspect.HUMILITAS, 128),
                2,
                2,
                2,
                new ItemStack(CybersusItems.icarusBlood)
        ).setPages(new ResearchPage("1"), new ResearchPage(runicMatrixRecipes.get(icarusBloodKey))).setConcealed()
                .setParents(basicInfo).registerResearchItem();
    }


    private static ResearchPage[] injectSimpleImplantResearchInfo(ItemImplant implant, ResearchPage craftResearchPage) {
        List<Ability> abilities = implant.getAbilities(null, -1, null);

        ResearchPage[] researchPages = new ResearchPage[2 + abilities.size()];
        researchPages[0] = new ResearchPage("description");
        researchPages[1] = craftResearchPage;

        for (int i = 0; i < abilities.size(); i++) {
            researchPages[i + 2] = new AbilityResearchPage(implant, abilities.get(i));
        }

        return researchPages;
    }


}

