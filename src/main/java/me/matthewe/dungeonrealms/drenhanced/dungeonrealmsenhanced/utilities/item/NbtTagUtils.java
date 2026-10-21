package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item;

import net.minecraft.world.item.ItemStack;

public final class NbtTagUtils {
  public static int getInt(String key, ItemStack stack) {
    return ItemData.tag(stack).getIntOr(key, -1);
  }

  public static String getString(String key, ItemStack stack) {
    return ItemData.tag(stack).getStringOr(key, "");
  }

  public static boolean hasKey(String key, ItemStack stack) {
    return ItemData.tag(stack).contains(key);
  }
}
