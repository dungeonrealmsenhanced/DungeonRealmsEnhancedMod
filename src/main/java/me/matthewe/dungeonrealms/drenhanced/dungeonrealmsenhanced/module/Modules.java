package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module;


import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.DREnhanced;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.modules.CoordsModule;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.modules.treasurescroll.TreasureScrollModule;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthew E on 12/31/2018 at 4:06 PM for the project DungeonRealmsDREnhanced
 */
public class Modules {
    private static List<Module> modules = new ArrayList<>();

    public static void addModule(Module module) {
        modules.add(module);
    }

    public static <T extends Module> T get(Class<T> clazz) {
        for (Module module : modules) {
            if (module.getClass().equals(clazz)) {
                return (T) module;
            }
        }
        return null;
    }

    public static List<Module> getModules() {
        return modules;
    }

    public static void init() {
        addModule(new CoordsModule());
//        addModule(new CPSModule());
        addModule(new TreasureScrollModule());
//        addModule(new ProfessionModule());
    }

    public static void loadModules() {
        modules.stream().filter(module -> !module.isLoaded()).forEach(Module::load);
        for (Module module : modules) {
            module.loadSettings();
        }
    }

    public static void registerListeners() {
        modules.forEach(module -> module.getListeners().forEach(listener -> {
            MinecraftForge.EVENT_BUS.register(listener);
            DREnhanced.log("[" + module.getName() + "] Registered listener " + listener.getClass().getSimpleName() + ".");
        }));
    }

    public static void unloadModules() {
        modules.stream().filter(Module::isLoaded).forEach(Module::unload);
    }
}
