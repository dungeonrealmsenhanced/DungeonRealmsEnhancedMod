package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.player.profession.mining;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.Tier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Matthew E on 3/13/2019 at 9:20 AM for the project DungeonRealmsDREnhanced
 */
public class Mining {
    private Map<Tier, List<MinedOre>> minedOreMap = new ConcurrentHashMap<>();
    private Map<Long, Integer> expMap;

    public Mining(Map<Tier, List<MinedOre>> minedOreMap, Map<Long, Integer> expMap) {
        this.minedOreMap = minedOreMap;
        this.expMap = expMap;
    }

    public Mining() {
        this.minedOreMap = new ConcurrentHashMap<>();
        this.expMap = new ConcurrentHashMap<>();
    }

    public void mineOre(MinedOre minedOre) {
        List<MinedOre> minedOres = new ArrayList<>();
        if (this.minedOreMap.containsKey(minedOre.getTier())) {
            minedOres = this.minedOreMap.get(minedOre.getTier());
            this.minedOreMap.remove(minedOre.getTier());
        }
        minedOres.add(minedOre);
        this.minedOreMap.put(minedOre.getTier(), minedOres);
    }

    public void onExperienceGain(int experience) {
        this.expMap.put(System.currentTimeMillis(), experience);
    }

    public int getSecondsUntilNextLevel(int neededExperience, int currentExperience) {
        final int experiencePerMinute = getExperiencePerMinute();
        if (experiencePerMinute <= 10) {
            return -1;
        }
        int experiencePerSecond = (int) ((double)experiencePerMinute / 60.0D);
        boolean leveledUp = false;
        int seconds = 0;
        while (!leveledUp) {
            if (currentExperience >= neededExperience) {
                leveledUp = true;
            } else {
                currentExperience += experiencePerSecond;
                seconds++;
            }
        }
        return seconds;
    }

    public int getExperiencePerMinute() {
        List<Long> toRemoveList = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : expMap.entrySet()) {
            if (entry.getKey() < System.currentTimeMillis() - 60000L) {
                toRemoveList.add(entry.getKey());
            }
        }
        for (Long aLong : toRemoveList) {
            expMap.remove(aLong);
        }

        int experience = 0;
        for (int exp : expMap.values()) {
            experience += exp;
        }
        return experience;
    }

    public Map<Tier, List<MinedOre>> getMinedOreMap() {
        return minedOreMap;
    }
}
