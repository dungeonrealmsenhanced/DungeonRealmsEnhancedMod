package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced;

import com.google.gson.*;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.player.DRPlayer;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.setting.Settings;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.world.Location;

import java.lang.reflect.Type;

/**
 * Created by Matthew E on 8/7/2019 at 9:13 AM for the project DungeonRealmsDREnhanced
 */
public class DRPlayerJsonAdapter implements JsonSerializer<DRPlayer>, JsonDeserializer<DRPlayer> {
    @Override
    public DRPlayer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        return null;
    }

    @Override
    public JsonElement serialize(DRPlayer drPlayer, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("uuid", drPlayer.getUuid().toString());
        jsonObject.add("statistics", DREnhanced.gsonBuilder.create().toJsonTree(drPlayer.getStatistics()));
        jsonObject.addProperty("lastTier", drPlayer.getLastTier().toString());

        Location location = drPlayer.getLocation();
        String locationString = location.getX() + "," + location.getY() + "," + location.getZ() + "," + location.getYaw() + "," + location.getPitch();
        jsonObject.addProperty("location", locationString);

        JsonObject settingsJsonObject = new JsonObject();

        Settings settings = drPlayer.getSettings();
//
//        for (DRSettingCategory value : DRSettingCategory.values()) {
//            if (!settings.getCategoryMap().containsKey(value)) {
//                value.g
//            }
//        }
        return jsonObject;
    }
}
