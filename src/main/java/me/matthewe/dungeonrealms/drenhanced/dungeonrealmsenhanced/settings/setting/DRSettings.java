package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.setting;

import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthew E on 3/10/2019 at 11:26 AM for the project DungeonRealmsDREnhanced
 */

public enum DRSettings {
    DEBUG_SPACING_ENABLE("Spacing Enabled", "Toggle spacing feature.", DRSettingCategory.DEBUG, true, boolean.class),
    DEBUG_SPACING("Spacing", "The spacing added to the debug.", DRSettingCategory.DEBUG, 8.0D, double.class),
    YOU_EXAMINE_YOUR_CATCH_MESSAGE("Disable Examine Message", new String[]{"Disable the fishing message", TextFormatting.RED + "You examine your catch..."}, DRSettingCategory.CHAT_FISHING, false, boolean.class),
    YOU_CAUGHT("Disable Caught Message", new String[]{"Disable the caught message", TextFormatting.GREEN + " ... you caught some "}, DRSettingCategory.CHAT_FISHING, false, boolean.class),
    FISHING_EXP_PERCENTAGE("Fishing XP Percentage", new String[]{"Enable EXP % in fishing exp debug"}, DRSettingCategory.CHAT_FISHING, false, boolean.class),
    MINING_EXP_PERCENTAGE("Mining XP Percentage", new String[]{"Enable EXP % in mining exp debug"}, DRSettingCategory.CHAT_MINING, false, boolean.class),
    MISC_PROFESSION_EXP("Used Profession EXP", new String[]{"Shows used experience bottle amount", "on profession items"}, DRSettingCategory.ITEMS_MISC, true, boolean.class),
    MISC_PROFESSION_PERCENT_LEFT("Next Tier EXP", new String[]{"Shows percentage until next profession tier", "on profession items"}, DRSettingCategory.ITEMS_MISC, true, boolean.class),
//    MISC_PROFESSION_ENCHANT_INFO("Show Enchant Info", new String[]{"Shows enchantment info", "Hold "+TextFormatting.GREEN+"Shift" +TextFormatting.GRAY + " over an item for more information."}, DRSettingCategory.ITEMS_MISC, false, boolean.class),
    //    DISABLE_CLEAR_CHAT("Disable Clear Chat", new String[] {"Disables the clear chat for", "chat clears and the welcome message."}, DRSettingCategory.CHAT, true, boolean.class),
    GAMMA("Gamma", "Change brightness.", DRSettingCategory.MISC, 1.0D, double.class),
//    DEV_MAX_EXP("Max Experience", "Max EXP.", DRSettingCategory.DEVELOPMENT, 100000, int.class),
//    DEV_XP_BOTTLE_NBT("XP Bottle NBT", "NBT", DRSettingCategory.DEVELOPMENT, "xp", String.class),
    MISC_PERCENT_FORMAT("Percent Format", "Percentage number format.", DRSettingCategory.MISC, "###.##", String.class),
    TESTING("Testing", "Toggles testing.", DRSettingCategory.DEVELOPMENT, false, boolean.class),
    NEW_LORE("New Lore", "Toggles new lore.", DRSettingCategory.DEVELOPMENT, false, boolean.class),
    INT_ONE("One", "Test", DRSettingCategory.DEVELOPMENT, 50.0D, double.class),
    INT_TWO("Two", "Test", DRSettingCategory.DEVELOPMENT, 50.0D, double.class),
    OLD_BOSS_BAR("Old Bossbar", new String[]{"This will display the open beta", "pink boss bar", " ", TextFormatting.AQUA + "Use /zone to view your current zone."}, DRSettingCategory.BOSS_BAR, false, boolean.class),
    LEVEL_HEALTH("Level Health", new String[]{"This will display your hp", "in your level bar.", " ", TextFormatting.AQUA + "Use /zone to view your current zone."}, DRSettingCategory.BOSS_BAR, false, boolean.class),
    GLOWING_RARITIES_COMMON("Common Overlay", new String[]{"Toggles gray overlay over common items."}, DRSettingCategory.ITEMS_OVERLAY, false, boolean.class),
    GLOWING_CLUE_SCROLL("Clue Scroll Overlay", new String[]{"Toggles red overlay over clue scrolls."}, DRSettingCategory.ITEMS_OVERLAY, true, boolean.class),
    GLOWING_RARITIES_UNCOMMON("Uncommon Overlay", new String[]{"Toggles green overlay over uncommon items."}, DRSettingCategory.ITEMS_OVERLAY, true, boolean.class),
    GLOWING_RARITIES_RARE("Rare Overlay", new String[]{"Toggles aqua overlay over rare items."}, DRSettingCategory.ITEMS_OVERLAY, true, boolean.class),
    GLOWING_RARITIES_EPIC("Epic Overlay", new String[]{"Toggles purple overlay over epic items."}, DRSettingCategory.ITEMS_OVERLAY, true, boolean.class),
    GEM_COUNT_INVENTORY_OVERLAY("Gem Count Overlay", new String[]{"Toggles showing total gems", "in your inventory."}, DRSettingCategory.ITEMS_MISC, true, boolean.class),
    GLOWING_RARITIES_LEGENDARY("Legendary Overlay", new String[]{"Toggles yellow overlay over legendary items."}, DRSettingCategory.ITEMS_OVERLAY, true, boolean.class),
    GLOWING_RARITIES_MYTHIC("Mythic Overlay", new String[]{"Toggles gold overlay over mythic items."}, DRSettingCategory.ITEMS_OVERLAY, true, boolean.class),
    ORIGIN_NAME("Origin", new String[]{"Show an items origin in lore when you hold shift. "}, DRSettingCategory.ITEMS_MISC, true, boolean.class),
    DURABILITY_PERCENTAGE("Durability Percent", new String[]{"Display durability " + TextFormatting.WHITE + "percent " + TextFormatting.GRAY + "of an item."}, DRSettingCategory.ITEMS_MISC, true, boolean.class),
    DURABILITY_PERCENTAGE_FORMAT("Percent Format", new String[]{"Format of durability on an items display name."}, DRSettingCategory.ITEMS_MISC, "&8(%color%%percent%%&8)", String.class),
    MISC_DELAY("Update Delay", new String[]{"Update delay for modules"}, DRSettingCategory.MISC, 500L, long.class),
    SEND_DATA("Data Collection", new String[]{"This will allow data collection", "to help improve the mod.", " ", TextFormatting.RED + "Coming Soon"}, DRSettingCategory.MISC, true, boolean.class);

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

    public static DRSettings getByString(String key) {
        for (DRSettings value : values()) {
            if (value.toString().equalsIgnoreCase(key)) {
                return value;
            }
        }
        return null;
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
