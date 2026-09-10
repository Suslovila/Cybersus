package com.suslovila.cybersus.common.item.implants;

import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.api.fuel.FuelComposite;
import com.suslovila.cybersus.api.fuel.FuelVariation;
import com.suslovila.cybersus.api.fuel.impl.fuel.essentia.FuelEssentia;
import com.suslovila.cybersus.api.implants.ImplantType;
import com.suslovila.cybersus.api.implants.ability.Ability;
import com.suslovila.cybersus.api.implants.ability.AbilityInstant;
import com.suslovila.cybersus.api.implants.ability.AbilityPassive;
import com.suslovila.cybersus.client.TextureStorage;
import com.suslovila.cybersus.utils.SusCollectionUtils;
import com.suslovila.cybersus.utils.SusGraphicHelper;
import fox.spiteful.forbidden.DarkAspects;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class ImplantGungnir extends ItemCybersusImplant {
    public static final ArrayList<Ability> abilities = new ArrayList<>();

    public ImplantGungnir() {
        super(ImplantType.HAND);
    }

    @Override
    public List<Ability> getAbilities(EntityPlayer player, int index, ItemStack implant) {
        return abilities;
    }

    static {
        abilities.add(new AbilityInstant("gungnir_strike") {

            @Override
            protected void onActivated(EntityPlayer player, int index, ItemStack implant) {

            }

            @Override
            public int getCooldownTotal(EntityPlayer player, int index, ItemStack implant) {
                return 1 * 60 * 20;
            }


            @Override
            public FuelComposite getFuelConsumeOnActivation(EntityPlayer player, int index, ItemStack implant) {
                return FuelComposite.EMPTY;
            }

            public void onRenderWorldLastEvent(RenderWorldLastEvent event, EntityPlayer player, int index, ItemStack implant) {
                SusGraphicHelper.bindTexture(TextureStorage.gungnirCircles);
                glColor4d(1.0, 1.0, 1.0, 0.0);
                glEnable(GL_BLEND);
                glBlendFunc(GL_SRC_ALPHA, GL_ONE);
                glEnable(GL_LIGHTING);
                SusGraphicHelper.pushLight();

                
            }


        });
    }

    @Override
    public String getName() {
        return "berserk_heart";
    }
}

