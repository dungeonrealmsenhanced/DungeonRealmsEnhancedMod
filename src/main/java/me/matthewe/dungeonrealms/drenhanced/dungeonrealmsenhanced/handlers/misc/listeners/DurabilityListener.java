package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.misc.listeners;

import java.util.List;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.modules.profession.ProfessionItem;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.setting.DRSettings;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.Listener;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.Percent;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.ItemData;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.ItemType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

/** Created by Matthew E on 3/12/2019 at 5:45 PM for the project DungeonRealmsDREnhanced */
public class DurabilityListener implements Listener {

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void onItemTooltip(ItemTooltipEvent event) {
    if (!me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.listeners.ServerContext
        .active()) return;
    me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.TooltipText.edit(
        event,
        tooltip -> {
          ItemStack itemStack = event.getItemStack();
          if (ItemData.hasData(itemStack)
              && DRSettings.DURABILITY_PERCENTAGE.get(boolean.class)
              && ((ItemType.isArmor(itemStack)
                  || ItemType.isWeapon(itemStack)
                  || ProfessionItem.of(itemStack) != null))) {
            String format = new String(DRSettings.DURABILITY_PERCENTAGE_FORMAT.get(String.class));
            List<String> toolTip = tooltip;
            if (toolTip == null || toolTip.isEmpty()) {
              return;
            }
            CompoundTag tagCompound = ItemData.tag(itemStack);
            if (tagCompound == null) {
              return;
            }
            if (tagCompound.contains("durability")) {
              int repairCost = tagCompound.getIntOr("durability", 0);

              double percentage = ((double) repairCost * 100.0D) / 2500.0D;
              String displayNameToolTip = toolTip.get(0);

              displayNameToolTip +=
                  " "
                      + format
                          .replaceAll(
                              "%color%", new Percent(percentage).getDurabilityColor().toString())
                          .replaceAll("&", "\u00a7")
                          .replaceAll("%percent%", String.valueOf((int) percentage));

              toolTip.set(0, displayNameToolTip);

              tooltip.set(0, displayNameToolTip);
            }
          }
        });
  }
}
