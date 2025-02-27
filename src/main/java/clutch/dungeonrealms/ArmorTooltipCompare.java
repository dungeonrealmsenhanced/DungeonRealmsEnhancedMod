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
                Map<String, double[]> tooltipModifiers = ItemUtils.getModifierMap(tooltipStack);

                int slotIndex = tooltipStackArmorType.ordinal() - 1;
                if (slotIndex == -1) return;
                ItemStack equipped = Minecraft.getMinecraft().player.inventory.armorInventory.get(slotIndex);

                Map<String, double[]> equippedModifiers = equipped != null ? ItemUtils.getModifierMap(equipped) : new HashMap<>();

                addCompareTooltip(event.getToolTip(), tooltipModifiers, equippedModifiers, equipped, tooltipStack);
            }
        } catch (Exception e) {
            e.printStackTrace();
            event.getEntityPlayer().sendMessage(new TextComponentString(TextFormatting.RED + e.getLocalizedMessage()));
        }
    }

    public void addCompareTooltip(List<String> tooltips, Map<String, double[]> itemModifiers, Map<String, double[]> equippedModifiers, ItemStack equippedStack, ItemStack tooltipStack) {
        addedBeginningText = false;

        tooltips.add("");
        tooltips.add(TextFormatting.DARK_GRAY + TextFormatting.STRIKETHROUGH.toString() + "────────── " + TextFormatting.GOLD + "Comparison" + TextFormatting.DARK_GRAY + TextFormatting.STRIKETHROUGH.toString() + " ──────────");

        // Equipped Item
        tooltips.add(TextFormatting.GRAY + "⚔ " + TextFormatting.BOLD + "Equipped: " + (equippedStack != null ? equippedStack.getDisplayName() : "None"));
        tooltips.add(getTierString(equippedStack));

        // Hovered Item
        tooltips.add("");
        tooltips.add(TextFormatting.GRAY + "🛡 " + TextFormatting.BOLD + "Item: " + tooltipStack.getDisplayName());
        tooltips.add(getTierString(tooltipStack));

        // Compare Attributes (Handle additions, removals, and changes)
        Set<String> allStats = new HashSet<>();
        allStats.addAll(equippedModifiers.keySet());
        allStats.addAll(itemModifiers.keySet());

        for (String stat : allStats) {
            double equippedValue = equippedModifiers.getOrDefault(stat, new double[]{0})[0];
            double hoveredValue = itemModifiers.getOrDefault(stat, new double[]{0})[0];

            if (equippedValue == hoveredValue) continue; // Ignore if no change

            if (equippedValue == 0) { // New stat added
                tooltips.add(TextFormatting.GREEN + "✔ +" + (int) hoveredValue + " " + stat);
            } else if (hoveredValue == 0) { // Stat removed
                tooltips.add(TextFormatting.RED + "" + TextFormatting.STRIKETHROUGH + "✖ " + stat);
            } else { // Stat changed
                String color = hoveredValue > equippedValue ? TextFormatting.GREEN.toString() : TextFormatting.RED.toString();
                String sign = hoveredValue > equippedValue ? "+" : "-";
                tooltips.add(color + "✔ " + sign + (int) Math.abs(hoveredValue - equippedValue) + " " + stat);
            }
        }

        tooltips.add(TextFormatting.DARK_GRAY + TextFormatting.STRIKETHROUGH.toString() + "────────────────────────────");
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
