package com.suslovila.cybersus.api.implants;

import com.suslovila.cybersus.common.item.ItemImplant;
import com.suslovila.cybersus.utils.TriConsumer;
import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.function.BiConsumer;

public class ImplantStorage implements IInventory {
    private final int implantAmount = ImplantType.getTotalSlotAmount();
    private final String name = "implant_storage";
    private final int stackLimit = 1;

    // this fields were taken from botania in order to implement mechanics for taking implants off
    public WeakReference<EntityPlayer> player;
    public boolean blockEvents = false;

    public final ImplantTypeHolder[] implantsByType = new ImplantTypeHolder[ImplantType.values().length];


    public ImplantStorage() {
        for (int typeIndex = 0; typeIndex < ImplantType.values().length; typeIndex++) {
            implantsByType[typeIndex] = new ImplantTypeHolder(ImplantType.values()[typeIndex]);
        }
    }

    public ImplantStorage(EntityPlayer player) {
        this();
        this.player = new WeakReference<>(player);
    }

    public void forEachImplant(BiConsumer<Integer, ItemStack> lambda) {
        Arrays.stream(implantsByType).forEach(implantTypeHolder -> {
            for (int indexInType = 0; indexInType < implantTypeHolder.implants.length; indexInType++) {
                ItemStack implant = implantTypeHolder.implants[indexInType];
                if (implant != null) {
                    lambda.accept(ImplantType.getFirstSlotIndexOf(implantTypeHolder.implantType) + indexInType, implant);
                }
            }
        });
    }
    public void forEachImplant(BiConsumer<Integer, ItemStack> lambda, ImplantType type) {
        ImplantTypeHolder implantTypeHolder = implantsByType[type.ordinal()];
            for (int indexInType = 0; indexInType < implantTypeHolder.implants.length; indexInType++) {
                ItemStack implant = implantTypeHolder.implants[indexInType];
                if (implant != null) {
                    lambda.accept(ImplantType.getFirstSlotIndexOf(implantTypeHolder.implantType) + indexInType, implant);
                }
            }
    }
    public void forEachImplant(BiConsumer<Integer, ItemStack> lambda, ItemImplant implantClass) {
        ImplantTypeHolder holder = implantsByType[implantClass.implantType.ordinal()];
        for(int indexInType = 0; indexInType < holder.implants.length; indexInType++) {
            ItemStack implant = holder.implants[indexInType];
            if (implant != null && implant.getItem() == implantClass) {
                lambda.accept(ImplantType.getFirstSlotIndexOf(implantClass.implantType) + indexInType, implant);
            }
        }
    }
    public <T> void forEachImplant(TriConsumer<Integer, ItemStack, T> lambda, T element) {
        Arrays.stream(implantsByType).forEach(implantTypeHolder -> {
            for (int indexInType = 0; indexInType < implantTypeHolder.implants.length; indexInType++) {
                ItemStack implant = implantTypeHolder.implants[indexInType];
                if (implant != null) {
                    lambda.accept(ImplantType.getFirstSlotIndexOf(implantTypeHolder.implantType) + indexInType, implant, element);
                }
            }
        });
    }
    public <T> void forEachImplant(TriConsumer<Integer, ItemStack, T> lambda, T element, ItemImplant implantClass) {
        ImplantTypeHolder holder = implantsByType[implantClass.implantType.ordinal()];
        for(int indexInType = 0; indexInType < holder.implants.length; indexInType++) {
            ItemStack implant = holder.implants[indexInType];
            if (implant != null && implant.getItem() == implantClass) {
                lambda.accept(ImplantType.getFirstSlotIndexOf(implantClass.implantType) + indexInType, implant, element);
            }
        }
    }
    public static class ImplantTypeHolder {
        public static final String TYPE_INDEX_NBT = "typeIndex";
        public static final String IMPLANTS_NBT = "implants";
        private final ImplantType implantType;
        private final ItemStack[] implants;

        public ImplantTypeHolder(ImplantType implantType) {
            this.implantType = implantType;
            this.implants = new ItemStack[ImplantType.getSlotAmount(implantType)];
        }

        public static ImplantTypeHolder readFrom(NBTTagCompound tag) {
            ImplantType type = ImplantType.values()[tag.getInteger(TYPE_INDEX_NBT)];
            ImplantTypeHolder holder = new ImplantTypeHolder(type);
            NBTTagList implants = tag.getTagList(IMPLANTS_NBT, Constants.NBT.TAG_COMPOUND);
            for (int implantIndex = 0; implantIndex < implants.tagCount(); implantIndex++) {
                NBTTagCompound slotNbt = implants.getCompoundTagAt(implantIndex);
                int indexInArray = slotNbt.getInteger(SLOT_INDEX_NBT);
                if (indexInArray >= ImplantType.getSlotAmount(type)) continue;
                ItemStack implant = ItemStack.loadItemStackFromNBT(slotNbt);
                holder.implants[indexInArray] = implant;
            }
            return holder;
        }

        public static ImplantTypeHolder readFrom(ByteBuf buf) {
            ImplantType type = ImplantType.values()[buf.readInt()];
            ImplantTypeHolder holder = new ImplantTypeHolder(type);
            int amount = buf.readInt();
            for (int implantIndex = 0; implantIndex < amount; implantIndex++) {
                int indexInArray = buf.readInt();
                if (indexInArray >= ImplantType.getSlotAmount(type)) continue;
                ItemStack implant = ByteBufUtils.readItemStack(buf);
                holder.implants[indexInArray] = implant;
            }
            return holder;
        }

