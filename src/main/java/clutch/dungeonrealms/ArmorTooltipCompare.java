package clutch.dungeonrealms;

import clutch.dungeonrealms.utils.ArmorUtils;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.ItemUtils;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.Tier;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

public class ArmorTooltipCompare {

    private boolean addedBeginningText = false;

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent event) {
        ItemStack tooltipStack = event.getItemStack();
        if (!tooltipStack.hasTagCompound()) return;

        try {
            ArmorUtils.ArmorType tooltipStackArmorType = ArmorUtils.getArmorType(tooltipStack);
            if (tooltipStackArmorType != ArmorUtils.ArmorType.NONE) {
                ItemAttributes tooltipModifiers = ArmorUtils.getModifiers(tooltipStack);
                int value = tooltipStackArmorType.ordinal() - 1;
                if (value == -1) return;
                ItemStack equipped = Minecraft.getMinecraft().player.inventory.armorInventory.get(value);
                if (equipped != tooltipStack) {
                    addCompareTooltip(event.getToolTip(), tooltipModifiers, equipped, tooltipStack);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            event.getEntityPlayer().sendMessage(new TextComponentString(TextFormatting.RED + e.getLocalizedMessage()));
        }
    }

    public void addCompareTooltip(List<String> tooltips, ItemAttributes itemAttributes, ItemStack equippedStacks, ItemStack tooltipStack) {
        ItemAttributes equippedAttributes = new ItemAttributes();
        if (equippedStacks != null) {
            equippedAttributes = ArmorUtils.getModifiers(equippedStacks);
        }
        addedBeginningText = false;

        tooltips.add("");
        tooltips.add(TextFormatting.DARK_GRAY + "────────── " +TextFormatting.STRIKETHROUGH  + TextFormatting.GOLD + "Comparison" + TextFormatting.DARK_GRAY +TextFormatting.STRIKETHROUGH+ " ──────────");

        // Equipped Item
        tooltips.add(TextFormatting.GRAY + "⚔ " + TextFormatting.BOLD + "Equipped: " + (equippedStacks != null ? equippedStacks.getDisplayName() : "None"));
        tooltips.add(getTierString(equippedStacks));

        // Hovered Item
        tooltips.add("");
        tooltips.add(TextFormatting.GRAY + "🛡 " + TextFormatting.BOLD + "Item: " + tooltipStack.getDisplayName());
        tooltips.add(getTierString(tooltipStack));

        // Compare Attributes (Properly handle additions, removals, and changes)
        Set<String> allStats = new HashSet<>();
        allStats.addAll(equippedAttributes.getAttributes());
        allStats.addAll(itemAttributes.getAttributes());

        for (String stat : allStats) {
            double equippedValue = equippedAttributes.getCompareValue(stat);
            double hoveredValue = itemAttributes.getCompareValue(stat);

            if (equippedValue == hoveredValue) continue; // Ignore if no change

            if (equippedValue == 0) { // Stat is newly added
                tooltips.add(TextFormatting.GREEN + "✔ +" + (int) hoveredValue + " " + stat);
            } else if (hoveredValue == 0) { // Stat is removed
                tooltips.add(TextFormatting.RED + "" + TextFormatting.STRIKETHROUGH + "✖ " + stat);
            } else { // Stat changed
                String color = hoveredValue > equippedValue ? TextFormatting.GREEN.toString() : TextFormatting.RED.toString();
                String sign = hoveredValue > equippedValue ? "+" : "-";
                tooltips.add(color + "✔ " + sign + (int) Math.abs(hoveredValue - equippedValue) + " " + stat);
            }
        }

        tooltips.add(TextFormatting.DARK_GRAY +TextFormatting.STRIKETHROUGH.toString()+ "────────────────────────────");
    }

    private String getTierString(ItemStack item) {
        Tier tier = getItemTier(item);
        if (tier == null) return TextFormatting.RED + "?";
        return tier.getChatFormatting() + "T" + tier.getNumber();
    }

    private Tier getItemTier(ItemStack item) {
        return ItemUtils.getTier(item);
    }
}
