package com.suslovila.cybersus.common.item;

import com.suslovila.cybersus.Cybersus;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;

public class ItemProtonIngot extends Item {
    public static final String name = "proton_ingot";
    private static IIcon[] icons;

    public ItemProtonIngot() {
        setUnlocalizedName(name);
        setTextureName(Cybersus.MOD_ID + ":" + name);
        setMaxStackSize(64);
        setHasSubtypes(true);
        setMaxDurability(0);
        setCreativeTab(Cybersus.tab);

        register();

    }

    public void register() {
        GameRegistry.registerItem(this, name);
    }
}

