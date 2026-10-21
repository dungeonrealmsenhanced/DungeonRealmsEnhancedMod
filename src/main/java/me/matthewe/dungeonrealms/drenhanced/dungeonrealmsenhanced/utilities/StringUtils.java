package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities;

import java.util.List;
import java.util.function.Function;
import net.minecraft.ChatFormatting;

/** Created by Matthew E on 3/10/2019 at 5:37 PM for the project DungeonRealmsDREnhanced */
public class StringUtils {
  public static String formatShard(String shard) {
    return shard
        .toUpperCase()
        .replaceAll("US", "US-")
        .replaceAll("SUB", "SUB-")
        .replaceAll("EU", "EU-")
        .replaceAll("BS", "BS-");
  }

  public static void editStringList(List<String> list, Function<String, String> modifier) {
    list.replaceAll(modifier::apply);
  }

  public static String formatDungeon(String dungeon) {
    String dungeonName = formatEnum(dungeon).trim();
    if (dungeonName.equalsIgnoreCase("Varenglade")) {
      return ChatFormatting.AQUA + dungeonName;
    }
    if (dungeonName.equalsIgnoreCase("Infernal Abyss")) {
      return ChatFormatting.LIGHT_PURPLE + dungeonName;
    }
    if (dungeonName.equalsIgnoreCase("Crystal Void")) {
      return ChatFormatting.YELLOW + dungeonName;
    }
    if (dungeonName.equalsIgnoreCase("Conjurers Chapel")) {
      return ChatFormatting.GREEN + dungeonName;
    }
    return dungeonName;
  }

  public static String formatTime(int seconds) {
    long minutes = 0;
    long hours = 0;
    while (seconds >= 60) {
      seconds -= 60;
      minutes++;
    }
    while (minutes >= 60) {
      minutes -= 60;
      hours++;
    }
    return hours + "h " + minutes + "m " + seconds + "s";
  }

  public static String formatEnum(String s) {
    String returnString = "";
    if (s.contains("_")) {
      for (String s1 : s.split("_")) {
        returnString += formatEnum(s1) + " ";
      }
    } else {
      String partOne = s.substring(0, 1).toUpperCase();
      String partTwo = s.substring(1).toLowerCase();
      returnString = partOne + partTwo;

      return returnString.trim();
    }
    return returnString.trim();
  }

  public static String clearColor(String s) {
    return new String(s).replaceAll("§.", "").trim();
  }
}
