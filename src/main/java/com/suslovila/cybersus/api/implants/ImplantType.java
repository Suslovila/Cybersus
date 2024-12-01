package com.suslovila.cybersus.api.implants;

import net.minecraft.util.IIcon;

import java.util.ArrayList;
import java.util.List;

public enum ImplantType {
    HEAD(1),
    BRAIN(4),
    SKELETON(1),
    HAND(2),
    LEGS(2),
    FOOT(2),
    HEART(1),
    CIRCULATORY_SYSTEM(1),
    OCULAR_SYSTEM(2),
    NERVOUS_SYSTEM(1),
    SKIN(1),
    LUNGS(1);

    ImplantType(int defAmount) {
        defaultSlotAmount = defAmount;
    }
    public int defaultSlotAmount;

    public static final List<Integer> slotAmounts = new ArrayList<>();
    public static final List<IIcon> iconLocations = new ArrayList<>();

    public static int getSlotAmount(ImplantType type) {
        return slotAmounts.get(type.ordinal());
    }

    public static IIcon getIcon(ImplantType type) {
        return iconLocations.get(type.ordinal());
    }

    public static final int typeAmount = ImplantType.values().length;



    // lazy initialisation in java is crazy -_-
    public static int[] getFirstIndexes() {
        if(firstIndexes == null) {
            firstIndexes = initializeFirstIndexes();
        }

        return firstIndexes;
    }
    private static int[] firstIndexes;

    private static int[] initializeFirstIndexes() {
        int[] array = new int[typeAmount];
        for (int i = 1; i < typeAmount; i++) {
            ImplantType previousType = ImplantType.values()[i - 1];
            array[i] = array[i - 1] + getSlotAmount(previousType);
        }
        return array;
    }




    public static int getTotalSlotAmount() {
        return getFirstIndexes()[typeAmount - 1] + getSlotAmount(ImplantType.values()[typeAmount - 1]);
    }

    public static int getFirstSlotIndexOf(ImplantType type) {
        return getFirstIndexes()[type.ordinal()];
    }

    public static ImplantType getTypeForSlotWithIndex(int slotIndex) {
        for (int typeIndex = 1; typeIndex < typeAmount; typeIndex++) {
            boolean isRequiredSlotBetween =
                    slotIndex < getFirstIndexes()[typeIndex] && slotIndex >= getFirstIndexes()[typeIndex - 1];
            if (isRequiredSlotBetween) {
                return ImplantType.values()[typeIndex - 1];
            }
        }
        if (slotIndex >= getFirstIndexes()[typeAmount - 1] && slotIndex < getTotalSlotAmount()) {
            return ImplantType.values()[typeAmount - 1];
        }
        return null;
    }
}


