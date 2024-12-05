//package com.suslovila.cybersus.api.essentia;
//
//import net.minecraft.item.ItemStack;
//import thaumcraft.api.aspects.Aspect;
//import thaumcraft.api.aspects.AspectList;
//
//public class EssentiaHolderItemWrapper implements IEssentiaHolder {
//    private final IEssentiaHolderItem itemType;
//    private final ItemStack itemStackIn;
//
//    public EssentiaHolderItemWrapper(IEssentiaHolderItem itemType, ItemStack itemStackIn) {
//        this.itemType = itemType;
//        this.itemStackIn = itemStackIn;
//    }
//
//    @Override
//    public AspectList getStoredEssentia() {
//        return itemType.getStoredAspects(itemStackIn);
//    }
//
//    @Override
//    public void setStoredAspects(AspectList aspects) {
//        itemType.setStoredAspects(itemStackIn, aspects);
//    }
//
//    @Override
//    public int  add(Aspect aspect, int amount) {
//        return itemType.addAspect(itemStackIn, aspect, amount);
//    }
//
//}
//
