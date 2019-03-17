package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.world;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.nostalgia.listeners.HealthBarListener;
import net.minecraft.world.BossInfo;

import java.util.Arrays;

/**
 * Created by Matthew E on 3/17/2019 at 6:22 PM for the project DungeonRealmsDREnhanced
 */
public enum Zone {
    SAFE(BossInfo.Color.GREEN), WILDERNESS(BossInfo.Color.YELLOW), CHAOTIC(BossInfo.Color.RED);

    private BossInfo.Color color;

    Zone(BossInfo.Color color) {
        this.color = color;
    }

    public static Zone get() {
        return HealthBarListener.getZone();
    }

    public BossInfo.Color getColor() {
        return color;
    }

    public static Zone byColor(BossInfo.Color color) {
        return Arrays.stream(values()).filter(zone -> zone.color == color).findFirst().orElse(null);
    }
}
