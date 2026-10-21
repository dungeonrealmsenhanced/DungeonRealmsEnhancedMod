package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.misc.listeners;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.listeners.ServerContext;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.Modules;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.Listener;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.render.*;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

public final class DefaultOverlayListener implements Listener {
  @SubscribeEvent
  public void render(RenderGuiEvent.Post e) {
    var mc = Minecraft.getInstance();
    if (!ServerContext.active()
        || mc.gui.hud.isHidden()
        || mc.gui.screen()
            instanceof
            me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.GuiSettings) return;
    RenderUtils.withGraphics(
        e.getGuiGraphics(),
        () ->
            Modules.getModules()
                .forEach(
                    m -> {
                      m.posX = Math.max(0, Math.min(e.getGuiGraphics().guiWidth() - 20, m.posX));
                      m.posY = Math.max(0, Math.min(e.getGuiGraphics().guiHeight() - 20, m.posY));
                      m.onRender(
                          new ScreenSize(mc),
                          e.getPartialTick().getGameTimeDeltaPartialTick(false));
                    }));
  }

  @SubscribeEvent
  public void layer(RenderGuiLayerEvent.Pre e) {
    if (ServerContext.active()
        && (e.getName().equals(VanillaGuiLayers.ARMOR_LEVEL)
            || e.getName().equals(VanillaGuiLayers.EFFECTS))) e.setCanceled(true);
  }
}
