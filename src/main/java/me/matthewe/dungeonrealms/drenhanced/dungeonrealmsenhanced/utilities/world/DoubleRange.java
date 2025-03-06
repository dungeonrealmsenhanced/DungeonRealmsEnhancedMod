package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.world;

import java.text.DecimalFormat;

public class DoubleRange {
    private double min;
    private double max;

    public DoubleRange(double min, double max) {
        this.min = min;
        this.max = max;
    }

    public static DoubleRange fromString(String input) {
        if (input==null)return null;
        if (input.isEmpty())return null;

        input = input.trim();

        if (input.contains("-")){
            String[] split = input.split("-");
            double max;
            double min;

            try {
                min =Double.parseDouble(split[0]);
                max =Double.parseDouble(split[1]);
            } catch (Exception e) {
                return null;
            }
            return new DoubleRange(min,max);
        }
        double min =0;
        try {
            min =Double.parseDouble(input.trim());
        } catch (Exception e) {
            return null;
        }
        return new DoubleRange(min,min);
    }


    private static final DecimalFormat df = new DecimalFormat("#.##");
    @Override
    public String toString() {
        if (min==max){
            return df.format(min);
        }
        return df.format(min)+"-"+df.format(max);
    }

    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
    }
}
