package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.DREnhanced;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.Module;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.Modules;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.render.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

public class GuiSettings extends Screen {
  public static boolean settingsOpened;
  private Module selected;
  private int offsetX, offsetY;
  private boolean dragging;

  public GuiSettings() {
    super(Component.literal("DREnhanced HUD Editor"));
  }

  public void display() {
    Minecraft.getInstance().gui.setScreen(this);
  }

  @Override
  protected void init() {
    settingsOpened = true;
    addRenderableWidget(
        Button.builder(
                Component.literal("Reset positions"),
                b -> {
                  for (Module m : Modules.getModules()) {
                    m.posX = m.defaultPosX;
                    m.posY = m.defaultPosY;
                  }
                })
            .bounds(8, height - 28, 120, 20)
            .build());
    addRenderableWidget(
        Button.builder(
                Component.literal("Toggle selected"),
                b -> {
                  if (selected != null) selected.setEnabled(!selected.isEnabled());
                })
            .bounds(132, height - 28, 120, 20)
            .build());
    addRenderableWidget(
        Button.builder(Component.literal("Done"), b -> onClose())
            .bounds(width - 88, height - 28, 80, 20)
            .build());
  }

  private int boxWidth(Module m) {
    return switch (m.getName()) {
      case "Coords", "Gems", "ShardCount" -> Math.max(60, m.width - m.posX + 2);
      default -> Math.max(60, m.width);
    };
  }

  private int boxHeight(Module m) {
    return switch (m.getName()) {
      case "Coords", "Gems", "ShardCount" -> 16;
      default -> Math.max(16, m.height);
    };
  }

  @Override
  public void extractRenderState(GuiGraphicsExtractor g, int mx, int my, float delta) {
    RenderUtils.withGraphics(
        g,
        () -> {
          for (Module m : Modules.getModules()) {
            m.update(new ScreenSize(minecraft), delta);
            m.posX = Math.max(2, Math.min(width - boxWidth(m) - 2, m.posX));
            m.posY = Math.max(28, Math.min(height - 40 - boxHeight(m), m.posY));
            m.update(new ScreenSize(minecraft), delta);
            g.fill(
                m.posX - 2,
                m.posY - 2,
                m.posX + boxWidth(m),
                m.posY + boxHeight(m),
                m == selected ? 0x885588ff : 0x66000000);
            m.renderEditing(new ScreenSize(minecraft), delta);
            g.text(
                font,
                m.isEnabled() ? "ON" : "OFF",
                m.posX,
                m.posY + boxHeight(m) - 9,
                m.isEnabled() ? 0xff55ff55 : 0xffff5555);
          }
        });
    super.extractRenderState(g, mx, my, delta);
    g.centeredText(font, "Drag modules. Select a module to toggle it.", width / 2, 8, 0xffffffff);
  }

  @Override
  public boolean mouseClicked(MouseButtonEvent e, boolean doubleClick) {
    if (super.mouseClicked(e, doubleClick)) return true;
    for (Module m : Modules.getModules())
      if (e.x() >= m.posX - 2
          && e.x() < m.posX + boxWidth(m)
          && e.y() >= m.posY - 2
          && e.y() < m.posY + boxHeight(m)) {
        selected = m;
        offsetX = (int) e.x() - m.posX;
        offsetY = (int) e.y() - m.posY;
        dragging = true;
        return true;
      }
    return false;
  }

  @Override
  public boolean mouseDragged(MouseButtonEvent e, double dx, double dy) {
    if (!dragging || selected == null) return super.mouseDragged(e, dx, dy);
    selected.posX = Math.max(0, Math.min(width - boxWidth(selected), (int) e.x() - offsetX));
    selected.posY =
        Math.max(24, Math.min(height - 40 - boxHeight(selected), (int) e.y() - offsetY));
    return true;
  }

  @Override
  public boolean mouseReleased(MouseButtonEvent e) {
    dragging = false;
    return super.mouseReleased(e);
  }

  @Override
  public void removed() {
    settingsOpened = false;
    DREnhanced.saveModuleSettings();
  }

  @Override
  public boolean isPauseScreen() {
    return false;
  }
}
