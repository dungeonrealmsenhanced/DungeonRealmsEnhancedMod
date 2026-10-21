package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.ItemData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ArmorUtils {

  public static List<Double> _getDoubleListFromList(ItemStack stack, String intName) {

    if (ItemData.tag(stack) == null) return Collections.singletonList(0d);

    if (!ItemData.tag(stack).contains("modifiers")) return Collections.singletonList(0d);
    CompoundTag itemAttributes = ItemData.tag(stack).getCompoundOrEmpty("modifiers");

    if (itemAttributes == null || itemAttributes.isEmpty()) {
      return Collections.singletonList(0d);
    }

    if (!itemAttributes.contains(intName)) return Collections.singletonList(0d);

    ListTag tagList = itemAttributes.getListOrEmpty(intName);
    List<Double> integerList = new ArrayList<>();
    for (int tagIndex = 0; tagIndex < tagList.size(); ++tagIndex) {
      integerList.add(tagList.getDoubleOr(tagIndex, 0.0));
    }
    return integerList;
  }

  public static List<Integer> _getIntListFromList(ItemStack stack, String intName) {

    if (ItemData.tag(stack) == null) return Collections.singletonList(0);

    if (!ItemData.tag(stack).contains("modifiers")) return Collections.singletonList(0);
    CompoundTag itemAttributes = ItemData.tag(stack).getCompoundOrEmpty("modifiers");

    if (itemAttributes == null || itemAttributes.isEmpty()) {
      return Collections.singletonList(0);
    }

    if (!itemAttributes.contains(intName)) return Collections.singletonList(0);

    ListTag tagList = itemAttributes.getListOrEmpty(intName);
    List<Integer> integerList = new ArrayList<>();
    for (int tagIndex = 0; tagIndex < tagList.size(); ++tagIndex) {
      integerList.add(tagList.getIntOr(tagIndex, 0));
    }
    return integerList;
  }

  public static List<Integer> getIntListFromList(ItemStack stack, String intName) {
    List<Integer> integers = _getIntListFromList(stack, intName);

    if (integers == null || integers.isEmpty()) return Collections.singletonList(0);
    return integers;
  }

  public static List<Double> getDoubleListFromList(ItemStack stack, String intName) {
    List<Double> doubles = _getDoubleListFromList(stack, intName);

    if (doubles == null || doubles.isEmpty()) return Collections.singletonList(0d);
    return doubles;
  }

  public static ArmorType getArmorType(ItemStack stack) {

    if (stack.getItem() == Items.LEATHER_HELMET
        || stack.getItem() == Items.CHAINMAIL_HELMET
        || stack.getItem() == Items.IRON_HELMET
        || stack.getItem() == Items.DIAMOND_HELMET
        || stack.getItem() == Items.GOLDEN_HELMET) {
      return ArmorType.HELMET;
    }

    if (stack.getItem() == Items.LEATHER_CHESTPLATE
        || stack.getItem() == Items.CHAINMAIL_CHESTPLATE
        || stack.getItem() == Items.IRON_CHESTPLATE
        || stack.getItem() == Items.DIAMOND_CHESTPLATE
        || stack.getItem() == Items.GOLDEN_CHESTPLATE) {
      return ArmorType.CHESTPLATE;
    }

    if (stack.getItem() == Items.LEATHER_LEGGINGS
        || stack.getItem() == Items.CHAINMAIL_LEGGINGS
        || stack.getItem() == Items.IRON_LEGGINGS
        || stack.getItem() == Items.DIAMOND_LEGGINGS
        || stack.getItem() == Items.GOLDEN_LEGGINGS) {
      return ArmorType.LEGGINGS;
    }

    if (stack.getItem() == Items.LEATHER_BOOTS
        || stack.getItem() == Items.CHAINMAIL_BOOTS
        || stack.getItem() == Items.IRON_BOOTS
        || stack.getItem() == Items.DIAMOND_BOOTS
        || stack.getItem() == Items.GOLDEN_BOOTS) {
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
