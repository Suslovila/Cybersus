package com.suslovila.cybersus.common.item.implants;

import com.suslovila.cybersus.api.fuel.FuelComposite;
import com.suslovila.cybersus.api.fuel.impl.fuel.essentia.FuelEssentia;
import com.suslovila.cybersus.api.implants.ImplantType;
import com.suslovila.cybersus.api.implants.ability.Ability;
import com.suslovila.cybersus.api.implants.ability.AbilityHack;
import com.suslovila.cybersus.client.clientProcess.processes.illusion.ProcessIllusion;
import com.suslovila.cybersus.extendedData.CustomWorldData;
import com.suslovila.cybersus.utils.SusGraphicHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

import java.util.ArrayList;
import java.util.List;

public class ImplantIllusionGenerator extends ItemCybersusImplant {
    public static final String name = "illusion_generator";
    public static final ArrayList<Ability> abilities = new ArrayList<>();

    static {
        abilities.add(new AbilityHack("illusions") {

            @Override
            public FuelComposite getFuelConsumeOnActivation(EntityPlayer player, int index, ItemStack implant) {
                FuelComposite fuelComposite = FuelComposite.allRequired();
                fuelComposite.addRequiredFuel(new FuelEssentia(new AspectList().add(Aspect.TRAP, 128).add(Aspect.MIND, 128)));

                return fuelComposite;
            }

            @Override
            public int getCooldownTotal(EntityPlayer player, int index, ItemStack implant) {
                return 20 * 30;
            }

            @Override
            public int getRequiredHackTime() {
                return 20 * 8;
            }

            @Override
            public void hackEntity(EntityPlayer hacker, Entity victim, int slotIndex, ItemStack implant) {
                if(!(victim instanceof EntityPlayerMP)) return;
                ProcessIllusion processIllusion = new ProcessIllusion(hacker, (EntityPlayerMP) victim, 20 * 20);
                CustomWorldData.syncProcess(processIllusion, (EntityPlayerMP) victim);
            }

            @Override
            public double getLockDistance(EntityPlayer player, int index, ItemStack implant) {
                return 50;
            }

            @Override
            public double getLoseDistance(EntityPlayer player, int index, ItemStack implant) {
                return 64;
            }

            @Override
            public int getTargetTimeLose(EntityPlayer player, int index, ItemStack implant) {
                return 60;
            }

            @Override
            public boolean canHackEntity(EntityPlayer hacker, Entity victim, int slotIndex, ItemStack implant) {
                return victim instanceof EntityPlayer;
            }

            @Override
            public void renderAbility(RenderGameOverlayEvent.Post event, ItemStack implant, float scale, double radius) {
                GL11.glPushAttrib(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                super.renderAbility(event, implant, scale, radius);
                GL11.glPopAttrib();
            }

        });
    }

    public ImplantIllusionGenerator() {
        super(ImplantType.OCULAR_SYSTEM);
    }

    @Override
    public List<Ability> getAbilities(EntityPlayer player, int index, ItemStack implant) {
        return abilities;
    }

    @Override
    public String getName() {
        return "illusion_generator";
    }
}

