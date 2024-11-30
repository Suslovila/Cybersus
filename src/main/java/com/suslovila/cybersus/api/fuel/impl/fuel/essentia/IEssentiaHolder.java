package com.suslovila.cybersus.api.fuel.impl.fuel.essentia;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IEssentiaContainerItem;



public interface IEssentiaHolder {
    AspectList getStoredEssentia();

    // returns the overlapping aspects
    void setStoredAspects(AspectList aspects);

    /**
     * adds aspect to holder
     * @param aspect
     * @param amount
     * @return overlapped amount
     */
    int add(Aspect aspect, int amount);

}

