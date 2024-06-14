package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.restful;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.commands.ChangelogCommand;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handler.Handler;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.restful.listeners.RestfulListener;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.restful.DREnhancedRestful;

/**
 * Created by Matthew E on 3/16/2019 at 2:46 PM for the project DungeonRealmsDREnhanced
 */
public class RestfulHandler extends Handler {
    private DREnhancedRestful drEnhancedRestful;

    public RestfulHandler() {
        super(true);
    }

    @Override
    public void onEnable() {

        this.drEnhancedRestful = new DREnhancedRestful();
//        registerListener(new RestfulListener(this));
//        registerCommand(new ChangelogCommand(this));
    }

    public DREnhancedRestful getDrEnhancedRestful() {
        return drEnhancedRestful;
    }

    @Override
    public void onDisable() {

    }
}
