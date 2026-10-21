package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handler;

import java.util.ArrayList;
import java.util.List;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handler.logger.HandlerLogger;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.neoforged.neoforge.common.NeoForge;

public abstract class Handler {
  private boolean enabled;
  public HandlerLogger logger;
  protected final List<Listener> listenerList = new ArrayList<>();
  public final Minecraft mc = Minecraft.getInstance();
  public final Font font = mc.font;

  public Handler(boolean debug) {
    logger = new HandlerLogger(getClass(), debug);
  }

  public void enable() {
    if (enabled) return;
    onEnable();
    listenerList.forEach(NeoForge.EVENT_BUS::register);
    enabled = true;
  }

  public void disable() {
    if (!enabled) return;
    onDisable();
    listenerList.forEach(NeoForge.EVENT_BUS::unregister);
    listenerList.clear();
    enabled = false;
  }

  public boolean isEnabled() {
    return enabled;
  }

  protected void registerListener(Listener listener) {
    listenerList.add(listener);
  }

  public abstract void onEnable();

  public abstract void onDisable();
}
