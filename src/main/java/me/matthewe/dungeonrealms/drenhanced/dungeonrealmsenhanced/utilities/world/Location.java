package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.world;

/**
 * Created by Matthew E on 3/10/2019 at 11:17 AM for the project DungeonRealmsDREnhanced
 */
public class Location {
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;

    public Location(double x, double y, double z, float yaw, float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public String getXYZToString() {
        return "X: " + (int)x + ", Y: " + (int)y + ", Z: " + (int)z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }
}
