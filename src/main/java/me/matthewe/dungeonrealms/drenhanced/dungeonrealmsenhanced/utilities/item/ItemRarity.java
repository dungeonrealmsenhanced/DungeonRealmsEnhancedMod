package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.color.RGBAColor;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.ItemStack;

/** Created by Matthew E on 5/28/2018 at 11:57 AM for the project DungeonRealmsEnhanced */
public enum ItemRarity {
  COMMON("Common", 0xAAAAAA, ChatFormatting.GRAY, new RGBAColor(1, 1, 1)),
  UNCOMMON("Uncommon", 0x5FF55, ChatFormatting.GREEN, new RGBAColor(0, 1, 0)),
  RARE("Rare", 0x55FFFF, ChatFormatting.AQUA, new RGBAColor(0, 1, 1)),
  EPIC("Epic", 0xAA00AA, ChatFormatting.DARK_PURPLE, new RGBAColor(0.3f, 0, 0.3f)),
  LEGENDARY("Legendary", 0xFFFF55, ChatFormatting.YELLOW, new RGBAColor(1, 1, 0)),
  MYTHIC("Mythic", 0xFFAA00, ChatFormatting.GOLD, new RGBAColor(1, 0.6666667f, 0.0f));

  private String name;
  private int color;

  private ChatFormatting textFormatting;
  private RGBAColor rgbaColor;

  ItemRarity(String name, int color, ChatFormatting textFormatting, RGBAColor rgbaColor) {
    this.name = name;
    this.color = color;
    this.textFormatting = textFormatting;
    this.rgbaColor = rgbaColor;
  }

  public static ItemRarity getByItemStack(ItemStack itemStack) {
    return ItemUtils.getRarity(itemStack);
  }

  public RGBAColor getRgbaColor() {
    return rgbaColor;
  }

  public String getName() {
    return name;
  }

  public ChatFormatting getTextFormatting() {
    return textFormatting;
  }

  public int getColor() {
    return color;
  }

  public static ItemRarity getByName(String name) {
    if (name == null) {
      return null;
    }
    for (ItemRarity itemRarity : values()) {
      if (itemRarity.toString().equalsIgnoreCase(name)) {
        return itemRarity;
      }
    }
    return null;
  }

  public boolean isBetter(ItemRarity best) {
    if (best == null) {
      return true;
    }
    return this.ordinal() > best.ordinal();
  }
}
