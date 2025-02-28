package clutch.dungeonrealms;

import clutch.dungeonrealms.utils.ArmorUtils;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.ItemUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import java.util.*;

public class ArmorTooltipCompare {

//    private boolean addedBeginningText = false;


    public static List<String> handleToolTip(ItemTooltipEvent event) {
        ItemStack tooltipStack = event.getItemStack();
        if (!tooltipStack.hasTagCompound()) return new ArrayList<>();

        try {
            if (ItemUtils.isWeapon(tooltipStack.getItem())){
                Map<String, double[]> tooltipModifiers = ItemUtils.getModifierMap(tooltipStack);

                ItemStack equipped = Minecraft.getMinecraft().player.inventory.armorInventory.get(0);

                if (ItemUtils.isWeapon(equipped.getItem())) {

                    Map<String, double[]> equippedModifiers = equipped != null ? ItemUtils.getModifierMap(equipped) : new HashMap<>();

                    List<String> newTooltip = new ArrayList<>();

                    addCompareTooltip(newTooltip, tooltipModifiers, equippedModifiers, equipped, tooltipStack);
                    return newTooltip;
                }
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

    private static final List<String> EXEMPTED = Arrays.asList("HEALTH_POINTS");

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
        allStats.addAll(equippedModifiers.keySet());
        allStats.addAll(itemModifiers.keySet());

        int count = 0;
        for (String stat : allStats) {
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

        tooltips.add(TextFormatting.DARK_GRAY + TextFormatting.STRIKETHROUGH.toString() + "----------------------------");
        if (count > 0) {
            tooltips1.addAll(tooltips);
        }
    }

    private static String formatStat(String stat) {
        // Minecraft.getMinecraft().player.sendMessage(new TextComponentString("SEARCH STAT " + stat));
        return ItemAttributes.get(stat).getTooltipName();
    }


}