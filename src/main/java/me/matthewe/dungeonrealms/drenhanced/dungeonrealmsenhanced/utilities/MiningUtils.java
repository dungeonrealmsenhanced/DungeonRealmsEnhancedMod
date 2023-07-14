package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.modules.profession.ProfessionItem;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.Tier;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Matthew E on 12/22/2020 at 11:50 AM for the project DungeonRealmsEnhancedMod
 */
public class MiningUtils {
    public static Map<Integer, Long> EXPERIENCE_REQUIREMENTS;
    public static Map<Tier, Long> STARTING_EXPERIENCE_FOR_TIER;

    static {
        EXPERIENCE_REQUIREMENTS = new ConcurrentHashMap<>();
        STARTING_EXPERIENCE_FOR_TIER = new ConcurrentHashMap<>();
        for (int i = 1; i <= 100; i++) {
            EXPERIENCE_REQUIREMENTS.put(i, (long) ProfessionItem.getNeededXP(i));
        }

        for (Tier tier : Tier.values()) {
            long amount;
            switch (tier) {

                case T1:
                    amount = MiningUtils.getRangeOfRequirements(1,19);
                    break;
                case T2:
                    amount = MiningUtils.getRangeOfRequirements(20,39);
                    break;
                case T3:
                    amount = MiningUtils.getRangeOfRequirements(40,59);
                    break;
                case T4:
                    amount = MiningUtils.getRangeOfRequirements(60,79);
                    break;
                case T5:
                    amount = MiningUtils.getRangeOfRequirements(80,100);
                    break;
                default:
                    amount = -1;
                    break;
            }
            STARTING_EXPERIENCE_FOR_TIER.put(tier,amount);
        }
    }

    public static long getExperienceRequirement(int tier) {
        Tier byNumber = Tier.getByNumber(tier);
        if (byNumber == null) {
            return 0;

        }
        return byNumber.getExperienceRequirement();

    }

    public static long getRangeOfRequirements(int min, int max) {
        long total = 0;

        if (min > max) {
            return -2;
        }
        if (min < 1 || max > 100) {
            return -3;
        }
        if (min == max) {
            if (EXPERIENCE_REQUIREMENTS.containsKey(min)) {
                return EXPERIENCE_REQUIREMENTS.get(min);
            }
            return -4;
        }
        for (int i = min; i <= max; i++) {

            if (EXPERIENCE_REQUIREMENTS.containsKey(i)) {
                total += EXPERIENCE_REQUIREMENTS.get(i);
            }
        }
        return total;
    }

    public static long getExperienceRemainingForTier(int currentLevel, int experience){

        Tier tier = getTier(currentLevel);

        long completedFromPastLevelsInTier;

        if (isFirstLevelInTier(currentLevel)) {
            completedFromPastLevelsInTier =0;
        } else {
            completedFromPastLevelsInTier = MiningUtils.getRangeOfRequirements(getRange(tier)[0], currentLevel - 1);
        }

        long gotten = completedFromPastLevelsInTier+experience;

        long totalExperienceRequirement = tier.getExperienceRequirement();

        return totalExperienceRequirement-gotten;
    }

    private static boolean isFirstLevelInTier(int currentLevel) {
        Tier tier = getTier(currentLevel);
        switch (tier) {

            case T1:
                if (currentLevel == 1) {
                    return true;
                }
                break;
            case T2:
                if (currentLevel == 20) {
                    return true;
                }
                break;
            case T3:
                if (currentLevel == 40) {
                    return true;
                }
                break;
            case T4:
                if (currentLevel == 60) {
                    return true;
                }
                break;
            case T5:
                if (currentLevel == 80) {
                    return true;
                }
                break;
        }
        return false;
    }


    public static int[] getRange(Tier tier) {
        switch (tier){

            case T1:

                return new int[]{1,19};
            case T2:
                return new int[]{20,39};
            case T3:
                return new int[]{40,59};
            case T4:
                return new int[]{60,79};
            case T5:
                return new int[]{80,100};
        }
        return new int[]{1,19};
    }

    public static Tier getTier(int level) {
        if (level < 20 && level >= 1) {
            return Tier.T1;
        } else if (level >= 20 && level < 40) {
            return Tier.T2;
        } else if (level >= 40 && level < 60) {
            return Tier.T3;
        } else if (level >= 60 && level < 80) {
            return Tier.T4;
        } else if (level >= 80 && level < 100) {
            return Tier.T5;
        } else if (level >= 100) {
            return Tier.T5;
        }
        return Tier.T1;
    }
    public static long getExperienceRemainingForLevel(int currentLevel, int currentExperience){
        if (EXPERIENCE_REQUIREMENTS.containsKey(currentLevel)) {
            return EXPERIENCE_REQUIREMENTS.get(currentLevel)-currentExperience;
        }
        return 0;
    }
    public static long getTotalExperienceForTier(int level, int currentExperience) {
        if (level == 1) {
            return currentExperience;
        }
        if (level < 1) {
            return currentExperience;
        }
        long rangeOfRequirements = getRangeOfRequirements(getRange(getTier(level))[0], level);
        if (rangeOfRequirements < 0) {
            return rangeOfRequirements;
        }
        return rangeOfRequirements+currentExperience;
    }

    public static long getTotalExperience(int level, int currentExperience) {
        if (level == 1) {
            return currentExperience;
        }
        if (level < 1) {
            return currentExperience;
        }
        long rangeOfRequirements = getRangeOfRequirements(1, level-1);
        if (rangeOfRequirements < 0) {
            return rangeOfRequirements;
        }
        return rangeOfRequirements+currentExperience;
    }

    public static long getNeededExperience(int level) {
        return EXPERIENCE_REQUIREMENTS.getOrDefault(level,-1L);
    }

    public static long getStartingNeededXPForWholeTier(Tier tier) {
        return STARTING_EXPERIENCE_FOR_TIER.get(tier);
    }
}
