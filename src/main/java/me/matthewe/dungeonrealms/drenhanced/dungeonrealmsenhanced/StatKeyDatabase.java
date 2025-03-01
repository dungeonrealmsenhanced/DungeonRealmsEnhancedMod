package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced;

import clutch.dungeonrealms.ItemAttributes;

import java.util.Map;

public class StatKeyDatabase {
    private Map<String, String> dictionary;

    public StatKeyDatabase() {
    }

    public static void main(String[] args) {
        new ItemAttributes().get().forEach((key, value) -> {
           System.out.println(key + "\t" + value.getTooltipName());
        });
    }
    public void loadDictionary() {
        //TODO SEND RESTFUL REQUEST

    }

    public Map<String, String> getDictionary() {
        return dictionary;
    }
}
