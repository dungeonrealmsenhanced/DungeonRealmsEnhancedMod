package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Matthew E on 5/28/2018 at 11:55 AM for the project DungeonRealmsEnhanced
 */
public class ItemUtils {
    public static String getStringLore(ItemStack is) {
        StringBuilder toReturn = new StringBuilder();
        for (String loreLine : getLore(is)) {
            toReturn.append(loreLine);
        }
        return toReturn.toString();
    }

    public static List<String> getLore(ItemStack item) {
        List<String> lore = new ArrayList<>();
        if ((item == null) || !item.hasTagCompound()) {
            return lore;
        }
        if (item.getTagCompound().hasKey("display", 10)) {
            NBTTagCompound nbtTagCompound = item.getTagCompound().getCompoundTag("display");

            if (nbtTagCompound.getTagId("Lore") == 9) {
                NBTTagList nbtTagList = nbtTagCompound.getTagList("Lore", 8);

                if (!nbtTagList.isEmpty()) {
                    for (int l1 = 0; l1 < nbtTagList.tagCount(); ++l1) {
                        lore.add(nbtTagList.getStringTagAt(l1));
                    }
                }
            }
        }
        return lore;
    }

    public static ItemRarity getRarity(ItemStack itemStack) {
        return ItemRarity.getByName(NbtTagUtils.getString("rarity", itemStack));
    }

    public static Map<String, int[]> getModifierMap(ItemStack itemStack) {
        Map<String, int[]> modifierMap = new ConcurrentHashMap<>();
        if ((itemStack.getItem() != Items.AIR) && itemStack.hasDisplayName() && itemStack.hasTagCompound()) {
            NBTTagCompound tagCompound = itemStack.getTagCompound();
            if (tagCompound == null) {
                return null;
            }
            NBTTagCompound modifiers = tagCompound.getCompoundTag("modifiers");
            for (String s : modifiers.getKeySet()) {
                String s1 = modifiers.getTag(s).toString();
                if (s1.startsWith("[")) {
                    String trim = s1.split("\\[")[1].trim().split("\\]")[0].trim();
                    if (trim.contains(",")) {
                        String[] split = trim.split(",");
                        int[] ints = new int[split.length];
                        for (int i = 0; i < split.length; i++) {
                            String s2 = split[i];
                            ints[i] = Integer.parseInt(s2.trim());
                        }
                        modifierMap.put(s, ints);
                    } else {
                        int[] ints = new int[1];
                        ints[0] = Integer.parseInt(trim.trim());
                        modifierMap.put(s, ints);
                    }
                }
            }
        }
        return !modifierMap.isEmpty() ? modifierMap : null;
    }

    public static boolean isWeapon(Item item) {
        if (item == Items.DIAMOND_SWORD) return true;
        if (item == Items.DIAMOND_AXE) return true;
        if (item == Items.DIAMOND_SHOVEL) return true;
        if (item == Items.DIAMOND_HOE) return true;

        if (item == Items.BOW) return true;

        if (item == Items.IRON_AXE) return true;
        if (item == Items.IRON_SWORD) return true;
        if (item == Items.IRON_SHOVEL) return true;
        if (item == Items.IRON_HOE) return true;

        if (item == Items.GOLDEN_SWORD) return true;
        if (item == Items.GOLDEN_AXE) return true;
        if (item == Items.GOLDEN_HOE) return true;
        if (item == Items.GOLDEN_SHOVEL) return true;

        if (item == Items.WOODEN_AXE) return true;
        if (item == Items.WOODEN_SHOVEL) return true;
        if (item == Items.WOODEN_SWORD) return true;
        if (item == Items.WOODEN_HOE) return true;

        if (item == Items.STONE_AXE) return true;
        if (item == Items.STONE_SWORD) return true;
        if (item == Items.STONE_HOE) return true;
        if (item == Items.STONE_SHOVEL) return true;

        return false;
    }

    public static boolean isArmor(Item item) {
        if (item == Items.DIAMOND_HELMET) return true;
        if (item == Items.DIAMOND_CHESTPLATE) return true;
        if (item == Items.DIAMOND_BOOTS) return true;
        if (item == Items.DIAMOND_LEGGINGS) return true;


        if (item == Items.IRON_HELMET) return true;
        if (item == Items.IRON_CHESTPLATE) return true;
        if (item == Items.IRON_LEGGINGS) return true;
        if (item == Items.IRON_BOOTS) return true;

        if (item == Items.GOLDEN_HELMET) return true;
        if (item == Items.GOLDEN_CHESTPLATE) return true;
        if (item == Items.GOLDEN_LEGGINGS) return true;
        if (item == Items.GOLDEN_BOOTS) return true;

        if (item == Items.LEATHER_HELMET) return true;
        if (item == Items.LEATHER_CHESTPLATE) return true;
        if (item == Items.LEATHER_LEGGINGS) return true;
        if (item == Items.LEATHER_BOOTS) return true;


        if (item == Items.CHAINMAIL_BOOTS) return true;
        if (item == Items.CHAINMAIL_CHESTPLATE) return true;
        if (item == Items.CHAINMAIL_LEGGINGS) return true;
        if (item == Items.CHAINMAIL_HELMET) return true;

        return false;
    }

    public static Tier getTier(ItemStack itemStack) {
        return Tier.getByNumber(NbtTagUtils.getInt("tier", itemStack));
    }
}
