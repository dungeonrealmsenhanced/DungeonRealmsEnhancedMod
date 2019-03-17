package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.nostalgia;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handler.Handler;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.nostalgia.listeners.DebugListener;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.nostalgia.listeners.HealthBarListener;

/**
 * Created by Matthew E on 3/17/2019 at 4:20 PM for the project DungeonRealmsDREnhanced
 */
public class NostalgiaHandler extends Handler {
    public NostalgiaHandler() {
        super(true);
    }

    @Override
    public void onEnable() {
        registerListener(new DebugListener(this));
        registerListener(new HealthBarListener());

    }

    @Override
    public void onDisable() {

    }
}
