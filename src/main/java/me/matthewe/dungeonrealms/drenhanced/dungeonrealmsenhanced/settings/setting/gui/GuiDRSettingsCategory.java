package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.setting.gui;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.setting.DRSettingCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class GuiDRSettingsCategory extends Screen {
  public GuiDRSettingsCategory() {
    super(Component.literal("DREnhanced Settings"));
  }

  public void display() {
    Minecraft.getInstance().gui.setScreen(this);
  }

  @Override
  protected void init() {
    var categories =
        java.util.Arrays.stream(DRSettingCategory.values())
            .filter(c -> !c.hasSubCategories())
            .toList();
    int columns = width < 420 ? 2 : 3;
    int w = Math.min(150, (width - 24) / columns);
    for (int i = 0; i < categories.size(); i++) {
      var category = categories.get(i);
      addRenderableWidget(
          Button.builder(
                  Component.literal(category.getName()),
                  b -> minecraft.gui.setScreen(new GuiDRSettings(category)))
              .bounds(
                  width / 2 - columns * w / 2 + (i % columns) * w,
                  35 + (i / columns) * 24,
                  w - 4,
                  20)
              .build());
    }
    addRenderableWidget(
        Button.builder(Component.literal("Done"), b -> onClose())
            .bounds(width / 2 - 60, height - 27, 120, 20)
            .build());
  }

  @Override
  public void extractRenderState(GuiGraphicsExtractor g, int x, int y, float delta) {
    super.extractRenderState(g, x, y, delta);
    g.centeredText(font, title, width / 2, 12, 0xffffffff);
  }

  @Override
  public boolean isPauseScreen() {
    return false;
  }
}
