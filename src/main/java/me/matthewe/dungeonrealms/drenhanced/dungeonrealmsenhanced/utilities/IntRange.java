package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities;

public class IntRange {
    private int min;
    private int max;

    public IntRange(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public static IntRange fromString(String input) {
        if (input==null)return null;
        if (input.isEmpty())return null;

        input = input.trim();

        if (input.contains("-")){
            String[] split = input.split("-");
            int max;
            int min;

            try {
                min =Integer.parseInt(split[0]);
                max =Integer.parseInt(split[1]);
            } catch (Exception e) {
                return null;
            }
            return new IntRange(min,max);
        }
        int min =0;
        try {
            min =Integer.parseInt(input.trim());
        } catch (Exception e) {
            return null;
        }
        return new IntRange(min,min);
    }

    public int getMin() {
        return min;
    }

    @Override
    public String toString() {
        if (min==max){
            return min+"";
        }
        return min+"-"+max;
    }

    public int getMax() {
        return max;
    }
}
