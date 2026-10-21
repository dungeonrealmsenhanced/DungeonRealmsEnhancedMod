package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.player;

import java.util.regex.Pattern;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.FooterExtractor;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;

public final class StatisticTracker {
  private static final Pattern DURATION = Pattern.compile("(\\d+)([wdhms])");

  public void updateStatistics() {
    var mc = Minecraft.getInstance();
    if (mc.player == null || mc.getConnection() == null) return;
    var tab = mc.gui.hud.getTabList();
    FooterExtractor.extractFooterInfo(tab);
    for (var info : mc.getConnection().getOnlinePlayers()) {
      String name = ChatFormatting.stripFormatting(tab.getNameForDisplay(info).getString());
      if (name == null) continue;
      for (String line : name.split("\n")) parseLine(line.trim());
    }
    DRPlayer.get().setUuid(mc.player.getUUID());
  }

  public static void parseLine(String line) {
    try {
      var stats = DRPlayer.get().getStatistics();
      String value =
          line.contains(":") ? line.substring(line.indexOf(':') + 1).trim().replace(",", "") : "";
      if (line.startsWith("Ore Mined:")) stats.setOreMined(Integer.parseInt(value));
      else if (line.startsWith("Bank Gems:")) stats.setBankGems(Long.parseLong(value));
      else if (line.startsWith("Fish Caught:")) stats.setFishCaught(Integer.parseInt(value));
      else if (line.startsWith("Loot Opened:")) stats.setLootOpened(Integer.parseInt(value));
      else if (line.startsWith("Player Kills:")) {
        var m = Pattern.compile("(\\d+)\\D+(\\d+)").matcher(value);
        if (m.find()) {
          stats.setPlayerKills(Integer.parseInt(m.group(1)));
          stats.setDeaths(Integer.parseInt(m.group(2)));
        }
      } else if (line.startsWith("Playtime:")) {
        long millis = 0;
        var m = DURATION.matcher(value);
        while (m.find())
          millis +=
              Long.parseLong(m.group(1))
                  * switch (m.group(2)) {
                    case "w" -> 604800000L;
                    case "d" -> 86400000L;
                    case "h" -> 3600000L;
                    case "m" -> 60000L;
                    default -> 1000L;
                  };
        stats.setPlayTime(millis);
      }
    } catch (NumberFormatException ignored) {
      /* Partial tab updates are normal during shard changes. */
    }
  }
}
