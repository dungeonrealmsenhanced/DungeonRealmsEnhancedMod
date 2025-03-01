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



    public static List<String> handleToolTip(ItemTooltipEvent event) {
        ItemStack tooltipStack = event.getItemStack();
        if (!tooltipStack.hasTagCompound()) return new ArrayList<>();

        try {
            Minecraft mc = Minecraft.getMinecraft();
            if (ItemUtils.isWeapon(tooltipStack.getItem())) {
                Map<String, double[]> tooltipModifiers = ItemUtils.getModifierMap(tooltipStack);

                ItemStack equipped = mc.player.inventory.getStackInSlot(0);
                if (equipped.isEmpty() || !ItemUtils.isWeapon(equipped.getItem())) {
                    equipped = mc.player.getHeldItemOffhand();
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

    private static final List<String> EXEMPTED = Arrays.asList("HEALTH_POINTS"); //dealing with decimal bug.

    public static void addCompareTooltip(List<String> tooltips1, Map<String, double[]> itemModifiers, Map<String, double[]> equippedModifiers, ItemStack equippedStack, ItemStack tooltipStack) {
//        addedBeginningText = false;

        List<String> tooltips = new ArrayList<>();

        tooltips.add("");
        tooltips.add(TextFormatting.DARK_GRAY + TextFormatting.STRIKETHROUGH.toString() + "---------- " + TextFormatting.AQUA + TextFormatting.BOLD.toString() + "Comparison" + TextFormatting.DARK_GRAY + TextFormatting.STRIKETHROUGH.toString() + "----------");

        // Equipped Item
        tooltips.add(TextFormatting.GRAY + TextFormatting.BOLD.toString() + "Old Item: " + (equippedStack != null ? equippedStack.getDisplayName() : "None"));
//        tooltips.add(getTierString(equippedStack));

        // Hovered Item
        tooltips.add(TextFormatting.GRAY + TextFormatting.BOLD.toString() + "New Item: " + tooltipStack.getDisplayName());
        tooltips.add("");
//        tooltips.add(getTierString(tooltipStack));

        // Compare Attributes (Handle additions, removals, and changes)
        Set<String> allStats = new HashSet<>();
        if (equippedModifiers!=null&&!equippedModifiers.isEmpty()){
            allStats.addAll(equippedModifiers.keySet());

        }
        allStats.addAll(itemModifiers.keySet());

        int count = 0;
        for (String stat : allStats) {
            double[] equippedValues = equippedModifiers!=null?equippedModifiers.getOrDefault(stat, new double[]{0, 0}): new double[]{0, 0};
            double[] hoveredValues = itemModifiers.getOrDefault(stat, new double[]{0, 0});

            if (stat.equalsIgnoreCase("MELEE_DAMAGE")) {
                double equippedMin = equippedValues.length > 0 ? equippedValues[0] : 0;
                double equippedMax = equippedValues.length > 1 ? equippedValues[1] : 0;
                double hoveredMin = hoveredValues.length > 0 ? hoveredValues[0] : 0;
                double hoveredMax = hoveredValues.length > 1 ? hoveredValues[1] : 0;

                if (equippedMin == hoveredMin && equippedMax == hoveredMax) continue; // No change

                if (equippedMin == 0 && equippedMax == 0) { // New melee damage added
                    tooltips.add(TextFormatting.GREEN + "✔ +" + (int) hoveredMin + " - " + (int) hoveredMax + " " + formatStat(stat));
                } else if (hoveredMin == 0 && hoveredMax == 0) { // Melee damage removed
                    tooltips.add(TextFormatting.RED + "" + TextFormatting.STRIKETHROUGH + "✖ " + formatStat(stat));
                } else { // Melee damage changed
                    String minColor = hoveredMin > equippedMin ? TextFormatting.GREEN.toString() : TextFormatting.RED.toString();
                    String maxColor = hoveredMax > equippedMax ? TextFormatting.GREEN.toString() : TextFormatting.RED.toString();

                    String minSign = hoveredMin > equippedMin ? "+" : "-";
                    String maxSign = hoveredMax > equippedMax ? "+" : "-";

                    double minDiff = Math.abs(hoveredMin - equippedMin);
                    double maxDiff = Math.abs(hoveredMax - equippedMax);

                    tooltips.add(minColor + "✔ " + minSign + (int) minDiff + TextFormatting.DARK_GRAY + " - " + maxColor + maxSign + (int) maxDiff + " " + formatStat(stat));
                }
                count++;
            } else {
                double equippedValue = equippedModifiers.getOrDefault(stat, new double[]{0})[0];
                double hoveredValue = itemModifiers.getOrDefault(stat, new double[]{0})[0];

                if (equippedValue == hoveredValue) continue; // Ignore if no change

                if (equippedValue == 0) { // New stat added
                    tooltips.add(TextFormatting.GREEN + "✔ +" + (int) hoveredValue + " " + formatStat(stat));
                    count++;
                } else if (hoveredValue == 0) { // Stat removed
                    tooltips.add(TextFormatting.RED + "" + TextFormatting.STRIKETHROUGH + "✖ " + formatStat(stat));
                    count++;
                } else { // Stat changed


                    String color = hoveredValue > equippedValue ? TextFormatting.GREEN.toString() : TextFormatting.RED.toString();
                    String sign = hoveredValue > equippedValue ? "+" : "-";


                    double toFormat = Math.abs(hoveredValue - equippedValue);
                    String formattedValue;
                    if (EXEMPTED.contains(stat)) {
                        formattedValue = String.valueOf((int) toFormat);
                    } else {

                        formattedValue = (toFormat % 1 == 0) ? String.valueOf((int) toFormat) : String.format("%.2f", toFormat);
                    }
                    tooltips.add(color + "✔ " + sign + formattedValue + " " + formatStat(stat));
                    count++;
                }
            }


        }

        tooltips.add(TextFormatting.DARK_GRAY + TextFormatting.STRIKETHROUGH.toString() + "----------------------------");
        if (count > 0) {
            tooltips1.addAll(tooltips);
        }
    }

    private static String formatStat(String stat) {
       try {
           // Minecraft.getMinecraft().player.sendMessage(new TextComponentString("SEARCH STAT " + stat));
           return DREnhanced.getStatKeyDatabase().formatKey(stat);
       } catch (Exception e) {
           return stat;
       }
    }


}