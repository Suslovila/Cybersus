package com.suslovila.cybersus.api.fuel.impl.fuel.essentia;

import baubles.api.BaublesApi;
import com.suslovila.cybersus.api.fuel.IFuel;
import com.suslovila.cybersus.api.fuel.impl.FuelEmpty;
import com.suslovila.cybersus.utils.Converter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FuelEssentia implements IFuel {

    private final AspectList aspects;

    public FuelEssentia(AspectList aspects) {
        this.aspects = aspects;
    }

    public FuelEssentia() {
        this.aspects = new AspectList();
    }


    // lazy init
    private static List<Converter<EntityPlayer, List<IEssentiaHolder>>> additionalEssentiaHolderProviders = new ArrayList<>();

    public static List<IEssentiaHolder> getPlacesToCheckForEssentia(EntityPlayer player) {

        List<IEssentiaHolder> holders = new ArrayList<>();
        for (Converter<EntityPlayer, List<IEssentiaHolder>> additionalHolder : additionalEssentiaHolderProviders) {
            holders.addAll(additionalHolder.convert(player));
        }

        IInventory baubles = BaublesApi.getBaubles(player);
        for (int a = 0; a < 4; a++) {
            ItemStack stack = baubles.getStackInSlot(a);
            if (stack == null) continue;
            Item itemType = stack.getItem();
            if (itemType instanceof IEssentiaHolderItem) {
                holders.add(new EssentiaHolderItemWrapper((IEssentiaHolderItem) itemType, stack));
            }
        }

        for (ItemStack stack : player.inventory.mainInventory) {
            if (stack == null) continue;
            Item itemType = stack.getItem();
            if (itemType instanceof IEssentiaHolderItem) {
                holders.add(new EssentiaHolderItemWrapper((IEssentiaHolderItem) itemType, stack));
            }
        }
        return holders;
    }

    @Override
    public boolean hasPlayerEnough(EntityPlayer player) {
        AspectList requiredEssentia = copyAspectList(this.aspects);
        List<IEssentiaHolder> playerEssentiaHolders = getPlacesToCheckForEssentia(player);
        for (IEssentiaHolder holder : playerEssentiaHolders) {
            AspectList essentiaInHolder = holder.getStoredEssentia();
            for (Map.Entry<Aspect, Integer> entry : essentiaInHolder.aspects.entrySet()) {
                requiredEssentia.remove(entry.getKey(), entry.getValue());
            }
        }

        return requiredEssentia.aspects.values().stream().noneMatch(amount -> amount != 0);
    }

    @Override
    public IFuel getLack(EntityPlayer player) {
        AspectList requiredEssentia = copyAspectList(this.aspects);
        List<IEssentiaHolder> playerEssentiaHolders = getPlacesToCheckForEssentia(player);

        for (IEssentiaHolder holder : playerEssentiaHolders) {
            Map<Aspect, Integer> essentiaInHolder = holder.getStoredEssentia().aspects;
            for (Map.Entry<Aspect, Integer> entry : essentiaInHolder.entrySet()) {
                Aspect aspectType = entry.getKey();
                Integer amount = entry.getValue();
                requiredEssentia.remove(aspectType, amount);
            }
        }
        return new FuelEssentia(requiredEssentia);
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
        List<IEssentiaHolder> playerEssentiaHolders = getPlacesToCheckForEssentia(player);
        ConcurrentHashMap<Aspect, Integer> fuelCopyAspects = new ConcurrentHashMap<>(copyAspectList(this.aspects).aspects);

        for (IEssentiaHolder holder : playerEssentiaHolders) {
            AspectList essentiaInHolder = copyAspectList(holder.getStoredEssentia());

            for (Map.Entry<Aspect, Integer> entry : fuelCopyAspects.entrySet()) {
                Aspect aspect = entry.getKey();
                Integer requiredAmount = entry.getValue();
                if (!essentiaInHolder.aspects.containsKey(aspect)) continue;
                int availableAmount = essentiaInHolder.aspects.get(aspect);

                int toTake = Math.min(availableAmount, requiredAmount);
                essentiaInHolder.remove(aspect, toTake);
                fuelCopyAspects.remove(aspect, toTake);
            }

            holder.setStoredAspects(essentiaInHolder);
        }
    }

    public AspectList copyAspectList(AspectList aspectList) {
        AspectList newAspectList = new AspectList();
        for (Map.Entry<Aspect, Integer> entry : aspectList.aspects.entrySet()) {
            newAspectList.add(entry.getKey(), entry.getValue());
        }
        return newAspectList;
    }

    @Override
    public IFuel addTo(EntityPlayer player) {
        Map<Aspect, Integer> essentiaToAdd = new ConcurrentHashMap<>(copyAspectList(this.aspects).aspects);
        for (IEssentiaHolder iEssentiaHolder : getPlacesToCheckForEssentia(player)) {
            for (Map.Entry<Aspect, Integer> entry : essentiaToAdd.entrySet()) {
                Aspect aspect = entry.getKey();
                Integer amount = entry.getValue();
                int overlappedAmount = iEssentiaHolder.add(aspect, amount);
                essentiaToAdd.remove(aspect, amount - overlappedAmount);
            }
        }
        return new FuelEssentia(new AspectList() {{
            this.aspects = new LinkedHashMap<>(essentiaToAdd);
        }});
    }

    @Override
    public boolean isEmpty() {
        return this.aspects.aspects.entrySet().stream().noneMatch(entry -> entry.getValue() != 0);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        int num = 0;
        for (Aspect aspect : aspects.getAspects()) {
            int amt = aspects.getAmount(aspect);
            if (amt > 0) {
                if (num > 0) stringBuilder.append(", ");


                stringBuilder.append(amt);
                stringBuilder.append(" ");
                stringBuilder.append(aspect.getName());
                num++;
            }
        }
        return stringBuilder.toString();
    }
}


