package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.player.profession.mining;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.modules.profession.ProfessionItem;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.Tier;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Created by Matthew Eisenberg on 3/13/2019 at 9:26 AM for the project DungeonRealmsDREnhanced
 */
public class MiningThread extends Thread {

    private boolean running;
    private Queue<MiningRequest> requestMap;

    public MiningThread() {
        this.running = false;
        this.requestMap = new PriorityQueue<>();
    }

    @Override
    public synchronized void start() {
        super.start();
        running = true;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void request(MiningRequestType type, MiningRequestResult result) {
        this.requestMap.add(new MiningRequest(type, result));
    }

    public boolean isRunning() {
        return running;
    }

    private ProfessionItem getProfessionItem() {
        return ProfessionItem.get();
    }

    @Override
    public void run() {
        while (this.running) {
            try {
                sleep(250L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (this.requestMap.isEmpty()) {
                continue;
            }
            MiningRequest miningRequest = this.requestMap.poll();
            ProfessionItem professionItem = getProfessionItem();
            if (professionItem == null) {
                requestMap.clear();
                continue;
            }
            Tier tier = professionItem.getTier();
            if (tier == null) {
                continue;
            }
            if (miningRequest != null) {
                switch (miningRequest.getMiningRequestType()) {
                    case ORE_REMAINING:
                        int neededExperience = professionItem.getNeededExperience(professionItem.getLevel() + 1);
                        int remainingExperience = neededExperience - professionItem.getExperience();

                        int ore = 0;
                        OreTier byTier = OreTier.getByTier(professionItem.getTier());
                        if (byTier != null) {
                            while (remainingExperience > 0) {
                                remainingExperience -= byTier.getRandomExperience();
                                ore++;
                            }
                            miningRequest.getResult().onResult(ore);
                        }
                        break;
                }
            }
        }
    }
}
