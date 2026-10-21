package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.misc.listeners;

import java.util.ArrayList;
import java.util.List;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.DREnhanced;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.Listener;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.ItemData;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

/** Created by Matthew E on 12/31/2018 at 1:30 PM for the project DungeonRealmsDREnhanced */
public class NBTListener implements Listener {
  @SubscribeEvent(priority = EventPriority.LOW)
  public void onItemTooltip(ItemTooltipEvent event) {
    if (!me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.listeners.ServerContext
        .active()) return;
    me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.TooltipText.edit(
        event,
        tooltip -> {
          ItemStack itemStack = event.getItemStack();
          if (ItemData.hasData(itemStack)
              && Minecraft.getInstance().hasAltDown()
              && DREnhanced.isDeveloper()) {
            List<String> toolTip = new ArrayList<>();
            toolTip.add(" ");
            CompoundTag tagCompound = ItemData.tag(itemStack);

            for (String s : tagCompound.keySet()) {
              toolTip.add(s + ":" + tagCompound.get(s).toString());
            }
            tooltip.addAll(toolTip);
          }
        });
  }
}
