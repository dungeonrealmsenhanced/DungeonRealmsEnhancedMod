package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item;

import java.util.List;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

/** Read-only access to server-provided custom data and modern item lore. */
public final class ItemData {
  private ItemData() {}

  public static CompoundTag tag(ItemStack stack) {
    if (stack == null || stack.isEmpty()) return new CompoundTag();
    var data = stack.get(DataComponents.CUSTOM_DATA);
    return data == null ? new CompoundTag() : data.copyTag();
  }

  public static boolean hasData(ItemStack stack) {
    return stack != null && stack.has(DataComponents.CUSTOM_DATA);
  }

  public static List<String> lore(ItemStack stack) {
    if (stack == null || stack.isEmpty()) return List.of();
    var lore = stack.get(DataComponents.LORE);
    return lore == null ? List.of() : lore.lines().stream().map(c -> c.getString()).toList();
  }
}
