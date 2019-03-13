package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities;

import java.util.List;

/**
 * Created by Matthew E on 3/13/2019 at 10:55 AM for the project DungeonRealmsDREnhanced
 */
public class NumberUtils {
    public static int getAverage(List<Integer> longs) {
        int total = 0;
        for (int aLong : longs) {
            total += aLong;
        }
        return total / longs.size();
    }
}
