package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.setting;

/**
 * Created by Matthew E on 3/10/2019 at 11:27 AM for the project DungeonRealmsDREnhanced
 */
public enum DRSettingCategory {
    MISC("Misc"), CHAT("Chat"), DEBUG("Debug"), ITEMS("Items"), DEVELOPMENT("Development");

    private String name;

    DRSettingCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}