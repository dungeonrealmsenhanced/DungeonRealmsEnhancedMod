package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.listeners.buff;

/**
 * Created by Matthew E on 12/27/2020 at 10:56 PM for the project DungeonRealmsEnhancedMod
 */
public interface BuffRequest {
    void onReturnInfo(BuffRequestReturn buffRequestReturn);

    void onFailure();
}
