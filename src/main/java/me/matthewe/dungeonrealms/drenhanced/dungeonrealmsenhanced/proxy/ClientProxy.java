package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.proxy;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.*;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.Handlers;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.keybinds.KeyBinds;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.listeners.*;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.Modules;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;

public final class ClientProxy {
  public static void registerModEvents(IEventBus bus) {
    bus.addListener(
        (RegisterKeyMappingsEvent e) -> {
          e.register(KeyBinds.OPEN_DR_SETTINGS);
          e.register(KeyBinds.OPEN_DR_OVERLAY_SETTINGS);
          e.register(KeyBinds.CHANGE_LOGS);
        });
  }

  public static void initialize() {
    Modules.init();
    Modules.loadModules();
    DREnhanced.loadModuleSettings();
    DREnhanced.statKeyDatabase.loadDictionary();
    Modules.registerListeners();
    NeoForge.EVENT_BUS.register(new StatisticListener());
    NeoForge.EVENT_BUS.register(new ItemCheckerListener());
    NeoForge.EVENT_BUS.register(new RarityOverlayListener());
    NeoForge.EVENT_BUS.register(new AdvancementHiderListener());
    NeoForge.EVENT_BUS.register(new ClientCommands());
    Handlers.init();
    Handlers.enableHandlers();
  }
}
