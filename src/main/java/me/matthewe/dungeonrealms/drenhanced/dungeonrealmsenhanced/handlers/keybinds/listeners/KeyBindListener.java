package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.keybinds.listeners;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.keybinds.*;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.restful.guis.ChangelogGui;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.GuiSettings;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.setting.gui.GuiDRSettingsCategory;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.Listener;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;

public final class KeyBindListener implements Listener {
  public KeyBindListener(KeyBindHandler handler) {}

  @SubscribeEvent
  public void tick(ClientTickEvent.Post event) {
    var mc = Minecraft.getInstance();
    while (KeyBinds.OPEN_DR_SETTINGS.consumeClick()) mc.gui.setScreen(new GuiDRSettingsCategory());
    while (KeyBinds.OPEN_DR_OVERLAY_SETTINGS.consumeClick()) mc.gui.setScreen(new GuiSettings());
    while (KeyBinds.CHANGE_LOGS.consumeClick()) mc.gui.setScreen(new ChangelogGui());
  }
}
