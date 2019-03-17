package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by Matthew E on 3/11/2019 at 11:57 AM for the project DungeonRealmsDREnhanced
 */
public enum ItemType {
    WEAPON, ARMOR, CLUE_SCROLL, PICKAXE, FISHING_ROD;

    public static ItemType getFromItemStack(ItemStack itemStack) {
        if ((itemStack.getItem() != Items.AIR) && itemStack.hasDisplayName() && itemStack.hasTagCompound()) {
            NBTTagCompound tagCompound = itemStack.getTagCompound();
            if (tagCompound == null) {
                return null;
            }
            if (tagCompound.hasKey("type")) {
                String type = tagCompound.getString("type");
                for (ItemType value : values()) {
                    if (value.toString().equalsIgnoreCase(type)) {
                        return value;
                    }
                }
            }
        }
        return null;
    }

    public static boolean isProfessionItem(ItemStack itemStack) {
        ItemType fromItemStack = ItemType.getFromItemStack(itemStack);

        return ((fromItemStack == PICKAXE || fromItemStack == FISHING_ROD));
    }

    public static boolean isWeapon(ItemStack itemStack) {
        ItemType fromItemStack = getFromItemStack(itemStack);
        return fromItemStack != null && fromItemStack == WEAPON;
    }

    public static boolean isClueScroll(ItemStack itemStack) {
        ItemType fromItemStack = getFromItemStack(itemStack);
        return fromItemStack != null && fromItemStack == CLUE_SCROLL;
    }

    public static boolean isArmor(ItemStack itemStack) {
        ItemType fromItemStack = getFromItemStack(itemStack);
        return fromItemStack != null && fromItemStack == ARMOR;
    }
}
