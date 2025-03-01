package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.restful.HttpUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StatKeyDatabase {
    private Map<String, String> keyDictionary;
    private ExecutorService executorService = Executors.newCachedThreadPool();
    private boolean complete = false;
    private boolean runningDataUpdate = false;
    private Gson gson = DREnhanced.gsonBuilder.setPrettyPrinting().create();


    public StatKeyDatabase() {
        keyDictionary = new ConcurrentHashMap<>();
    }


    public void loadDictionary() {
        //TODO SEND RESTFUL REQUEST
        updateData();

    }


    public void updateData() {
        if (complete) return;
        if (runningDataUpdate) return;

        runningDataUpdate = true;
        executorService.submit(() -> {
            try {
                String content = HttpUtils.getStringFromUrl("http://134.122.26.146:8105/data/all");
                JsonObject jsonObject = gson.fromJson(content, JsonObject.class);
                if (jsonObject == null) {
                    failDataUpdate();
                    return;
                }

                if (jsonObject.has("error")) {
                    failDataUpdate();
                    return;
                }
                JsonObject data = jsonObject.get("data").getAsJsonObject();

                DREnhanced.getStatKeyDatabase().update(data);
            } catch (Exception e) {
                e.printStackTrace();
                failDataUpdate();
            }
        });


    }

    private void failDataUpdate() {
        runningDataUpdate = false;
    }

    private void completeDataUpdate() {
        runningDataUpdate = false;
        complete = true;
    }


    public String formatKey(String key) {
        return keyDictionary.getOrDefault(key, key);
    }

    public Map<String, String> getKeyDictionary() {
        return keyDictionary;
    }

    public void update(JsonObject data) {
        updateMappings(data.getAsJsonArray("mappings.csv"));
    }

    private void updateMappings(JsonArray array) {
        keyDictionary.clear();
        for (int i = 1; i < array.size(); i++) {
            JsonArray entry = array.get(i).getAsJsonArray();
            String key = entry.get(0).getAsString();
            String value = entry.get(1).getAsString();
            keyDictionary.put(key, value);
        }
    }
}
