package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.misc.listeners;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.listeners.ServerContext;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;

public final class PauseMenuButtonListener implements Listener {
  @SubscribeEvent
  public void init(ScreenEvent.Init.Post e) {
    if (!(e.getScreen() instanceof PauseScreen) || !ServerContext.active()) return;
    int x = e.getScreen().width - 110;
    e.addListener(
        Button.builder(Component.literal("DR Logout"), b -> send("logout"))
            .bounds(x, 8, 102, 20)
            .build());
    e.addListener(
        Button.builder(Component.literal("DR Hub"), b -> send("hub"))
            .bounds(x, 32, 102, 20)
            .build());
  }

  private void send(String command) {
    var mc = Minecraft.getInstance();
    if (mc.getConnection() != null && ServerContext.active()) {
      mc.getConnection().sendCommand(command);
      mc.gui.setScreen(null);
    }
  }
}
