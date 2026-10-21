package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.restful.guis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ChangelogGui extends Screen {
  public ChangelogGui() {
    super(Component.literal("DREnhanced 2.0.0 - Minecraft 26.3"));
  }

  public void display() {
    Minecraft.getInstance().gui.setScreen(this);
  }

  @Override
  protected void init() {
    addRenderableWidget(
        Button.builder(Component.literal("Done"), b -> onClose())
            .bounds(width / 2 - 60, height - 28, 120, 20)
            .build());
  }

  @Override
  public void extractRenderState(GuiGraphicsExtractor g, int x, int y, float delta) {
    super.extractRenderState(g, x, y, delta);
    g.centeredText(font, title, width / 2, 20, 0xffffffff);
    g.textWithWordWrap(
        font,
        Component.literal(
            "Updated from Forge 1.12.2 to NeoForge 26.3.\n\n"
                + "Modern HUD rendering, settings, key bindings, item data components and"
                + " tooltips.\n\n"
                + "Legacy settings are imported into a separate 26.3 settings file.\n\n"
                + "H: Settings   Numpad 5: HUD Editor\n"
                + "/drenhanced: Settings   /zone: Current zone"),
        Math.max(12, width / 2 - 180),
        55,
        Math.min(360, width - 24),
        0xffffffff);
  }

  @Override
  public boolean isPauseScreen() {
    return false;
  }
}
