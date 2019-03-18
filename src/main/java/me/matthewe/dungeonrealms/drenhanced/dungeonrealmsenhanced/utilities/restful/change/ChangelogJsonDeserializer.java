package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.restful.change;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Matthew E on 3/17/2019 at 7:55 PM for the project drenhancedrestfulapi
 */
public class ChangelogJsonDeserializer implements JsonDeserializer<Changelog> {
    @Override
    public Changelog deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = (JsonObject) json;
        String version = jsonObject.get("version").getAsString();
        long date = jsonObject.get("date").getAsLong();

        List<Change> changes = new ArrayList<>();


        for (Map.Entry<String, JsonElement> entry : jsonObject.getAsJsonObject("changes").entrySet()) {
            JsonArray changeDescription = (JsonArray) entry.getValue();

            List<String> descriptionStringList = new ArrayList<>();
            for (JsonElement jsonElement : changeDescription) {
                descriptionStringList.add(jsonElement.getAsString());
            }

            changes.add(new Change(entry.getKey(), descriptionStringList.toArray(new String[0])));
        }
        return new Changelog(version, date, changes);

    }
}
