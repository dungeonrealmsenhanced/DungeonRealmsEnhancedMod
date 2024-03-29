package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.setting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Matthew E on 3/10/2019 at 11:27 AM for the project DungeonRealmsDREnhanced
 */
public enum DRSettingCategory {
    MISC("Misc"),
    CHAT_FISHING("Chat Fishing"),
    CHAT_MINING("Chat Mining"),
    CHAT("Chat", DRSettingCategory.CHAT_FISHING,CHAT_MINING),
    DEBUG("Debug"),
    BOSS_BAR("Bossbar"),
    //    NOSTALGIA_TOGGLES("Toggles"),
//    NOSTALGIA_TAB_LIST("Tab List"),
//    NOSTALGIA_SOUNDS("Sounds"),
    NOSTALGIA("Nostalgia", DRSettingCategory.DEBUG, BOSS_BAR),
//        DRSettingCategory.NOSTALGIA_TOGGLES, DRSettingCategory.NOSTALGIA_TAB_LIST, DRSettingCategory.NOSTALGIA_SOUNDS}),

    ITEMS_MISC("Items Misc"),
    ITEMS_OVERLAY("Items Overlay"),
    ITEMS("Items", DRSettingCategory.ITEMS_OVERLAY, DRSettingCategory.ITEMS_MISC),


    DEVELOPMENT("Development");

    private String name;
    private List<DRSettingCategory> subCategoryList;

    DRSettingCategory(String name, DRSettingCategory... drSettingCategories) {
        this.name = name;
        this.subCategoryList = new ArrayList<>();
        if ((drSettingCategories != null) && (drSettingCategories.length > 0)) {
            subCategoryList.addAll(Arrays.asList(drSettingCategories));
        }
    }

    public static DRSettingCategory getByString(String key) {
        for (DRSettingCategory value : values()) {
            if (value.toString().equalsIgnoreCase(key)) {
                return value;
            }
        }
        return null;
    }

    public boolean hasSubCategories() {
        return (subCategoryList != null) && !subCategoryList.isEmpty();
    }

    public List<DRSettingCategory> getSubCategoryList() {
        return subCategoryList;
    }

    public String getName() {
        return name;
    }

    public boolean isSubCategory() {
        return Arrays.stream(values()).anyMatch(value -> value.hasSubCategories() && value.getSubCategoryList().contains(this));
    }

    public DRSettingCategory getParentCategory() {
        for (DRSettingCategory value : values()) {
            if (!value.isSubCategory() && value.getSubCategoryList().contains(this)) {
                return value;
            }
        }
        return null;

    }
}