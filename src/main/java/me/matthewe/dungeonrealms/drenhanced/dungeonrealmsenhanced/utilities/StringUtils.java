package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities;

/**
 * Created by Matthew E on 3/10/2019 at 5:37 PM for the project DungeonRealmsDREnhanced
 */
public class StringUtils {
    public static String formatShard(String shard) {
        return shard.replaceAll("US", "US-").replaceAll("SUB", "SUB-").replaceAll("BS", "BS-");
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
}
