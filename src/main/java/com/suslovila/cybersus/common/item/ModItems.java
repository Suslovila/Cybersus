package com.suslovila.cybersus.common.item;

import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.common.item.implants.*;
import com.suslovila.cybersus.common.item.implants.essentiaHeart.ImplantEssentiaHeart;
import com.suslovila.cybersus.common.item.implants.essentiaHeart.ImplantShadowSkin;
import com.suslovila.cybersus.common.item.implants.witchery.ImplantSleepDiver;
import com.suslovila.cybersus.common.item.implants.witchery.ImplantTormentor;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModItems {


    public static ImplantPhoenixHeart phoenixHeart;
    public static ImplantExploder exploser;

    public static ImplantSleepDiver sleepDiver;
    public static ImplantTormentor tormentor;
    public static ImplantSoulBreaker soulBreaker;

    public static ImplantEssentiaHeart implantEssentiaHeart;
    public static ImplantShadowSkin shadowSkin;

    public static ItemRune itemRune;

    public static ItemHeartBlank heartBlank;
    public static ImplantGravityIcreaser gravityIcreaser;
    public static ItemMotherboardBlank motherboardBlank;


    public static ItemPortableMultiAspectContainer portableMultiAspectContainer = new ItemPortableMultiAspectContainer();
    public static ItemPortableSingleAspectContainer portablesingleAspectContainer = new ItemPortableSingleAspectContainer();



    public static void register() {
        registerImplants();
//        itemRune = new ItemRune();
//        GameRegistry.registerItem(itemRune, ItemRune.name);

    }

    public static void registerImplants() {
        if(Cybersus.thaumcraftLoaded) {
            GameRegistry.registerItem(portableMultiAspectContainer, ItemPortableMultiAspectContainer.name);
            GameRegistry.registerItem(portablesingleAspectContainer, ItemPortableSingleAspectContainer.name);

            phoenixHeart = new ImplantPhoenixHeart();
            shadowSkin = new ImplantShadowSkin();
            exploser = new ImplantExploder();
            heartBlank = new ItemHeartBlank();
            gravityIcreaser = new ImplantGravityIcreaser();
            motherboardBlank = new ItemMotherboardBlank();

            if(Cybersus.forbiddenMagicLoaded) {
//                implantEssentiaHeart = new ImplantEssentiaHeart();
            }
            if(Cybersus.witcheryLoaded) {
                sleepDiver = new ImplantSleepDiver();
                tormentor = new ImplantTormentor();
            }
//            soulBreaker = new ImplantSoulBreaker();
        }
    }
}
