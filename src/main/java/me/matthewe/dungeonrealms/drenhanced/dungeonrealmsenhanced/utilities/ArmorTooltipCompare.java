package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities;

import java.util.*;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.DREnhanced;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.ItemData;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.ItemUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

public class ArmorTooltipCompare {

  private static final List<String> EXEMPTED =
      Arrays.asList(
          "HEALTH_POINTS", "MELEE_DAMAGE", "RANGE_DAMAGE", "DAMAGE"); // dealing with decimal bug.
  private static final Map<String, String> DAMAGE_TYPE_MAP = new HashMap<>();

  static {
    DAMAGE_TYPE_MAP.put("MELEE_DAMAGE", "DAMAGE");
    DAMAGE_TYPE_MAP.put("RANGE_DAMAGE", "DAMAGE");
  }

  public static List<String> handleToolTip(ItemTooltipEvent event) {
    ItemStack tooltipStack = event.getItemStack();
    if (!ItemData.hasData(tooltipStack)) return new ArrayList<>();

    try {
      Minecraft mc = Minecraft.getInstance();
      if (ItemUtils.isWeapon(tooltipStack.getItem())) {
        Map<String, double[]> tooltipModifiers = ItemUtils.getModifierMap(tooltipStack);

        ItemStack equipped = mc.player.getInventory().getItem(0);
        if (equipped == null || equipped.isEmpty() || !ItemUtils.isWeapon(equipped.getItem())) {
          equipped = mc.player.getMainHandItem();
        }

        if (equipped.isEmpty() || !ItemUtils.isWeapon(equipped.getItem())) return new ArrayList<>();

        Map<String, double[]> equippedModifiers = ItemUtils.getModifierMap(equipped);
        List<String> newTooltip = new ArrayList<>();
        addCompareTooltip(newTooltip, tooltipModifiers, equippedModifiers, equipped, tooltipStack);

        return newTooltip;
      }

      ArmorUtils.ArmorType tooltipStackArmorType = ArmorUtils.getArmorType(tooltipStack);

      if (tooltipStackArmorType != ArmorUtils.ArmorType.NONE) {
        Map<String, double[]> tooltipModifiers = ItemUtils.getModifierMap(tooltipStack);

        int slotIndex = tooltipStackArmorType.ordinal() - 1;
        if (slotIndex == -1) return new ArrayList<>();
        ItemStack equipped =
            Minecraft.getInstance()
                .player
                .getItemBySlot(
                    switch (tooltipStackArmorType) {
                      case HELMET -> net.minecraft.world.entity.EquipmentSlot.HEAD;
                      case CHESTPLATE -> net.minecraft.world.entity.EquipmentSlot.CHEST;
                      case LEGGINGS -> net.minecraft.world.entity.EquipmentSlot.LEGS;
                      default -> net.minecraft.world.entity.EquipmentSlot.FEET;
                    });

        Map<String, double[]> equippedModifiers =
            equipped != null ? ItemUtils.getModifierMap(equipped) : new HashMap<>();

        List<String> newTooltip = new ArrayList<>();
        addCompareTooltip(newTooltip, tooltipModifiers, equippedModifiers, equipped, tooltipStack);
        return newTooltip;
      }
    } catch (Exception e) {
      e.printStackTrace();
      DREnhanced.log("Could not compare item: %s", e.toString());
    }
    return new ArrayList<>();
  }

