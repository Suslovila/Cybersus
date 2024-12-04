package com.suslovila.cybersus.utils;

import com.gamerforea.eventhelper.util.EventUtils;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RegionHelper {
	private static final boolean canUseEventHelper = Loader.isModLoaded("EventHelper") && FMLCommonHandler.instance().getSide().isServer();

	public static boolean cantPlace(@Nonnull EntityPlayer player, int x, int y, int z) {
		return canUseEventHelper && EventUtils.cantPlace(player, x, y, z);
	}

	public static boolean cantBreak(@Nonnull EntityPlayer player, int x, int y, int z) {
		return canUseEventHelper && EventUtils.cantBreak(player, x, y, z);
	}

	public static boolean cantDamage(@Nonnull Entity attacker, @Nonnull Entity victim) {
		return canUseEventHelper && EventUtils.cantDamage(attacker, victim);
	}

	public static boolean cantInteract(@Nonnull EntityPlayer player, @Nullable ItemStack stack, int x, int y, int z, @Nonnull ForgeDirection side) {
		return canUseEventHelper && EventUtils.cantInteract(player, stack, x, y, z, side);
	}

	public static boolean isInPrivate(@Nonnull World world, int x, int y, int z) {
		return canUseEventHelper && EventUtils.isInPrivate(world, x, y, z);
	}

	public static boolean isInPrivate(@Nonnull Entity entity) {
		return canUseEventHelper && EventUtils.isInPrivate(entity);
	}
}
