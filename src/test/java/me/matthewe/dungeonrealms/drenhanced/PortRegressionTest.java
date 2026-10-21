package me.matthewe.dungeonrealms.drenhanced;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.GsonBuilder;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.modules.profession.ProfessionItem;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.ModulePlacement;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.setting.*;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.FooterExtractor;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.json.SettingsJsonAdapter;
import org.junit.jupiter.api.Test;

class PortRegressionTest {
  @Test
  void enabledStateUsesThirdValueInsteadOfXCoordinate() {
    assertEquals(
        new ModulePlacement(80, 50, true), ModulePlacement.decode(new Integer[] {80, 50, 1}));
    assertEquals(
        new ModulePlacement(1, 50, false), ModulePlacement.decode(new Integer[] {1, 50, 0}));
  }

  @Test
  void damagedPlacementsDoNotAbortOtherModules() {
    assertNull(ModulePlacement.decode(null));
    assertNull(ModulePlacement.decode(new Integer[] {1, 2}));
    assertNull(ModulePlacement.decode(new Integer[] {null, 2, 1}));
    assertEquals(
        new ModulePlacement(0, 0, false), ModulePlacement.decode(new Integer[] {-90, -1, 7}));
  }

  @Test
  void footerSupportsFormattingCommasAndMultipleLines() {
    assertEquals(
        new FooterExtractor.ShardData("US-1", 1234),
        FooterExtractor.parseFooter("\u00a7aUS-1 42ms * play.dungeonrealms.net\n1,234 here"));
  }

  @Test
  void missingFooterClearsStaleShardData() {
    assertEquals(new FooterExtractor.ShardData(null, 0), FooterExtractor.parseFooter(null));
    assertEquals(new FooterExtractor.ShardData(null, 0), FooterExtractor.parseFooter("Loading..."));
  }

  @Test
  void overflowingPlayerCountIsIgnored() {
    assertEquals(0, FooterExtractor.parseFooter("999999999999999999 here").players());
  }

  @Test
  void cappedProfessionLevelsDoNotRecurseBeyondCap() {
    assertEquals(176, ProfessionItem.getNeededXP(1));
    assertEquals(0, ProfessionItem.getNeededXP(100));
    assertEquals(0, ProfessionItem.getNeededXP(Integer.MAX_VALUE));
    assertTrue(ProfessionItem.getNeededXP(20) > ProfessionItem.getNeededXP(19));
  }

  @Test
  void settingsRoundTripMaintainsPrimitiveTypesAndFillsNewDefaults() {
    var gson =
        new GsonBuilder().registerTypeAdapter(Settings.class, new SettingsJsonAdapter()).create();
    var original = new Settings();
    original
        .getCategory(DRSettings.DEBUG_SPACING.getCategory())
        .setSettingValue(DRSettings.DEBUG_SPACING, 12.5D);
    original
        .getCategory(DRSettings.GLOWING_RARITIES_RARE.getCategory())
        .setSettingValue(DRSettings.GLOWING_RARITIES_RARE, false);
    var restored = gson.fromJson(gson.toJson(original), Settings.class);
    assertEquals(
        12.5D,
        restored
            .getCategory(DRSettings.DEBUG_SPACING.getCategory())
            .getSettingValue(DRSettings.DEBUG_SPACING, double.class));
    assertEquals(
        false,
        restored
            .getCategory(DRSettings.GLOWING_RARITIES_RARE.getCategory())
            .getSettingValue(DRSettings.GLOWING_RARITIES_RARE, boolean.class));
    var empty = gson.fromJson("{}", Settings.class);
    assertEquals(
        true,
        empty
            .getCategory(DRSettings.DISPLAY_DPS.getCategory())
            .getSettingValue(DRSettings.DISPLAY_DPS, boolean.class));
  }
}