  public static void addCompareTooltip(
      List<String> tooltips1,
      Map<String, double[]> itemModifiers,
      Map<String, double[]> equippedModifiers,
      ItemStack equippedStack,
      ItemStack tooltipStack) {
    List<String> tooltips = new ArrayList<>();

    tooltips.add("");
    tooltips.add(
        ChatFormatting.DARK_GRAY
            + ChatFormatting.STRIKETHROUGH.toString()
            + "---------- "
            + ChatFormatting.AQUA
            + ChatFormatting.BOLD.toString()
            + "Comparison"
            + ChatFormatting.DARK_GRAY
            + ChatFormatting.STRIKETHROUGH.toString()
            + "----------");

    tooltips.add(
        ChatFormatting.GRAY
            + ChatFormatting.BOLD.toString()
            + "Old Item: "
            + (equippedStack != null ? equippedStack.getHoverName().getString() : "None"));
    tooltips.add(
        ChatFormatting.GRAY
            + ChatFormatting.BOLD.toString()
            + "New Item: "
            + tooltipStack.getHoverName().getString());
    tooltips.add("");

    Set<String> allStats =
        equippedModifiers == null ? new HashSet<>() : new HashSet<>(equippedModifiers.keySet());
    allStats.addAll(itemModifiers.keySet());

    int count = 0;
    for (String stat : allStats) {
      String normalizedStat = DAMAGE_TYPE_MAP.getOrDefault(stat, stat);
      double[] equippedValues =
          equippedModifiers != null
              ? equippedModifiers.getOrDefault(stat, new double[] {0, 0})
              : new double[] {0, 0};
      double[] hoveredValues = itemModifiers.getOrDefault(stat, new double[] {0, 0});

      if (normalizedStat.equals("DAMAGE")) {
        compareDamage(tooltips, stat, equippedValues, hoveredValues);
        count++;
      } else {
        compareStats(tooltips, stat, equippedValues, hoveredValues);
        count++;
      }
    }

    tooltips.add(
        ChatFormatting.DARK_GRAY
            + ChatFormatting.STRIKETHROUGH.toString()
            + "----------------------------");
    if (count > 0) {
      tooltips1.addAll(tooltips);
    }
  }

  private static void compareDamage(
      List<String> tooltips, String stat, double[] equippedValues, double[] hoveredValues) {
    double equippedMin = equippedValues[0];
    double equippedMax = equippedValues[1];
    double hoveredMin = hoveredValues[0];
    double hoveredMax = hoveredValues[1];

    if (equippedMin == hoveredMin && equippedMax == hoveredMax) return;

    String minDiff = formatNumber(Math.abs(hoveredMin - equippedMin), stat);
    String maxDiff = formatNumber(Math.abs(hoveredMax - equippedMax), stat);

    tooltips.add(
        (hoveredMin > equippedMin ? ChatFormatting.GREEN : ChatFormatting.RED)
            + "✔ "
            + (hoveredMin > equippedMin ? "+" : "-")
            + minDiff
            + ChatFormatting.DARK_GRAY
            + " - "
            + (hoveredMax > equippedMax ? ChatFormatting.GREEN : ChatFormatting.RED)
            + (hoveredMax > equippedMax ? "+" : "-")
            + maxDiff
            + " "
            + formatStat(stat));
  }

  private static String formatNumber(double value, String stat) {
    if (EXEMPTED.contains(stat)) {
      return String.valueOf((int) value);
    }
    return (value % 1 == 0) ? String.valueOf((int) value) : String.format("%.2f", value);
  }

  private static void compareStats(
      List<String> tooltips, String stat, double[] equippedValues, double[] hoveredValues) {
    double equippedValue = equippedValues[0];
    double hoveredValue = hoveredValues[0];

    if (equippedValue == hoveredValue) return;

    String formattedValue = formatNumber(Math.abs(hoveredValue - equippedValue), stat);
    boolean statDecreased = hoveredValue < equippedValue;

    tooltips.add(
        (statDecreased ? ChatFormatting.RED + "✘ " : ChatFormatting.GREEN + "✔ ")
            + (hoveredValue > equippedValue ? "+" : "-")
            + formattedValue
            + " "
            + formatStat(stat));
  }

  private static String formatStat(String stat) {
    try {
      return DREnhanced.getStatKeyDatabase().formatKey(stat);
    } catch (Exception e) {
      return stat;
    }
  }
}
