package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.world;

public final class ZoneUtils {
  public static boolean isInSafeZone() {
    return Zone.get() == Zone.SAFE;
  }
}
