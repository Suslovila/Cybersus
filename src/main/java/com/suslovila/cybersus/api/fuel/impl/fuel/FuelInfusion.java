package com.suslovila.cybersus.api.fuel.impl.fuel;

import com.emoniph.witchery.infusion.Infusion;
import com.suslovila.cybersus.api.fuel.IFuel;
import com.suslovila.cybersus.api.fuel.impl.FuelEmpty;
import net.minecraft.entity.player.EntityPlayer;

public class FuelInfusion implements IFuel {

    Infusion infusionType;
    int requiredEnergy;

    public FuelInfusion(Infusion infusionType, int energy) {
        this.infusionType = infusionType;
        this.requiredEnergy = energy;
    }


    @Override
    public boolean hasPlayerEnough(EntityPlayer player) {
        return Infusion.getInfusionID(player) == infusionType.infusionID && Infusion.getCurrentEnergy(player) >= requiredEnergy;
    }

    @Override
    public IFuel getLack(EntityPlayer player) {
        if (Infusion.getInfusionID(player) == infusionType.infusionID) {
            return new FuelInfusion(infusionType, Math.max(0, Infusion.getCurrentEnergy(player) - this.requiredEnergy));
        }
        return new FuelInfusion(infusionType, this.requiredEnergy);
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
        Infusion.aquireEnergy(player.worldObj, player, requiredEnergy, false);
    }

    @Override
    public IFuel addTo(EntityPlayer player) {
        int maxEnergy = Infusion.getMaxEnergy(player);
        int currentPlayerEnergy = Infusion.getCurrentEnergy(player);
        int emptySpace = maxEnergy - currentPlayerEnergy;
        int toAdd = Math.min(this.requiredEnergy, emptySpace);
        Infusion.setCurrentEnergy(player, currentPlayerEnergy + toAdd);

        return new FuelInfusion(this.infusionType, this.requiredEnergy - toAdd);
    }

    @Override
    public boolean isEmpty() {
        return this.requiredEnergy <= 0 || this.infusionType.infusionID == Infusion.DEFUSED.infusionID;
    }
}
