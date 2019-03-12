package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.render;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.Module;

/**
 * Created by Matthew E on 12/31/2018 at 4:44 PM for the project DungeonRealmsDREnhanced
 */
@FunctionalInterface
public interface RenderText<T extends Module> {
    String getString(T t);
}
