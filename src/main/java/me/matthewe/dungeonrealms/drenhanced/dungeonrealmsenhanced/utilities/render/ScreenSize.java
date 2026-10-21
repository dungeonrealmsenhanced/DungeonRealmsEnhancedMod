package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.render;

import net.minecraft.client.Minecraft;

public record ScreenSize(int width, int height) {
  public ScreenSize(Minecraft mc) {
    this(mc.getWindow().getGuiScaledWidth(), mc.getWindow().getGuiScaledHeight());
  }

  public int getScaledWidth() {
    return width;
  }

  public int getScaledHeight() {
    return height;
  }
}
