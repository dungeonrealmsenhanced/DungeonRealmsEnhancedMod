package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities;

import java.util.regex.Pattern;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.modules.ShardCountModule;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.PlayerTabOverlay;

public final class FooterExtractor {
  private static final Pattern ONLINE = Pattern.compile("([\\d,]+) here");
  private static final Pattern SHARD =
      Pattern.compile("(?m)^(\\S+) \\d+ms \\* play\\.dungeonrealms\\.net");

  public static void extractFooterInfo(PlayerTabOverlay tab) {
    parse(getFooterText(tab));
  }

  public record ShardData(String shard, int players) {}

  public static ShardData parseFooter(String text) {
    text = text == null ? "" : ChatFormatting.stripFormatting(text);
    var online = ONLINE.matcher(text);
    var shard = SHARD.matcher(text.trim());
    int count = 0;
    try {
      count = online.find() ? Integer.parseInt(online.group(1).replace(",", "")) : 0;
    } catch (NumberFormatException ignored) {
    }
    return new ShardData(shard.find() ? shard.group(1) : null, count);
  }

  public static void parse(String text) {
    var data = parseFooter(text);
    ShardCountModule.setCount(data.players());
    ShardCountModule.setShard(data.shard());
  }

  public static String getFooterText(PlayerTabOverlay tab) {
    return tab == null || tab.footer == null ? "" : tab.footer.getString();
  }
}
