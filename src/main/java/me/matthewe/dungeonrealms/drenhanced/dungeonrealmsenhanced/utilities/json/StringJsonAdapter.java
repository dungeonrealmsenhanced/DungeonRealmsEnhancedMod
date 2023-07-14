package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.json;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Created by Matthew E on 8/7/2019 at 10:35 AM for the project DungeonRealmsDREnhanced
 */
public class StringJsonAdapter implements JsonDeserializer<String>, JsonSerializer<String> {
    @Override
    public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String s = null;

        return json.getAsString();
    }

    @Override
    public JsonElement serialize(String src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(new String(src).replaceAll("\u0026", "&"));
    }
}
