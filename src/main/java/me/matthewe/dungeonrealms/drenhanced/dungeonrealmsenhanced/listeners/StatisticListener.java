package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.listeners;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.DREnhanced;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.events.DungeonRealmsJoinEvent;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.player.*;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.world.Location;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.GameShuttingDownEvent;

public final class StatisticListener {
  private int ticks;
  private boolean joined;
  private final StatisticTracker tracker = new StatisticTracker();

  @SubscribeEvent
  public void tick(ClientTickEvent.Post e) {
    if (!ServerContext.active()) return;
    var player = Minecraft.getInstance().player;
    if (!joined) {
      joined = true;
      DRPlayer.get().setLoaded(true);
      DRPlayer.get().resetMining();
      NeoForge.EVENT_BUS.post(new DungeonRealmsJoinEvent());
    }
    DRPlayer.get().update();
    DRPlayer.get()
        .setLocation(
            new Location(
                player.getX(), player.getY(), player.getZ(), player.getYRot(), player.getXRot()));
    if (++ticks % 20 == 0) {
      tracker.updateStatistics();
      RarityOverlayListener.updateGems();
    }
    if (ticks % 6000 == 0) DREnhanced.saveModuleSettings();
  }

  @SubscribeEvent
  public void disconnect(ClientPlayerNetworkEvent.LoggingOut e) {
    DREnhanced.saveModuleSettings();
    DRPlayer.get().setLoaded(false);
    DRPlayer.get().resetMining();
    joined = false;
    ticks = 0;
    ServerContext.forced = false;
    RarityOverlayListener.inventoryGems = 0;
    me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.modules.ShardCountModule
        .setShard(null);
    me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.nostalgia.listeners
        .HealthBarListener.reset();
  }

  @SubscribeEvent
  public void shutdown(GameShuttingDownEvent e) {
    DREnhanced.saveModuleSettings();
    me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.Handlers.disableHandlers();
    DREnhanced.statKeyDatabase.close();
  }
}
