package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.StatKeyDatabase;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.ItemUtils;

public class WeaponCalculator {
    public static double calculateRollPercentageArmor(int level, ItemUtils.ItemType itemType, StatKeyDatabase.TierValue.RarityValue.ArmorValue armorValue, int currentHp) {
        // Get base HP from armor value
        int baseHp =currentHp;

        // Apply level-based scaling
        if (level < 90) {
            double reductionFactor = 1 - ((90 - level) * 0.01); // 1% reduction per level below 90
            baseHp *= reductionFactor;
        } else if (level > 90) {
            double increaseFactor = 1 + ((level - 90) * 0.01); // 1% increase per level above 90
            baseHp *= increaseFactor;
        }

        // Apply item type multiplier
        double typeMultiplier = getItemTypeMultiplier(itemType);
        baseHp *= typeMultiplier;

        // Return percentage comparison (direct HP scaling, not averaged)
        return (currentHp / (double) baseHp) * 100;
    }

    public static double calculateRollPercentage(int level, ItemUtils.ItemType itemType, StatKeyDatabase.TierValue.RarityValue.WeaponValue baseWeapon, int currentMin, int currentMax) {
        // Get base min and max directly
        double baseMin = baseWeapon.getDamageMin().getMin();
        double baseMax = baseWeapon.getDamageMax().getMax();

        // Apply level-based scaling
        if (level < 90) {
            double reductionFactor = 1 - ((90 - level) * 0.01); // 1% reduction per level below 90
            baseMin *= reductionFactor;
            baseMax *= reductionFactor;
        } else if (level > 90) {
            double increaseFactor = 1 + ((level - 90) * 0.01); // 1% increase per level above 90
            baseMin *= increaseFactor;
            baseMax *= increaseFactor;
        }

        // Apply item type multiplier
        double typeMultiplier = getItemTypeMultiplier(itemType);
        baseMin *= typeMultiplier;
        baseMax *= typeMultiplier;

        // Calculate current roll average
        double currentAverage = (currentMin + currentMax) / 2.0;
        double expectedAverage = (baseMin + baseMax) / 2.0;

        // Return percentage comparison
        return (currentAverage / expectedAverage) * 100;
    }

    private static int getTier(int level) {
        if (level <= 10) return 1;
        if (level <= 30) return 2;
        if (level <= 50) return 3;
        if (level <= 70) return 4;
        if (level <= 90) return 5;
        return 5; // Default to max tier
    }

    private static int getMedianLevel(int tier) {
        switch (tier) {
            case 1: return 10;
            case 2: return 30;
            case 3: return 50;
            case 4: return 70;
            case 5: return 90;
            default: return 90;
        }
    }

    private static double getItemTypeMultiplier(ItemUtils.ItemType itemType) {
        switch (itemType) {
            case SWORD: return 1.0;
            case HOE: return 1.05;
            case AXE: return 1.1;
            case POLEARM: return 1.15;
            case BOW: return 1.2;
            default: return 1.0; // Default to sword multiplier
        }
    }


}
