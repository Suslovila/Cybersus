package com.suslovila.cybersus.api.fuel;

import cpw.mods.fml.common.network.ByteBufUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import java.awt.Color;
import java.util.ArrayList;

// represents the fuel consumed by any implant
public interface IFuel {
    /**
     * gives lack amount of current fuel
     * @param player
     * @return lack fuel
     */
    public IFuel getLack(EntityPlayer player);
    /**
     *
     * @param player
     * @return hasPlayerEnoughFuel
     */
    boolean hasPlayerEnough(EntityPlayer player);

    /**
     * must check firstly that player has enough fuel. If true, takes it. In any case returns
     * amount of fuel that is not enough (if player had enough, returns fuel that is empty)
     * @param player
     * @return lack of fuel (if player had enough, returns Fuel obj instance that is empty, meaning success of taking
     */
    IFuel takeFrom(EntityPlayer player);

    /**
     * does not check if player has enough. Just take fuel
     * @param player
     */
    void forceTakeFrom(EntityPlayer player);

    /**
     * adds fuel to player
     * @param player
     * @return amount that WAS NOT added
     */
    IFuel addTo(EntityPlayer player);

    /**
     * checks if fuel is empty, meaning zero amount
     * @return if fuel is empty
     */
    boolean isEmpty();

}