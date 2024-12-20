package com.suslovila.cybersus.common.item;

import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.utils.KhariumSusNBTHelper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.awt.*;
import java.util.List;

public class ItemSuppressed extends Item {
    public static String SUPPRESSED_STACK_KEY = Cybersus.prefixAppender.doAndGet("suppressed_stack");
    public static String TIME_SUPPRESSED_LEFT = Cybersus.prefixAppender.doAndGet("suppressed_time_left");

    public ItemSuppressed() {
        setUnlocalizedName(getName());
        setTextureName(Cybersus.MOD_ID + ":" + getName());
        setCreativeTab(Cybersus.tab);

        register();

    }

    public void register() {
        GameRegistry.registerItem(this, getName());
    }

    private static String getName() {
        return "item_suppressed";
    }

    @Override
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List info, boolean isShiftPressed) {
        NBTTagCompound tag = KhariumSusNBTHelper.getOrCreateTag(stack);
        if (tag.hasKey(SUPPRESSED_STACK_KEY)) {
            ItemStack suppressedStack = ItemStack.loadItemStackFromNBT(tag.getCompoundTag(SUPPRESSED_STACK_KEY));
            suppressedStack.getItem().addInformation(suppressedStack, player, info, isShiftPressed);
        }
    }
    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        NBTTagCompound tag = KhariumSusNBTHelper.getOrCreateTag(stack);
        if (tag.hasKey(SUPPRESSED_STACK_KEY)) {
            ItemStack suppressedStack = ItemStack.loadItemStackFromNBT(tag.getCompoundTag(SUPPRESSED_STACK_KEY));
            String suppresedString = StatCollector.translateToLocal("key.suppressed");
            return suppresedString + ": " + suppressedStack.getItem().getItemStackDisplayName(suppressedStack);
        }

        return super.getItemStackDisplayName(stack);
    }

    public IIcon getIcon(ItemStack stack, int pass) {
        NBTTagCompound tag = KhariumSusNBTHelper.getOrCreateTag(stack);
        if (tag.hasKey(SUPPRESSED_STACK_KEY) && pass != 5) {
            ItemStack suppressedStack = ItemStack.loadItemStackFromNBT(tag.getCompoundTag(SUPPRESSED_STACK_KEY));
            return suppressedStack.getItem().getIcon(suppressedStack, pass);
        }
        return itemIcon;

    }


    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int renderPass) {
        if (renderPass != 5)
            return 0xFFFFFF;

        Color color = new Color(66, 62, 62);
        return color.getRGB();
    }

    @Override
    public int getRenderPasses(int metadata) {
        return 5;
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int slot, boolean isSelected) {
        // Проверяем, выполняется ли код на сервере
        if (!worldIn.isRemote && entityIn instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityIn;
            NBTTagCompound tag = KhariumSusNBTHelper.getOrCreateTag(stack);
            if (tag.hasKey(TIME_SUPPRESSED_LEFT)) {
                int timeLeft = tag.getInteger(TIME_SUPPRESSED_LEFT);
                if (timeLeft == 0) {
                    if (player.inventory.mainInventory[slot].equals(stack)) {
                        if (tag.hasKey(SUPPRESSED_STACK_KEY)) {
                            ItemStack suppressedStack = ItemStack.loadItemStackFromNBT(tag.getCompoundTag(SUPPRESSED_STACK_KEY));
                            player.inventory.mainInventory[slot] = suppressedStack;
                        }
                    }
                    tag.removeTag(TIME_SUPPRESSED_LEFT);
                    tag.removeTag(SUPPRESSED_STACK_KEY);
                }

                else {
                    tag.setInteger(TIME_SUPPRESSED_LEFT, timeLeft - 1);
                }
            }
        }
    }

}
