package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.StatKeyDatabase;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.ItemUtils;

public class WeaponCalculator {

    public static double calculateRollPercentage(int level, ItemUtils.ItemType itemType, StatKeyDatabase.TierValue.RarityValue.WeaponValue baseWeapon, int currentMin, int currentMax) {
        double baseMin = (baseWeapon.getDamageMin().getMin() + baseWeapon.getDamageMax().getMax()) / 2.0;
        double baseMax = (baseWeapon.getDamageMin().getMin() + baseWeapon.getDamageMax().getMax()) / 2.0;

        // Calculate level-based scaling
        int medianLevel = getMedianLevel(getTier(level));
        double levelScaling = (level - medianLevel) * 0.01;

        baseMin *= (1 + levelScaling);
        baseMax *= (1 + levelScaling);

        // If level > 100, apply minimum roll adjustment
        if (level > 100) {
            double minRollIncrease = (level - 100) * 0.05;
            baseMin += (baseMax - baseMin) * minRollIncrease;
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
