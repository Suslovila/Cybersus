package com.suslovila.cybersus.common.item;

import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.api.implants.upgrades.rune.RuneType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.creativetab.CreativeTabs;

import java.util.List;

public class ItemRune extends Item {
    public static final String name = "rune";
    private static IIcon[] icons;

    public ItemRune() {
        setUnlocalizedName(name);
        setTextureName(Cybersus.MOD_ID + ":" + name);
        setMaxStackSize(64);
        setHasSubtypes(true);
        setMaxDurability(0);
        setCreativeTab(Cybersus.tab);
    }


    @Override
    public void registerIcons(IIconRegister register) {
        icons = new IIcon[RuneType.values().length];
        for (int index = 0; index < RuneType.values().length; index++) {
            icons[index] = register.registerIcon(Cybersus.MOD_ID + ":rune_" + RuneType.values()[index]);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIconFromDamage(int meta) {
        return (meta < icons.length) ? icons[meta] : icons[0];
    }

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack) {
        return getUnlocalizedName() + "." + par1ItemStack.getMetadata();
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tabs, List list) {
        for (RuneType runeType : RuneType.values()) {
            list.add(new ItemStack(this, 1, runeType.ordinal()));
        }
    }
}

