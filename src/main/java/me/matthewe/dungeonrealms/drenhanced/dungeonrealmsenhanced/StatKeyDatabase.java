package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.IntRange;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.ItemRarity;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.Tier;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.restful.HttpUtils;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.world.DoubleRange;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    private Map<Tier, TierValue> tierValues =new HashMap<>();

    public String getKeyFromValue(String elemental) {
        return keyDictionaryReverse.getOrDefault(elemental, elemental);
    }

    public IntRange getOrbStat(String key, String value, Tier tier, ItemRarity rarity) {
        if (key.equalsIgnoreCase("HPS")) {
            return getHPS(tier,rarity);
        }
        if (tierValues.containsKey(tier)) {
            TierValue tierValue = tierValues.get(tier);
            return tierValue.orbValues.getOrDefault(key, null);
        }
        return null;
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
            public IntRange hps;
            private ItemRarity rarity;
            private Map<String, IntRange> orbValues;

            private WeaponValue weaponValue;
            private ArmorValue armorValue;

            public static class WeaponValue {
                private IntRange damageMin;
                private IntRange damageMax;

                public WeaponValue(IntRange damageMin, IntRange damageMax) {
                    this.damageMin = damageMin;
                    this.damageMax = damageMax;
                }

                @Override
                public String toString() {
                    return "WeaponValue{" +
                            "damageMin=" + damageMin +
                            ", damageMax=" + damageMax +
                            '}';
                }

                public IntRange getDamageMin() {
                    return damageMin;
                }

                public IntRange getDamageMax() {
                    return damageMax;
                }
            }

            public static class ArmorValue {
                private IntRange hp;
                private IntRange armorMin;
                private IntRange armorMax;
                private DoubleRange energy;
                private IntRange dmgReductionMin;
                private IntRange dmgReductionMax;

                public DoubleRange getEnergy() {
                    return energy;
                }

                public IntRange getHp() {
                    return hp;
                }

                public ArmorValue(IntRange hp, IntRange armorMin, IntRange armorMax, DoubleRange energy, IntRange dmgReductionMin, IntRange dmgReductionMax) {
                    this.hp = hp;
                    this.armorMin = armorMin;
                    this.armorMax = armorMax;
                    this.energy = energy;
                    this.dmgReductionMin = dmgReductionMin;
                    this.dmgReductionMax = dmgReductionMax;
                }
            }

            public RarityValue(ItemRarity rarity) {
                this.rarity = rarity;
                this.orbValues =new HashMap<>();
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

                System.out.println(gson.toJson(data));
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
        updateOrbValues(data.getAsJsonArray("orb_values.csv"));
        updateArmorValues(data.getAsJsonArray("armor.csv"));
        System.out.println(tierValues);
        System.out.println(this.keyDictionary);

        System.out.println("RARE T5 VIT: " + getWeaponElementals(Tier.T5,ItemRarity.RARE));
        TierValue.RarityValue.WeaponValue weaponValue = getWeaponValue(Tier.T5, ItemRarity.RARE);
        System.out.println(weaponValue);
    }


    private void updateArmorValues(JsonArray array) {
        List<JsonArray> armorArray = new ArrayList<>();
        List<JsonArray> weaponArray = new ArrayList<>();
        boolean armor = true;
        for (int i = 0; i < array.size(); i++) {
            JsonArray entry = array.get(i).getAsJsonArray();
            String stat = entry.get(0).getAsString();
            boolean first = false;
            if (stat.equals("Armor")) {
                armor = true;
                first=true;
            }


            if (stat.equals("Weapon")) {
                armor = false;
                first=true;
            }

            if (first) continue;
            if (armor) {
                armorArray.add(entry);
            } else {
                weaponArray.add(entry);
            }
        }


        //Handle weapon values
        for (JsonArray entry : weaponArray) {
            if (entry.get(0).getAsString().equalsIgnoreCase("dmg-min"))continue;

            System.out.println(entry);
            IntRange min = IntRange.fromString(entry.get(0).getAsString());
            IntRange max = IntRange.fromString(entry.get(1).getAsString());
            Tier tier = Tier.getByNumber(Integer.parseInt(entry.get(2).getAsString()));
            ItemRarity itemRarity = ItemRarity.getByName(entry.get(3).getAsString());
            handleWeaponValue(itemRarity, tier, min,max);

        }

        //Handle armor values
        for (JsonArray entry : armorArray) {
            if (entry.get(0).getAsString().equalsIgnoreCase("Tier"))continue;

            Tier tier = Tier.getByNumber(Integer.parseInt(entry.get(0).getAsString()));
            IntRange hp = IntRange.fromString(entry.get(1).getAsString());
            DoubleRange energy = DoubleRange.fromString(entry.get(2).getAsString());
            IntRange armorMin = IntRange.fromString(entry.get(3).getAsString());
            IntRange armorMax = IntRange.fromString(entry.get(4).getAsString());
            IntRange dmgReductionMin = IntRange.fromString(entry.get(5).getAsString());
            IntRange dmgReductionMax = IntRange.fromString(entry.get(6).getAsString());
            ItemRarity itemRarity = ItemRarity.getByName(entry.get(8).getAsString());
            handleArmorValue(tier,hp,energy,armorMin,armorMax, dmgReductionMin, dmgReductionMax, itemRarity);


        }
    }

    private void handleArmorValue(Tier tier, IntRange hp, DoubleRange energy, IntRange armorMin, IntRange armorMax, IntRange dmgReductionMin, IntRange dmgReductionMax, ItemRarity itemRarity) {
        if (tier==null)return;
        if (itemRarity==null)return;
        if (energy==null)return;
        if (hp==null)return;
        if (armorMin==null)return;
        if (armorMax==null)return;
        if (dmgReductionMin==null)return;
        if (dmgReductionMax==null)return;


        if (!tierValues.containsKey(tier)) {
            TierValue tierValue = new TierValue(tier);
            tierValues.put(tier,tierValue);
        }

        TierValue tierValue = tierValues.get(tier);
        TierValue.RarityValue rarityValue = tierValue.editRarityValue(itemRarity);
        if (rarityValue==null)return;
        rarityValue.armorValue =new TierValue.RarityValue.ArmorValue(hp,armorMin,armorMax,energy, dmgReductionMin, dmgReductionMax);

    }

    private void handleWeaponValue(ItemRarity itemRarity, Tier tier, IntRange min, IntRange max) {

        if (tier==null)return;
        if (itemRarity==null)return;
        if (min==null)return;
        if (max==null)return;


        if (!tierValues.containsKey(tier)) {
            TierValue tierValue = new TierValue(tier);
            tierValues.put(tier,tierValue);
        }

        TierValue tierValue = tierValues.get(tier);
        TierValue.RarityValue rarityValue = tierValue.editRarityValue(itemRarity);
        if (rarityValue==null)return;
        rarityValue.weaponValue =new TierValue.RarityValue.WeaponValue(min,max);


    }

    public TierValue.RarityValue.WeaponValue getWeaponValue(Tier tier, ItemRarity rarity) {
        if (tierValues.containsKey(tier)) {
            TierValue tierValue = tierValues.get(tier);
            if (tierValue.rarityValues.containsKey(rarity)) {
                TierValue.RarityValue rarityValue = tierValue.rarityValues.get(rarity);
                return rarityValue.weaponValue;
            }
        }
        return null;
    }
    public TierValue.RarityValue.ArmorValue getArmorValue(Tier tier, ItemRarity rarity) {
        if (tierValues.containsKey(tier)) {
            TierValue tierValue = tierValues.get(tier);
            if (tierValue.rarityValues.containsKey(rarity)) {
                TierValue.RarityValue rarityValue = tierValue.rarityValues.get(rarity);
                return rarityValue.armorValue;
            }
        }
        return null;
    }

    private void updateOrbValues(JsonArray array) {
        for (int i = 1; i < array.size(); i++) {
            JsonArray entry = array.get(i).getAsJsonArray();
            if (entry.get(0).getAsString().equalsIgnoreCase("stat"))continue;


            String stat = entry.get(0).getAsString();
            Tier tier = Tier.getByNumber(Integer.parseInt(entry.get(1).getAsString()));

            if (!tierValues.containsKey(tier)) {
                TierValue tierValue = new TierValue(tier);
                tierValues.put(tier,tierValue);
            }

            TierValue tierValue = tierValues.get(tier);
            if (stat.equalsIgnoreCase("hps")) {
                ItemRarity itemRarity = ItemRarity.getByName(entry.get(3).getAsString());

                if (itemRarity==null)continue;
                TierValue.RarityValue rarityValue = tierValue.editRarityValue(itemRarity);
                rarityValue.hps = IntRange.fromString(entry.get(2).getAsString());
            } else {


                int min =Integer.parseInt( entry.get(2).getAsString());
                int max = Integer.parseInt(entry.get(3).getAsString());



                tierValue.orbValues.put(stat, new IntRange(min, max));
            }


        }
    }

    public IntRange getHPS(Tier tier, ItemRarity rarity) {
        if (tierValues.containsKey(tier)) {
            TierValue tierValue = tierValues.get(tier);
            if (tierValue.rarityValues.containsKey(rarity)) {
                TierValue.RarityValue rarityValue = tierValue.rarityValues.get(rarity);
                return rarityValue.hps;
            }
        }
        return null;
    }


    public IntRange getWeaponElementals(Tier tier, ItemRarity rarity) {
        if (tierValues.containsKey(tier)) {
            TierValue tierValue = tierValues.get(tier);
            return tierValue.orbValues.getOrDefault("elemental_damage", null);
        }
        return null;
    }

    public IntRange getElementals(Tier tier, ItemRarity rarity) {
        if (tierValues.containsKey(tier)) {
            TierValue tierValue = tierValues.get(tier);
            return tierValue.orbValues.getOrDefault("ELEMENTALS", null);
        }
        return null;
    }

    public IntRange getRBLODGE(Tier tier, ItemRarity rarity) {
        if (tierValues.containsKey(tier)) {
            TierValue tierValue = tierValues.get(tier);
            return tierValue.orbValues.getOrDefault("RBLODGE", null);
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
