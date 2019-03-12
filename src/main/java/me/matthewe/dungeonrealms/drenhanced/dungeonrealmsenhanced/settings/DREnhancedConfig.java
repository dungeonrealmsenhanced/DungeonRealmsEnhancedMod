package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.player.DRPlayer;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.Module;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.Modules;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Matthew E on 12/31/2018 at 3:51 PM for the project DungeonRealmsDREnhanced
 */
public class DREnhancedConfig {
    public Map<String, Integer[]> modules;
    public DRPlayer drPlayer = DRPlayer.get();

    public DREnhancedConfig(Map<String, Integer[]> modules, DRPlayer drPlayer) {
        this.modules = modules;
        this.drPlayer = drPlayer;
    }

    public DREnhancedConfig(DREnhancedConfig drEnhancedConfig) {
        this(drEnhancedConfig.modules, drEnhancedConfig.drPlayer);
    }

    public DREnhancedConfig() {
        this(getDefaultConfig());
    }

    public Integer[] getCoords(String module) {
        return modules.get(module);
    }

    public void setCoords(String module, int x, int y, boolean status) {
        if (modules.containsKey(module)) {
            modules.remove(module);
        }
        modules.put(module, new Integer[]{x, y, status ? 1 : 0});
    }


    public void setDrPlayer(DRPlayer drPlayer) {
        this.drPlayer = drPlayer;
    }

    public DRPlayer getDrPlayer() {
        return drPlayer;
    }

    private static DREnhancedConfig getDefaultConfig() {
        Map<String, Integer[]> defaultModules = new ConcurrentHashMap<>();
        for (Module module : Modules.getModules()) {
            defaultModules.put(module.getName(), new Integer[]{module.defaultPosX, module.defaultPosY, module.isEnabled() ? 1 : 0});
        }
        return new DREnhancedConfig(defaultModules, DRPlayer.get());
    }
}

