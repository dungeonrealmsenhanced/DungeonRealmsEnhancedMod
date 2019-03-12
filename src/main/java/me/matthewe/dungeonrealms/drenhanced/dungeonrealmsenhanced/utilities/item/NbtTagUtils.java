package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by Matthew E on 5/26/2018 at 2:46 PM for the project DungeonRealmsEnhanced
 */
public class NbtTagUtils {
    public static int getInt(String key, ItemStack itemStack) {
        NBTTagCompound nbtTagCompound = itemStack.serializeNBT();
        if (nbtTagCompound.hasKey("tag")) {
            NBTTagCompound compoundTag = nbtTagCompound.getCompoundTag("tag");
            if (compoundTag.hasKey(key)) {
                return compoundTag.getInteger(key);
            }
        }
        return -1;
    }

    public static String getString(String key, ItemStack itemStack) {
        NBTTagCompound nbtTagCompound = itemStack.serializeNBT();
        if (nbtTagCompound.hasKey("tag")) {
            NBTTagCompound compoundTag = nbtTagCompound.getCompoundTag("tag");
            if (compoundTag.hasKey(key)) {
                return compoundTag.getString(key);
            }
        }
        return null;
    }

    public static boolean hasKey(String key, ItemStack itemStack) {
        NBTTagCompound nbtTagCompound = itemStack.serializeNBT();
        if (nbtTagCompound.hasKey("tag")) {
            NBTTagCompound compoundTag = nbtTagCompound.getCompoundTag("tag");
            return compoundTag != null && compoundTag.hasKey(key);
        }
        return false;
    }
}
