package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.listeners.buff;

/**
 * Created by Matthew E on 12/27/2020 at 10:55 PM for the project DungeonRealmsEnhancedMod
 */
public class Buff {
    private int minutes;
    private String  player;
    private BuffType buffType;
    private int percent;

    public Buff(int minutes, String player, BuffType buffType, int percent) {
        this.minutes = minutes;
        this.player = player;
        this.buffType = buffType;
        this.percent = percent;
    }

    @Override
    public String toString() {
        return "Buff{" +
                "minutes=" + minutes +
                ", player='" + player + '\'' +
                ", buffType=" + buffType +
                ", percent=" + percent +
                '}';
    }

    public int getMinutes() {
        return minutes;
    }

    public String getPlayer() {
        return player;
    }

    public BuffType getBuffType() {
        return buffType;
    }

    public int getPercent() {
        return percent;
    }
}
