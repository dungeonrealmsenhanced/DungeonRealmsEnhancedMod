package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.listeners;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.setting.DRSettings;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.*;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.ItemData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;

public final class RarityOverlayListener {
  public static long inventoryGems;

  public static void updateGems() {
    inventoryGems = 0;
    var player = Minecraft.getInstance().player;
    if (player == null) return;
    for (var stack : player.getInventory().getNonEquipmentItems()) {
      if (stack.is(Items.EMERALD)) inventoryGems += stack.getCount();
      if (ItemData.tag(stack).contains("gemValue"))
        inventoryGems += Math.max(0, ItemData.tag(stack).getLongOr("gemValue", 0L));
    }
  }

  @SubscribeEvent
  public void draw(ScreenEvent.Render.Foreground e) {
    if (!ServerContext.active() || !(e.getScreen() instanceof AbstractContainerScreen<?> screen))
      return;
    var g = e.getGuiGraphics();
    for (var slot : screen.getMenu().slots) {
      var stack = slot.getItem();
      if (stack.isEmpty()) continue;
      ItemRarity rarity = ItemUtils.getRarity(stack);
      int color = 0;
      if (ItemType.isClueScroll(stack) && DRSettings.GLOWING_CLUE_SCROLL.get(boolean.class))
        color = 0xffff5555;
      else if (rarity != null) {
        DRSettings setting =
            switch (rarity) {
              case COMMON -> DRSettings.GLOWING_RARITIES_COMMON;
              case UNCOMMON -> DRSettings.GLOWING_RARITIES_UNCOMMON;
              case RARE -> DRSettings.GLOWING_RARITIES_RARE;
              case EPIC -> DRSettings.GLOWING_RARITIES_EPIC;
              case LEGENDARY -> DRSettings.GLOWING_RARITIES_LEGENDARY;
              case MYTHIC -> DRSettings.GLOWING_RARITIES_MYTHIC;
            };
        if (setting.get(boolean.class)) {
          var c = rarity.getRgbaColor();
          color =
              0xff000000 | ((int) (c.r * 255) << 16) | ((int) (c.g * 255) << 8) | (int) (c.b * 255);
        }
      }
      if (color != 0)
        g.outline(screen.leftPos + slot.x - 1, screen.topPos + slot.y - 1, 18, 18, color);
    }
    if (DRSettings.GEM_COUNT_INVENTORY_OVERLAY.get(boolean.class)) {
      updateGems();
      g.text(
          Minecraft.getInstance().font,
          String.format("%,dg", inventoryGems),
          screen.leftPos + 8,
          screen.topPos - 12,
          0xff55ff55);
    }
  }
}
