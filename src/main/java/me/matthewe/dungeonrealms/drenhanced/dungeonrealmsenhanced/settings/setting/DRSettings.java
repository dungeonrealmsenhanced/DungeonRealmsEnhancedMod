package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.setting;

import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthew E on 3/10/2019 at 11:26 AM for the project DungeonRealmsDREnhanced
 */

public enum DRSettings {
    DEBUG_SPACING_ENABLE("Spacing Enabled", "Toggle spacing feature.", DRSettingCategory.DEBUG, true, boolean.class),
    DEBUG_SPACING("Spacing", "The spacing added to the debug.", DRSettingCategory.DEBUG, 10.0D, double.class),
    DISABLE_CLEAR_CHAT("Disable Clear Chat", new String[] {"Disables the clear chat for", "chat clears and the welcome message."}, DRSettingCategory.CHAT, true, boolean.class),
    GAMMA("Gamma", "Change brightness.", DRSettingCategory.MISC, 1.0D, double.class),
    TESTING("Testing", "Toggles testing.", DRSettingCategory.DEVELOPMENT, false, boolean.class),
    NEW_LORE("New Lore", "Toggles new lore.", DRSettingCategory.DEVELOPMENT, false, boolean.class),
    INT_ONE("One", "Test", DRSettingCategory.DEVELOPMENT, 50.0D, double.class),
    INT_TWO("Two", "Test", DRSettingCategory.DEVELOPMENT, 50.0D, double.class),
    GLOWING_RARITIES_COMMON("Common Overlay", new String[] {"Toggles gray overlay over common items."}, DRSettingCategory.ITEMS_OVERLAY, false, boolean.class),
    GLOWING_RARITIES_UNCOMMON("Uncommon Overlay", new String[] {"Toggles green overlay over uncommon items."}, DRSettingCategory.ITEMS_OVERLAY, true, boolean.class),
    GLOWING_RARITIES_RARE("Rare Overlay", new String[] {"Toggles aqua overlay over rare items."}, DRSettingCategory.ITEMS_OVERLAY, true, boolean.class),
    GLOWING_RARITIES_EPIC("Epic Overlay", new String[] {"Toggles purple overlay over epic items."}, DRSettingCategory.ITEMS_OVERLAY, true, boolean.class),
    GLOWING_RARITIES_LEGENDARY("Legendary Overlay", new String[] {"Toggles yellow overlay over legendary items."}, DRSettingCategory.ITEMS_OVERLAY, true, boolean.class),
    ORIGIN_NAME("Origin", new String[] {"Show an items origin in lore when you hold shift. "}, DRSettingCategory.ITEMS_MISC, true, boolean.class),
    DURABILITY_PERCENTAGE("Durability Percent", new String[] {"Display durability " + TextFormatting.WHITE+"percent "+TextFormatting.GRAY+"of an item."}, DRSettingCategory.ITEMS_MISC, true, boolean.class),
    DURABILITY_PERCENTAGE_FORMAT("Percent Format", new String[] {"Format of durability on an items display name."}, DRSettingCategory.ITEMS_MISC, "&8&l(&b&l%percent%%&8&l)", String.class),
    SEND_DATA("Data Collection",new String[]{ "This will allow data collection", "to help improve the mod."}, DRSettingCategory.MISC, true, boolean.class);

    private String name;
    private String[] description;
    private DRSettingCategory category;
    private Object value;
    private Class clazz;

    <T extends Object> DRSettings(String name, String description, DRSettingCategory category, T value, Class<T> clazz) {
        this.name = name;
        this.description = new String[]{description};
        this.category = category;
        this.value = value;
        this.clazz = clazz;
    }

    <T extends Object> DRSettings(String name, String[] description, DRSettingCategory category, T value, Class<T> clazz) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.value = value;
        this.clazz = clazz;
    }

    public String[] getDescription() {
        return description;
    }

    public static List<DRSettings> getByCategory(DRSettingCategory category) {
        List<DRSettings> drSettings = new ArrayList<>();
        for (DRSettings value : DRSettings.values()) {
            if (value.getCategory() == category) {
                drSettings.add(value);
            }
        }
        return drSettings;
    }

    public <T extends Object> T get(Class<T> clazz) {
        return Settings.get().getCategory(category).getSettingValue(this, clazz);
    }

    public boolean booleanValue() {
        return (boolean) value;
    }

    public int intValue() {
        return (int) value;
    }

    public double doubleValue() {
        return (double) value;
    }

    public String stringValue() {
        return (String) value;
    }

    public String getName() {
        return name;
    }

    public DRSettingCategory getCategory() {
        return category;
    }

    public Object getValue() {
        return value;
    }

    public Class getClazz() {
        return clazz;
    }
}
