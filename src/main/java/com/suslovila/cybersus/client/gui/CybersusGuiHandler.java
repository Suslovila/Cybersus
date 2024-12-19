package com.suslovila.cybersus.client.gui;

import baubles.client.gui.GuiPlayerExpanded;
import com.suslovila.cybersus.Cybersus;
import com.suslovila.cybersus.api.implants.ImplantStorage;
import com.suslovila.cybersus.api.implants.ability.AbilityHack;
import com.suslovila.cybersus.common.block.container.ContainerImplantHolder;
import com.suslovila.cybersus.common.block.container.ContainerRuneInstaller;
import com.suslovila.cybersus.common.block.container.envyEye.ContainerBaublesEnvy;
import com.suslovila.cybersus.common.block.runeInstaller.TileRuneInstaller;
import com.suslovila.cybersus.common.item.ItemPortableMultiAspectContainer;
import com.suslovila.cybersus.common.item.ModItems;
import com.suslovila.cybersus.common.item.implants.ImplantEyeOfEnvy;
import com.suslovila.cybersus.extendedData.CybersusPlayerExtendedData;
import com.suslovila.cybersus.utils.KhariumSusNBTHelper;
import com.suslovila.cybersus.utils.SusObjectWrapper;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.UUID;

import static com.suslovila.cybersus.api.implants.ability.AbilityHack.HACK_VICTIM_ID_NBT;
import static com.suslovila.cybersus.api.implants.ability.AbilityHack.HACK_VICTIM_UUID_NBT;
import static com.suslovila.cybersus.common.item.ModItems.eyeOfEnvy;

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
            case BAUBLES_ENVY: {
                SusObjectWrapper<EntityPlayer> wrappedPlayer = new SusObjectWrapper<>(null);
                CybersusPlayerExtendedData.getWrapped(player).ifPresent(data -> {
                            data.implantStorage.forEachImplant((index, implant, wrappedPlayerIn) -> {
                                if (wrappedPlayerIn.value == null) {
                                    AbilityHack hack = (AbilityHack) ImplantEyeOfEnvy.abilities.get(0);
                                    NBTTagCompound tag = KhariumSusNBTHelper.getOrCreateTag(implant);
                                    UUID victimUUID = KhariumSusNBTHelper.getUUIDOrNull(tag, HACK_VICTIM_UUID_NBT);
                                    if (!hack.isHacking(implant) && victimUUID != null) {
                                        EntityPlayer victimPlayer = player.worldObj.getPlayerEntityByUUID(victimUUID);
                                        if (victimPlayer != null) {
                                            wrappedPlayerIn.value = victimPlayer;
                                        }
                                    }
                                }
                            }, wrappedPlayer, ModItems.eyeOfEnvy);

                        }

                );
                if (wrappedPlayer.value != null) {
                    return new ContainerPlayer(wrappedPlayer.value.inventory, !wrappedPlayer.value.worldObj.isRemote, wrappedPlayer.value);
                }

            }
            default: {
                return null;
            }
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

//            case BAUBLES_ENVY: {
//                return new GuiPlayerExpanded(player);
//            }
            case BAUBLES_ENVY: {
                SusObjectWrapper<EntityPlayer> wrappedPlayer = new SusObjectWrapper<>(null);
                CybersusPlayerExtendedData.getWrapped(player).ifPresent(data -> {
                            data.implantStorage.forEachImplant((index, implant, wrappedPlayerIn) -> {
                                if (wrappedPlayerIn.value == null) {
                                    AbilityHack hack = (AbilityHack) ImplantEyeOfEnvy.abilities.get(0);
                                    NBTTagCompound tag = KhariumSusNBTHelper.getOrCreateTag(implant);
                                    Entity focusedEntity = player.worldObj.getEntityByID(tag.getInteger(HACK_VICTIM_ID_NBT));
                                    if (!hack.isHacking(implant) && focusedEntity != null) {
                                        if (focusedEntity instanceof EntityPlayer) {
                                            wrappedPlayerIn.value = (EntityPlayer) focusedEntity;
                                        }
                                    }
                                }
                            }, wrappedPlayer, ModItems.eyeOfEnvy);

                        }

                );
                if (wrappedPlayer.value != null) {
                    return new GuiInventory(wrappedPlayer.value);
                }

            }
        }

        return null;
    }
}

