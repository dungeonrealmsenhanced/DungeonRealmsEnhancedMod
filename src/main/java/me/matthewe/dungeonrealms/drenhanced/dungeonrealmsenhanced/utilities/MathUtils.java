package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities;

public class MathUtils {
    public static double sumArray(double[] values) {
        double sum = 0;
        for (double value : values) {
            sum += value;
        }
        return sum;
    }

    public static double sumArraysBySize(double[] array1, double[] array2) {
        if (array1.length == array2.length) {
            double[] result = new double[array1.length];
            for (int i = 0; i < array1.length; i++) {
                result[i] = array1[i] + array2[i];
            }
            return sumArray(result);
        } else {
            return sumArray(array1) + sumArray(array2);
        }
    }

    public static double[] elementWiseSum(double[] array1, double[] array2) {
        int maxLength = Math.max(array1.length, array2.length);
        double[] result = new double[maxLength];
        
        for (int i = 0; i < maxLength; i++) {
            double val1 = i < array1.length ? array1[i] : 0;
            double val2 = i < array2.length ? array2[i] : 0;
            result[i] = val1 + val2;
        }
        
        return result;
    }
}
