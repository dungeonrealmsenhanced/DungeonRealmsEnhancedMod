package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.listeners;

import net.minecraft.client.gui.screens.advancements.AdvancementsScreen;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;

public final class AdvancementHiderListener {
  @SubscribeEvent
  public void open(ScreenEvent.Opening e) {
    if (ServerContext.active() && e.getNewScreen() instanceof AdvancementsScreen)
      e.setCanceled(true);
  }
}
