package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/** Created by Matthew E on 5/28/2018 at 11:55 AM for the project DungeonRealmsEnhanced */
public class ItemUtils {
  public static String getStringLore(ItemStack is) {
    StringBuilder toReturn = new StringBuilder();
    for (String loreLine : getLore(is)) {
      toReturn.append(loreLine);
    }
    return toReturn.toString();
  }

  public static List<String> getLore(ItemStack item) {
    return ItemData.lore(item);
  }

  public static ItemRarity getRarity(ItemStack itemStack) {
    return isMythic(itemStack)
        ? ItemRarity.MYTHIC
        : ItemRarity.getByName(NbtTagUtils.getString("rarity", itemStack));
  }

  public static int[] calculateDPS(ItemStack itemStack) {
    if (!isWeapon(itemStack.getItem())) return new int[] {0, 0};
    Map<String, double[]> modifierMap = getModifierMap(itemStack);

    double[] damage = getDamage(itemStack, modifierMap);

    double dmgToAdd = 0;

    for (String elemental :
        Arrays.asList("ICE_DAMAGE", "PURE_DAMAGE", "FIRE_DAMAGE", "POISON_DAMAGE")) {

      if (modifierMap.containsKey(elemental)) {

        dmgToAdd += modifierMap.get(elemental)[0];
      }
    }

    double multipliers = 1.0D;
    if (modifierMap.containsKey("VS_MONSTERS")) {
      double multiplier = modifierMap.get("VS_MONSTERS")[0];

      multipliers += (multiplier / 100.0D);
    }

    double criticalMultiplier = 1.0;
    if (modifierMap.containsKey("CRITICAL_HIT")) {
      double criticalChance = modifierMap.get("CRITICAL_HIT")[0] / 100.0; // Convert % to decimal
      criticalMultiplier = 1 + criticalChance; // Weighted average of crit damage
    }

    double executeMultiplier = 1.0;
    if (modifierMap.containsKey("EXECUTE")) {
      double executePercent = modifierMap.get("EXECUTE")[0] / 100.0;
      executeMultiplier += executePercent * 0.50; // Assume 50% effectiveness over time
    }

    double crushingMultiplier = 1.0;
    if (modifierMap.containsKey("CRUSHING")) {
      double crushingPercent = modifierMap.get("CRUSHING")[0] / 100.0;
      crushingMultiplier += crushingPercent * 0.50; // Assume 50% effectiveness over time
    }

    double dpsMin =
        damage[0] * multipliers * criticalMultiplier * executeMultiplier * crushingMultiplier;
    double dpsMax =
        damage[1] * multipliers * criticalMultiplier * executeMultiplier * crushingMultiplier;

    dpsMax += dmgToAdd;
    dpsMin += dmgToAdd;
    return new int[] {(int) Math.round(dpsMin), (int) Math.round(dpsMax)};
  }

  public static double[] getDamage(ItemStack itemStack, Map<String, double[]> modifierMap) {

    if (itemStack.getItem() == Items.BOW) {
      double[] damage = modifierMap.getOrDefault("RANGE_DAMAGE", new double[] {0, 0});
      return damage.length == 1 ? new double[] {damage[0], damage[0]} : damage;
    }
    double[] damage = modifierMap.getOrDefault("MELEE_DAMAGE", new double[] {0, 0});
    return damage.length == 1 ? new double[] {damage[0], damage[0]} : damage;
  }

  public static Map<String, double[]> getModifierMap(ItemStack itemStack) {
    return getModifierMap(itemStack, false);
  }

  public static Map<String, double[]> getModifierMap(ItemStack stack, boolean skipAugmentations) {
    Map<String, double[]> values = new java.util.LinkedHashMap<>();
    CompoundTag data = ItemData.tag(stack);
    CompoundTag modifiers = data.getCompoundOrEmpty("modifiers");
    for (String key : modifiers.keySet()) {
      double[] numbers = numbers(modifiers.get(key));
      if (numbers.length > 0) values.put(key, numbers);
    }
    if (!skipAugmentations) {
      CompoundTag augmentations = data.getCompoundOrEmpty("augmentations");
      for (String key : augmentations.keySet()) {
        double[] added = numbers(augmentations.get(key));
        if (added.length == 0) continue;
        double[] old = values.get(key);
        if (old == null) {
          values.put(key, added);
          continue;
        }
        double[] merged = new double[Math.max(old.length, added.length)];
        for (int i = 0; i < merged.length; i++)
          merged[i] = old[Math.min(i, old.length - 1)] + added[Math.min(i, added.length - 1)];
        values.put(key, merged);
      }
    }
    return values;
  }

