package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.mining.listeners;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.modules.profession.ProfessionItem;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.player.DRPlayer;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.player.profession.mining.Mining;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.setting.DRSettings;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.Listener;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.Percent;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.ItemData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientChatReceivedEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

/** Created by Matthew E on 3/19/2019 at 2:43 PM for the project DungeonRealmsDREnhanced */
public class MiningListener implements Listener {
  private Pattern pattern =
      Pattern.compile(".*\\+([0-9]*)\\sEXP\\s(\\[)([0-9]*)\\/([0-9]*)\\sEXP\\]");
  private Pattern patternExperienceBuff =
      Pattern.compile(".*\\+([0-9]*)\\sEXP\\s\\[EXP\\sBUFF\\]\\s(\\[)([0-9]*)\\/([0-9]*)\\sEXP\\]");
  public static final boolean BOTTLE_EXPERIENCE_ENABLED = false;
  public static final boolean NEW_LORE_ENABLED = false;

  @SubscribeEvent
  public void onItemTooltip(ItemTooltipEvent event) {
    if (!me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.listeners.ServerContext
        .active()) return;
    me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.TooltipText.edit(
        event,
        tooltip -> {
          ItemStack itemStack = event.getItemStack();
          if (itemStack != null && !itemStack.isEmpty() && ItemData.hasData(itemStack)) {

            if (DRSettings.MISC_PROFESSION_EXP.get(boolean.class)
                && ProfessionItem.of(itemStack) != null) {
              CompoundTag tagCompound = ItemData.tag(itemStack);

              List<String> newToolTip = new ArrayList<>();
              String last = tooltip.subList(tooltip.size() - 1, tooltip.size()).get(0);
              for (String s : tooltip.subList(0, tooltip.size() - 1)) {
                newToolTip.add(s);
              }
              if (tagCompound.contains("bottleXP") && BOTTLE_EXPERIENCE_ENABLED) {
                double integer = tagCompound.getDoubleOr("bottleXP", 0.0);
                double percentage = ((double) integer * 100.0D) / 100000.0D;

                if (Minecraft.getInstance().hasShiftDown()) {
                  newToolTip.add(
                      ChatFormatting.GRAY
                          + ChatFormatting.BOLD.toString()
                          + "XP Bottles Used: "
                          + new Percent(percentage).getColor()
                          + new DecimalFormat("#,###").format(integer)
                          + ChatFormatting.GRAY
                          + "/"
                          + new Percent(100).getColor()
                          + "100,000");

                } else {
                  String expBar = "||||||||||||||||||||||||||||||||||||||||||||||||||";
                  double percentDone = percentage;
                  int display = (int) (percentDone / 2D);
                  display = Math.max(1, Math.min(display, 50));
                  String formattedXPBar;
                  if (percentDone == 0) {
                    formattedXPBar = ChatFormatting.RED + expBar;
                  } else {
                    formattedXPBar =
                        ChatFormatting.GREEN
                            + expBar.substring(0, display)
                            + ChatFormatting.RED
                            + expBar.substring(display, expBar.length());
                  }

                  newToolTip.add(
                      ChatFormatting.GRAY
                          + ChatFormatting.BOLD.toString()
                          + "XP Bottles Used: "
                          + formattedXPBar);
                }
              }
              newToolTip.add(last);

              if (!newToolTip.isEmpty()) {
                tooltip.clear();
                tooltip.addAll(newToolTip);
              }
            }
            ProfessionItem of = ProfessionItem.of(itemStack);
            if (of != null
                && (DRSettings.MISC_PROFESSION_PERCENT_LEFT.get(boolean.class))
                && DRPlayer.get() != null) {

              List<String> newToolTip = new ArrayList<>();
              String last = tooltip.subList(tooltip.size() - 1, tooltip.size()).get(0);
              for (String s : tooltip.subList(0, tooltip.size() - 1)) {
                newToolTip.add(s);
              }
              Mining mining = DRPlayer.get().getMining();

              long remainingExperienceForNextTier = mining.getRemainingExperienceForNextTier(of);

              long maxXPForTier = of.getTier().getExperienceRequirement();

              if ((remainingExperienceForNextTier > 0) && (maxXPForTier > 0)) {
                double percent =
                    (100.0D
                        - ((double) remainingExperienceForNextTier * 100.0D)
                            / (double) maxXPForTier);
                if (percent > 100.0D) {
                  percent = 100.0D;
                }
                newToolTip.add(
                    ChatFormatting.GRAY
                        + "Progress: "
                        + new Percent(percent).getColor()
                        + new DecimalFormat(DRPlayer.get().getPercentFormat()).format(percent)
                        + ChatFormatting.BOLD
                        + "%");
              }

              newToolTip.add(last);

              if (!newToolTip.isEmpty()) {
                tooltip.clear();
                tooltip.addAll(newToolTip);
              }
            }
          }
        });
  }

  @SubscribeEvent(priority = EventPriority.LOW)
  public void onClientChatReceived(ClientChatReceivedEvent event) {
    if (!me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.listeners.ServerContext
        .active()) return;
    ProfessionItem item = ProfessionItem.get();
    if (item == null) return;
    var match =
        java.util.regex.Pattern.compile(
                "\\+([\\d,]+) EXP\\s*\\[\\s*([\\d,]+)\\s*/\\s*([\\d,]+)\\s*EXP\\s*\\]")
            .matcher(event.getMessage().getString());
    if (!match.find()) return;
    try {
      int gain = Integer.parseInt(match.group(1).replace(",", ""));
      int current = Integer.parseInt(match.group(2).replace(",", ""));
      int max = Integer.parseInt(match.group(3).replace(",", ""));
      if (max <= 0) return;
      if ((item.isFishingRod() && DRSettings.FISHING_EXP_PERCENTAGE.get(boolean.class))
          || (item.isPickaxe() && DRSettings.MINING_EXP_PERCENTAGE.get(boolean.class))) {
        double percent = Math.max(0, Math.min(100, 100.0 * current / max));
        var suffix =
            Component.literal(
                    " ["
                        + new DecimalFormat(DRPlayer.get().getPercentFormat()).format(percent)
                        + "%]")
                .withStyle(net.minecraft.ChatFormatting.GREEN)
                .withStyle(
                    style ->
                        style.withHoverEvent(
                            new HoverEvent.ShowText(
                                Component.literal(
                                    "+"
                                        + new DecimalFormat(DRPlayer.get().getPercentFormat())
                                            .format(100.0 * gain / max)
                                        + "%"))));
        event.setMessage(event.getMessage().copy().append(suffix));
      }
      DRPlayer.get().getMining().onExperienceGain(gain);
    } catch (NumberFormatException ignored) {
    }
  }
}
