//package com.suslovila.cybersus.api.essentia;
//
//import com.suslovila.cybersus.api.fuel.IFuel;
//import net.minecraft.entity.player.EntityPlayer;
//import thaumcraft.api.aspects.Aspect;
//import thaumcraft.api.aspects.AspectList;
//
//
//public interface IEssentiaHolder {
//    /**
//     * gives lack amount of current fuel
//     * @param player
//     * @return lack fuel
//     */
//    public AspectList getLack(EntityPlayer player);
//    /**
//     * does not check if player has enough. Just take fuel
//     */
//    void forceTakeFrom(AspectList aspectList);
//
//    /**
//     * adds aspects
//     * @return amount that WAS NOT added
//     */
//    AspectList add(AspectList aspectList);
//
//    /**
//     * checks if fuel is empty, meaning zero amount
//     * @return if fuel is empty
//     */
//}
//