  private static double[] numbers(net.minecraft.nbt.Tag tag) {
    if (tag instanceof net.minecraft.nbt.NumericTag numeric) {
      double v = numeric.doubleValue();
      return Double.isFinite(v) ? new double[] {v} : new double[0];
    }
    if (tag instanceof net.minecraft.nbt.ListTag list) {
      if (list.isEmpty()
          || list.stream().anyMatch(t -> !(t instanceof net.minecraft.nbt.NumericTag)))
        return new double[0];
      double[] values =
          list.stream()
              .mapToDouble(t -> ((net.minecraft.nbt.NumericTag) t).doubleValue())
              .toArray();
      return java.util.Arrays.stream(values).allMatch(Double::isFinite) ? values : new double[0];
    }
    return new double[0];
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

  public static boolean isHelmet(Item item) {
    if (item == Items.DIAMOND_HELMET) return true;
    if (item == Items.GOLDEN_HELMET) return true;
    if (item == Items.LEATHER_HELMET) return true;
    if (item == Items.CHAINMAIL_HELMET) return true;
    if (item == Items.IRON_HELMET) return true;
    return false;
  }

  public static boolean isChestplate(Item item) {
    if (item == Items.DIAMOND_CHESTPLATE) return true;
    if (item == Items.GOLDEN_CHESTPLATE) return true;
    if (item == Items.LEATHER_CHESTPLATE) return true;
    if (item == Items.CHAINMAIL_CHESTPLATE) return true;
    if (item == Items.IRON_CHESTPLATE) return true;
    return false;
  }

  public static boolean isLeggings(Item item) {
    if (item == Items.DIAMOND_LEGGINGS) return true;
    if (item == Items.GOLDEN_LEGGINGS) return true;
    if (item == Items.LEATHER_LEGGINGS) return true;
    if (item == Items.CHAINMAIL_LEGGINGS) return true;
    if (item == Items.IRON_LEGGINGS) return true;
    return false;
  }

  public static boolean isBoots(Item item) {
    if (item == Items.DIAMOND_BOOTS) return true;
    if (item == Items.GOLDEN_BOOTS) return true;
    if (item == Items.LEATHER_BOOTS) return true;
    if (item == Items.CHAINMAIL_BOOTS) return true;
    if (item == Items.IRON_BOOTS) return true;
    return false;
  }

  public static boolean isAxe(Item item) {
    if (item == Items.DIAMOND_AXE) return true;
    if (item == Items.GOLDEN_AXE) return true;
    if (item == Items.WOODEN_AXE) return true;

    if (item == Items.STONE_AXE) return true;
    if (item == Items.IRON_AXE) return true;
    return false;
  }

  public static boolean isClub(Item item) {
    if (item == Items.DIAMOND_SHOVEL) return true;
    if (item == Items.GOLDEN_SHOVEL) return true;
    if (item == Items.WOODEN_SHOVEL) return true;

    if (item == Items.STONE_SHOVEL) return true;
    if (item == Items.IRON_SHOVEL) return true;
    return false;
  }

  public static boolean isEquippedOrInHotbarSlot0(ItemStack itemStack) {
    Minecraft mc = Minecraft.getInstance();

    if (isArmor(itemStack.getItem())) {
      for (EquipmentSlot slot : EquipmentSlot.values()) {
        if (slot.isArmor()) {
          if (mc.player.getItemBySlot(slot) == itemStack) {
            return false; // Armor is equipped, do not allow
          }
        }
      }
    }

    if (!isArmor(itemStack.getItem()) && !isWeapon(itemStack.getItem())) {
      return true;
    }

    if (isPickaxe(itemStack.getItem())) {
      return true;
    }

    if (mc.player.getMainHandItem() == itemStack || mc.player.getOffhandItem() == itemStack) {
      return true;
    }

    if (!mc.player.getInventory().getItem(0).isEmpty()
        && mc.player.getInventory().getItem(0) == itemStack) {
      return true;
    }

    return false;
  }

  public static boolean isScythe(Item item) {
    if (item == Items.DIAMOND_HOE) return true;
    if (item == Items.GOLDEN_HOE) return true;
    if (item == Items.WOODEN_HOE) return true;

    if (item == Items.STONE_HOE) return true;
    if (item == Items.IRON_HOE) return true;
    return false;
  }

  public static boolean isSword(Item item) {
    if (item == Items.DIAMOND_SWORD) return true;
    if (item == Items.GOLDEN_SWORD) return true;
    if (item == Items.WOODEN_SWORD) return true;

    if (item == Items.STONE_SWORD) return true;
    if (item == Items.IRON_SWORD) return true;
    return false;
  }

  public static boolean isPickaxe(Item item) {
    if (item == Items.DIAMOND_PICKAXE) return true;
    if (item == Items.GOLDEN_PICKAXE) return true;
    if (item == Items.IRON_PICKAXE) return true;

    if (item == Items.STONE_PICKAXE) return true;
    if (item == Items.WOODEN_PICKAXE) return true;
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

  public static int getLevel(ItemStack itemStack) {
    if ((itemStack.getItem() != Items.AIR) && ItemData.hasData(itemStack)) {
      CompoundTag tagCompound = ItemData.tag(itemStack);
      if (tagCompound == null) {
        return 0;
      }

      if (tagCompound.contains("level")) {
        return tagCompound.getIntOr("level", 0);
      }
    }
    return 0;
  }

  public static int getEnchant(ItemStack itemStack) {
    if ((itemStack.getItem() != Items.AIR) && ItemData.hasData(itemStack)) {
      CompoundTag tagCompound = ItemData.tag(itemStack);
      if (tagCompound == null) {
        return 0;
      }

      if (tagCompound.contains("enchant")) {
        return tagCompound.getIntOr("enchant", 0);
      }
    }
    return 0;
  }

  public static ItemType getType(ItemStack stack) {

    if (stack.getItem() == Items.LEATHER_HELMET
        || stack.getItem() == Items.CHAINMAIL_HELMET
        || stack.getItem() == Items.IRON_HELMET
        || stack.getItem() == Items.DIAMOND_HELMET
        || stack.getItem() == Items.GOLDEN_HELMET) {
      return ItemType.HELMET;
    }

    if (stack.getItem() == Items.LEATHER_CHESTPLATE
        || stack.getItem() == Items.CHAINMAIL_CHESTPLATE
        || stack.getItem() == Items.IRON_CHESTPLATE
        || stack.getItem() == Items.DIAMOND_CHESTPLATE
        || stack.getItem() == Items.GOLDEN_CHESTPLATE) {
      return ItemType.CHESTPLATE;
    }

    if (stack.getItem() == Items.LEATHER_LEGGINGS
        || stack.getItem() == Items.CHAINMAIL_LEGGINGS
        || stack.getItem() == Items.IRON_LEGGINGS
        || stack.getItem() == Items.DIAMOND_LEGGINGS
        || stack.getItem() == Items.GOLDEN_LEGGINGS) {
      return ItemType.LEGGINGS;
    }

    if (stack.getItem() == Items.LEATHER_BOOTS
        || stack.getItem() == Items.CHAINMAIL_BOOTS
        || stack.getItem() == Items.IRON_BOOTS
        || stack.getItem() == Items.DIAMOND_BOOTS
        || stack.getItem() == Items.GOLDEN_BOOTS) {
      return ItemType.BOOTS;
    }
    if (stack.is(Items.BOW)) return ItemType.BOW;
    if (isSword(stack.getItem())) return ItemType.SWORD;
    if (isAxe(stack.getItem())) return ItemType.AXE;
    if (isPickaxe(stack.getItem())) return ItemType.PICKAXE;
    if (isClub(stack.getItem())) return ItemType.POLEARM;
    if (isScythe(stack.getItem())) return ItemType.HOE;
    if (stack.getItem() == Items.FISHING_ROD) return ItemType.FISHING_ROD;
    return ItemType.NONE;
  }

  public enum ItemType {
    NONE,
    BOOTS,
    LEGGINGS,
    CHESTPLATE,
    HELMET,
    SWORD,
    BOW,
    FISHING_ROD,
    PICKAXE,
    AXE,
    POLEARM,
    HOE;
  }

  public static boolean hasClueScrolls() {
    return getClueScrolls() != null;
  }

  public static List<ClueScroll> getClueScrolls() {
    Minecraft minecraft = Minecraft.getInstance();
    LocalPlayer player = minecraft.player;
    if (player == null) {
      return null;
    }

    List<ClueScroll> clueScrolls = new ArrayList<>();
    for (Slot slot : player.inventoryMenu.slots) {
      ItemStack itemStack = slot.getItem();

      if ((itemStack.getItem() == Items.AIR)
          || (itemStack.getCount() == 0)
          || (!me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.ItemType
              .isClueScroll(itemStack))) {
        continue;
      }
      ClueScroll clueScroll = ClueScroll.of(itemStack);
      if (clueScroll != null) {
        clueScrolls.add(clueScroll);
      }
    }
    return !clueScrolls.isEmpty() ? clueScrolls : null;
  }

  public static boolean isMythic(ItemStack itemStack) {
    if ((itemStack.getItem() != Items.AIR) && ItemData.hasData(itemStack)) {
      CompoundTag tagCompound = ItemData.tag(itemStack);
      if (tagCompound == null) {
        return false;
      }
      return tagCompound.contains("mythic") && tagCompound.getBooleanOr("mythic", false);
    }
    return false;
  }
}