        public void writeTo(NBTTagCompound tag) {
            NBTTagList implantsNbt = new NBTTagList();
            for (int index = 0; index < implants.length; index++) {
                ItemStack implantStack = implants[index];
                if (implantStack != null && implantStack.stackSize > 0) {
                    NBTTagCompound slotContentNbt = new NBTTagCompound();
                    slotContentNbt.setInteger(SLOT_INDEX_NBT, index);
                    implantStack.writeToNBT(slotContentNbt);
                    implantsNbt.appendTag(slotContentNbt);
                }
            }
            tag.setInteger(TYPE_INDEX_NBT, implantType.ordinal());
            tag.setTag(IMPLANTS_NBT, implantsNbt);
        }

        public void writeTo(ByteBuf buf) {
            buf.writeInt(implantType.ordinal());
            int validCount = (int) Arrays.stream(implants).filter(this::isValid).count();
            buf.writeInt(validCount);
            for (int index = 0; index < implants.length; index++) {
                ItemStack implantStack = implants[index];
                if (isValid(implantStack)) {
                    buf.writeInt(index);
                    ByteBufUtils.writeItemStack(buf, implantStack);
                }
            }
        }

        private boolean isValid(ItemStack stack) {
            return stack != null && stack.stackSize > 0;
        }
    }

    @Override
    public int getSizeInventory() {
        return implantAmount;
    }

    @Override
    public ItemStack getStackInSlot(int slotId) {
        ImplantType possibleType = ImplantType.getTypeForSlotWithIndex(slotId);
        if (possibleType == null) return null;
        int firstSlot = ImplantType.getFirstSlotIndexOf(possibleType);
        return implantsByType[possibleType.ordinal()].implants[slotId - firstSlot];
    }

    @Override
    public ItemStack decrStackSize(int slotId, int count) {
        if (slotId < getSizeInventory()) {
            ItemStack implant = getStackInSlot(slotId);
            if (implant == null) return null;
            if (implant.stackSize > count) {
                ItemStack result = getStackInSlot(slotId).splitStack(count);
                markDirty();
                return result;
            }
            if (implant.stackSize < count) {
                return null;
            }
            setInventorySlotContents(slotId, null);
            markDirty();
            if(!this.blockEvents) {
                ((ItemImplant) implant.getItem()).onUnequipped(player.get(), slotId, implant);
            }
            return implant;
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int slotId, ItemStack itemstack) {
        if (slotId >= implantAmount) {
            return;
        }
        ImplantType possibleType = ImplantType.getTypeForSlotWithIndex(slotId);
        if (possibleType == null) return;
        ItemStack currentImplant = getStackInSlot(slotId);
        if(currentImplant != null && player != null && player.get() != null) {
            Item itemType = (currentImplant.getItem());
            if(!this.blockEvents) {
                ((ItemImplant) itemType).onUnequipped(player.get(), slotId, currentImplant);
            }        }

        int firstSlot = ImplantType.getFirstSlotIndexOf(possibleType);
        implantsByType[possibleType.ordinal()].implants[slotId - firstSlot] = itemstack;
        if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit()) {
            itemstack.stackSize = this.getInventoryStackLimit();
        }
        markDirty();
    }

    @Override
    public String getInventoryName() {
        return name;
    }

    @Override
    public int getInventoryStackLimit() {
        return stackLimit;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        return true;
    }

    @Override
    public void openChest() {
    }

    @Override
    public void closeChest() {
    }

    public void writeToNBT(NBTTagCompound rootTag) {
        NBTTagList holders = new NBTTagList();
        for (ImplantTypeHolder holder : implantsByType) {
            NBTTagCompound nbtForHolder = new NBTTagCompound();
            holder.writeTo(nbtForHolder);
            holders.appendTag(nbtForHolder);
        }
        rootTag.setTag(IMPLANTS_NBT, holders);
    }

    public void readFromNBT(NBTTagCompound data) {
        if (!data.hasKey(IMPLANTS_NBT)) return;
        NBTTagList nbtTagList = data.getTagList(IMPLANTS_NBT, Constants.NBT.TAG_COMPOUND);
        for (int j = 0; j < nbtTagList.tagCount(); j++) {
            NBTTagCompound holderNbt = nbtTagList.getCompoundTagAt(j);
            ImplantTypeHolder holder = ImplantTypeHolder.readFrom(holderNbt);
            implantsByType[holder.implantType.ordinal()] = holder;
        }
    }

    public void writeTo(ByteBuf buf) {
        for (ImplantTypeHolder holder : implantsByType) {
            holder.writeTo(buf);
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slotId) {
        ItemStack implant = getStackInSlot(slotId);
        if (implant != null) {
            setInventorySlotContents(slotId, null);
        }
        return implant;
    }

    @Override
    public boolean isItemValidForSlot(int slotIndex, ItemStack itemstack) {
        if (slotIndex >= implantAmount) return false;
        ImplantType typeForSlot = ImplantType.getTypeForSlotWithIndex(slotIndex);
        if(typeForSlot == null) return false;
        return typeForSlot == (itemstack.getItem() instanceof ItemImplant ? ((ItemImplant) itemstack.getItem()).implantType : null);
    }

    @Override
    public boolean isCustomInventoryName() {
        return false;
    }

    @Override
    public void markDirty() {
    }

    public static ImplantStorage readFrom(ByteBuf buf) {
        ImplantStorage storage = new ImplantStorage();
        for (int index = 0; index < ImplantType.values().length; index++) {
            ImplantTypeHolder typeWithImplants = ImplantTypeHolder.readFrom(buf);
            storage.implantsByType[typeWithImplants.implantType.ordinal()] = typeWithImplants;
        }
        return storage;
    }

    private static final String IMPLANTS_NBT = "Items";
    private static final String SLOT_INDEX_NBT = "index";
}


