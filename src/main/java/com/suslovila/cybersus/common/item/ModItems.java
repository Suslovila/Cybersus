package com.suslovila.cybersus.common.item;

import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.common.item.implants.*;
import com.suslovila.cybersus.common.item.implants.sinHeart.ImplantSinHeart;
import com.suslovila.cybersus.common.item.implants.ImplantShadowSkin;
import com.suslovila.cybersus.common.item.implants.witchery.ImplantSleepModule;
import com.suslovila.cybersus.common.item.implants.witchery.ImplantTormentor;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModItems {


    public static ImplantPhoenixHeart phoenixHeart;
    public static ImplantExploder exploser;

    public static ImplantSleepModule sleepModule;
    public static ImplantTormentor tormentor;
    public static ImplantSoulBreaker soulBreaker;

    public static ImplantSinHeart implantSinHeart;
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
                implantSinHeart = new ImplantSinHeart();
            }
            if(Cybersus.witcheryLoaded) {
                sleepModule = new ImplantSleepModule();
                tormentor = new ImplantTormentor();
            }
//            soulBreaker = new ImplantSoulBreaker();
        }
    }
}
