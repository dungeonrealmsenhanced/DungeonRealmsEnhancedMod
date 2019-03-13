package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.mining;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handler.Handler;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.player.profession.mining.MiningThread;

/**
 * Created by Matthew Eisenberg on 3/13/2019 at 9:52 AM for the project DungeonRealmsDREnhanced
 */
public class MiningHandler extends Handler {
    private static MiningThread miningThread;

    public MiningHandler() {
        super(true);
    }

    @Override
    public void onEnable() {
        miningThread = new MiningThread();
        miningThread.start();
        miningThread.setRunning(true);
    }

    public  static MiningThread getMiningThread() {
        return miningThread;
    }

    @Override
    public void onDisable() {
        miningThread.setRunning(false);
        try {
            miningThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
