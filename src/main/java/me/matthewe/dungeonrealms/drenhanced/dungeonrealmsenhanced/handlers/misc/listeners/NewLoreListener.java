package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.misc.listeners;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.setting.DRSettings;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.Listener;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.ItemRarity;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.ItemType;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.ItemUtils;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.StringUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Matthew E on 3/11/2019 at 11:52 AM for the project DungeonRealmsDREnhanced
 */
public class NewLoreListener implements Listener {
    @SubscribeEvent(priority = EventPriority.LOW)
    public void onItemTooltip(ItemTooltipEvent event) {
        ItemStack itemStack = event.getItemStack();
        if ((itemStack.getItem() != Items.AIR) && itemStack.hasDisplayName() && itemStack.hasTagCompound() && DRSettings.NEW_LORE.get(boolean.class)) {
            NBTTagCompound tagCompound = itemStack.getTagCompound();
            if (tagCompound == null) {
                return;
            }
            Map<String, int[]> modifierMap = ItemUtils.getModifierMap(itemStack);
            if (modifierMap == null) {
                return;
            }


            if (GuiScreen.isCtrlKeyDown() && GuiScreen.isAltKeyDown()) {
                for (Map.Entry<String, int[]> stringEntry : modifierMap.entrySet()) {

                    TextComponentString component = new TextComponentString(stringEntry.getKey());
                    Style style = component.getStyle();
                    style.setColor(TextFormatting.RED);
                    style.setUnderlined(true);
                    style.setBold(true);
                    String intsString = "";
                    for (int i : stringEntry.getValue()) {
                        intsString += "" + TextFormatting.AQUA + i + "\n";
                    }


                    style.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString(intsString)));
                    component.setStyle(style);
                    event.getEntityPlayer().sendMessage(component);
                }
            }
            if (ItemType.isWeapon(itemStack)) {
                int minDamage = 0;
                int maxDamage = 0;
                if (modifierMap.containsKey("MELEE_DAMAGE")) {
                    int[] ints = modifierMap.get("MELEE_DAMAGE");
                    minDamage = ints[0];
                    maxDamage = ints[1];
                    modifierMap.remove("MELEE_DAMAGE");
                }
                int plus = 0;
                List<String> newLore = new ArrayList<>();
                if (tagCompound.hasKey("enchant")) {
                    plus = tagCompound.getInteger("enchant");
                    if (plus > 0) {

                        newLore.add(itemStack.getDisplayName().split("" + TextFormatting.RED + "\\[\\+" + plus + "\\] ")[1]);
                    }
                } else {
                    newLore.add(itemStack.getDisplayName());
                }
                if ((minDamage > 0) && (maxDamage > 0)) {
                    newLore.add(TextFormatting.GRAY + "Damage: " + TextFormatting.WHITE + minDamage + "-" + maxDamage);
                }
                if (modifierMap.containsKey("ICE_DAMAGE")) {
//                    newLore.add(TextFormatting.AQUA + "❉ Ice " + TextFormatting.GRAY + "Damage: " + TextFormatting.AQUA + "+" + modifierMap.get("ICE_DAMAGE")[0]);
                    modifierMap.remove("ICE_DAMAGE");
                }
                if (modifierMap.containsKey("FIRE_DAMAGE")) {
//                    newLore.add(TextFormatting.RED + "✹ Fire " + TextFormatting.GRAY + "Damage: " + TextFormatting.RED + "+" + modifierMap.get("FIRE_DAMAGE")[0]);
                    modifierMap.remove("FIRE_DAMAGE");
                }
                if (modifierMap.containsKey("POISON_DAMAGE")) {
//                    newLore.add(TextFormatting.DARK_GREEN + "✤ Poison  " + TextFormatting.GRAY + "Damage: " + TextFormatting.DARK_GREEN + "+" + modifierMap.get("POISON_DAMAGE")[0]);
                    modifierMap.remove("POISON_DAMAGE");
                }
                if (modifierMap.containsKey("PURE_DAMAGE")) {
//                    newLore.add(TextFormatting.GOLD + "✤ Pure  " + TextFormatting.GRAY + "Damage: " + TextFormatting.GOLD + "+" + modifierMap.get("PURE_DAMAGE")[0]);
                    modifierMap.remove("PURE_DAMAGE");
                }
                newLore.add(" ");
                for (Map.Entry<String, int[]> stringEntry : modifierMap.entrySet()) {
                    int[] value = stringEntry.getValue();
                    String valueString = "";
                    if (value.length == 1) {
                        valueString = value[0] + "%";
                    } else {
                        valueString = value[0] + " - " + value[1];
                    }
                    newLore.add(TextFormatting.GRAY + StringUtils.formatEnum(stringEntry.getKey()) + ": " + TextFormatting.GREEN + valueString);
                }

                newLore.add(" ");
                newLore.add("[" + plus + "/12] Enchantments");
                ItemRarity rarity = ItemUtils.getRarity(itemStack);
                if (rarity != null) {
                    newLore.add(ItemUtils.getRarity(itemStack).getTextFormatting() + TextFormatting.ITALIC.toString() + rarity.getName());
                }
                if (!GuiScreen.isCtrlKeyDown()) {
                    event.getToolTip().clear();
                    event.getToolTip().addAll(newLore);
                }
            }
        }
    }
}
