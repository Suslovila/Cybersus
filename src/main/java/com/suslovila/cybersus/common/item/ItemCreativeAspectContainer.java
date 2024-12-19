package com.suslovila.cybersus.common.item;

import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.api.fuel.impl.fuel.essentia.IEssentiaHolderItem;
import com.suslovila.cybersus.api.implants.upgrades.rune.RuneUsingItem;
import com.suslovila.cybersus.client.gui.CybersusGui;
import com.suslovila.cybersus.research.CybersusAspect;
import com.suslovila.cybersus.utils.KhariumSusNBTHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.common.Thaumcraft;

import java.util.List;

public class ItemCreativeAspectContainer extends Item implements RuneUsingItem, IEssentiaHolderItem {
    public static final String name = "creative_aspect_holder";

    public ItemCreativeAspectContainer() {
        setUnlocalizedName(name);
        setTextureName(Cybersus.MOD_ID + ":" + name);
        setMaxStackSize(1);
        setCreativeTab(Cybersus.tab);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean wtfIsThisVariable) {
        list.add("Creative mode only");
    }

    @Override
    public AspectList getStoredAspects(ItemStack stack) {
        return CybersusAspect.getAspectListOfAllMax().copy();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(int par1, int renderPass) {

        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        return super.getIconFromDamageForRenderPass(par1, renderPass);
    }

    @Override
    public void setStoredAspects(ItemStack stack, AspectList aspects) {

    }

    @Override
    public int addAspect(ItemStack stack, Aspect aspect, int amount) {
        return amount;
    }

    public int getMaxVisCountForEachAspect(ItemStack stack) {
        return Integer.MAX_VALUE;
    }

    @Override
    public int getMaxRuneAmount() {
        return 5;
    }

    public int getMaxAspectTypesAmount(ItemStack stack) {
        return Aspect.aspects.size();

    }


}