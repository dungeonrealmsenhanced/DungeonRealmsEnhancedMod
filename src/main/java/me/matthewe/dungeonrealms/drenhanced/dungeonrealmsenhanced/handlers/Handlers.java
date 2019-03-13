package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handler.Handler;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.keybinds.KeyBindHandler;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.mining.MiningHandler;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.misc.MiscHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthew E on 3/12/2019 at 10:54 AM for the project DungeonRealmsDREnhanced
 */
public class Handlers {
    private static List<Handler> handlers = new ArrayList<>();

    public static void addHandler(Handler handler) {
        handlers.add(handler);
    }

    public static <T extends Handler> T get(Class<T> clazz) {
        for (Handler handler : handlers) {
            if (handler.getClass().equals(clazz)) {
                return (T) handler;
            }
        }
        return null;
    }

    public static List<Handler> getHandlers() {
        return handlers;
    }

    public static void enableHandlers() {
        handlers.stream().filter(handler -> !handler.isEnabled()).forEach(Handler::enable);
    }

    public static void disableHandlers() {
        handlers.stream().filter(Handler::isEnabled).forEach(Handler::disable);
    }

    public static void init() {
        addHandler(new MiscHandler());
        addHandler(new KeyBindHandler());
        addHandler(new MiningHandler());
    }
}
