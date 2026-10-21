package me.matthewe.drenhanced.smoke;

import java.nio.file.*;
import java.util.*;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.DREnhanced;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.restful.guis.ChangelogGui;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.Modules;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.GuiSettings;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.setting.DRSettingCategory;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.setting.gui.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.common.NeoForge;

/** Runs only with -PclientSmokeTest, using the isolated run-smoke directory. */
@Mod(value = "drenhanced_smoke", dist = Dist.CLIENT)
public final class ClientSmokeTest {
  private final List<String> results = new ArrayList<>();
  private final List<Screen> screens = new ArrayList<>();
  private int ticks, stage = -1, frames;
  private boolean finished;

  public ClientSmokeTest() {
    NeoForge.EVENT_BUS.register(this);
  }

  private void check(boolean ok, String name) {
    if (!ok) throw new AssertionError(name);
    results.add("PASS " + name);
  }

  private void finish(Throwable error) {
    if (finished) return;
    finished = true;
    if (error != null) {
      results.add("FAIL " + error);
      error.printStackTrace();
    }
    try {
      Files.write(FMLPaths.GAMEDIR.get().resolve("smoke-results.txt"), results);
    } catch (Exception e) {
      e.printStackTrace();
    }
    Minecraft.getInstance().stop();
  }

  @SubscribeEvent
  public void frame(ScreenEvent.Render.Post e) {
    if (stage >= 0 && stage < screens.size() && e.getScreen() == screens.get(stage)) frames++;
  }

  @SubscribeEvent
  public void tick(ClientTickEvent.Post event) {
    if (finished) return;
    if (++ticks > 2400) {
      finish(new AssertionError("Client smoke test timed out"));
      return;
    }
    var mc = Minecraft.getInstance();
    try {
      if (stage < 0) {
        if (ticks < 60 || mc.gui.screen() == null || DREnhanced.INSTANCE == null) return;
        check(DREnhanced.class.getResource("DREnhanced.class").toString().contains(".jar"),
            "production classes loaded from packaged JAR");
        check(Modules.getModules().size() == 5, "five active HUD modules initialized");
        check(
            !DREnhanced.statKeyDatabase.getKeyDictionary().isEmpty(),
            "bundled stat tables loaded without REST");
        var folder = Path.of(DREnhanced.INSTANCE.folderLocation);
        Files.createDirectories(folder);
        String legacy = "{\"modules\":{\"Coords\":[80,50,1]}}";
        Files.deleteIfExists(folder.resolve("module_save-26.3.json"));
        Files.writeString(folder.resolve("module_save.json"), legacy);
        DREnhanced.loadModuleSettings();
        var coords = Modules.getModules().getFirst();
        check(coords.posX == 80 && coords.isEnabled(), "legacy enable state uses third value");
        coords.posX = 1;
        coords.setEnabled(false);
        DREnhanced.saveModuleSettings();
        coords.setEnabled(true);
        DREnhanced.loadModuleSettings();
        check(!coords.isEnabled(), "modern save reload keeps disabled module at x=1 disabled");
        check(
            Files.readString(folder.resolve("module_save.json")).equals(legacy),
            "legacy settings file remains unchanged");
        for (var module : Modules.getModules()) {
          module.posX = module.defaultPosX;
          module.posY = module.defaultPosY;
        }
        screens.add(new GuiDRSettingsCategory());
        for (var category : DRSettingCategory.values())
          if (!category.hasSubCategories()) screens.add(new GuiDRSettings(category));
        screens.add(new GuiSettings());
        screens.add(new ChangelogGui());
        stage = 0;
        frames = 0;
        mc.gui.setScreen(screens.getFirst());
        return;
      }
      if (frames >= 12) {
        check(true, "rendered " + screens.get(stage).getTitle().getString());
        if (stage == 0 || stage == screens.size() - 2) Screenshot.grab(mc, false);
        if (++stage >= screens.size()) {
          finish(null);
          return;
        }
        frames = 0;
        mc.gui.setScreen(screens.get(stage));
      }
    } catch (Throwable error) {
      finish(error);
    }
  }
}
