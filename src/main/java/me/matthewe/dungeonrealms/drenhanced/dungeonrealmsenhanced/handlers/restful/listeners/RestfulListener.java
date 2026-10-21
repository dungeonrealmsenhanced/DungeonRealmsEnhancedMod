package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.restful.listeners;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.DREnhanced;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.events.DungeonRealmsJoinEvent;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.restful.RestfulHandler;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;

public final class RestfulListener implements Listener {
  public RestfulListener(RestfulHandler handler) {}

  @SubscribeEvent
  public void joined(DungeonRealmsJoinEvent e) {
    var p = Minecraft.getInstance().player;
    if (p != null)
      p.sendSystemMessage(
          Component.literal(
              "DREnhanced " + DREnhanced.VERSION + " | H: Settings | Numpad 5: HUD Editor"));
  }
}
