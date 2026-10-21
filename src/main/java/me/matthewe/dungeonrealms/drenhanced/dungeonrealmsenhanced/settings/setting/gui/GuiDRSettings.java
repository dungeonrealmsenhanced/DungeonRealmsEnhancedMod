package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.setting.gui;

import java.util.*;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.DREnhanced;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.player.DRPlayer;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.setting.*;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class GuiDRSettings extends Screen {
  private final DRSettingCategory category;
  private final Map<DRSettings, EditBox> edits = new LinkedHashMap<>();
  private int page;
  private int rows;
  private String error = "";

  public GuiDRSettings(DRSettingCategory c) {
    super(Component.literal(c.getName()));
    category = c;
  }

  public void display() {
    net.minecraft.client.Minecraft.getInstance().gui.setScreen(this);
  }

  @Override
  protected void init() {
    edits.clear();
    var settings = DRSettings.getByCategory(category);
    rows = Math.max(1, (height - 95) / 27);
    page = Math.min(page, Math.max(0, (settings.size() - 1) / rows));
    int left = Math.max(10, width / 2 - 185);
    for (int i = page * rows; i < Math.min(settings.size(), (page + 1) * rows); i++) {
      var setting = settings.get(i);
      int y = 35 + (i % rows) * 27;
      if (setting.getClazz() == boolean.class) {
        addRenderableWidget(
            Button.builder(
                    label(setting),
                    b -> {
                      DRPlayer.get().setSettingValue(setting, !setting.get(boolean.class));
                      b.setMessage(label(setting));
                    })
                .bounds(left, y, Math.min(370, width - 20), 20)
                .tooltip(
                    net.minecraft.client.gui.components.Tooltip.create(
                        Component.literal(String.join(" ", setting.getDescription()))))
                .build());
      } else {
        var edit =
            new EditBox(
                font,
                width / 2 + 15,
                y,
                Math.min(150, width / 2 - 25),
                20,
                Component.literal(setting.getName()));
        edit.setMaxLength(150);
        edit.setValue(
            String.valueOf(
                Settings.get().getCategory(category).getSettingValue(setting, setting.getClazz())));
        edits.put(setting, edit);
        addRenderableWidget(edit);
      }
    }
    addRenderableWidget(
        Button.builder(
                Component.literal("<"),
                b -> {
                  if (save()) {
                    page = Math.max(0, page - 1);
                    rebuildWidgets();
                  }
                })
            .bounds(left, height - 28, 30, 20)
            .build());
    addRenderableWidget(
        Button.builder(
                Component.literal(">"),
                b -> {
                  if (save()) {
                    page = Math.min(Math.max(0, (settings.size() - 1) / rows), page + 1);
                    rebuildWidgets();
                  }
                })
            .bounds(left + 35, height - 28, 30, 20)
            .build());
    addRenderableWidget(
        Button.builder(Component.literal("Done"), b -> onClose())
            .bounds(width / 2 + 40, height - 28, 110, 20)
            .build());
  }

  private Component label(DRSettings s) {
    return Component.literal(s.getName() + ": " + (s.get(boolean.class) ? "ON" : "OFF"));
  }

  private boolean save() {
    Map<DRSettings, Object> parsed = new LinkedHashMap<>();
    try {
      for (var entry : edits.entrySet()) {
        var key = entry.getKey();
        String text = entry.getValue().getValue().trim();
        Class<?> type = key.getClazz();
        Object value;
        if (type == double.class) {
          double v = Double.parseDouble(text);
          if (!Double.isFinite(v)) throw new IllegalArgumentException();
          value = v;
        } else if (type == long.class) value = Long.parseLong(text);
        else if (type == int.class) value = Integer.parseInt(text);
        else {
          if (key == DRSettings.MISC_PERCENT_FORMAT) new java.text.DecimalFormat(text);
          value = text;
        }
        if (key == DRSettings.DEBUG_SPACING && ((Number) value).doubleValue() < 0)
          throw new IllegalArgumentException();
        if (key == DRSettings.DEBUG_SPACING && ((Number) value).doubleValue() > 80)
          throw new IllegalArgumentException();
        if (key == DRSettings.MISC_DELAY && ((Number) value).longValue() < 50)
          throw new IllegalArgumentException();
        parsed.put(key, value);
      }
    } catch (IllegalArgumentException e) {
      error = "Enter valid values (spacing 0-80; delay at least 50 ms).";
      return false;
    }
    parsed.forEach((k, v) -> DRPlayer.get().setSettingValue(k, v));
    DREnhanced.saveModuleSettings();
    error = "";
    return true;
  }

  @Override
  public void onClose() {
    if (save()) minecraft.gui.setScreen(new GuiDRSettingsCategory());
  }

  @Override
  public void extractRenderState(GuiGraphicsExtractor g, int x, int y, float delta) {
    super.extractRenderState(g, x, y, delta);
    g.centeredText(font, title, width / 2, 12, 0xffffffff);
    for (var e : edits.entrySet())
      g.text(
          font,
          e.getKey().getName(),
          Math.max(10, width / 2 - 185),
          e.getValue().getY() + 5,
          0xffffffff);
    if (!error.isEmpty()) g.centeredText(font, error, width / 2, height - 45, 0xffff5555);
  }

  @Override
  public boolean isPauseScreen() {
    return false;
  }
}
