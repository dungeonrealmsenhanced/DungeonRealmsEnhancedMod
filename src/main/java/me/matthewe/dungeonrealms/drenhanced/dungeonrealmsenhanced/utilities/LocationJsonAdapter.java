package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities;

import com.google.gson.*;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.world.Location;

import java.lang.reflect.Type;

/**
 * Created by Matthew E on 8/7/2019 at 9:21 AM for the project DungeonRealmsDREnhanced
 */
public class LocationJsonAdapter implements JsonSerializer<Location>, JsonDeserializer<Location> {
    @Override
    public Location deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        double x = 0;
        double y = 0;
        double z = 0;
        float yaw = 0;
        float pitch = 0;
        if (json.isJsonObject()) {

            JsonObject jsonObject = json.getAsJsonObject();
            x = jsonObject.has("x") ? jsonObject.get("x").getAsDouble() : 0.0D;
            y = jsonObject.has("y") ? jsonObject.get("y").getAsDouble() : 0.0D;
            z = jsonObject.has("z") ? jsonObject.get("z").getAsDouble() : 0.0D;
            yaw = jsonObject.has("yaw") ? jsonObject.get("yaw").getAsFloat() : 0.0F;
            pitch = jsonObject.has("pitch") ? jsonObject.get("pitch").getAsFloat() : 0.0F;
        } else if (json.isJsonPrimitive()) {
            String asString = json.getAsString();
            String[] strings = asString.trim().replaceAll(",", ":").split(":");
            x = Double.parseDouble(strings[0].trim());
            y = Double.parseDouble(strings[1].trim());
            z = Double.parseDouble(strings[2].trim());
            yaw = Float.parseFloat(strings[3].trim());
            pitch = Float.parseFloat(strings[4].trim());
        }
        return new Location(x, y, z, yaw, pitch);
    }

    @Override
    public JsonElement serialize(Location location, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(location.getX() + "," + location.getY() + "," + location.getZ() + "," + location.getYaw() + "," + location.getPitch());
    }
}
