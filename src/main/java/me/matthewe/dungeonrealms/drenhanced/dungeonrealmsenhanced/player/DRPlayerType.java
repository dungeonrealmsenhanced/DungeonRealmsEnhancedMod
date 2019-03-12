package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.player;

/**
 * Created by Matthew E on 3/10/2019 at 5:19 PM for the project DungeonRealmsDREnhanced
 */
public enum DRPlayerType {
    REAL("play.dungeonrealms.net"), PS;
    private String ip;

    DRPlayerType(String ip) {
        this.ip = ip;

    }

    DRPlayerType() {
        this.ip = null;
    }

    public String getIp() {
        return ip;
    }

    public boolean isReal() {
        return this == REAL;
    }
}
