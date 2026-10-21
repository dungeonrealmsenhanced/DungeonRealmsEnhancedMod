package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.nostalgia.listeners;

import java.util.regex.Pattern;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.listeners.ServerContext;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.setting.DRSettings;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.Listener;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.world.Zone;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.CustomizeGuiOverlayEvent;

public final class HealthBarListener implements Listener {
  private static int health = -1, maxHealth = -1;
  private static Zone zone = Zone.UNKNOWN;
  private static final Pattern HP = Pattern.compile("- HP\\s+([\\d,]+)(?:\\s*/\\s*([\\d,]+))?");

  public static void reset() {
    health = -1;
    maxHealth = -1;
    zone = Zone.UNKNOWN;
  }

  @SubscribeEvent
  public void boss(CustomizeGuiOverlayEvent.BossEventProgress e) {
    if (!ServerContext.active()) return;
    var boss = e.getBossEvent();
    var m = HP.matcher(boss.getName().getString());
    if (!m.find()) return;
    try {
      health = Integer.parseInt(m.group(1).replace(",", ""));
      maxHealth = m.group(2) == null ? health : Integer.parseInt(m.group(2).replace(",", ""));
    } catch (NumberFormatException ignored) {
      return;
    }
    var current = Zone.byColor(boss.getColor());
    zone = current == null ? Zone.UNKNOWN : current;
    var g = e.getGuiGraphics();
    var font = Minecraft.getInstance().font;
    if (DRSettings.OLD_BOSS_BAR.get(boolean.class)) {
      e.setCanceled(true);
      int x = e.getX(), y = e.getY();
      g.fill(x, y, x + 182, y + 5, 0xff330033);
      g.fill(
          x,
          y,
          x + (int) (182 * Math.max(0, Math.min(1, (double) health / Math.max(1, maxHealth)))),
          y + 5,
          0xffff55ff);
      g.centeredText(font, "HP " + health + " / " + maxHealth, x + 91, y - 9, 0xffff55ff);
    } else if (DRSettings.LEVEL_HEALTH.get(boolean.class)) {
      e.setCanceled(true);
      g.centeredText(
          font,
          "HP " + health + " / " + maxHealth,
          g.guiWidth() / 2,
          g.guiHeight() - 41,
          0xff55ff55);
    }
  }

  public static Zone getZone() {
    return zone;
  }

  public static int getHealth() {
    return health;
  }

  public static int getMaxHealth() {
    return maxHealth;
  }

  public static int[] getCurrentHealth() {
    return new int[] {health, maxHealth};
  }
}
