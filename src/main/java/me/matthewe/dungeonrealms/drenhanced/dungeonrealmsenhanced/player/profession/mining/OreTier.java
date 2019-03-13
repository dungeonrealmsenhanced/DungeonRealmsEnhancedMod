package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.player.profession.mining;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.Tier;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Matthew Eisenberg on 3/13/2019 at 9:40 AM for the project DungeonRealmsDREnhanced
 */
public enum OreTier {
    TIER_1(0, 90, 35),
    TIER_2(20, 275, 35),
    TIER_3(40, 460, 80),
    TIER_4(60, 820, 40),
    TIER_5(80, 1025, 55);


    private int level;
    private int baseExperience;
    private int randomExperience;

    OreTier(int level, int baseExperience, int randomExperience) {
        this.level = level;
        this.baseExperience = baseExperience;
        this.randomExperience = randomExperience;
    }

    public static OreTier getByTier(Tier tier) {
        switch (tier) {
            case T1:
                return TIER_1;
            case T2:
                return TIER_2;
            case T3:
                return TIER_3;
            case T4:
                return TIER_4;
            case T5:
                return TIER_5;
        }
        return null;
    }

    public static OreTier byNumber(int number) {
        switch (number) {
            case 1:
                return TIER_1;
            case 2:
                return TIER_2;
            case 3:
                return TIER_3;
            case 4:
                return TIER_4;
            case 5:
                return TIER_5;
        }
        return null;
    }

    public int getExperience() {
        return baseExperience + ThreadLocalRandom.current().nextInt(randomExperience);
    }

    public int getLevel() {
        return level;
    }

    public int getBaseExperience() {
        return baseExperience;
    }

    public int getRandomExperience() {
        return randomExperience;
    }

    public int getNextTierLevel() {
        return Math.min(((getLevel() / 20) + 1) * 20, 100);
    }

}