package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.restful;


import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class DREnhancedRestful {
    private DREnhancedInformation information;

    public DREnhancedRestful() {
    }

    public DREnhancedInformation getInformation() {
        return information;
    }

    public void update(Consumer<DREnhancedInformation> consumer) {

        (new Thread(() -> {
            try {
                String content = HttpUtils.getStringFromUrl("http://202.5.31.117:4569/drenhanced/info");
                if (content == null) {
                    consumer.accept(null);
                    return;
                }
                JsonObject jsonObject = null;
                try {
                    jsonObject = new GsonBuilder().setPrettyPrinting().create().fromJson(content, JsonObject.class);

                } catch (Exception e) {
                    consumer.accept(null);
                    return;
                }
                if (jsonObject == null) {
                    consumer.accept(null);
                    return;
                }
                String source = jsonObject.has("source") ? jsonObject.get("source").getAsString() : null;
                String version = jsonObject.has("version") ? jsonObject.get("version").getAsString() : null;

                List<DREnhancedInformation.Developer> developers = new ArrayList<>();
                if (jsonObject.has("developers")) {

                    for (JsonElement jsonElement : jsonObject.get("developers").getAsJsonArray()) {

                        JsonObject developerJsonObject = (JsonObject) jsonElement;

                        String uuid = developerJsonObject.has("uuid") ? developerJsonObject.get("uuid").getAsString() : null;
                        String username = developerJsonObject.has("username") ? developerJsonObject.get("username").getAsString() : null;
                        boolean mainDeveloper = developerJsonObject.has("mainDeveloper") && developerJsonObject.get("mainDeveloper").getAsBoolean();
                        if ((uuid != null) && (username != null)) {
                            developers.add(new DREnhancedInformation.Developer(uuid, username, mainDeveloper));
                        }
                    }
                }
                if ((source != null) && (version != null)) {
                    information = new DREnhancedInformation(version, developers.toArray(new DREnhancedInformation.Developer[0]), source);
                    System.out.println("Updated information.");
                    consumer.accept(information);
                }
            } catch (Exception var9) {
                var9.printStackTrace();
                consumer.accept(null);
            }
        })).start();

    }
}
