package com.suslovila.cybersus.research;

import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.common.item.ModItems;
import com.suslovila.cybersus.common.item.implants.sinHeart.ImplantSinHeart;
import fox.spiteful.forbidden.DarkAspects;
import fox.spiteful.forbidden.items.ForbiddenItems;
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

public class CybersusResearchRegistry {

    private static HashMap<String, InfusionRecipe> runicMatrixRecipes = new HashMap<>();
    private static HashMap<String, CrucibleRecipe> crucibleRecipes = new HashMap<>();

    // ItemStacks
//    private static final ItemStack essentiaReservoirVoid = ItemStack(ModBlocks.BlockEssentiaReservoirVoid, 1, 0);

    public static final String blankHeartKey = "BLANK_HEART";
    public static final String shadowSkinKey = "SHADOW_SKIN";

    public static final String gravityIncreaserKey = "GRAVITY_INCREASER";
    public static final String aspectHoldersKey = "ASPECT_HOLDERS";
    public static final String multiAspectHolderKey = "MULTI_ASPECT_HOLDER";
    public static final String singleAspectHolderKey = "SINGLE_ASPECT_HOLDER";
    public static final String motherboardBlankKey = "BLANK_MOTHERBOARD";
    public static final String phoenixHeartKey = "PHOENIX_HEART";
    public static final String sinHeartKey = "SIN_HEART";



    public static void integrateCrucibleRecipe() {
        for (int i = 0; i < ImplantSinHeart.sinAspects.size(); i++) {
            addCrucibleRecipe(ModItems.implantSinHeart, 1, i, sinHeartKey + i, new ItemStack(ModItems.implantSinHeart, 1, ImplantSinHeart.sinAspects.size()), (new AspectList()).add(DarkAspects.NETHER, 512).add(ImplantSinHeart.sinAspects.get(i), 2048));
        }
    }

