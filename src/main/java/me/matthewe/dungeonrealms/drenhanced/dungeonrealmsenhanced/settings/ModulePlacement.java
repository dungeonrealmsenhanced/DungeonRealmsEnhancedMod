package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings;

/** Persisted coordinates are [x, y, enabled], including legacy 1.12.2 files. */
public record ModulePlacement(int x, int y, boolean enabled) {
  public static ModulePlacement decode(Integer[] values) {
    if (values == null
        || values.length < 3
        || values[0] == null
        || values[1] == null
        || values[2] == null) return null;
    return new ModulePlacement(Math.max(0, values[0]), Math.max(0, values[1]), values[2] == 1);
  }
}
