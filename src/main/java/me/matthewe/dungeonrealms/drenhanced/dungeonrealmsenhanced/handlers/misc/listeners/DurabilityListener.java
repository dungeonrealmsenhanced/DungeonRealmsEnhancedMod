package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.misc.listeners;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.modules.profession.ProfessionItem;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.setting.DRSettings;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.Listener;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.Percent;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.ItemType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

/**
 * Created by Matthew E on 3/12/2019 at 5:45 PM for the project DungeonRealmsDREnhanced
 */
public class DurabilityListener implements Listener {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onItemTooltip(ItemTooltipEvent event) {
        ItemStack itemStack = event.getItemStack();
        if (itemStack.hasTagCompound() && DRSettings.DURABILITY_PERCENTAGE.get(boolean.class) && ((ItemType.isArmor(itemStack) || ItemType.isWeapon(itemStack)|| ProfessionItem.of(itemStack)!=null))) {
            String format = new String(DRSettings.DURABILITY_PERCENTAGE_FORMAT.get(String.class));
            List<String> toolTip = event.getToolTip();
            if (toolTip == null || toolTip.isEmpty()) {
                return;
            }
            NBTTagCompound tagCompound = itemStack.getTagCompound();
            if (tagCompound == null) {
                return;
            }
            if (tagCompound.hasKey("RepairCost")) {
                int repairCost = tagCompound.getInteger("RepairCost");

                double percentage = ((double) repairCost * 100.0D) / 2500.0D;
                String displayNameToolTip = toolTip.get(0);

                displayNameToolTip += " " + format.replaceAll("%color%", new Percent(percentage).getDurabilityColor().toString()).replaceAll("&", "\u00a7").replaceAll("%percent%", String.valueOf((int) percentage));


                toolTip.set(0, displayNameToolTip);

                event.getToolTip().set(0, displayNameToolTip);
            }

        }
    }
}