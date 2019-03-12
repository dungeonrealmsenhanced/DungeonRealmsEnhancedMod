package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.misc;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handler.Handler;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.misc.listeners.*;

/**
 * Created by Matthew E on 3/12/2019 at 11:03 AM for the project DungeonRealmsDREnhanced
 */
public class MiscHandler extends Handler {

    public MiscHandler() {
        super(true);
    }

    @Override
    public void onEnable() {
        this.registerListener(new DefaultOverlayListener());
        this.registerListener(new NBTListener());
        this.registerListener(new ItemOriginListener());
        this.registerListener(new PauseMenuButtonListener());
        this.registerListener(new TipListener());
        this.registerListener(new NewLoreListener());
    }

    @Override
    public void onDisable() {

    }
}
