package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities;

public class IntRange {
    private int min;
    private int max;

    public IntRange(int min, int max) {
        this.min = min;
        this.max = max;
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
