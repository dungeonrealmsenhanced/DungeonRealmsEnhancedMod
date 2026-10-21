package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.misc.listeners;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.DREnhanced;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.StatKeyDatabase;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.setting.DRSettings;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.*;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.ItemData;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.ItemRarity;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.ItemUtils;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.Tier;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

/** Created by Matthew E on 3/12/2019 at 11:10 AM for the project DungeonRealmsDREnhanced */
public class ItemOriginListener implements Listener {
  private static SimpleDateFormat DATE_FORMAT;
  private DecimalFormat decimalFormat = new DecimalFormat("#.##");

  public ItemOriginListener() {}

  static {
    if (if24HourFormat()) {
      DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.UK);
    } else {
      DATE_FORMAT = new SimpleDateFormat("M/d/yyyy h:mm a", Locale.US);
    }
  }

  public static boolean if24HourFormat() {

    TimeZone timeZone = TimeZone.getDefault();

    DateFormat timeFormat =
        SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT, Locale.getDefault());
    timeFormat.setTimeZone(timeZone);

    String formattedTime = timeFormat.format(new Date(0)); // Using 00:00 time for testing

    return !(formattedTime.toLowerCase().contains("am")
        || formattedTime.toLowerCase().contains("pm"));
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void onItemTooltip(ItemTooltipEvent event) {
    if (!me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.listeners.ServerContext
        .active()) return;
    me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.TooltipText.edit(
        event,
        tooltip -> {
          try {
            ItemStack itemStack = event.getItemStack();
            if (ItemData.hasData(itemStack)) {
              List<String> toolTip = new ArrayList<>();

              boolean displayToolTip = false;

              CompoundTag tagCompound = ItemData.tag(itemStack);

              if (tagCompound.contains("origin")) {
                if (!Minecraft.getInstance().hasControlDown()
                    && !Minecraft.getInstance().hasShiftDown()) {
                  toolTip.add(
                      ChatFormatting.GRAY
                          + ChatFormatting.STRIKETHROUGH.toString()
                          + "------------------");

                  toolTip.add(
                      ChatFormatting.GREEN + "CTRL " + ChatFormatting.GRAY + "- View Origin");

                  if (!ItemUtils.isEquippedOrInHotbarSlot0(itemStack)) {
                    toolTip.add(
                        ChatFormatting.GREEN + "SHIFT " + ChatFormatting.GRAY + "- Compare");
                  }

                  toolTip.add(
                      ChatFormatting.GRAY
                          + ChatFormatting.STRIKETHROUGH.toString()
                          + "------------------");
                  displayToolTip = true;
                }
                if (Minecraft.getInstance().hasControlDown()
                    && DRSettings.ORIGIN_NAME.get(boolean.class)) {
                  toolTip.add(
                      ChatFormatting.GRAY
                          + ChatFormatting.STRIKETHROUGH.toString()
                          + "------------------");
                  String origin = tagCompound.getStringOr("origin", "");
                  if (origin.startsWith("Monster Drop (")) {
                    String trim = origin.split("Monster Drop \\(")[1].trim().split("\\)")[0].trim();
                    toolTip.add(
                        ChatFormatting.AQUA + ChatFormatting.BOLD.toString() + "Monster Drop");
                    toolTip.add(
                        ChatFormatting.GRAY
                            + " Killer"
                            + ChatFormatting.WHITE
                            + ": "
                            + ChatFormatting.AQUA
                            + trim.split("/")[0].trim());
                    Date date = new Date(Long.parseLong(trim.split("/")[1].trim()));
                    String dateString = DATE_FORMAT.format(date);
                    toolTip.add(
                        ChatFormatting.GRAY
                            + " Date"
                            + ChatFormatting.WHITE
                            + ": "
                            + ChatFormatting.AQUA
                            + dateString);
                    displayToolTip = true;
                  } else if (origin.startsWith("Altar")) {
                    String trim = origin.split("Altar \\(")[1].trim().split("\\)")[0].trim();
                    String[] split = trim.split("/");
                    toolTip.add(ChatFormatting.AQUA + ChatFormatting.BOLD.toString() + "Altar");

                    toolTip.add(
                        ChatFormatting.GRAY
                            + " Player"
                            + ChatFormatting.WHITE
                            + ": "
                            + ChatFormatting.AQUA
                            + split[0].trim());
                    toolTip.add(
                        ChatFormatting.GRAY
                            + " Location"
                            + ChatFormatting.WHITE
                            + ": "
                            + ChatFormatting.AQUA
                            + split[1].trim());
                    Date date = new Date(Long.parseLong(split[2]));
                    String dateString = DATE_FORMAT.format(date);
                    toolTip.add(
                        ChatFormatting.GRAY
                            + " Date"
                            + ChatFormatting.WHITE
                            + ": "
                            + ChatFormatting.AQUA
                            + dateString);
                    displayToolTip = true;
                  } else if (origin.startsWith("Beta Vendor")) {
                    String trim = origin.split("Beta Vendor \\(")[1].trim().split("\\)")[0].trim();
                    String[] split = trim.split("/");
                    toolTip.add(
                        ChatFormatting.AQUA + ChatFormatting.BOLD.toString() + "Beta Vendor");

                    toolTip.add(
                        ChatFormatting.GRAY
                            + " Player"
                            + ChatFormatting.WHITE
                            + ": "
                            + ChatFormatting.AQUA
                            + split[0].trim());
                    Date date = new Date(Long.parseLong(split[1]));
                    String dateString = DATE_FORMAT.format(date);
                    toolTip.add(
                        ChatFormatting.GRAY
                            + " Date"
                            + ChatFormatting.WHITE
                            + ": "
                            + ChatFormatting.AQUA
                            + dateString);
                    displayToolTip = true;
                  } else if (origin.startsWith("Clue Scroll")) {
                    String trim = origin.split("Clue Scroll \\(")[1].trim().split("\\)")[0].trim();
                    String[] split = trim.split("/");
                    toolTip.add(
                        ChatFormatting.AQUA + ChatFormatting.BOLD.toString() + "Clue Scroll");

                    toolTip.add(
                        ChatFormatting.GRAY
                            + " Player"
                            + ChatFormatting.WHITE
                            + ": "
                            + ChatFormatting.AQUA
                            + split[0].trim());
                    Date date = new Date(Long.parseLong(split[1]));
                    String dateString = DATE_FORMAT.format(date);
                    toolTip.add(
                        ChatFormatting.GRAY
                            + " Date"
                            + ChatFormatting.WHITE
                            + ": "
                            + ChatFormatting.AQUA
                            + dateString);
                    displayToolTip = true;

                  } else if (origin.equalsIgnoreCase("Loot Chest")) {

                    displayToolTip = true;
                  } else if (origin.startsWith("Dungeon Reward")) {
                    String trim =
                        origin.split("Dungeon Reward \\(")[1].trim().split("\\)")[0].trim();
                    String[] split = trim.split("/");
                    toolTip.add(
                        ChatFormatting.AQUA + ChatFormatting.BOLD.toString() + "Dungeon Reward");

                    toolTip.add(
                        ChatFormatting.GRAY
                            + " Dungeon"
                            + ChatFormatting.WHITE
                            + ": "
                            + StringUtils.formatDungeon(split[0].trim()));
                    toolTip.add(
                        ChatFormatting.GRAY
                            + " Player"
                            + ChatFormatting.WHITE
                            + ": "
                            + ChatFormatting.AQUA
                            + split[1].trim());
                    Date date = new Date(Long.parseLong(split[2]));
                    String dateString = DATE_FORMAT.format(date);
                    toolTip.add(
                        ChatFormatting.GRAY
                            + " Date"
                            + ChatFormatting.WHITE
                            + ": "
                            + ChatFormatting.AQUA
                            + dateString);
                    displayToolTip = true;
                  } else if (origin.startsWith("Merchant GUI")) {
                    String trim = origin.split("Merchant GUI\\(")[1].trim().split("\\)")[0].trim();

                    toolTip.add(ChatFormatting.AQUA + ChatFormatting.BOLD.toString() + "Merchant");
                    String playerName = trim.split("/")[0].trim();
                    Date date = new Date(Long.parseLong(trim.split("/")[1].trim()));
                    String dateString = DATE_FORMAT.format(date);
                    toolTip.add(
                        ChatFormatting.GRAY
                            + " Player"
                            + ChatFormatting.WHITE
                            + ": "
                            + ChatFormatting.AQUA
                            + playerName);
                    toolTip.add(
                        ChatFormatting.GRAY
                            + " Date"
                            + ChatFormatting.WHITE
                            + ": "
                            + ChatFormatting.AQUA
                            + dateString);
                    displayToolTip = true;
                  } else if (origin.startsWith("Vote Crate")) {
                    String trim = origin.split("Vote Crate \\(")[1].trim().split("\\)")[0].trim();

                    toolTip.add(
                        ChatFormatting.AQUA + ChatFormatting.BOLD.toString() + "Vote Crate");
                    toolTip.add(
                        ChatFormatting.GRAY
                            + " Player"
                            + ChatFormatting.WHITE
                            + ": "
                            + ChatFormatting.AQUA
                            + trim);
                    displayToolTip = true;

                  } else if (origin.startsWith("Mining Treasure Find")) {

                    toolTip.add(ChatFormatting.GRAY + origin);
                    displayToolTip = true;
                  } else if (origin.startsWith("Fishing Treasure Find")) {
                    String trim = origin.split("Fishing Treasure Find -")[1].trim();

                    toolTip.add(
                        ChatFormatting.AQUA
                            + ChatFormatting.BOLD.toString()
                            + "Fishing Treasure Find");
                    toolTip.add(
                        ChatFormatting.GRAY
                            + " Player"
                            + ChatFormatting.WHITE
                            + ": "
                            + ChatFormatting.AQUA
                            + trim);
                    displayToolTip = true;
                  }
                  if (tagCompound.contains("shard")) {
                    toolTip.add(
                        ChatFormatting.GRAY
                            + " Shard"
                            + ChatFormatting.WHITE
                            + ": "
                            + ChatFormatting.AQUA
                            + StringUtils.formatShard(tagCompound.getStringOr("shard", "")));
                  }
                  toolTip.add(
                      ChatFormatting.GRAY
                          + ChatFormatting.STRIKETHROUGH.toString()
                          + "------------------");
                } else if (Minecraft.getInstance().hasShiftDown()) {
                  List<String> strings = ArmorTooltipCompare.handleToolTip(event);

                  if (!ItemUtils.isEquippedOrInHotbarSlot0(itemStack)) {
                    if (!strings.isEmpty()) {

                      toolTip.addAll(strings);
                      displayToolTip = true;
                    }
                  }
                }
              }
              if (displayToolTip) {

                if (ItemUtils.isWeapon(itemStack.getItem())) {
                  List<String> finalToolTip = new ArrayList<>();

                  double[] damage =
                      ItemUtils.getDamage(itemStack, ItemUtils.getModifierMap(itemStack));

                  List<String> cache = new ArrayList<>();
                  cache.addAll(tooltip);

                  int[] ints = ItemUtils.calculateDPS(itemStack);
                  String toAdd = "";
                  if (DRSettings.DISPLAY_DPS.get(boolean.class)) {
                    toAdd = ChatFormatting.GRAY + " (DPS: " + ints[0] + " - " + ints[1] + ")";
                  }
                  cache.set(
                      1,
                      ChatFormatting.RED
                          + "DMG: +"
                          + Math.round(damage[0])
                          + " - "
                          + Math.round(damage[1])
                          + toAdd);

                  finalToolTip.addAll(cache);

                  tooltip.clear();
                  tooltip.addAll(finalToolTip);
                  tooltip.addAll(toolTip);
                } else {
                  tooltip.addAll(toolTip);
                }

                handleStatPercentages(event, tooltip);
              }
            }
          } catch (Exception e) {
            tooltip.add(ChatFormatting.RED + "ERROR (Please message .matthewe on discord)");
            e.printStackTrace();
          }
        });
  }

  private void handleStatPercentages(ItemTooltipEvent event, List<String> tooltip) {
    if (!DRSettings.SHOW_ORB_PERCENTAGE.get(boolean.class)) {
      return;
    }
    List<String> newToolTip = new ArrayList<>();

    for (String line : tooltip) {
      newToolTip.add(line);
    }
    ItemStack itemStack = event.getItemStack();
    Map<String, double[]> modifierMap = ItemUtils.getModifierMap(itemStack, true);
    if (modifierMap == null) return;
    StatKeyDatabase database = DREnhanced.getStatKeyDatabase();

    Map<String, String> orbStats = new HashMap<>();
    for (String s : modifierMap.keySet()) {
      String orDefault = database.getKeyDictionary().getOrDefault(s, s);
      if (orDefault != null) {
        orbStats.put(s, orDefault);
      }
    }

    Tier tier = ItemUtils.getTier(itemStack);
    ItemRarity rarity = ItemUtils.getRarity(itemStack);

    int level = ItemUtils.getLevel(itemStack);
    int enchant = ItemUtils.getEnchant(itemStack);

    ItemUtils.ItemType type = ItemUtils.getType(itemStack);
    if (type == ItemUtils.ItemType.NONE) return;

    if (level == 0) return;
    StringUtils.editStringList(
        newToolTip,
        s -> {
          String colorStripped = StringUtils.clearColor(s);
          if (colorStripped.contains("✘")) return s;
          if (colorStripped.contains("✔")) return s;
          if (colorStripped.contains("Trinket")) return s;
          if (colorStripped.contains("Passive")) return s;

          for (Map.Entry<String, String> entry : orbStats.entrySet()) {

            if (!colorStripped.contains(entry.getValue())) {
              if (!colorStripped.contains(entry.getKey())) {

                continue;
              }
            }
            IntRange intRange = null;

            boolean dmg = false;
            boolean hp = false;
            boolean energy = false;

            if (colorStripped.contains("ICE DMG")
                || colorStripped.contains("POISON DMG")
                || colorStripped.contains("FIRE DMG")) {
              intRange = database.getWeaponElementals(tier, rarity);
            } else if (colorStripped.contains("INT")
                || colorStripped.contains("VIT")
                || colorStripped.contains("DEX")
                || colorStripped.contains("STR")) {
              intRange = database.getElementals(tier, rarity);

            } else if (colorStripped.contains("DODGE")
                || colorStripped.contains("REFLECT")
                || colorStripped.contains("BLOCK")) {
              intRange = database.getRBLODGE(tier, rarity);
            } else if (colorStripped.startsWith("DMG: +")) {
              if (rarity != ItemRarity.MYTHIC) {

                intRange = new IntRange(0, 0);
                dmg = true;
              }
            } else if (colorStripped.startsWith("HP: +")) {
              if (rarity != ItemRarity.MYTHIC) {

                intRange = new IntRange(0, 0);
                hp = true;
              }
            } else if (colorStripped.startsWith("ENERGY REGEN: +")) {
              if (rarity != ItemRarity.MYTHIC) {

                intRange = new IntRange(0, 0);
                energy = true;
              }
            } else {
              intRange = database.getOrbStat(entry.getKey(), entry.getValue(), tier, rarity);
            }

            if (intRange == null) {
              return s;
            }

            int min = intRange.getMin();
            int max = intRange.getMax();
            int stat = (int) modifierMap.get(entry.getKey())[0];

            double percentage = 0;

            if (dmg) {
              stat = (int) (stat - (stat * (enchant * 0.05))); // undo enchants
              int maxDMG = (int) modifierMap.get(entry.getKey())[1];
              maxDMG = (int) (maxDMG - (maxDMG * (enchant * 0.05))); // undo enchants

              StatKeyDatabase.TierValue.RarityValue.WeaponValue weaponValue =
                  database.getWeaponValue(tier, rarity);
              if (weaponValue != null) {

                return ChatFormatting.GRAY
                    + "["
                    + (percentage >= 100
                        ? "MAX"
                        : (int)
                                WeaponCalculator.calculateRollPercentage(
                                    level, type, weaponValue, stat, maxDMG)
                            + "%")
                    + "] "
                    + s;
              }
            } else if (energy) {
              double eStat = modifierMap.get(entry.getKey())[0];
              double en = (eStat - (eStat * (enchant * 0.05))); // undo enchants

              StatKeyDatabase.TierValue.RarityValue.ArmorValue armorValue =
                  database.getArmorValue(tier, rarity);
              if (armorValue != null) {
                double minE = armorValue.getEnergy().getMin();
                double maxE = armorValue.getEnergy().getMax();

                percentage = ((double) (en - minE) / (maxE - minE)) * 100;

                return ChatFormatting.GRAY
                    + "["
                    + (percentage >= 100 ? "MAX" : (int) percentage + "%")
                    + "] "
                    + s;
              }
            } else if (hp) {
              stat = (int) (stat - (stat * (enchant * 0.05))); // undo enchants

              StatKeyDatabase.TierValue.RarityValue.ArmorValue armorValue =
                  database.getArmorValue(tier, rarity);

              if (armorValue != null) {

                percentage =
                    WeaponCalculator.calculateRollPercentageArmor(level, type, armorValue, stat);

                return ChatFormatting.GRAY
                    + "["
                    + (percentage >= 100
                        ? "MAX"
                        : (int)
                                WeaponCalculator.calculateRollPercentageArmor(
                                    level, type, armorValue, stat)
                            + "%")
                    + "] "
                    + s;
              }
            } else {
              percentage = ((double) (stat - min) / (max - min)) * 100;
            }
            return ChatFormatting.GRAY
                + "["
                + (percentage >= 100 ? "MAX" : (int) percentage + "%")
                + "] "
                + s;
          }

          return s;
        });
    tooltip.clear();
    tooltip.addAll(newToolTip);
  }
}
