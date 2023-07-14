package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.json;

import com.google.gson.*;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.DREnhanced;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.setting.DRSettingCategory;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.setting.DRSettings;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.setting.SettingCategory;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.setting.Settings;
import org.apache.logging.log4j.LogManager;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Matthew E on 8/7/2019 at 9:45 AM for the project DungeonRealmsDREnhanced
 */
public class SettingsJsonAdapter implements JsonDeserializer<Settings>, JsonSerializer<Settings> {
    @Override
    public Settings deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        if (jsonObject.has("categoryMap")) {
            Map<DRSettingCategory, SettingCategory> categoryMap = new ConcurrentHashMap<>();
            for (Map.Entry<String, JsonElement> entry : jsonObject.get("categoryMap").getAsJsonObject().entrySet()) {
                String key = entry.getKey();
                Map<DRSettings, Object> newValueMap = new ConcurrentHashMap<>();
                DRSettingCategory byString = DRSettingCategory.getByString(key);
                if (byString != null) {

                    LogManager.getLogger().debug(entry.getValue().toString());
                    JsonObject asJsonObject = entry.getValue().getAsJsonObject();
                    if (asJsonObject.has("valueMap")) {
                        JsonElement valueMap = asJsonObject.get("valueMap");
                        if (valueMap.isJsonObject()) {
                            LogManager.getLogger().debug(entry.getKey() + " has value map");
                            Set<Map.Entry<String, JsonElement>> entries = valueMap.getAsJsonObject().entrySet();
                            if (entries != null) {
                                for (Map.Entry<String, JsonElement> entry1 : entries) {
                                    DRSettings byString1 = DRSettings.getByString(entry1.getKey());

                                    JsonElement value1 = entry1.getValue();
                                    System.out.println(value1.toString());
                                    if (byString1 != null && value1.isJsonPrimitive()) {

                                        Object value = DREnhanced.gsonBuilder.create().fromJson(value1, byString1.getClazz());
                                        LogManager.getLogger().debug(byString1.toString() + ":" + value);
                                        newValueMap.put(byString1, value);
                                    }
                                }
                            }
                        }
                    }
                    categoryMap.put(byString, new SettingCategory(byString, newValueMap));

                }
            }
            return new Settings(categoryMap);
        }
        Map<DRSettingCategory, SettingCategory> categoryMap = new ConcurrentHashMap<>();

        Settings settings;
        for (DRSettingCategory category : DRSettingCategory.values()) {
            if (jsonObject.has(category.toString())) {
                JsonObject categoryObject = jsonObject.get(category.toString()).getAsJsonObject();
                SettingCategory settingCategory = new SettingCategory(category);
                Map<DRSettings, Object> newValueMap = new ConcurrentHashMap<>();
                for (Map.Entry<DRSettings, Object> entry : settingCategory.getValueMap().entrySet()) {
                    if (categoryObject.has(entry.getKey().toString())) {
                        JsonElement jsonElement = categoryObject.get(entry.getKey().toString());
                        Object value = DREnhanced.gsonBuilder.create().fromJson(jsonElement, entry.getValue().getClass());
                        newValueMap.put(entry.getKey(), value);
                    } else {
                        newValueMap.put(entry.getKey(), entry.getValue());

                    }
                }
                categoryMap.put(category, new SettingCategory(category, newValueMap));
            } else {
                categoryMap.put(category, new SettingCategory(category));

            }
        }
        settings = new Settings(categoryMap);

        return settings;
    }

    @Override
    public JsonElement serialize(Settings settings, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        for (DRSettingCategory category : DRSettingCategory.values()) {
            JsonObject jsonObject1 = new JsonObject();
            SettingCategory category1 = settings.getCategory(category);
            for (Map.Entry<DRSettings, Object> entry : category1.getValueMap().entrySet()) {
                jsonObject1.add(entry.getKey().toString(), DREnhanced.gsonBuilder.create().toJsonTree(entry.getValue()));
            }
            jsonObject.add(category.toString(), jsonObject1);
        }
        return jsonObject;
    }
}
