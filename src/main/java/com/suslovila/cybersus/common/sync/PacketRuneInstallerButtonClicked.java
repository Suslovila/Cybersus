package com.suslovila.cybersus.common.sync;

import com.suslovila.cybersus.api.implants.upgrades.rune.RuneType;
import com.suslovila.cybersus.api.implants.upgrades.rune.RuneUsingItem;
import com.suslovila.cybersus.common.item.ModItems;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import thaumcraft.common.lib.utils.InventoryUtils;
import com.suslovila.cybersus.common.block.runeInstaller.TileRuneInstaller;

public class PacketRuneInstallerButtonClicked implements IMessage {
    private int installerPositionX;
    private int installerPositionY;
    private int installerPositionZ;


    private RuneType runeType;
    private boolean isAddRuneButton;

    public PacketRuneInstallerButtonClicked() {
    }

    public PacketRuneInstallerButtonClicked(TileEntity tile, RuneType runeType, boolean actionType) {
        this.installerPositionX = tile.xCoord;
        installerPositionY = tile.yCoord;
        installerPositionZ = tile.zCoord;
        this.runeType = runeType;
        this.isAddRuneButton = actionType;
    }

    public void toBytes(ByteBuf buffer) {
        buffer.writeInt(installerPositionX);
        buffer.writeInt(installerPositionY);
        buffer.writeInt(installerPositionZ);

        buffer.writeInt(runeType.ordinal());
        buffer.writeBoolean(isAddRuneButton);
    }

    public void fromBytes(ByteBuf buffer) {
        installerPositionX = buffer.readInt();
        installerPositionY = buffer.readInt();
        installerPositionZ = buffer.readInt();

        runeType = RuneType.values()[buffer.readInt()];
        isAddRuneButton = buffer.readBoolean();
    }

    public static class Handler implements IMessageHandler<PacketRuneInstallerButtonClicked, IMessage> {
        @Override
        public IMessage onMessage(PacketRuneInstallerButtonClicked packet, MessageContext ctx) {
            World world = ctx.getServerHandler().playerEntity.worldObj;
            //chunk protection
            if (world.blockExists(packet.installerPositionX, packet.installerPositionY, packet.installerPositionZ)) {
                TileEntity tile = world.getTileEntity(packet.installerPositionX, packet.installerPositionY, packet.installerPositionZ);
                if (tile instanceof TileRuneInstaller) {
                    TileRuneInstaller installer = (TileRuneInstaller) tile;

                    ItemStack stack = installer.inventory.getStackInSlot(0);
                    if (stack != null) {
                        Item item = stack.getItem();
                        if (item instanceof RuneUsingItem) {
                            RuneUsingItem runeUsingItem = (RuneUsingItem) item;
                            int maxAmount = runeUsingItem.getMaxRuneAmount();
                            int runeTypeCurrentAmount = RuneUsingItem.getRuneAmountOfType(stack, packet.runeType);
                            int wholeRuneAmount = 0;
                            for (RuneType runeType : RuneType.values()) {
                                wholeRuneAmount += RuneUsingItem.getRuneAmountOfType(stack, runeType);
                            }

                            if (packet.isAddRuneButton) {
                                if (wholeRuneAmount < maxAmount) {
                                    ItemStack stackLeft =
                                            InventoryUtils.extractStack(
                                                    ctx.getServerHandler().playerEntity.inventory,
                                                    new ItemStack(ModItems.itemRune, 1, packet.runeType.ordinal()),
                                                    0,
                                                    false,
                                                    false,
                                                    false,
                                                    true
                                            );
                                    if (stackLeft != null) {
                                        RuneUsingItem.setRuneAmountOfType(stack, packet.runeType, runeTypeCurrentAmount + 1);
                                    }
                                }
                            } else {
                                if (runeTypeCurrentAmount > 0) {
                                    ItemStack stackLeft =
                                            InventoryUtils.placeItemStackIntoInventory(
                                                    new ItemStack(ModItems.itemRune, 1, packet.runeType.ordinal()),
                                                    ctx.getServerHandler().playerEntity.inventory,
                                                    0,
                                                    true
                                            );
                                    if (stackLeft == null || stackLeft.stackSize == 0) {
                                        RuneUsingItem.setRuneAmountOfType(stack, packet.runeType, runeTypeCurrentAmount - 1);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return null;
        }
    }


}
