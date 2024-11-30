package com.suslovila.cybersus.api.fuel;

import com.suslovila.cybersus.api.fuel.impl.FuelEmpty;
import com.suslovila.cybersus.utils.CollectionUtils;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

// represents variants that can be valid. At least one fuelComposite must fit
public class FuelVariation {
    public static final FuelVariation EMPTY = new FuelVariation(Collections.singletonList(new FuelComposite(Collections.singletonList(FuelEmpty.INSTANCE))));
    public List<FuelComposite> fuelComposites = new ArrayList<>();
    public FuelVariation(List<FuelComposite> fuels) {
        this.fuelComposites = fuels;
    }
    public FuelVariation() {
    }
    public boolean tryTakeFuelFromPlayer(EntityPlayer player) {
        Optional<FuelComposite> firstWhichPlayerHas = CollectionUtils.first(fuelComposites, (fuelComposite -> fuelComposite.hasPlayerEnough(player)));
        if(firstWhichPlayerHas.isPresent()) {
            FuelComposite fuelComposite = firstWhichPlayerHas.get();
            fuelComposite.forceTakeFrom(player);
        }

        return firstWhichPlayerHas.isPresent();
    }

    public boolean hasPlayerEnough(EntityPlayer player) {
        Optional<FuelComposite> firstWhichPlayerHas = CollectionUtils.first(fuelComposites, (fuelComposite -> fuelComposite.hasPlayerEnough(player)));
        return firstWhichPlayerHas.isPresent();
    }

    public FuelVariation addSimpleVariant(IFuel fuel) {
        fuelComposites.add(FuelComposite.allRequired(fuel));

        return this;
    }
    public FuelVariation addVariant(FuelComposite fuelComposite) {
        fuelComposites.add(fuelComposite);

        return this;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        int num = 0;
        for (FuelComposite fuel : fuelComposites) {
            if (!(fuel.fuels.stream().allMatch(IFuel::isEmpty)) && num > 0) stringBuilder.append("or ");

            stringBuilder.append(fuel);
            num++;
        }


        return stringBuilder.toString();
    }
}
