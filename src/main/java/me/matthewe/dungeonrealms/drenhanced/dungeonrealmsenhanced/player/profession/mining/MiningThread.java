package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.player.profession.mining;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.modules.profession.ProfessionItem;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.NumberUtils;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.Tier;

import java.util.*;

/**
 * Created by Matthew E on 3/13/2019 at 9:26 AM for the project DungeonRealmsDREnhanced
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
                    case XP_PER_MINUTE:

                        break;
                    case MINING_STATS:


                        final int neededExperience = professionItem.getNeededExperience(professionItem.getLevel() + 1);
                        int remainingExperience = professionItem.getExperience();
                        final int startRemainingExperience = professionItem.getExperience();

                        MiningDataResult.Builder builder = MiningDataResult.builder();
                        OreTier byTier = OreTier.getByTier(professionItem.getTier());
                        if (byTier != null) {

                            int failCount = 0;
                            int workCount = 0;
                            int ore = 0;
                            List<Integer> expList = new ArrayList<>();
                            while (remainingExperience < neededExperience) {
                                int spotTier = professionItem.getTier().getNumber();
//                                if (spotTier >= 2) {
//                                    spotTier--;
//                                }
                                int successRate = professionItem.getTier().getNumber() > spotTier ? (professionItem.getTier().getNumber() - spotTier) * 20 + 20 : 0;

                                if (professionItem.getTier().getNumber() == spotTier)
                                    successRate = 50 + 2 * (20 - Math.abs(byTier.getNextTierLevel() - professionItem.getLevel()));

                                if (professionItem.getTier().getNumber() > spotTier) {
                                    successRate = 100;
                                }
                                int roll = new Random().nextInt(100);
                                if (successRate <= roll) {
                                    failCount++;

                                } else {
                                    workCount++;

                                    int exp = byTier.getExperience();
                                    expList.add(exp);

                                    remainingExperience += exp;
                                }
                                ore++;

                            }
                            builder.averageCurrentExperience(NumberUtils.getAverage(expList)).currentTierFailCount(failCount).currentTierSuccessCount(workCount).currentTierOreRemaining(ore);

                            int secondOre = 0;
                            int secondFailCount = 0;
                            int secondWorkCount = 0;
                            List<Integer> secondExpList = new ArrayList<>();
                            remainingExperience = startRemainingExperience;
                            int spotTier = professionItem.getTier().getNumber();
                            if (spotTier >= 2) {
                                spotTier--;

                            }
                            byTier = OreTier.byNumber(spotTier);
                            while (remainingExperience < neededExperience) {
                                int successRate = professionItem.getTier().getNumber() > spotTier ? (professionItem.getTier().getNumber() - spotTier) * 20 + 20 : 0;

                                if (professionItem.getTier().getNumber() == spotTier)
                                    successRate = 50 + 2 * (20 - Math.abs(byTier.getNextTierLevel() - professionItem.getLevel()));

                                if (professionItem.getTier().getNumber() > spotTier) {
                                    successRate = 100;
                                }
                                int roll = new Random().nextInt(100);
                                if (successRate <= roll) {
                                    secondFailCount++;

                                } else {
                                    secondWorkCount++;
                                    int exp = byTier.getExperience();
                                    secondExpList.add(exp);

                                    remainingExperience += exp;

                                }
                                secondOre++;

                            }
                            builder.averageLowerExperience(NumberUtils.getAverage(secondExpList)).lowerTierFailCount(secondFailCount).lowerTierOreRemaining(secondOre).lowerTierSuccessCount(secondWorkCount);
                            miningRequest.getResult().onResult(builder.build());
                        }
                        break;
                }
            }
        }
    }
}
