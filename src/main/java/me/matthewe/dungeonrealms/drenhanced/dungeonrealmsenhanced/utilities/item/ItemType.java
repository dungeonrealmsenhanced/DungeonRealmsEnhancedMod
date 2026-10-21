package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.StringUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/** Created by Matthew E on 3/11/2019 at 11:57 AM for the project DungeonRealmsDREnhanced */
public enum ItemType {
  WEAPON,
  ARMOR,
  CLUE_SCROLL,
  PICKAXE,
  FISHING_ROD;

  public static ItemType getFromItemStack(ItemStack itemStack) {
    if ((itemStack.getItem() != Items.AIR) && ItemData.hasData(itemStack)) {
      CompoundTag tagCompound = ItemData.tag(itemStack);
      if (tagCompound == null) {
        return null;
      }
      if (tagCompound.contains("type")) {
        String type = tagCompound.getStringOr("type", "");
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

  public String getName() {
    return StringUtils.formatEnum(this.toString());
  }
}
