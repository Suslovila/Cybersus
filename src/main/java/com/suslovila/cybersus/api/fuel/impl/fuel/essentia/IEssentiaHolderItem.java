
package com.suslovila.cybersus.api.fuel.impl.fuel.essentia;

import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public interface IEssentiaHolderItem {
    public AspectList getStoredAspects(ItemStack stack);

    public void setStoredAspects(ItemStack stack, AspectList aspects);

    public int addAspect(ItemStack stack, Aspect aspect, int amount);

}