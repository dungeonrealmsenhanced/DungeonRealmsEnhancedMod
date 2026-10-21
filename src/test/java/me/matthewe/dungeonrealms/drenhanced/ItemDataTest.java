package me.matthewe.dungeonrealms.drenhanced;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.modules.profession.ProfessionItem;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.*;
import net.minecraft.SharedConstants;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemLore;
import net.neoforged.testframework.junit.EphemeralTestServerProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(EphemeralTestServerProvider.class)
class ItemDataTest {
  @BeforeAll
  static void bootstrap() {
    SharedConstants.tryDetectVersion();
    Bootstrap.bootStrap();
  }

  private static ListTag range(double min, double max) {
    var list = new ListTag();
    list.add(DoubleTag.valueOf(min));
    list.add(DoubleTag.valueOf(max));
    return list;
  }

  private static ItemStack sword() {
    var item = new ItemStack(Items.DIAMOND_SWORD);
    var tag = new CompoundTag();
    tag.putString("type", "WEAPON");
    tag.putString("rarity", "RARE");
    tag.putInt("level", 60);
    var mods = new CompoundTag();
    mods.put("MELEE_DAMAGE", range(100, 200));
    mods.putInt("VS_MONSTERS", 100);
    mods.putInt("ICE_DAMAGE", 10);
    var augments = new CompoundTag();
    augments.putDouble("MELEE_DAMAGE", 5);
    tag.put("modifiers", mods);
    tag.put("augmentations", augments);
    item.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    return item;
  }

  @Test
  void readingCustomDataNeverMutatesServerItem(net.minecraft.server.MinecraftServer server) {
    var item = sword();
    ItemData.tag(item).putString("type", "ARMOR");
    assertEquals("WEAPON", ItemData.tag(item).getStringOr("type", ""));
    assertTrue(ItemData.tag(ItemStack.EMPTY).isEmpty());
  }

  @Test
  void componentLoreWorksWithoutLegacyDisplayNbt(net.minecraft.server.MinecraftServer server) {
    var item = new ItemStack(Items.PAPER);
    item.set(DataComponents.LORE, new ItemLore(List.of(Component.literal("Find the key"))));
    assertEquals(List.of("Find the key"), ItemUtils.getLore(item));
    assertEquals(List.of(), ItemUtils.getLore(ItemStack.EMPTY));
  }

  @Test
  void numericModifiersAndScalarAugmentsPreserveDamageRange(
      net.minecraft.server.MinecraftServer server) {
    var item = sword();
    assertArrayEquals(new double[] {105, 205}, ItemUtils.getModifierMap(item).get("MELEE_DAMAGE"));
    assertArrayEquals(
        new double[] {100, 200}, ItemUtils.getModifierMap(item, true).get("MELEE_DAMAGE"));
    assertArrayEquals(new int[] {220, 420}, ItemUtils.calculateDPS(item));
  }

  @Test
  void incompleteAndNonNumericModifiersAreIgnored(net.minecraft.server.MinecraftServer server) {
    var item = new ItemStack(Items.IRON_SWORD);
    var data = new CompoundTag();
    var mods = new CompoundTag();
    mods.putString("bad", "not a number");
    mods.putDouble("notFinite", Double.NaN);
    data.put("modifiers", mods);
    item.set(DataComponents.CUSTOM_DATA, CustomData.of(data));
    assertTrue(ItemUtils.getModifierMap(item).isEmpty());
    assertArrayEquals(new int[] {0, 0}, ItemUtils.calculateDPS(item));
  }

  @Test
  void mythicFlagTakesPrecedenceOverRarityLabel(net.minecraft.server.MinecraftServer server) {
    var item = sword();
    var tag = ItemData.tag(item);
    tag.putBoolean("mythic", true);
    item.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    assertEquals(ItemRarity.MYTHIC, ItemUtils.getRarity(item));
  }

  @Test
  void professionReadsLevelAndExperienceFromCustomData(
      net.minecraft.server.MinecraftServer server) {
    var item = new ItemStack(Items.DIAMOND_PICKAXE);
    var data = new CompoundTag();
    data.putString("type", "PICKAXE");
    data.putInt("level", 65);
    data.putInt("xp", 12345);
    item.set(DataComponents.CUSTOM_DATA, CustomData.of(data));
    var profession = ProfessionItem.of(item);
    assertNotNull(profession);
    assertTrue(profession.isPickaxe());
    assertEquals(65, profession.getLevel());
    assertEquals(12345, profession.getExperience());
  }

  @Test
  void bundledStatSnapshotContainsArmorWeaponAndKeyMappings(
      net.minecraft.server.MinecraftServer server) throws Exception {
    var database = new me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.StatKeyDatabase();
    try (var reader =
        new java.io.InputStreamReader(
            getClass().getResourceAsStream("/assets/dungeonrealmsenhanced/stat-data.json"),
            java.nio.charset.StandardCharsets.UTF_8)) {
      database.update(
          new com.google.gson.Gson().fromJson(reader, com.google.gson.JsonObject.class));
    }
    assertFalse(database.getKeyDictionary().isEmpty());
    assertNotNull(database.getWeaponValue(Tier.T5, ItemRarity.RARE));
    assertNotNull(database.getArmorValue(Tier.T5, ItemRarity.RARE));
  }

  @Test
  void tooltipAdditionsPreserveOriginalRichText(net.minecraft.server.MinecraftServer server) {
    var rich = Component.literal("Named Sword").withStyle(net.minecraft.ChatFormatting.AQUA);
    var lines = new java.util.ArrayList<net.minecraft.network.chat.Component>(List.of(rich));
    var event =
        new net.neoforged.neoforge.event.entity.player.ItemTooltipEvent(
            sword(),
            null,
            lines,
            net.minecraft.world.item.TooltipFlag.NORMAL,
            net.minecraft.world.item.Item.TooltipContext.EMPTY,
            net.minecraft.world.item.component.TooltipDisplay.DEFAULT);
    me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.TooltipText.edit(
        event, text -> text.set(0, text.getFirst() + " (50%)"));
    assertEquals("Named Sword (50%)", lines.getFirst().getString());
    assertTrue(lines.getFirst().getSiblings().contains(rich));
    assertEquals(
        net.minecraft.network.chat.TextColor.fromLegacyFormat(net.minecraft.ChatFormatting.AQUA),
        rich.getStyle().getColor());
  }
}
