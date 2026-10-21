package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.world;

import java.util.Arrays;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.nostalgia.listeners.HealthBarListener;
import net.minecraft.world.BossEvent;

/** Created by Matthew E on 3/17/2019 at 6:22 PM for the project DungeonRealmsDREnhanced */
public enum Zone {
  UNKNOWN(null),
  SAFE(BossEvent.BossBarColor.GREEN),
  WILDERNESS(BossEvent.BossBarColor.YELLOW),
  CHAOTIC(BossEvent.BossBarColor.RED);

  private BossEvent.BossBarColor color;

  Zone(BossEvent.BossBarColor color) {
    this.color = color;
  }

  public static Zone get() {
    return HealthBarListener.getZone();
  }

  public BossEvent.BossBarColor getColor() {
    return color;
  }

  public static Zone byColor(BossEvent.BossBarColor color) {
    return Arrays.stream(values()).filter(zone -> zone.color == color).findFirst().orElse(null);
  }
}
