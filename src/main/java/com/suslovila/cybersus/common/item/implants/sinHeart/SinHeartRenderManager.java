package com.suslovila.cybersus.common.item.implants.sinHeart;

import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.api.fuel.FuelComposite;
import com.suslovila.cybersus.api.fuel.impl.fuel.essentia.FuelEssentia;
import com.suslovila.cybersus.api.implants.ability.AbilityPassive;
import com.suslovila.cybersus.client.RenderHelper;
import com.suslovila.cybersus.utils.SusGraphicHelper;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

import java.util.Arrays;
import java.util.List;

public class SinHeartRenderManager {
    @SideOnly(Side.CLIENT)
    public static final List<ModelWrapped> hornModels = Arrays.asList(
            new ModelWrapped("models/zagredHorns.obj"),
            new ModelWrapped("models/simpleHorns.obj"),
            new ModelWrapped("models/liebeHorns.obj")

//            new ModelWrapped(),
//            new ModelWrapped(),
//            new ModelWrapped()
    );
    @SideOnly(Side.CLIENT)
    public static final List<WingTexture> wingModels = Arrays.asList(
            new WingTexture(new ResourceLocation(Cybersus.MOD_ID, "textures/wings/liebeWing.png"), 1.5),
            new WingTexture(new ResourceLocation(Cybersus.MOD_ID, "textures/wings/longWing.png"), 2.5),
            new WingTexture(new ResourceLocation(Cybersus.MOD_ID, "textures/wings/luciferoWing.png"), 0.0),
            new WingTexture(new ResourceLocation(Cybersus.MOD_ID, "textures/wings/megikulaWing.png"), 0.5)
    );

}
