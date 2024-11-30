package com.suslovila.cybersus.client.gui;

import com.suslovila.cybersus.common.block.container.ContainerImplantHolder;
import com.suslovila.cybersus.common.block.container.ContainerRuneInstaller;
import com.suslovila.cybersus.common.block.runeInstaller.TileRuneInstaller;
import com.suslovila.cybersus.common.item.ItemPortableMultiAspectContainer;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class CybersusGuiHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if (!world.blockExists(x, y, z)) {
            return null;
        }
        Object tile = world.getTileEntity(x, y, z);
        CybersusGui gui = CybersusGui.values()[id];

        switch (gui) {
            case IMPLANT_INSTALLER: {
                return new ContainerImplantHolder(player);
            }
            case RUNE_INSTALLER: {
                if (tile instanceof TileRuneInstaller) {
                    return new ContainerRuneInstaller((TileRuneInstaller) tile, player);
                } else return null;
            }
            default:
                return null;
        }

    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if (!world.blockExists(x, y, z)) {
            return null;
        }
        Object tile = world.getTileEntity(x, y, z);
        CybersusGui gui = CybersusGui.values()[id];

        switch (gui) {
            case IMPLANT_INSTALLER:
                return new GuiImplantInstaller(player);
            default:
                return null;
            case ITEM_ASPECT_HOLDER: {
                if (player.getHeldItem().getItem() instanceof ItemPortableMultiAspectContainer) {
                    return new GuiItemPortableContainer(player.getHeldItem());
                } else return null;
            }
            case RUNE_INSTALLER: {
                if (tile instanceof TileRuneInstaller) {
                    return new GuiRuneInstaller((TileRuneInstaller) tile, player);
                } else return null;
            }
        }
    }
}

