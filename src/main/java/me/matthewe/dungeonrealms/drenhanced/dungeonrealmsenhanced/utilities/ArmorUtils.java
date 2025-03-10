package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArmorUtils {


    public static List<Double> _getDoubleListFromList(ItemStack stack, String intName) {
        // If does not have nbt tags then return
        if (stack.getTagCompound() == null) return Collections.singletonList(0d);
        // If does not have nbt tag attributes then return
        if (!stack.getTagCompound().hasKey("modifiers", 10)) return Collections.singletonList(0d);
        NBTTagCompound itemAttributes = stack.getTagCompound().getCompoundTag("modifiers");
        // If attributes has not tags then return
        if (itemAttributes==null||itemAttributes.isEmpty()){
            return Collections.singletonList(0d);
        }
        // If attributes has not tags then return
        if (!itemAttributes.hasKey(intName)) return Collections.singletonList(0d);
        // Return all
        NBTTagList tagList = itemAttributes.getTagList(intName, 3);
        List<Double> integerList = new ArrayList<>();
        for (int tagIndex = 0; tagIndex < tagList.tagCount(); ++tagIndex) {
            integerList.add(tagList.getDoubleAt(tagIndex));
        }
        return integerList;
    }
    public static List<Integer> _getIntListFromList(ItemStack stack, String intName) {
        // If does not have nbt tags then return
        if (stack.getTagCompound() == null) return Collections.singletonList(0);
        // If does not have nbt tag attributes then return
        if (!stack.getTagCompound().hasKey("modifiers", 10)) return Collections.singletonList(0);
        NBTTagCompound itemAttributes = stack.getTagCompound().getCompoundTag("modifiers");
        // If attributes has not tags then return
        if (itemAttributes==null||itemAttributes.isEmpty()){
            return Collections.singletonList(0);
        }
        // If attributes has not tags then return
        if (!itemAttributes.hasKey(intName)) return Collections.singletonList(0);
        // Return all
        NBTTagList tagList = itemAttributes.getTagList(intName, 3);
        List<Integer> integerList = new ArrayList<>();
        for (int tagIndex = 0; tagIndex < tagList.tagCount(); ++tagIndex) {
            integerList.add(tagList.getIntAt(tagIndex));
        }
        return integerList;
    }
    public static List<Integer> getIntListFromList(ItemStack stack, String intName) {
        List<Integer> integers = _getIntListFromList(stack, intName);

        if (integers==null || integers.isEmpty())return Collections.singletonList(0);
        return integers;
    }

    public static List<Double> getDoubleListFromList(ItemStack stack, String intName) {
        List<Double> doubles = _getDoubleListFromList(stack, intName);

        if (doubles==null || doubles.isEmpty())return Collections.singletonList(0d);
        return doubles;
    }

    public static ArmorType getArmorType(ItemStack stack) {
        // Helmets
        if (stack.getItem() == Items.LEATHER_HELMET || stack.getItem() == Items.CHAINMAIL_HELMET ||
                stack.getItem() == Items.IRON_HELMET || stack.getItem() == Items.DIAMOND_HELMET ||
                stack.getItem() == Items.GOLDEN_HELMET) {
            return ArmorType.HELMET;
        }
        // Chestplate
        if (stack.getItem() == Items.LEATHER_CHESTPLATE || stack.getItem() == Items.CHAINMAIL_CHESTPLATE ||
                stack.getItem() == Items.IRON_CHESTPLATE || stack.getItem() == Items.DIAMOND_CHESTPLATE ||
                stack.getItem() == Items.GOLDEN_CHESTPLATE) {
            return ArmorType.CHESTPLATE;
        }
        // Leggings
        if (stack.getItem() == Items.LEATHER_LEGGINGS || stack.getItem() == Items.CHAINMAIL_LEGGINGS ||
                stack.getItem() == Items.IRON_LEGGINGS || stack.getItem() == Items.DIAMOND_LEGGINGS ||
                stack.getItem() == Items.GOLDEN_LEGGINGS) {
            return ArmorType.LEGGINGS;
        }
        // Boots
        if (stack.getItem() == Items.LEATHER_BOOTS || stack.getItem() == Items.CHAINMAIL_BOOTS ||
                stack.getItem() == Items.IRON_BOOTS || stack.getItem() == Items.DIAMOND_BOOTS ||
                stack.getItem() == Items.GOLDEN_BOOTS) {
            return ArmorType.BOOTS;
        }
        return ArmorType.NONE;
    }

    public enum ArmorType {
        NONE,
        BOOTS,
        LEGGINGS,
        CHESTPLATE,
        HELMET
    }
}