package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.restful.change;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Created by Matthew E on 3/17/2019 at 7:55 PM for the project drenhancedrestfulapi
 */
public class ChangelogJsonSerializer implements JsonSerializer<Changelog> {
    @Override
    public JsonElement serialize(Changelog changelog, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("version", changelog.getVersion());
        jsonObject.addProperty("date", changelog.getDate());


        JsonObject changes = new JsonObject();
        for (Change change : changelog.getChanges()) {
            JsonArray descriptionArray = new JsonArray();
            for (String s : change.getDescription()) {
                descriptionArray.add(s);
            }
            changes.add(change.getTitle(), descriptionArray);
        }
        jsonObject.add("changes", changes);

        return jsonObject;
    }
}
