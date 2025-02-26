package com.suslovila.cybersus.common.item;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.research.CybersusAspect;
import com.suslovila.cybersus.utils.KhariumSusNBTHelper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;

public class ItemSuppressed extends Item {
    public static String SUPPRESSED_STACK_KEY = Cybersus.prefixAppender.doAndGet("suppressed_stack");
    public static String TIME_SUPPRESSED_LEFT = Cybersus.prefixAppender.doAndGet("suppressed_time_left");

    public ItemSuppressed() {
        setUnlocalizedName(getName());
        setTextureName(Cybersus.MOD_ID + ":" + getName());
//        setCreativeTab(Cybersus.tab);

        register();

    }

    public void register() {
        GameRegistry.registerItem(this, getName());
    }

    private static String getName() {
        return "item_suppressed";
    }

//    @Override
//    public boolean requiresMultipleRenderPasses() {
//        return false;
//    }
//
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
            EnumRarity rarity = getRarity(suppressedStack);

            String suppresedString = ChatFormatting.DARK_RED.toString() + StatCollector.translateToLocal("key.suppressed") + ": ";
            return suppresedString + rarity.rarityColor.toString() +  suppressedStack.getItem().getItemStackDisplayName(suppressedStack);
        }

        return super.getItemStackDisplayName(stack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(int par1, int renderPass) {

        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.5f);
        return super.getIconFromDamageForRenderPass(par1, renderPass);
    }


//
//    public IIcon getIcon(ItemStack stack, int pass) {
//        NBTTagCompound tag = KhariumSusNBTHelper.getOrCreateTag(stack);
//        if (tag.hasKey(SUPPRESSED_STACK_KEY) && pass != 5) {
//            ItemStack suppressedStack = ItemStack.loadItemStackFromNBT(tag.getCompoundTag(SUPPRESSED_STACK_KEY));
//            return suppressedStack.getItem().getIcon(suppressedStack, pass);
//        }
//        return itemIcon;
//
//    }
//
//
//    @Override
//    @SideOnly(Side.CLIENT)
//    public int getColorFromItemStack(ItemStack stack, int renderPass) {
//        if (renderPass != 5)
//            return 0xFFFFFF;
//
//        Color color = new Color(66, 62, 62);
//        return color.getRGB();
//    }
//
//    @Override
//    public int getRenderPasses(int metadata) {
//        return 5;
//    }


    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int renderPass) {
//        Color previousColor = new Color(CybersusAspect.HUMILITAS.getColor(), false);
//        Color newColor = new Color(previousColor.getRed() / 255f, previousColor.getGreen() / 255f, previousColor.getBlue() / 255f, 0.7f);
//        return newColor.getRGB();

        return CybersusAspect.HUMILITAS.getColor();
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
