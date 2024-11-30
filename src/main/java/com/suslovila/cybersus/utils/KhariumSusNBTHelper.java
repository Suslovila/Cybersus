package com.suslovila.cybersus.utils;

import com.suslovila.cybersus.Cybersus;
import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import java.util.UUID;
import java.util.function.Consumer;

public class KhariumSusNBTHelper {
    public static final int OBJECT_HEADER = 64;
    public static final int ARRAY_HEADER = 96;
    public static final int OBJECT_REFERENCE = 32;
    public static final int STRING_SIZE = 224;
    public static final int TAG_END = 0;
    public static final int TAG_BYTE = 1;
    public static final int TAG_SHORT = 2;
    public static final int TAG_INT = 3;
    public static final int TAG_LONG = 4;
    public static final int TAG_FLOAT = 5;
    public static final int TAG_DOUBLE = 6;
    public static final int TAG_BYTE_ARRAY = 7;
    public static final int TAG_STRING = 8;
    public static final int TAG_LIST = 9;
    public static final int TAG_COMPOUND = 10;
    public static final int TAG_INT_ARRAY = 11;
    public static final int TAG_LONG_ARRAY = 12;
    public static final int TAG_ANY_NUMERIC = 99;
    public static final int MAX_DEPTH = 512;

    public static NBTTagCompound getOrCreateTag(ItemStack itemStack) {
        if (!itemStack.hasTagCompound()) {
            itemStack.setTagCompound(new NBTTagCompound());
        }
        return itemStack.getTagCompound();
    }

    public static void forEach(NBTTagList nbtTagList, Consumer<NBTTagCompound> block) {
        for (int i = 0; i < nbtTagList.tagCount(); i++) {
            NBTTagCompound tag = nbtTagList.getCompoundTagAt(i);
            block.accept(tag);
        }
    }

    public static void writeTo(int value, NBTTagCompound rootNbt, String key) {
        rootNbt.setInteger(key, value);
    }

    public static void writeTo(double value, NBTTagCompound rootNbt, String key) {
        rootNbt.setDouble(key, value);
    }

    public static void writeTo(String value, NBTTagCompound rootNbt, String key) {
        rootNbt.setString(key, value);
    }

    public static void writeTo(float value, NBTTagCompound rootNbt, String key) {
        rootNbt.setFloat(key, value);
    }

    public static void writeTo(boolean value, NBTTagCompound rootNbt, String key) {
        rootNbt.setBoolean(key, value);
    }

    public static int getOrCreateInteger(NBTTagCompound nbtTagCompound, String key, int defaultValue) {
        if (!nbtTagCompound.hasKey(key)) {
            nbtTagCompound.setInteger(key, defaultValue);
            return defaultValue;
        }
        return nbtTagCompound.getInteger(key);
    }

    public static String getOrCreateString(NBTTagCompound nbtTagCompound, String key, String defaultValue) {
        if (!nbtTagCompound.hasKey(key)) {
            nbtTagCompound.setString(key, defaultValue);
            return defaultValue;
        }
        return nbtTagCompound.getString(key);
    }
    public static long getOrCreateLong(NBTTagCompound nbtTagCompound, String key, long defaultValue) {
        if (!nbtTagCompound.hasKey(key)) {
            nbtTagCompound.setLong(key, defaultValue);
            return defaultValue;
        }
        return nbtTagCompound.getLong(key);
    }
    public static boolean getOrCreateBoolean(NBTTagCompound nbtTagCompound, String key, boolean defaultValue) {
        if (!nbtTagCompound.hasKey(key)) {
            nbtTagCompound.setBoolean(key, defaultValue);
            return defaultValue;
        }
        return nbtTagCompound.getBoolean(key);
    }

    public static float getOrCreateFloat(NBTTagCompound nbtTagCompound, String key, float defaultValue) {
        if (!nbtTagCompound.hasKey(key)) {
            nbtTagCompound.setFloat(key, defaultValue);
            return defaultValue;
        }
        return nbtTagCompound.getFloat(key);
    }

    public static double getOrCreateDouble(NBTTagCompound nbtTagCompound, String key, double defaultValue) {
        if (!nbtTagCompound.hasKey(key)) {
            nbtTagCompound.setDouble(key, defaultValue);
            return defaultValue;
        }
        return nbtTagCompound.getDouble(key);
    }

    private static final String UUID_LEAST_NBT = Cybersus.prefixAppender.doAndGet("UUIDLeast");
    private static final String UUID_MOST_NBT = Cybersus.prefixAppender.doAndGet("UUIDMost");

    public static void setUUID(NBTTagCompound nbtTagCompound, String key, UUID value) {
        NBTTagCompound innerTag = new NBTTagCompound();
        innerTag.setLong(UUID_LEAST_NBT, value.getLeastSignificantBits());
        innerTag.setLong(UUID_MOST_NBT, value.getMostSignificantBits());
        nbtTagCompound.setTag(key, innerTag);
    }

    public static void writeUUID(ByteBuf buf, UUID value) {
        buf.writeLong(value.getLeastSignificantBits());
        buf.writeLong(value.getMostSignificantBits());
    }

    public static UUID readUUID(ByteBuf buf) {
        long least = buf.readLong();
        long most = buf.readLong();
        return new UUID(most, least);
    }

    public static UUID getOrCreateUUID(NBTTagCompound nbtTagCompound, String key, UUID defaultValue) {
        if (!nbtTagCompound.hasKey(key)) {
            NBTTagCompound innerTag = new NBTTagCompound();
            innerTag.setLong(UUID_MOST_NBT, defaultValue.getMostSignificantBits());
            innerTag.setLong(UUID_LEAST_NBT, defaultValue.getLeastSignificantBits());
            nbtTagCompound.setTag(key, innerTag);
            return defaultValue;
        }
        return getUUID(nbtTagCompound, key);
    }

    public static UUID getUUIDOrNull(NBTTagCompound nbtTagCompound, String key) {
        if (nbtTagCompound.hasKey(key)) {
            return getUUID(nbtTagCompound, key);
        }
        return null;
    }

    public static UUID getUUID(NBTTagCompound nbtTagCompound, String key) {
        NBTTagCompound innerTag = nbtTagCompound.getCompoundTag(key);
        return new UUID(innerTag.getLong(UUID_MOST_NBT), innerTag.getLong(UUID_LEAST_NBT));
    }
}