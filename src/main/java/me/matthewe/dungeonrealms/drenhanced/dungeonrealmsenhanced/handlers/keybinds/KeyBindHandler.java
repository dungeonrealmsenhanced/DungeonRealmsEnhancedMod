package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.keybinds;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handler.Handler;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.keybinds.listeners.KeyBindListener;

/**
 * Created by Matthew E on 3/12/2019 at 10:57 AM for the project DungeonRealmsDREnhanced
 */
public class KeyBindHandler extends Handler {
    public KeyBinds keyBinds;
    private static KeyBindHandler instance;

    public static KeyBindHandler getInstance() {
        return instance;
    }

    public KeyBindHandler() {
        super(true);
        instance=this;
    }

    @Override
    public void onEnable() {
        this.keyBinds = new KeyBinds(this);
        this.keyBinds.init();

        this.registerListener(new KeyBindListener(this));
    }

    @Override
    public void onDisable() {

    }
}
