package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.setting;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Matthew E on 3/10/2019 at 11:32 AM for the project DungeonRealmsDREnhanced
 */
public class SettingCategory {
    private DRSettingCategory category;
    private Map<DRSettings, Object> valueMap = new ConcurrentHashMap<>();

    public SettingCategory(DRSettingCategory category, Map<DRSettings, Object> valueMap) {
        this.category = category;
        this.valueMap = valueMap;
        for (DRSettings value : DRSettings.values()) {
            if (value.getCategory() == category && !valueMap.containsKey(value)) {
                valueMap.put(value, value.getValue());
            }
        }
    }

    public SettingCategory(DRSettingCategory category) {
        this.category = category;
        this.loadDefault();
    }

    public <T extends Object> T getSettingValue(DRSettings settings, Class<T> clazz) {
        if (valueMap.containsKey(settings)) {
            return (T) valueMap.get(settings);
        }
        valueMap.put(settings, settings.getValue());
        return (T) valueMap.get(settings);
    }

    private void loadDefault() {
        this.valueMap = new ConcurrentHashMap<>();
        for (DRSettings value : DRSettings.values()) {
            if (value.getCategory() == category) {
                valueMap.put(value, value.getValue());
            }
        }
    }

    public DRSettingCategory getCategory() {
        return category;
    }

    public Map<DRSettings, Object> getValueMap() {
        return valueMap;
    }

    public void setSettingValue(DRSettings key, Object o) {
        if (valueMap.containsKey(key)) {
            valueMap.remove(key);
        }
        valueMap.put(key, o);
    }
}
