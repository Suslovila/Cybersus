package com.suslovila.cybersus.client;

import com.suslovila.cybersus.Cybersus;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;

public class TextureStorage {

    public static ResourceLocationPreLoad textureInnerCircle;
    public static ResourceLocationPreLoad textureInner;
    public static ResourceLocationPreLoad textureOuter;
    public static ArrayList<ResourceLocation> textureOuterCircles = new ArrayList<>();


    public static ResourceLocationPreLoad slotActive;
    public static ResourceLocationPreLoad slotDeactivated;
    public static ResourceLocationPreLoad slotInCooldown;

    public static ResourceLocationPreLoad spinningCircle;


    public static ResourceLocationPreLoad hackHotbar;
    public static ResourceLocationPreLoad hackHotbarActivated;


    public static void init() {
        textureInnerCircle = new ResourceLocationPreLoad(Cybersus.MOD_ID, "textures/gui/implants/ffhack_circle_inner.png");
        textureInner = new ResourceLocationPreLoad(Cybersus.MOD_ID, "textures/processes/gravity_trap_inner.png");
        textureOuter = new ResourceLocationPreLoad(Cybersus.MOD_ID, "textures/processes/gravity_trap_outer.png");

        for (int i = 0; i < 3; i++) {
            int realIndex = i + 1;
            textureOuterCircles.add(new ResourceLocationPreLoad(Cybersus.MOD_ID, "textures/gui/implants/fhack_circle_outer_" + realIndex + ".png"));
        }

        slotActive = new ResourceLocationPreLoad(Cybersus.MOD_ID, "textures/gui/implants/abilitySlotActive.png");
        slotDeactivated = new ResourceLocationPreLoad(Cybersus.MOD_ID, "textures/gui/implants/abilitySlotDeactivated.png");
        slotInCooldown = new ResourceLocationPreLoad(Cybersus.MOD_ID, "textures/gui/implants/abilitySlotCooldown.png");

        spinningCircle = new ResourceLocationPreLoad(Cybersus.MOD_ID, "textures/misc/radial4.png");
        hackHotbar = new ResourceLocationPreLoad(Cybersus.MOD_ID, "textures/gui/implants/hack_hotbar.png");
        hackHotbarActivated = new ResourceLocationPreLoad(Cybersus.MOD_ID, "textures/gui/implants/hack_hotbar_activated.png");
    }
}
