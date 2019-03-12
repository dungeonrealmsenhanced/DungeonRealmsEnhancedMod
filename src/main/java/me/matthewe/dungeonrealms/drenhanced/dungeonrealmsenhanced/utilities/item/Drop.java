package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item;

/**
 * Created by Matthew E on 12/31/2018 at 8:52 PM for the project DungeonRealmsDREnhanced
 */
public class Drop {
    private Tier tier;
    private ItemRarity itemRarity;
    private int killCount;
    private int dryStreak;
    private String nbt;

    public Drop(Tier tier, ItemRarity itemRarity, int killCount, int dryStreak, String nbt) {
        this.tier = tier;
        this.itemRarity = itemRarity;
        this.killCount = killCount;
        this.dryStreak = dryStreak;
        this.nbt = nbt;
    }

    public Tier getTier() {
        return tier;
    }

    public ItemRarity getItemRarity() {
        return itemRarity;
    }

    public int getKillCount() {
        return killCount;
    }

    public int getDryStreak() {
        return dryStreak;
    }

    public String getNbt() {
        return nbt;
    }
}
