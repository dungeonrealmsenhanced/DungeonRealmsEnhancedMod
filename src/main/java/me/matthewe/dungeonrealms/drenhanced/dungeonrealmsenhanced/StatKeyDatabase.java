package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.IntRange;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.ItemRarity;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.Tier;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.restful.HttpUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StatKeyDatabase {
    private Map<String, String> keyDictionary;
    private Map<String, String> keyDictionaryReverse;
    private ExecutorService executorService = Executors.newCachedThreadPool();
    private boolean complete = false;
    private boolean runningDataUpdate = false;
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private Map<Tier, TierValue> tierValues;

    public String getKeyFromValue(String elemental) {
        return keyDictionaryReverse.getOrDefault(elemental, elemental);
    }


    public static class TierValue {
        private Tier tier;


        private Map<String, IntRange> orbValues;
        private Map<ItemRarity, RarityValue> rarityValues;

        public TierValue(Tier tier) {
            this.tier = tier;
            this.rarityValues = new HashMap<>();
            this.orbValues = new HashMap<>();
        }


        @Override
        public String toString() {
            return tier +" {"+rarityValues+"}";
        }

        public void addRarityValue(RarityValue rarityValue) {
            rarityValues.put(rarityValue.rarity, rarityValue);

        }

        public RarityValue editRarityValue(ItemRarity rarity) {
            if (!rarityValues.containsKey(rarity)) {
                rarityValues.put(rarity, new RarityValue(rarity));
            }
            return rarityValues.get(rarity);
        }

        public static class RarityValue {
            private ItemRarity rarity;
            private IntRange elementals;

            private IntRange damageMin;
            private IntRange damageMax;

            public RarityValue(ItemRarity rarity) {
                this.rarity = rarity;
            }

            @Override
            public String toString() {
                return rarity+"_"+elementals.toString();
            }
        }
    }
    public StatKeyDatabase() {
        keyDictionary = new ConcurrentHashMap<>();
        keyDictionaryReverse = new ConcurrentHashMap<>();
    }


    public void loadDictionary() {
        //TODO SEND RESTFUL REQUEST
        updateData();




    }

    public static void main(String[] args) {
        StatKeyDatabase statKeyDatabase = new StatKeyDatabase();
        statKeyDatabase.loadDictionary();

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

                update(data);
                completeDataUpdate();
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
        tierValues= new HashMap<>();



        updateMappings(data.getAsJsonArray("mappings.csv"));
        updateElementals(data.getAsJsonArray("int_str_dex_vit.csv"));
        updateOrbValues(data.getAsJsonArray("orb_values.csv"));
        System.out.println(tierValues);
        System.out.println(this.keyDictionary);

        System.out.println("RARE T5 VIT: " + getElemental(Tier.T5,ItemRarity.RARE));
    }
    private void updateOrbValues(JsonArray array) {
        for (int i = 1; i < array.size(); i++) {
            JsonArray entry = array.get(i).getAsJsonArray();
            if (entry.get(0).getAsString().equalsIgnoreCase("stat"))continue;


            String stat = entry.get(0).getAsString();

            Tier tier = Tier.getByNumber(Integer.parseInt(entry.get(1).getAsString()));
            int min =Integer.parseInt( entry.get(2).getAsString());
            int max = Integer.parseInt(entry.get(3).getAsString());

            if (!tierValues.containsKey(tier)) {
                TierValue tierValue = new TierValue(tier);
                tierValues.put(tier,tierValue);
            }

            TierValue tierValue = tierValues.get(tier);

            tierValue.orbValues.put(stat, new IntRange(min, max));
        }
    }

    public IntRange getElementalValue(Tier tier, String stat) {
        if (tierValues.containsKey(tier)) {
            return tierValues.get(tier).orbValues.getOrDefault(stat, null);
        }
        return null;
    }
    private void updateElementals(JsonArray array) {
        for (int i = 1; i < array.size(); i++) {
            JsonArray entry = array.get(i).getAsJsonArray();
            String key = entry.get(0).getAsString();
            if (key.equalsIgnoreCase("tier"))continue;


            Tier tier = Tier.getByNumber(Integer.parseInt(key));

            ItemRarity rarity = ItemRarity.getByName(entry.get(1).getAsString());
            int min =Integer.parseInt( entry.get(2).getAsString());
            int max = Integer.parseInt(entry.get(3).getAsString());

            if (!tierValues.containsKey(tier)) {
                TierValue tierValue = new TierValue(tier);
                tierValues.put(tier,tierValue);
            }

            TierValue tierValue = tierValues.get(tier);

            TierValue.RarityValue rarityValue = tierValue.editRarityValue(rarity);
            rarityValue.elementals = new IntRange(min,max);
        }
    }

    public IntRange getElemental(Tier tier, ItemRarity rarity) {
        if (tierValues.containsKey(tier)) {
            TierValue tierValue = tierValues.get(tier);
            if (tierValue.rarityValues.containsKey(rarity)){
                return tierValue.rarityValues.get(rarity).elementals;
            }
        }
        return null;
    }

    private void updateMappings(JsonArray array) {
        keyDictionary.clear();
        keyDictionaryReverse.clear();
        for (int i = 1; i < array.size(); i++) {
            JsonArray entry = array.get(i).getAsJsonArray();
            String key = entry.get(0).getAsString();
            if (key.equalsIgnoreCase("Key"))continue;
            String value = entry.get(1).getAsString();
            keyDictionary.put(key, value);
            keyDictionaryReverse.put(value,key);
        }
    }
}
