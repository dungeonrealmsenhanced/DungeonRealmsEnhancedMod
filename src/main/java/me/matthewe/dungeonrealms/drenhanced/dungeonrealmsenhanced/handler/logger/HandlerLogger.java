package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handler.logger;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.DREnhanced;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handler.Handler;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.DRLogger;

/**
 * Created by Matthew E on 3/12/2019 at 10:41 AM for the project DungeonRealmsDREnhanced
 */
public class HandlerLogger implements DRLogger {
    private String handlerName;
    private boolean debug;

    public HandlerLogger(Class<? extends Handler> clazz, boolean debug) {
        this.handlerName = clazz.getSimpleName();
        this.debug = debug;
    }

    @Override
    public void info(String message, Object... objects) {
        String formattedMessage = String.format(message, objects);
        DREnhanced.log("[%s] [%s]: %s", "INFO", this.handlerName, formattedMessage);
    }

    @Override
    public void debug(String message, Object... objects) {
        if (this.debug) {
            String formattedMessage = String.format(message, objects);
            DREnhanced.log("[%s] [%s]: %s", "DEBUG", this.handlerName, formattedMessage);
        }
    }
}
