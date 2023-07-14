package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.player.profession.mining;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.modules.profession.ProfessionItem;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.setting.DRSettings;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.MiningUtils;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.Tier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * Created by Matthew E on 3/13/2019 at 9:20 AM for the project DungeonRealmsDREnhanced
 */
public class Mining {
    private Map<Long, Integer> expMapTenMinutes = new ConcurrentHashMap<>();
    private Map<Long, Integer> expMapMinutes = new ConcurrentHashMap<>();


    public Mining() {
        this.expMapTenMinutes = new ConcurrentHashMap<>();
        this.expMapMinutes = new ConcurrentHashMap<>();

    }


    public void onExperienceGain(int experience) {
        this.expMapTenMinutes.put(System.currentTimeMillis(), experience);
        this.expMapMinutes.put(System.currentTimeMillis(), experience);
    }




    public long getStartingNeededXPForWholeTier(Tier tier) {

        return MiningUtils.getStartingNeededXPForWholeTier(tier);

    }



    public long getRemainingExperienceForNextTier(ProfessionItem professionItem) {
       return MiningUtils.getExperienceRemainingForTier(professionItem.getLevel(),professionItem.getExperience());

    }




    public long getSecondsUntilNextLevel(ProfessionItem professionItem) {

        final int experiencePerMinute = getExperiencePer5Minutes();
        if (experiencePerMinute <= 10) {
            return -1;
        }

        double experiencePerSecond = ((double) experiencePerMinute / 60.0D) / 5;
        long experienceRemainingForLevel = MiningUtils.getExperienceRemainingForLevel(professionItem.getLevel(), professionItem.getExperience());

        return (long) ((double)experienceRemainingForLevel/experiencePerSecond);
    }

    public int getExperiencePer5Minutes() {
        if (expMapTenMinutes==null||expMapTenMinutes.isEmpty()){
            return 0;

        }
        for (Map.Entry<Long, Integer> entry : expMapTenMinutes.entrySet()) {
            if (entry.getKey() < System.currentTimeMillis() - (TimeUnit.MINUTES.toMillis(5))) {
                expMapTenMinutes.remove(entry.getKey());
            }
        }
       if (expMapTenMinutes.isEmpty()){
           return 0;

       }
        return expMapTenMinutes.values().stream().mapToInt(exp -> exp).sum();
    }


    public long getSecondsUntilNextTier(ProfessionItem professionItem) {
        final int experiencePerMinute = getExperiencePer5Minutes();
        if (experiencePerMinute <= 10) {
            return -1;
        }
        double experiencePerSecond = ((double) experiencePerMinute / 60.0D)/5.0D;
//        int experiencePerSecond = (int) ((double) experiencePerMinute / 60.0D);
        long experienceRemainingForTier = MiningUtils.getExperienceRemainingForTier(professionItem.getLevel(), professionItem.getExperience());

        return (long) ((double)experienceRemainingForTier/(double)experiencePerSecond);
    }
}
