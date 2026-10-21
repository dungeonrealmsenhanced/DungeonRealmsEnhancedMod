package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced;

import com.google.gson.GsonBuilder;
import java.io.*;
import java.nio.file.*;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.Module;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.Modules;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.player.DRPlayer;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.proxy.ClientProxy;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.DREnhancedConfig;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.setting.Settings;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.LocationJsonAdapter;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.json.SettingsJsonAdapter;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.restful.change.*;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.world.Location;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLPaths;

@Mod(value = DREnhanced.MOD_ID, dist = Dist.CLIENT)
public class DREnhanced {
  public static final String MOD_ID = "dungeonrealmsenhanced";
  public static final String MOD_NAME = "DREnhanced";
  public static final String VERSION = "2.0.0+mc26.3";
  public static DREnhanced INSTANCE;
  public String folderLocation;
  public static final GsonBuilder gsonBuilder =
      new GsonBuilder()
          .setPrettyPrinting()
          .disableHtmlEscaping()
          .registerTypeAdapter(Changelog.class, new ChangelogJsonSerializer())
          .registerTypeAdapter(Changelog.class, new ChangelogJsonDeserializer())
          .registerTypeAdapter(Location.class, new LocationJsonAdapter())
          .registerTypeAdapter(Settings.class, new SettingsJsonAdapter());
  public static final StatKeyDatabase statKeyDatabase = new StatKeyDatabase();
  public static final String[] DEVELOPERS = {
    "1d48bd80-4cd0-4874-ba65-94284bc24ecc", "56a838cd-afd8-4e30-a34f-822e936eb949"
  };
  private static boolean initialized;

  public DREnhanced(IEventBus bus) {
    INSTANCE = this;
    folderLocation = FMLPaths.GAMEDIR.get().resolve(MOD_ID).toString() + File.separator;
    bus.addListener(this::setup);
    ClientProxy.registerModEvents(bus);
  }

  private void setup(FMLClientSetupEvent event) {
    event.enqueueWork(
        () -> {
          try {
            Files.createDirectories(Path.of(folderLocation));
          } catch (IOException e) {
            throw new UncheckedIOException(e);
          }
          ClientProxy.initialize();
          initialized = true;
          log("Loaded %s for Minecraft 26.3 / NeoForge", VERSION);
        });
  }

  public static StatKeyDatabase getStatKeyDatabase() {
    return statKeyDatabase;
  }

  public static boolean isDeveloper() {
    var player = Minecraft.getInstance().player;
    if (player == null) return false;
    for (String id : DEVELOPERS) if (id.equalsIgnoreCase(player.getUUID().toString())) return true;
    return false;
  }

  public static void loadModuleSettings() {
    Path current = Path.of(INSTANCE.folderLocation, "module_save-26.3.json");
    Path legacy = Path.of(INSTANCE.folderLocation, "module_save.json");
    Path source = Files.exists(current) ? current : legacy;
    if (!Files.exists(source)) return;
    try (Reader reader = Files.newBufferedReader(source)) {
      DREnhancedConfig config = gsonBuilder.create().fromJson(reader, DREnhancedConfig.class);
      if (config == null) return;
      if (config.modules != null)
        for (Module module : Modules.getModules()) {
          var placement =
              me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.ModulePlacement
                  .decode(config.getCoords(module.getName()));
          if (placement == null) continue;
          module.posX = placement.x();
          module.posY = placement.y();
          module.setEnabled(placement.enabled());
        }
      if (config.drPlayer != null) DRPlayer.drPlayer = config.drPlayer;
      DRPlayer.get().setLoaded(false);
    } catch (Exception e) {
      log("Could not read %s: %s", source, e.toString());
      if (source.equals(current)) {
        try {
          Files.copy(
              source,
              source.resolveSibling(
                  "module_save-26.3.invalid-" + System.currentTimeMillis() + ".json"));
        } catch (IOException backupError) {
          throw new UncheckedIOException(backupError);
        }
      }
    }
  }

  public static void saveModuleSettings() {
    if (!initialized) return;
    Path target = Path.of(INSTANCE.folderLocation, "module_save-26.3.json");
    Path temp = target.resolveSibling("module_save-26.3.json.tmp");
    DREnhancedConfig config = new DREnhancedConfig();
    for (Module module : Modules.getModules())
      config.setCoords(module.getName(), module.posX, module.posY, module.isEnabled());
    try {
      Files.createDirectories(target.getParent());
      Files.writeString(temp, gsonBuilder.create().toJson(config));
      try {
        Files.move(
            temp, target, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
      } catch (AtomicMoveNotSupportedException e) {
        Files.move(temp, target, StandardCopyOption.REPLACE_EXISTING);
      }
    } catch (IOException e) {
      log("Could not save settings: %s", e.toString());
    }
  }

  public static void log(String format, Object... args) {
    org.slf4j.LoggerFactory.getLogger(MOD_NAME).info(String.format(format, args));
  }
}
