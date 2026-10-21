package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.listeners;

import java.util.Locale;
import net.minecraft.client.Minecraft;

public final class ServerContext {
  public static boolean forced;

  public static boolean active() {
    var mc = Minecraft.getInstance();
    if (mc.player == null || mc.level == null) return false;
    if (forced) return true;
    var server = mc.getCurrentServer();
    if (server == null) return false;
    String host = server.ip.toLowerCase(Locale.ROOT).split(":")[0];
    return host.equals("dungeonrealms.net") || host.endsWith(".dungeonrealms.net");
  }
}
