package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.misc.listeners;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.setting.DRSettings;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.Listener;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.StringUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Matthew E on 3/12/2019 at 11:10 AM for the project DungeonRealmsDREnhanced
 */
public class ItemOriginListener implements Listener  {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onItemTooltip(ItemTooltipEvent event) {
        ItemStack itemStack = event.getItemStack();
        if (itemStack.hasTagCompound() && GuiScreen.isShiftKeyDown() && DRSettings.ORIGIN_NAME.get(boolean.class)) {
            List<String> toolTip = new ArrayList<>();
            boolean displayToolTip = false;

            NBTTagCompound tagCompound = itemStack.getTagCompound();
            if (tagCompound.hasKey("origin")) {
                toolTip.add(TextFormatting.GRAY + TextFormatting.STRIKETHROUGH.toString() + "------------------");
                String origin = tagCompound.getString("origin");
                if (origin.startsWith("Monster Drop (")) {
                    String trim = origin.split("Monster Drop \\(")[1].trim().split("\\)")[0].trim();
                    toolTip.add(TextFormatting.AQUA + TextFormatting.BOLD.toString() + "Monster Drop");
                    toolTip.add(TextFormatting.GRAY + " Killer" + TextFormatting.WHITE + ": " + TextFormatting.AQUA + trim.split("/")[0].trim());
                    Date date = new Date(Long.parseLong(trim.split("/")[1].trim()));
                    String dateString = new SimpleDateFormat("MM/dd/yyyy h:mm a").format(date);
                    toolTip.add(TextFormatting.GRAY + " Date" + TextFormatting.WHITE + ": " + TextFormatting.AQUA + dateString);
                    displayToolTip = true;
                } else if (origin.startsWith("Altar")) {
                    String trim = origin.split("Altar \\(")[1].trim().split("\\)")[0].trim();

                    toolTip.add(TextFormatting.AQUA + TextFormatting.BOLD.toString() + "Altar");
                    toolTip.add(TextFormatting.GRAY + " Player" + TextFormatting.WHITE + ": " + TextFormatting.AQUA + trim.split(", ")[1].trim());
                    toolTip.add(TextFormatting.GRAY + " Location" + TextFormatting.WHITE + ": " + TextFormatting.AQUA + trim.split(",")[0].trim());
                    displayToolTip = true;
                } else if (origin.startsWith("Merchant")) {
                    String trim = origin.split("Merchant \\(")[1].trim().split("\\)")[0].trim();

                    toolTip.add(TextFormatting.AQUA + TextFormatting.BOLD.toString() + "Merchant");
                    toolTip.add(TextFormatting.GRAY + " Player" + TextFormatting.WHITE + ": " + TextFormatting.AQUA + trim);
                    displayToolTip = true;
                } else if (origin.startsWith("Vote Crate")) {
                    String trim = origin.split("Vote Crate \\(")[1].trim().split("\\)")[0].trim();

                    toolTip.add(TextFormatting.AQUA + TextFormatting.BOLD.toString() + "Vote Crate");
                    toolTip.add(TextFormatting.GRAY + " Player" + TextFormatting.WHITE + ": " + TextFormatting.AQUA + trim);
                    displayToolTip = true;
                } else if (origin.startsWith("Mining Treasure Find")) {
                    String trim = origin.split("Mining Treasure Find -")[1].trim();

                    toolTip.add(TextFormatting.AQUA + TextFormatting.BOLD.toString() + "Mining Treasure Find");
                    toolTip.add(TextFormatting.GRAY + " Player" + TextFormatting.WHITE + ": " + TextFormatting.AQUA + trim);
                    displayToolTip = true;
                }else if (origin.startsWith("Fishing Treasure Find")) {
                    String trim = origin.split("Fishing Treasure Find -")[1].trim();

                    toolTip.add(TextFormatting.AQUA + TextFormatting.BOLD.toString() + "Fishing Treasure Find");
                    toolTip.add(TextFormatting.GRAY + " Player" + TextFormatting.WHITE + ": " + TextFormatting.AQUA + trim);
                    displayToolTip = true;
                }
                if (tagCompound.hasKey("shard")) {
                    toolTip.add(TextFormatting.GRAY + " Shard" + TextFormatting.WHITE + ": " + TextFormatting.AQUA + StringUtils.formatShard(tagCompound.getString("shard")));
                }
                toolTip.add(TextFormatting.GRAY + TextFormatting.STRIKETHROUGH.toString() + "------------------");
            }
            if (displayToolTip) {
                event.getToolTip().addAll(toolTip);
            }
        }
    }
}
