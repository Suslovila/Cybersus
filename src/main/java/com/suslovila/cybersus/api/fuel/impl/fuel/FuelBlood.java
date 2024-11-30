package com.suslovila.cybersus.api.fuel.impl.fuel;

import WayofTime.alchemicalWizardry.api.soulNetwork.SoulNetworkHandler;
import com.emoniph.witchery.infusion.Infusion;
import com.suslovila.cybersus.api.fuel.IFuel;
import com.suslovila.cybersus.api.fuel.impl.FuelEmpty;
import fox.spiteful.forbidden.compat.BloodMagic;
import net.minecraft.entity.player.EntityPlayer;

public class FuelBlood implements IFuel {

    int requiredBlood;

    public FuelBlood(int requiredBlood) {
        this.requiredBlood = requiredBlood;
    }


    @Override
    public boolean hasPlayerEnough(EntityPlayer player) {
        return SoulNetworkHandler.getCurrentEssence(player.getDisplayName()) >= requiredBlood;
    }

    @Override
    public IFuel getLack(EntityPlayer player) {
        return new FuelBlood(Math.max(0, SoulNetworkHandler.getCurrentEssence(player.getDisplayName()) - requiredBlood));
    }

    @Override
    public IFuel takeFrom(EntityPlayer player) {
        IFuel lack = getLack(player);

        if (lack.isEmpty()) {
            forceTakeFrom(player);
            return FuelEmpty.INSTANCE;
        }
        return lack;
    }


    @Override
    public void forceTakeFrom(EntityPlayer player) {
        int currentAmount = SoulNetworkHandler.getCurrentEssence(player.getDisplayName());
        SoulNetworkHandler.setCurrentEssence(player.getDisplayName(), currentAmount - this.requiredBlood);
    }

    @Override
    public IFuel addTo(EntityPlayer player) {
        int maxEnergy = SoulNetworkHandler.getCurrentMaxOrb(player.getDisplayName());
        int currentPlayerEnergy = SoulNetworkHandler.getCurrentEssence(player.getDisplayName());
        int emptySpace = maxEnergy - currentPlayerEnergy;
        int toAdd = Math.min(this.requiredBlood, emptySpace);
        SoulNetworkHandler.setCurrentEssence(player.getDisplayName(), currentPlayerEnergy + toAdd);

        return new FuelBlood(this.requiredBlood - toAdd);
    }

    @Override
    public boolean isEmpty() {
        return this.requiredBlood <= 0;
    }
}
