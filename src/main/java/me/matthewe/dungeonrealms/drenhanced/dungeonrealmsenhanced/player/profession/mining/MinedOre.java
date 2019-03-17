package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.player.profession.mining;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.Tier;

import java.util.UUID;

/**
 * Created by Matthew E on 3/13/2019 at 9:22 AM for the project DungeonRealmsDREnhanced
 */
public class MinedOre {
    private UUID uuid;
    private Tier tier;
    private int experienceGain;
    private int oreAmount;
    private long mineDate;

    public MinedOre(UUID uuid, Tier tier, int experienceGain, int oreAmount, long mineDate) {
        this.uuid = uuid;
        this.tier = tier;
        this.experienceGain = experienceGain;
        this.oreAmount = oreAmount;
        this.mineDate = mineDate;
    }

    public UUID getUuid() {
        return uuid;
    }

    public long getMineDate() {
        return mineDate;
    }

    public Tier getTier() {
        return tier;
    }

    public int getExperienceGain() {
        return experienceGain;
    }

    public int getOreAmount() {
        return oreAmount;
    }
}
