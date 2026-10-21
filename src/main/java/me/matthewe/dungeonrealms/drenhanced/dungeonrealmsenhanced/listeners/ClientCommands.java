package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.listeners;

import static net.minecraft.commands.Commands.literal;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.DREnhanced;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.nostalgia.listeners.HealthBarListener;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.restful.guis.ChangelogGui;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.GuiSettings;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.setting.gui.GuiDRSettingsCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;

public final class ClientCommands {
  private static int open(net.minecraft.client.gui.screens.Screen screen) {
    Minecraft.getInstance().schedule(() -> Minecraft.getInstance().gui.setScreen(screen));
    return 1;
  }

  @SubscribeEvent
  public void register(RegisterClientCommandsEvent e) {
    var d = e.getDispatcher();
    d.register(
        literal("drenhanced")
            .executes(c -> open(new GuiDRSettingsCategory()))
            .then(literal("overlay").executes(c -> open(new GuiSettings())))
            .then(
                literal("enable")
                    .executes(
                        c -> {
                          ServerContext.forced = true;
                          c.getSource()
                              .sendSuccess(
                                  () ->
                                      Component.literal("DREnhanced enabled for this connection."),
                                  false);
                          return 1;
                        })));
    d.register(literal("changelogs").executes(c -> open(new ChangelogGui())));
    d.register(
        literal("zone")
            .executes(
                c -> {
                  c.getSource()
                      .sendSuccess(
                          () ->
                              Component.literal(
                                  ServerContext.active()
                                      ? "Current zone: " + HealthBarListener.getZone().name()
                                      : "Connect to DungeonRealms first."),
                          false);
                  return 1;
                }));
    d.register(
        literal("info1")
            .executes(
                c -> {
                  c.getSource()
                      .sendSuccess(
                          () ->
                              Component.literal(
                                  "DREnhanced "
                                      + DREnhanced.VERSION
                                      + " / Minecraft 26.3 / NeoForge"),
                          false);
                  return 1;
                }));
  }
}
