package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.DREnhanced;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.ItemUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import java.util.*;

public class ArmorTooltipCompare {

    private static final List<String> EXEMPTED = Arrays.asList("HEALTH_POINTS","MELEE_DAMAGE", "RANGE_DAMAGE", "DAMAGE"); // dealing with decimal bug.
    private static final Map<String, String> DAMAGE_TYPE_MAP = new HashMap<>();

    static {
        DAMAGE_TYPE_MAP.put("MELEE_DAMAGE", "DAMAGE");
        DAMAGE_TYPE_MAP.put("RANGE_DAMAGE", "DAMAGE");
    }

    public static List<String> handleToolTip(ItemTooltipEvent event) {
        ItemStack tooltipStack = event.getItemStack();
        if (!tooltipStack.hasTagCompound()) return new ArrayList<>();

        try {
            Minecraft mc = Minecraft.getMinecraft();
            if (ItemUtils.isWeapon(tooltipStack.getItem())) {
                Map<String, double[]> tooltipModifiers = ItemUtils.getModifierMap(tooltipStack);

                ItemStack equipped = mc.player.inventory.getStackInSlot(0);
                if (equipped == null || equipped.isEmpty() || !ItemUtils.isWeapon(equipped.getItem())) {
                    equipped = mc.player.getHeldItemMainhand();
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
                ItemStack equipped = Minecraft.getMinecraft().player.inventory.armorInventory.get(slotIndex);

                Map<String, double[]> equippedModifiers = equipped != null ? ItemUtils.getModifierMap(equipped) : new HashMap<>();

                List<String> newTooltip = new ArrayList<>();
                addCompareTooltip(newTooltip, tooltipModifiers, equippedModifiers, equipped, tooltipStack);
                return newTooltip;
            }
        } catch (Exception e) {
            e.printStackTrace();
            event.getEntityPlayer().sendMessage(new TextComponentString(TextFormatting.RED + e.getCause().toString()));
        }
        return new ArrayList<>();
    }

    public static void addCompareTooltip(List<String> tooltips1, Map<String, double[]> itemModifiers, Map<String, double[]> equippedModifiers, ItemStack equippedStack, ItemStack tooltipStack) {
        List<String> tooltips = new ArrayList<>();

        tooltips.add("");
        tooltips.add(TextFormatting.DARK_GRAY + TextFormatting.STRIKETHROUGH.toString() + "---------- " + TextFormatting.AQUA + TextFormatting.BOLD.toString() + "Comparison" + TextFormatting.DARK_GRAY + TextFormatting.STRIKETHROUGH.toString() + "----------");

        tooltips.add(TextFormatting.GRAY + TextFormatting.BOLD.toString() + "Old Item: " + (equippedStack != null ? equippedStack.getDisplayName() : "None"));
        tooltips.add(TextFormatting.GRAY + TextFormatting.BOLD.toString() + "New Item: " + tooltipStack.getDisplayName());
        tooltips.add("");

        Set<String> allStats = equippedModifiers == null ? new HashSet<>() : new HashSet<>(equippedModifiers.keySet());
        allStats.addAll(itemModifiers.keySet());

        int count = 0;
        for (String stat : allStats) {
            String normalizedStat = DAMAGE_TYPE_MAP.getOrDefault(stat, stat);
            double[] equippedValues = equippedModifiers != null ? equippedModifiers.getOrDefault(stat, new double[]{0, 0}) : new double[]{0, 0};
            double[] hoveredValues = itemModifiers.getOrDefault(stat, new double[]{0, 0});

            if (normalizedStat.equals("DAMAGE")) {
                compareDamage(tooltips, stat, equippedValues, hoveredValues);
                count++;
            } else {
                compareStats(tooltips, stat, equippedValues, hoveredValues);
                count++;
            }
        }

        tooltips.add(TextFormatting.DARK_GRAY + TextFormatting.STRIKETHROUGH.toString() + "----------------------------");
        if (count > 0) {
            tooltips1.addAll(tooltips);
        }
    }

    private static void compareDamage(List<String> tooltips, String stat, double[] equippedValues, double[] hoveredValues) {
        double equippedMin = equippedValues[0];
        double equippedMax = equippedValues[1];
        double hoveredMin = hoveredValues[0];
        double hoveredMax = hoveredValues[1];

        if (equippedMin == hoveredMin && equippedMax == hoveredMax) return;

        String minDiff = formatNumber(Math.abs(hoveredMin - equippedMin), stat);
        String maxDiff = formatNumber(Math.abs(hoveredMax - equippedMax), stat);

        tooltips.add((hoveredMin > equippedMin ? TextFormatting.GREEN : TextFormatting.RED) + "✔ " + (hoveredMin > equippedMin ? "+" : "-") + minDiff + TextFormatting.DARK_GRAY + " - " + (hoveredMax > equippedMax ? TextFormatting.GREEN : TextFormatting.RED) + (hoveredMax > equippedMax ? "+" : "-") + maxDiff + " " + formatStat(stat));
    }

    private static String formatNumber(double value, String stat) {
        if (EXEMPTED.contains(stat)) {
            return String.valueOf((int) value);
        }
        return (value % 1 == 0) ? String.valueOf((int) value) : String.format("%.2f", value);
    }

    private static void compareStats(List<String> tooltips, String stat, double[] equippedValues, double[] hoveredValues) {
        double equippedValue = equippedValues[0];
        double hoveredValue = hoveredValues[0];

        if (equippedValue == hoveredValue) return;

        String formattedValue = formatNumber(Math.abs(hoveredValue - equippedValue), stat);
        boolean statDecreased = hoveredValue < equippedValue;

        tooltips.add((statDecreased ? TextFormatting.RED + "✘ " : TextFormatting.GREEN + "✔ ")
                + (hoveredValue > equippedValue ? "+" : "-") + formattedValue
                + " " + formatStat(stat));
    }

    private static String formatStat(String stat) {
        try {
            return DREnhanced.getStatKeyDatabase().formatKey(stat);
        } catch (Exception e) {
            return stat;
        }
    }
}
