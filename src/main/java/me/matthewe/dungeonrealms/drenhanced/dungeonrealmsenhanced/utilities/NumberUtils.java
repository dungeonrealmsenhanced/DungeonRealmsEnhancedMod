package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities;

import java.util.ArrayList;
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
    public static boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static int[] range(int min, int max) {
        List<Integer> integers = new ArrayList<>();
        if (min == max) {
            return new int[]{min, max};
        }
        for (int i = min; i <= max; i++) {
            integers.add(i);
        }
        int[] ints = new int[integers.size() - 1];
        for (int i = 0; i < ints.length; i++) {
            ints[i] = integers.get(i);
        }
        return ints;

    }


    public static int getInteger(String input) {
        return Integer.parseInt(input);
    }

    public static double getNumber(String input) {
        if (!isInteger(input) && isDouble(input)) {
            return getDouble(input);
        }
        return getInteger(input);
    }

    public static boolean isNumber(String input) {
        return isInteger(input) || isDouble(input);
    }

    public static double getDouble(String input) {
        return Double.parseDouble(input);
    }

    public static boolean isDouble(String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
