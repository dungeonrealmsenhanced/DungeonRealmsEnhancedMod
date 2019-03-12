package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.color;

/**
 * Created by Matthew E on 3/12/2019 at 9:20 AM for the project DungeonRealmsDREnhanced
 */
public class RGBAColor {
    public float r;
    public float g;
    public float b;
    public float a;

    public RGBAColor(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public RGBAColor(float r, float g, float b) {
        this(r, g, b, 1.0F);
    }

}