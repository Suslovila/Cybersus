package com.suslovila.cybersus.common.item;

import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.api.implants.ImplantType;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class ItemHeartBlank extends Item {
    public static final String name = "heart_blank";
    private static IIcon[] icons;

    public ItemHeartBlank() {
        setUnlocalizedName(name);
        setTextureName(Cybersus.MOD_ID + ":" + name);
        setMaxStackSize(64);
        setHasSubtypes(true);
        setMaxDurability(0);
        setCreativeTab(Cybersus.tab);
        this.setMaxStackSize(1);

        register();

    }

    public void register() {
        GameRegistry.registerItem(this, name);
    }
}

