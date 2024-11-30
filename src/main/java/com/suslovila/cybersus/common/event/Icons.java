package com.suslovila.cybersus.common.event;

import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.api.implants.ImplantType;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraftforge.client.event.TextureStitchEvent;

public class Icons {
    public static final Icons INSTANCE = new Icons();
    private Icons(){}
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onItemIconRegister(TextureStitchEvent event) {
        TextureMap iconRegister = event.map;
        if (iconRegister.getTextureType() == 1) {
            for (ImplantType type : ImplantType.values()) {
                ImplantType.iconLocations.add(
                    type.ordinal(), iconRegister.registerIcon(Cybersus.MOD_ID + ":" + type)
                );
            }
        }
    }
}