    public static void addCrucibleRecipe(Item item, int count, int damage, String key, ItemStack catalyst, AspectList aspects) {
        CrucibleRecipe recipe = ThaumcraftApi.addCrucibleRecipe(key, new ItemStack(item, count, damage), catalyst, aspects);
        crucibleRecipes.put(key, recipe);
    }
    public static void integrateInfusion() {
        runicMatrixRecipes.put(blankHeartKey, ThaumcraftApi.addInfusionCraftingRecipe(
                blankHeartKey,
                new ItemStack(ModItems.heartBlank),
                9,
                new AspectList().add(Aspect.LIFE, 64).add(Aspect.ENERGY, 64).add(Aspect.MECHANISM, 256).add(Aspect.MAGIC, 92).add(Aspect.MAN, 16),
                new ItemStack(ConfigItems.itemEldritchObject, 1, 3),
                new ItemStack[]{new ItemStack(ConfigItems.itemResource, 1, 2), new ItemStack(ConfigItems.itemResource, 1, 2), new ItemStack(ConfigItems.itemResource, 1, 2),
                        new ItemStack(ConfigItems.itemResource, 1, 15),
                        new ItemStack(ConfigItems.itemResource, 1, 16), new ItemStack(ConfigItems.itemResource, 1, 16), new ItemStack(ConfigItems.itemResource, 1, 16)}
        ));


        runicMatrixRecipes.put(shadowSkinKey, ThaumcraftApi.addInfusionCraftingRecipe(
                shadowSkinKey,
                new ItemStack(ModItems.shadowSkin),
                7,
                new AspectList().add(Aspect.DARKNESS, 256).add(Aspect.EXCHANGE, 64).add(Aspect.MECHANISM, 24).add(Aspect.MAGIC, 72).add(Aspect.MAN, 16).add(Aspect.VOID, 128),
                new ItemStack(Items.leather),
                new ItemStack[]{new ItemStack(ConfigItems.itemResource, 1, 17), new ItemStack(ConfigItems.itemResource, 1, 16), new ItemStack(ConfigItems.itemResource, 1, 17),
                        new ItemStack(ConfigItems.itemResource, 1, 16), new ItemStack(ConfigItems.itemResource, 1, 17), new ItemStack(ConfigItems.itemResource, 1, 16)}
        ));

        runicMatrixRecipes.put(gravityIncreaserKey, ThaumcraftApi.addInfusionCraftingRecipe(
                gravityIncreaserKey,
                new ItemStack(ModItems.gravityIcreaser),
                8,
                new AspectList().add(CybersusAspect.GRAVITAS, 512).add(Aspect.MAGIC, 32).add(Aspect.MECHANISM, 12).add(Aspect.TRAP, 324).add(Aspect.ENERGY, 128).add(Aspect.EARTH, 92).add(Aspect.ORDER, 256),
                new ItemStack(ModItems.heartBlank),
                new ItemStack[]{new ItemStack(ConfigItems.itemResource, 1, 3), new ItemStack(ConfigItems.itemResource, 1, 16), new ItemStack(ConfigItems.itemResource, 1, 17),
                        new ItemStack(ConfigItems.itemFocusPortableHole),
                        new ItemStack(ConfigItems.itemResource, 1, 16), new ItemStack(ConfigItems.itemResource, 1, 17), new ItemStack(ConfigItems.itemResource, 1, 16)}
        ));


        runicMatrixRecipes.put(multiAspectHolderKey, ThaumcraftApi.addInfusionCraftingRecipe(
                multiAspectHolderKey,
                new ItemStack(ModItems.portableMultiAspectContainer),
                6,
                new AspectList().add(CybersusAspect.GRAVITAS, 64).add(CybersusAspect.HUMILITAS, 256).add(Aspect.MECHANISM, 64).add(Aspect.VOID, 512).add(Aspect.ENERGY, 128).add(Aspect.ORDER, 192).add(Aspect.WATER, 256),
                new ItemStack(ConfigBlocks.blockJar),
                new ItemStack[]{
                        new ItemStack(Items.gold_ingot), new ItemStack(ConfigItems.itemResource, 1, 16), new ItemStack(ConfigBlocks.blockMetalDevice, 1, 3),
                        new ItemStack(ConfigBlocks.blockCosmeticOpaque, 1, 2), new ItemStack(ConfigItems.itemResource, 1, 16), new ItemStack(Items.gold_ingot),
                        new ItemStack(ConfigBlocks.blockCosmeticOpaque, 1, 2), new ItemStack(ConfigItems.itemResource, 1, 16), new ItemStack(ConfigBlocks.blockMetalDevice, 1, 3),
                        new ItemStack(ConfigBlocks.blockCosmeticOpaque, 1, 2), new ItemStack(ConfigItems.itemResource, 1, 16), new ItemStack(ConfigBlocks.blockMetalDevice, 1, 3),
                }
        ));

        runicMatrixRecipes.put(singleAspectHolderKey, ThaumcraftApi.addInfusionCraftingRecipe(
                singleAspectHolderKey,
                new ItemStack(ModItems.portablesingleAspectContainer),
                6,
                new AspectList().add(CybersusAspect.GRAVITAS, 64).add(CybersusAspect.HUMILITAS, 256).add(Aspect.MECHANISM, 64).add(Aspect.VOID, 512).add(Aspect.ENERGY, 128).add(Aspect.ORDER, 192).add(Aspect.WATER, 256),
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
                new ItemStack(ModItems.motherboardBlank),
                6,
                new AspectList().add(Aspect.MECHANISM, 512).add(Aspect.MIND, 1024).add(Aspect.ENERGY, 128).add(Aspect.ORDER, 512).add(Aspect.EXCHANGE, 64),
                new ItemStack(ConfigItems.itemResource, 1, 16),
                new ItemStack[]{
                        new ItemStack(Items.gold_ingot), new ItemStack(ConfigItems.itemResource, 1, 16), new ItemStack(Items.redstone),
                        new ItemStack(Items.gold_ingot), new ItemStack(ConfigItems.itemResource, 1, 16), new ItemStack(Items.redstone),
                        new ItemStack(Items.gold_ingot), new ItemStack(ConfigItems.itemResource, 1, 16), new ItemStack(Items.redstone),
                        new ItemStack(Items.gold_ingot), new ItemStack(ConfigItems.itemResource, 1, 16), new ItemStack(Items.redstone),
                }
        ));

        runicMatrixRecipes.put(phoenixHeartKey, ThaumcraftApi.addInfusionCraftingRecipe(
                phoenixHeartKey,
                new ItemStack(ModItems.phoenixHeart),
                6,
                new AspectList().add(Aspect.FIRE, 512).add(Aspect.MAGIC, 128).add(Aspect.LIFE, 1024).add(Aspect.EXCHANGE, 256).add(Aspect.MECHANISM, 64),
                new ItemStack(ModItems.heartBlank),
                new ItemStack[]{
                        new ItemStack(Items.blaze_powder), new ItemStack(Items.feather), new ItemStack(Items.redstone),
                        new ItemStack(Items.blaze_powder), new ItemStack(Items.feather), new ItemStack(Items.redstone),
                }
        ));

        runicMatrixRecipes.put(sinHeartKey, ThaumcraftApi.addInfusionCraftingRecipe(
                sinHeartKey,
                new ItemStack(ModItems.implantSinHeart, 1, ImplantSinHeart.sinAspects.size()),
                25,
                new AspectList().add(DarkAspects.NETHER, 2048).add(Aspect.MAGIC, 1024).add(Aspect.EXCHANGE, 256).add(Aspect.SOUL, 512),
                new ItemStack(ModItems.heartBlank),
                new ItemStack[]{
                        new ItemStack(ConfigItems.itemCompassStone), new ItemStack(ForbiddenItems.deadlyShards, 1, 0), new ItemStack(ForbiddenItems.deadlyShards, 1, 1),
                        new ItemStack(ForbiddenItems.deadlyShards, 1, 2), new ItemStack(ForbiddenItems.deadlyShards, 1, 3), new ItemStack(ForbiddenItems.deadlyShards, 1, 4),
                        new ItemStack(ForbiddenItems.deadlyShards, 1, 5), new ItemStack(ForbiddenItems.deadlyShards, 1, 6), new ItemStack(ConfigItems.itemCompassStone),
                        new ItemStack(ConfigItems.itemCompassStone), new ItemStack(ConfigItems.itemCompassStone)

                }
        ));
    }

