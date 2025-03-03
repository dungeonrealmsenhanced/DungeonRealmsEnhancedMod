package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.misc.listeners;

import clutch.dungeonrealms.ArmorTooltipCompare;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.setting.DRSettings;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.Listener;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.StringUtils;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.ItemUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Matthew E on 3/12/2019 at 11:10 AM for the project DungeonRealmsDREnhanced
 */
public class ItemOriginListener implements Listener {
    private static SimpleDateFormat DATE_FORMAT;

    public ItemOriginListener() {

    }

    static {
        if (if24HourFormat()) {
            DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.UK);
        } else {
            DATE_FORMAT = new SimpleDateFormat("M/d/yyyy h:mm a", Locale.US);
        }
    }
    public static boolean if24HourFormat() {
        // Get the system's current time zone
        TimeZone timeZone = TimeZone.getDefault();

        // Format a time string using the locale-specific format
        DateFormat timeFormat = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT, Locale.getDefault());
        timeFormat.setTimeZone(timeZone);

        // Generate a formatted time string
        String formattedTime = timeFormat.format(new Date(0)); // Using 00:00 time for testing

        // Check if the formatted time contains "AM" or "PM"
        return !(formattedTime.toLowerCase().contains("am") || formattedTime.toLowerCase().contains("pm"));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onItemTooltip(ItemTooltipEvent event) {
        try {
            ItemStack itemStack = event.getItemStack();
            if (itemStack.hasTagCompound()) {
                List<String> toolTip = new ArrayList<>();
                boolean displayToolTip = false;

                NBTTagCompound tagCompound = itemStack.getTagCompound();
                if (tagCompound.hasKey("origin")) {
                    if( !GuiScreen.isCtrlKeyDown() && !GuiScreen.isShiftKeyDown()) {
                        toolTip.add(TextFormatting.GRAY + TextFormatting.STRIKETHROUGH.toString() + "------------------");

                        toolTip.add(TextFormatting.GREEN +"CTRL " + TextFormatting.GRAY + "- View Origin");

                        if (!ItemUtils.isEquippedOrInHotbarSlot0(itemStack)){
                            toolTip.add(TextFormatting.GREEN +"SHIFT " + TextFormatting.GRAY + "- Compare");
                        }

                        toolTip.add(TextFormatting.GRAY + TextFormatting.STRIKETHROUGH.toString() + "------------------");
                        displayToolTip=true;
                    }
                    if (GuiScreen.isCtrlKeyDown() && DRSettings.ORIGIN_NAME.get(boolean.class)) {
                        toolTip.add(TextFormatting.GRAY + TextFormatting.STRIKETHROUGH.toString() + "------------------");
                        String origin = tagCompound.getString("origin");
                        if (origin.startsWith("Monster Drop (")) {
                            String trim = origin.split("Monster Drop \\(")[1].trim().split("\\)")[0].trim();
                            toolTip.add(TextFormatting.AQUA + TextFormatting.BOLD.toString() + "Monster Drop");
                            toolTip.add(TextFormatting.GRAY + " Killer" + TextFormatting.WHITE + ": " + TextFormatting.AQUA + trim.split("/")[0].trim());
                            Date date = new Date(Long.parseLong(trim.split("/")[1].trim()));
                            String dateString = DATE_FORMAT.format(date);
                            toolTip.add(TextFormatting.GRAY + " Date" + TextFormatting.WHITE + ": " + TextFormatting.AQUA + dateString);
                            displayToolTip = true;
                        } else if (origin.startsWith("Altar")) {
                            String trim = origin.split("Altar \\(")[1].trim().split("\\)")[0].trim();
                            String[] split = trim.split("/");
                            toolTip.add(TextFormatting.AQUA + TextFormatting.BOLD.toString() + "Altar");
//                        toolTip.add(TextFormatting.RED + TextFormatting.BOLD.toString() + "DISABLED");


                            toolTip.add(TextFormatting.GRAY + " Player" + TextFormatting.WHITE + ": " + TextFormatting.AQUA + split[0].trim());
                            toolTip.add(TextFormatting.GRAY + " Location" + TextFormatting.WHITE + ": " + TextFormatting.AQUA + split[1].trim());
                            Date date = new Date(Long.parseLong(split[2]));
                            String dateString = DATE_FORMAT.format(date);
                            toolTip.add(TextFormatting.GRAY + " Date" + TextFormatting.WHITE + ": " + TextFormatting.AQUA + dateString);
                            displayToolTip = true;
                        } else if (origin.startsWith("Beta Vendor")) {
                            String trim = origin.split("Beta Vendor \\(")[1].trim().split("\\)")[0].trim();
                            String[] split = trim.split("/");
                            toolTip.add(TextFormatting.AQUA + TextFormatting.BOLD.toString() + "Beta Vendor");


                            toolTip.add(TextFormatting.GRAY + " Player" + TextFormatting.WHITE + ": " + TextFormatting.AQUA + split[0].trim());
                            Date date = new Date(Long.parseLong(split[1]));
                            String dateString = DATE_FORMAT.format(date);
                            toolTip.add(TextFormatting.GRAY + " Date" + TextFormatting.WHITE + ": " + TextFormatting.AQUA + dateString);
                            displayToolTip = true;
                        }  else if (origin.startsWith("Clue Scroll")) {
                            String trim = origin.split("Clue Scroll \\(")[1].trim().split("\\)")[0].trim();
                            String[] split = trim.split("/");
                            toolTip.add(TextFormatting.AQUA + TextFormatting.BOLD.toString() + "Clue Scroll");


                            toolTip.add(TextFormatting.GRAY + " Player" + TextFormatting.WHITE + ": " + TextFormatting.AQUA + split[0].trim());
                            Date date = new Date(Long.parseLong(split[1]));
                            String dateString = DATE_FORMAT.format(date);
                            toolTip.add(TextFormatting.GRAY + " Date" + TextFormatting.WHITE + ": " + TextFormatting.AQUA + dateString);
                            displayToolTip = true;

                        } else if (origin.equalsIgnoreCase("Loot Chest")) {

                            displayToolTip = true;
                        } else if (origin.startsWith("Dungeon Reward")) {
                            String trim = origin.split("Dungeon Reward \\(")[1].trim().split("\\)")[0].trim();
                            String[] split = trim.split("/");
                            toolTip.add(TextFormatting.AQUA + TextFormatting.BOLD.toString() + "Dungeon Reward");
//                        toolTip.add(TextFormatting.RED + TextFormatting.BOLD.toString() + "DISABLED");


                            toolTip.add(TextFormatting.GRAY + " Dungeon" + TextFormatting.WHITE + ": " + StringUtils.formatDungeon(split[0].trim()));
                            toolTip.add(TextFormatting.GRAY + " Player" + TextFormatting.WHITE + ": " + TextFormatting.AQUA + split[1].trim());
                            Date date = new Date(Long.parseLong(split[2]));
                            String dateString = DATE_FORMAT.format(date);
                            toolTip.add(TextFormatting.GRAY + " Date" + TextFormatting.WHITE + ": " + TextFormatting.AQUA + dateString);
                            displayToolTip = true;
                        } else if (origin.startsWith("Merchant GUI")) {
                            String trim = origin.split("Merchant GUI\\(")[1].trim().split("\\)")[0].trim();

                            toolTip.add(TextFormatting.AQUA + TextFormatting.BOLD.toString() + "Merchant");
                            String playerName = trim.split("/")[0].trim();
                            Date date = new Date(Long.parseLong(trim.split("/")[1].trim()));
                            String dateString = DATE_FORMAT.format(date);
                            toolTip.add(TextFormatting.GRAY + " Player" + TextFormatting.WHITE + ": " + TextFormatting.AQUA + playerName);
                            toolTip.add(TextFormatting.GRAY + " Date" + TextFormatting.WHITE + ": " + TextFormatting.AQUA + dateString);
                            displayToolTip = true;
                        } else if (origin.startsWith("Vote Crate")) {
                            String trim = origin.split("Vote Crate \\(")[1].trim().split("\\)")[0].trim();

                            toolTip.add(TextFormatting.AQUA + TextFormatting.BOLD.toString() + "Vote Crate");
                            toolTip.add(TextFormatting.GRAY + " Player" + TextFormatting.WHITE + ": " + TextFormatting.AQUA + trim);
                            displayToolTip = true;

                        } else if (origin.startsWith("Mining Treasure Find")) {

                            toolTip.add(TextFormatting.GRAY + origin);
                            displayToolTip = true;
                        } else if (origin.startsWith("Fishing Treasure Find")) {
                            String trim = origin.split("Fishing Treasure Find -")[1].trim();

                            toolTip.add(TextFormatting.AQUA + TextFormatting.BOLD.toString() + "Fishing Treasure Find");
                            toolTip.add(TextFormatting.GRAY + " Player" + TextFormatting.WHITE + ": " + TextFormatting.AQUA + trim);
                            displayToolTip = true;
                        }
                        if (tagCompound.hasKey("shard")) {
                            toolTip.add(TextFormatting.GRAY + " Shard" + TextFormatting.WHITE + ": " + TextFormatting.AQUA + StringUtils.formatShard(tagCompound.getString("shard")));
                        }
                        toolTip.add(TextFormatting.GRAY + TextFormatting.STRIKETHROUGH.toString() + "------------------");
                    } else if (GuiScreen.isShiftKeyDown()) {
                        List<String> strings = ArmorTooltipCompare.handleToolTip(event);

                        if (!ItemUtils.isEquippedOrInHotbarSlot0(itemStack)){
                            if (!strings.isEmpty()) {
                                toolTip.addAll(strings);
                                displayToolTip=true;
                            }
                        }

                    }

                }
                if (displayToolTip) {
                    event.getToolTip().addAll(toolTip);

                }

            }
        } catch (Exception e) {
            event.getToolTip().add(TextFormatting.RED + "ERROR (Please message .matthewe on discord)");
            e.printStackTrace();
        }
    }
}
