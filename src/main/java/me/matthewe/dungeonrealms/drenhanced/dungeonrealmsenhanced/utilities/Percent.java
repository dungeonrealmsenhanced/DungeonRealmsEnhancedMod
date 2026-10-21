package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities;

import net.minecraft.ChatFormatting;

/** Created by Matthew E on 8/7/2019 at 12:57 PM for the project DungeonRealmsDREnhanced */
public class Percent {
  private double percent;

  public Percent(double percent) {
    this.percent = percent;
  }

  public Percent(Number min, Number max) {
    this.percent = (((double) min * 100.0D) / (double) max);
  }

  public int getPercentAsInt() {
    if (percent > 100) {
      percent = 100;
    }
    return (int) percent;
  }

  public double getPercent() {
    if (percent > 100) {
      percent = 100;
    }
    return percent;
  }

  public ChatFormatting getDurabilityColor() {
    if (percent <= 30) {
      return ChatFormatting.RED;
    } else if (percent > 30 && percent <= 65) {
      return ChatFormatting.YELLOW;
    } else if (percent > 65 && percent <= 100) {
      return ChatFormatting.GREEN;
    }
    return ChatFormatting.GREEN;
  }

  public ChatFormatting getColor() {
    if (percent <= 25) {
      return ChatFormatting.RED;
    } else if (percent > 25 && percent <= 50) {
      return ChatFormatting.YELLOW;
    } else if (percent > 50 && percent <= 75) {
      return ChatFormatting.GOLD;
    } else if (percent > 75 && percent <= 100) {
      return ChatFormatting.GREEN;
    }
    return ChatFormatting.GREEN;
  }
}
