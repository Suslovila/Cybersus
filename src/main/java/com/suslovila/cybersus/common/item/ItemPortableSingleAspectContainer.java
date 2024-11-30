package com.suslovila.cybersus.common.item;

import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.api.fuel.impl.fuel.essentia.IEssentiaHolderItem;
import com.suslovila.cybersus.api.implants.upgrades.rune.RuneType;
import com.suslovila.cybersus.api.implants.upgrades.rune.RuneUsingItem;
import com.suslovila.cybersus.client.gui.CybersusGui;
import com.suslovila.cybersus.utils.KhariumSusNBTHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.common.Thaumcraft;

import java.util.List;

public class ItemPortableSingleAspectContainer extends ItemPortableMultiAspectContainer  {
    public static final String name = "single_aspect_holder";

    public ItemPortableSingleAspectContainer() {
        setUnlocalizedName(name);
        setTextureName(Cybersus.MOD_ID + ":" + name);
        setMaxStackSize(1);
        setCreativeTab(Cybersus.tab);
    }


    @Override
    public int getMaxVisCountForEachAspect(ItemStack stack) {
        return 1024;
    }

    @Override
    public int getMaxAspectTypesAmount(ItemStack stack) {
        return 1;
    }
}