package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced;


import java.util.HashMap;
import java.util.Map;

public class StatKeyDatabase {
    private Map<String, String> keyDictionary;

    public StatKeyDatabase() {
        keyDictionary = new HashMap<String, String>();
    }


    public void loadDictionary() {
        //TODO SEND RESTFUL REQUEST

    }

    public String formatKey(String key) {
        return keyDictionary.getOrDefault(key, key);
    }

    public Map<String, String> getKeyDictionary() {
        return keyDictionary;
    }
}
