package com.suslovila.cybersus.api.implants.upgrades.rune;

import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.utils.KhariumSusNBTHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public interface RuneUsingItem {
    int getMaxRuneAmount();

    public static int getRuneAmountOfType(ItemStack stack, RuneType type) {
            if (stack.getItem() instanceof RuneUsingItem) {
                NBTTagCompound tagCompound = KhariumSusNBTHelper.getOrCreateTag(stack);
                        return KhariumSusNBTHelper.getOrCreateInteger(tagCompound, Cybersus.MOD_ID + ":rune_amount_of_type: " + type.toString(), 0);
            } else {
                return 0;
            }
        }

        public static void setRuneAmountOfType(ItemStack stack, RuneType type, int amount) {
            if (stack.getItem() instanceof RuneUsingItem) {
                RuneUsingItem runeClass = (RuneUsingItem) stack.getItem();
                NBTTagCompound tagCompound = KhariumSusNBTHelper.getOrCreateTag(stack);
                tagCompound.setInteger(
                        Cybersus.MOD_ID + ":rune_amount_of_type: " + type.toString(),
                        Math.min(amount, runeClass.getMaxRuneAmount())
                );
            }
        }
}
