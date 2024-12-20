package com.suslovila.cybersus.api.fuel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.suslovila.cybersus.utils.SusCollectionUtils;
import net.minecraft.entity.player.EntityPlayer;

// collective class for fuels helping operating with several fuel instances/
// all methods just wrap the same methods for each fuel

public class FuelComposite {
    public final List<IFuel> fuels;
    public final List<FuelVariation> fuelVariations;

    public static final FuelComposite EMPTY = new FuelComposite(new ArrayList<>());

    public FuelComposite(List<IFuel> fuels) {
        this.fuels = fuels;
        fuelVariations = new ArrayList<>();
    }

    public boolean hasPlayerEnough(EntityPlayer player) {
        return fuels.stream().allMatch(fuel -> fuel.hasPlayerEnough(player)) && fuelVariations.stream().allMatch(fuelVariation -> fuelVariation.hasPlayerEnough(player));
    }

    public void forceTakeFrom(EntityPlayer player) {
        fuels.forEach(fuel -> fuel.forceTakeFrom(player));
        fuelVariations.forEach(fuelVariation -> fuelVariation.tryTakeFuelFromPlayer(player));
    }
//
//    public boolean isEmpty() {
//        boolean areVariationsEmpty = fuelVariations.isEmpty() || fuelVariations.stream().anyMatch(FuelVariation::isEmpty);
//        return fuels.stream().allMatch(IFuel::isEmpty) && areVariationsEmpty;
//    }

    public boolean tryTakeFuelFromPlayer(EntityPlayer player) {
        boolean hasEnough = hasPlayerEnough(player);
        if (hasEnough) {
            forceTakeFrom(player);
        }
        return hasEnough;
    }

    public static FuelComposite allRequired(IFuel... fuels) {
        return new FuelComposite(SusCollectionUtils.arrayListOf(fuels));
    }

    public FuelComposite addComplexVariation(FuelComposite... fuelComposites) {
        FuelVariation fuelVariation = new FuelVariation(SusCollectionUtils.arrayListOf(fuelComposites));
        this.fuelVariations.add(fuelVariation);

        return this;
    }

    public FuelComposite addSimpleVariationOfFuels(IFuel... fuels) {
        List<FuelComposite> wrappedFuelComposites = Arrays.stream(fuels).map(iFuel -> new FuelComposite(Collections.singletonList(iFuel))).collect(Collectors.toList());
        FuelVariation fuelVariation = new FuelVariation(wrappedFuelComposites);
        this.fuelVariations.add(fuelVariation);

        return this;
    }

    public FuelComposite addRequiredFuel(IFuel fuel) {
        this.fuels.add(fuel);

        return this;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        int num = 0;
        stringBuilder.append("(");
        for (IFuel fuel : fuels) {
            if (!fuel.isEmpty() && num > 0) stringBuilder.append("and ");

            stringBuilder.append(fuel.toString());
            num++;
        }

        for (FuelVariation variation : fuelVariations) {
            stringBuilder.append("and (");

            stringBuilder.append(variation.toString());
            num++;
            stringBuilder.append(")");
        }
        stringBuilder.append(")");

        return stringBuilder.toString();
    }
}