package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.setting;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.player.DRPlayer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Matthew E on 3/10/2019 at 11:32 AM for the project DungeonRealmsDREnhanced
 */
public class Settings {
    private Map<DRSettingCategory, SettingCategory> categoryMap = new ConcurrentHashMap<>();

    public Settings(Map<DRSettingCategory, SettingCategory> categoryMap) {
        this.categoryMap = categoryMap;

        for (DRSettingCategory value : DRSettingCategory.values()) {
            if (value.hasSubCategories()){
                continue;
            }
            if (!categoryMap.containsKey(value)) {
                categoryMap.put(value, new SettingCategory(value));
            }
        }
    }

    public Map<DRSettingCategory, SettingCategory> getCategoryMap() {
        return categoryMap;
    }

    public void setCategoryMap(Map<DRSettingCategory, SettingCategory> categoryMap) {
        this.categoryMap = categoryMap;
    }

    public static Settings get() {
        return DRPlayer.get().getSettings();
    }

    public Settings() {
        loadDefault();
    }

    public void loadDefault() {
        this.categoryMap = new ConcurrentHashMap<>();
        for (DRSettingCategory value : DRSettingCategory.values()) {
            if (value.hasSubCategories()){
                continue;
            }
            categoryMap.put(value, new SettingCategory(value));
        }
    }

    public SettingCategory getCategory(DRSettingCategory category) {
        if (categoryMap.containsKey(category)) {
            return categoryMap.get(category);
        }
        categoryMap.put(category, new SettingCategory(category));
        return categoryMap.get(category);
    }


}
