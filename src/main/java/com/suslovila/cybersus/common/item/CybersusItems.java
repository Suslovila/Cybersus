package com.suslovila.cybersus.common.item;

import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.common.item.implants.*;
import com.suslovila.cybersus.common.item.implants.reactionIncreaser.ImplantReactionIncreaser;
import com.suslovila.cybersus.common.item.implants.sinHeart.ImplantSinHeart;
import com.suslovila.cybersus.common.item.implants.ImplantShadowSkin;
import com.suslovila.cybersus.common.item.implants.witchery.ImplantSleepModule;
import com.suslovila.cybersus.common.item.implants.witchery.ImplantTormentor;
import cpw.mods.fml.common.registry.GameRegistry;

public class CybersusItems {


    public static ImplantPhoenixHeart phoenixHeart;
    public static ImplantExploder exploder;

    public static ImplantSleepModule sleepModule;
    public static ImplantTormentor tormentor;
    public static ImplantSoulBreaker soulBreaker;

    public static ImplantSinHeart implantSinHeart;
    public static ImplantShadowSkin shadowSkin;

    public static ItemRune itemRune;

    public static ItemHeartBlank heartBlank;
    public static ImplantGravityIncreaser gravityIcreaser;
    public static ItemMotherboardBlank motherboardBlank;

    public static ImplantIllusionGenerator illusionGenerator;

    public static ItemPortableMultiAspectContainer portableMultiAspectContainer;
    public static ItemPortableSingleAspectContainer portablesingleAspectContainer;
    public static ItemCreativeAspectContainer portableCreativeAspectContainer;

    public static ImplantBerserkHeart berserkHeart;
    public static ImplantEyeOfEnvy eyeOfEnvy;

    public static ItemEyeBlank eyeBlank;
    public static ItemSuppressed itemSuppressed;
    public static ImplantSuppressor suppressor;
    public static ImplantReactionIncreaser reactionIncreaser;
    public static ImplantPortalPlacer portalPlacer;
    public static ItemAlloyX87 alloyX87;
    public static ItemIcarusBlood icarusBlood;
    public static ItemGravitationDust gravitationDust;
    public static ItemPhasolite phasolite;
    public static ItemSynthGlass synthGlass;
    public static ItemSynaptite synaptite;
    public static ItemSynthDerm synthDerm;
    public static ItemSynthNerv synthNerv;
    public static ItemMyomass myomass;


    public static void register() {
        registerImplants();
//        itemRune = new ItemRune();
//        GameRegistry.registerItem(itemRune, ItemRune.name);

    }

    public static void registerImplants() {
        gravitationDust = new ItemGravitationDust();
        phasolite = new ItemPhasolite();
        if(Cybersus.thaumcraftLoaded) {
            portableMultiAspectContainer = new ItemPortableMultiAspectContainer();
            portablesingleAspectContainer = new ItemPortableSingleAspectContainer();
            portableCreativeAspectContainer = new ItemCreativeAspectContainer();

            GameRegistry.registerItem(portableMultiAspectContainer, ItemPortableMultiAspectContainer.name);
            GameRegistry.registerItem(portablesingleAspectContainer, ItemPortableSingleAspectContainer.name);
            GameRegistry.registerItem(portableCreativeAspectContainer, ItemCreativeAspectContainer.name);

            berserkHeart = new ImplantBerserkHeart();
            phoenixHeart = new ImplantPhoenixHeart();
            shadowSkin = new ImplantShadowSkin();
            exploder = new ImplantExploder();
            heartBlank = new ItemHeartBlank();
            gravityIcreaser = new ImplantGravityIncreaser();
            motherboardBlank = new ItemMotherboardBlank();
            illusionGenerator = new ImplantIllusionGenerator();
//            eyeOfEnvy = new ImplantEyeOfEnvy();
            eyeBlank = new ItemEyeBlank();
            if(Cybersus.forbiddenMagicLoaded) {
                implantSinHeart = new ImplantSinHeart();
            }
            if(Cybersus.witcheryLoaded) {
                sleepModule = new ImplantSleepModule();
                tormentor = new ImplantTormentor();
            }
//            soulBreaker = new ImplantSoulBreaker();

//            itemSuppressed = new ItemSuppressed();
//            suppressor = new ImplantSuppressor();

            reactionIncreaser = new ImplantReactionIncreaser();
//            portalPlacer = new ImplantPortalPlacer();

            alloyX87 = new ItemAlloyX87();
            icarusBlood = new ItemIcarusBlood();
            synthGlass = new ItemSynthGlass();
            synaptite = new ItemSynaptite();
            synthDerm = new ItemSynthDerm();
            synthNerv = new ItemSynthNerv();
            myomass = new ItemMyomass();
        }
    }
}
