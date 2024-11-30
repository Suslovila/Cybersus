package com.suslovila.cybersus.api.fuel.impl;

import com.suslovila.cybersus.api.fuel.IFuel;
import net.minecraft.entity.player.EntityPlayer;

public final class FuelEmpty implements IFuel {
    public static IFuel INSTANCE = new FuelEmpty();

    private FuelEmpty() {
    }

    @Override
    public IFuel getLack(EntityPlayer player) {
        return INSTANCE;
    }

    @Override
    public boolean hasPlayerEnough(EntityPlayer player) {
        return true;
    }

    @Override
    public IFuel takeFrom(EntityPlayer player) {
        return INSTANCE;
    }

    @Override
    public void forceTakeFrom(EntityPlayer player) {

    }
    @Override
    public IFuel addTo(EntityPlayer player) {
        return INSTANCE;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }
}