    private static final String cybersusCategory = Cybersus.MOD_ID;
    private static final String basicInfo = "BASIC_INFO";

    public static void integrateResearch() {
        ResearchCategories.registerCategory(
                cybersusCategory,
                new ResourceLocation(Cybersus.MOD_ID, "textures/misc/mod_logo.png"),
                new ResourceLocation(Cybersus.MOD_ID, "textures/misc/cybersus_background.png")
        );

        new CybersusResearchItem(
                basicInfo,
                cybersusCategory,
                new AspectList().add(Aspect.MECHANISM, 8).add(Aspect.MAGIC, 4).add(Aspect.ENERGY, 6).add(Aspect.MAN, 4).add(Aspect.ORDER, 4),
                0,
                0,
                0,
                new ResourceLocation(Cybersus.MOD_ID, "textures/misc/mod_logo.png")
        ).setPages(new ResearchPage("1"), new ResearchPage("2"), new ResearchPage("3")).setSpecial()
                .registerResearchItem();


        new CybersusResearchItem(
                blankHeartKey,
                cybersusCategory,
                new AspectList().add(Aspect.MECHANISM, 8).add(Aspect.MAGIC, 4).add(Aspect.ENERGY, 6),
                2,
                4,
                2,
                new ItemStack(ModItems.heartBlank)
        ).setPages(new ResearchPage("1"), new ResearchPage(runicMatrixRecipes.get(blankHeartKey))).setParents(basicInfo, "PRIMPEARL").setParents(aspectHoldersKey)
                .registerResearchItem();

        new CybersusResearchItem(
                gravityIncreaserKey,
                cybersusCategory,
                new AspectList().add(CybersusAspect.GRAVITAS, 1).add(Aspect.MAGIC, 1).add(Aspect.MECHANISM, 1).add(Aspect.TRAP, 1).add(Aspect.ENERGY, 1).add(Aspect.EARTH, 1).add(Aspect.ORDER, 1),
                2,
                -3,
                3,
                new ItemStack(ModItems.gravityIcreaser)
        ).setPages(new ResearchPage("1"), new ResearchPage(runicMatrixRecipes.get(gravityIncreaserKey))).setParents(aspectHoldersKey)
                .registerResearchItem();
        new CybersusResearchItem(
                shadowSkinKey,
                cybersusCategory,
                new AspectList().add(Aspect.DARKNESS, 1).add(Aspect.EXCHANGE, 1).add(Aspect.MECHANISM, 1).add(Aspect.MAGIC, 1).add(Aspect.MAN, 1).add(Aspect.VOID, 1),
                5,
                -3,
                3,
                new ItemStack(ModItems.shadowSkin)
        ).setPages(new ResearchPage("1"), new ResearchPage(runicMatrixRecipes.get(shadowSkinKey))).setParents(aspectHoldersKey)
                .registerResearchItem();

        new CybersusResearchItem(
                aspectHoldersKey,
                cybersusCategory,
                new AspectList().add(CybersusAspect.HUMILITAS, 16).add(Aspect.VOID, 16).add(Aspect.MAGIC, 16).add(Aspect.WATER, 32),
                2,
                0,
                3,
                new ItemStack(ModItems.portableMultiAspectContainer)
        ).setPages(new ResearchPage("1"), new ResearchPage(runicMatrixRecipes.get(singleAspectHolderKey)), new ResearchPage(runicMatrixRecipes.get(multiAspectHolderKey)), new ResearchPage("2")).setRound().setSpecial().setParents(basicInfo)
                .registerResearchItem();

        new CybersusResearchItem(
                motherboardBlankKey,
                cybersusCategory,
                new AspectList().add(Aspect.MECHANISM, 1).add(Aspect.MIND, 1).add(Aspect.ENERGY, 1).add(Aspect.ORDER, 1).add(Aspect.EXCHANGE, 1),
                -7,
                7,
                3,
                new ItemStack(ModItems.motherboardBlank)
        ).setPages(new ResearchPage("1"), new ResearchPage(runicMatrixRecipes.get(motherboardBlankKey))).setParents(aspectHoldersKey)
                .registerResearchItem();

        new CybersusResearchItem(
                phoenixHeartKey,
                cybersusCategory,
                new AspectList().add(Aspect.MECHANISM, 1).add(Aspect.FIRE, 1).add(Aspect.LIFE, 1).add(Aspect.EXCHANGE, 1).add(Aspect.MAGIC, 1),
                4,
                6,
                0,
                new ItemStack(ModItems.phoenixHeart)
        ).setPages(new ResearchPage("1"), new ResearchPage(runicMatrixRecipes.get(phoenixHeartKey))).setParents(blankHeartKey)
                .registerResearchItem();

        CrucibleRecipe[] sinHeartRecipes = new CrucibleRecipe[ImplantSinHeart.sinAspects.size()];
        for(int i = 0; i < ImplantSinHeart.sinAspects.size(); i++) {
            sinHeartRecipes[i] = crucibleRecipes.get(sinHeartKey + i);
        }

        new CybersusResearchItem(
                sinHeartKey,
                cybersusCategory,
                new AspectList().add(DarkAspects.NETHER, 1).add(Aspect.MAGIC, 1).add(Aspect.EXCHANGE, 1).add(Aspect.SOUL, 1),
                5,
                4,
                0,
                new ItemStack(ModItems.implantSinHeart, 1, ImplantSinHeart.sinAspects.size())
        ).setPages(new ResearchPage("1"), new ResearchPage(runicMatrixRecipes.get(sinHeartKey)), new ResearchPage(sinHeartRecipes), new ResearchPage("2"), new ResearchPage("3"), new ResearchPage("4"))
                .setParents(blankHeartKey).registerResearchItem();

    }
}

