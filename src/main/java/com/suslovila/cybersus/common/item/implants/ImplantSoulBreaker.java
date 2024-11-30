package com.suslovila.cybersus.common.item.implants;

import com.suslovila.cybersus.api.fuel.FuelComposite;
import com.suslovila.cybersus.api.implants.ImplantType;
import com.suslovila.cybersus.api.implants.ability.Ability;
import com.suslovila.cybersus.api.implants.ability.AbilityPassive;
import com.suslovila.cybersus.common.processes.ProcessGravityTrap;
import com.suslovila.cybersus.common.processes.ProcessSoulBreak;
import com.suslovila.cybersus.common.sync.CybersusPacketHandler;
import com.suslovila.cybersus.common.sync.PacketSyncProcess;
import com.suslovila.cybersus.extendedData.CustomWorldData;
import com.suslovila.cybersus.utils.SusVec3;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.ArrayList;
import java.util.List;

public class ImplantSoulBreaker extends ItemCybersusImplant {
    public static final ArrayList<Ability> abilities = new ArrayList<>();

    public ImplantSoulBreaker() {
        super(ImplantType.BRAIN);

    }

    @Override
    public String getName() {
        return "soul_breaker";
    }

    @Override
    public List<Ability> getAbilities(EntityPlayer player, int index, ItemStack implant) {
        return abilities;
    }

    static {
        abilities.add(new AbilityPassive("soul_breaking") {

            @Override
            public FuelComposite getFuelConsumePerCheck(EntityPlayer player, int index, ItemStack implant) {
                return FuelComposite.EMPTY;
            }

            @Override
            public FuelComposite getFuelConsumeOnActivation(EntityPlayer player, int index, ItemStack implant) {
                return FuelComposite.EMPTY;
            }

            @Override
            public int getCooldownTotal(EntityPlayer player, int index, ItemStack implant) {
                return 120;
            }

            @Override
            public void onPlayerHurtEventIfAttacker(LivingHurtEvent event, EntityPlayer player, int index, ItemStack implant) {
                if(player.worldObj.isRemote) return;
                ProcessGravityTrap processSoulBreak = new ProcessGravityTrap(SusVec3.getEntityPos(event.entityLiving).add(new SusVec3(0.0, event.entityLiving.height / 2, 0.0)), 20 * 1000);
                CustomWorldData.getCustomData(player.worldObj).addProcess(processSoulBreak);
                CustomWorldData.syncProcess(processSoulBreak);
//                FXBrokenSoul fxBrokenSoul = new FXBrokenSoul(player.worldObj, player.posX, player.posY, player.posZ, 0.0, 0.0, 0.0, 1000, 0.2f, true);
//                ParticleEngine.instance.addEffect(player.worldObj, fxBrokenSoul);
            }
            @Override
            public void onPlayerHurtEventIfVictim(LivingHurtEvent event, EntityPlayer player, int index, ItemStack implant) {
//                FXBrokenSoul fxBrokenSoul = new FXBrokenSoul(player.worldObj, player.posX, player.posY, player.posZ, 0.0, 0.0, 0.0, 1000, 0.2f, true);
//                ParticleEngine.instance.addEffect(player.worldObj, fxBrokenSoul);
            }
        });
    }
}