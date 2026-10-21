package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;

/** Scope rendering to NeoForge's GUI extraction pass; never issue OpenGL calls. */
public final class RenderUtils {
  private static GuiGraphicsExtractor graphics;

  private RenderUtils() {}

  public static void withGraphics(GuiGraphicsExtractor g, Runnable draw) {
    var previous = graphics;
    graphics = g;
    try {
      draw.run();
    } finally {
      graphics = previous;
    }
  }

  public static void drawText(String text, float x, float y, int color) {
    if (graphics != null)
      graphics.text(Minecraft.getInstance().font, text, (int) x, (int) y, color | 0xff000000, true);
  }

  public static void drawText(int x, int y, String text, int color) {
    drawText(text, x, y, color);
  }

  public static int[] drawRectLines(int x, int y, int right, int bottom, int color) {
    if (graphics != null) graphics.fill(x, y, right, bottom, color);
    return new int[] {right, bottom};
  }

  public static int[] drawRectLines(int x, int y, String text) {
    int[] size = getTextBoxDimentions(x, y, text);
    return drawRectLines(x, y, size[0], size[1], 0x44000000);
  }

  public static int[] getTextBoxDimentions(int x, int y, String text) {
    return new int[] {x + Minecraft.getInstance().font.width(text) + 4, y + 12};
  }

  public static boolean isMouseInside(int mx, int my, int x, int y, int right, int bottom) {
    return mx >= x && mx < right && my >= y && my < bottom;
  }

  public static boolean isMouseWithin(int mx, int my, int x, int y, int w, int h) {
    return isMouseInside(mx, my, x, y, x + w, y + h);
  }
}
