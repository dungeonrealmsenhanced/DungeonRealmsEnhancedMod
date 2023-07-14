package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.listeners.buff;

import java.util.List;

/**
 * Created by Matthew E on 12/27/2020 at 10:56 PM for the project DungeonRealmsEnhancedMod
 */
public class BuffRequestReturn {
    private List<Buff> activeBuffs;

    public BuffRequestReturn(List<Buff> activeBuffs) {
        this.activeBuffs = activeBuffs;
    }

    public List<Buff> getActiveBuffs() {
        return activeBuffs;
    }
}
