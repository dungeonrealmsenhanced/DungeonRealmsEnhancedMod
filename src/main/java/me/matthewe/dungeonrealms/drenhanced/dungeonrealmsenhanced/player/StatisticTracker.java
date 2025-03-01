package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.player;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.Tier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Matthew E on 12/31/2018 at 2:42 PM for the project DungeonRealmsDREnhanced
 */
public class StatisticTracker extends Thread {
    private long times = 0;
    private boolean running = false;

    @Override
    public synchronized void start() {
        super.start();
        running = true;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {
        while (running) {
            times++;
//            System.out.println("[StatisticTracker] Running (" + times + ")");
            this.updateStatistics();
            try {
                sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateStatistics() {

        if (Minecraft.getMinecraft().ingameGUI == null) {
            return;
        }
        GuiPlayerTabOverlay tabList = Minecraft.getMinecraft().ingameGUI.getTabList();

        if (Minecraft.getMinecraft().getConnection() == null) {
            return;
        }

        List<NetworkPlayerInfo> playersC = new ArrayList<>(Minecraft.getMinecraft().getConnection().getPlayerInfoMap());

        for (NetworkPlayerInfo loadedPlayer : playersC) {
            String playerName = tabList.getPlayerName(loadedPlayer).trim();
            if (playerName.isEmpty()) {
                continue;
            }
            playerName = playerName.replaceAll("\n", "");
            boolean print = false;
            String tempPlayerName = TextFormatting.getTextWithoutFormattingCodes(playerName);
            if ((tempPlayerName == null) || tempPlayerName.isEmpty()) {
                continue;
            }
            for (int i = 0; i < tempPlayerName.length(); i++) {
                if (tempPlayerName.charAt(i) != ' ') {
                    print = true;
                }
            }
            if (!print) {
                continue;
            }

            playerName = TextFormatting.getTextWithoutFormattingCodes(playerName);
            if ((playerName == null) || playerName.isEmpty()) {
                continue;
            }

            playerName = playerName.trim();
            boolean guild = true;
            if (playerName.startsWith("Ore Mined: ")){
                int oreMined = Integer.parseInt(playerName.split("Ore Mined: ")[1].trim());
                DRPlayer.drPlayer.getStatistics().setOreMined(oreMined);
            }
            if (playerName.startsWith("Bank Gems: ")) {
//                System.out.println(playerName);
                DRPlayer.drPlayer.getStatistics().setBankGems((long) Integer.parseInt(playerName.split("Bank Gems: ")[1].trim()));
            }
            if (playerName.startsWith("Player Kills: ")) {
                String[] playerKillsAndDeaths = playerName.split("Player Kills: ")[1].trim().split(" ");
                int playerKills = Integer.parseInt(playerKillsAndDeaths[0].trim());
                int deaths = Integer.parseInt(playerKillsAndDeaths[2].trim());
                DRPlayer.drPlayer.getStatistics().setDeaths(deaths);
                DRPlayer.drPlayer.getStatistics().setPlayerKills(playerKills);
            }
            if (playerName.startsWith("Playtime: ")) {

                long time = 0L;
                String playtime = playerName.split("Playtime: ")[1].trim();
                if (playtime.contains(" ")) {
                    for (String s : playtime.split(" ")) {
                        if (s.contains("w")) {
                            time += TimeUnit.DAYS.toMillis(Long.parseLong(s.split("w")[0].trim()) * 7);
                        } else if (s.contains("d")) {
                            time += TimeUnit.DAYS.toMillis(Long.parseLong(s.split("d")[0].trim()));

                        } else if (s.contains("h")) {
                            time += TimeUnit.HOURS.toMillis(Long.parseLong(s.split("h")[0].trim()));
                        } else if (s.contains("m")) {
                            time += TimeUnit.MINUTES.toMillis(Long.parseLong(s.split("m")[0].trim()));
                        } else if (s.contains("s")) {
                            time += TimeUnit.SECONDS.toMillis(Long.parseLong(s.split("s")[0].trim()));
                        }
                    }
                } else {
                    String s = playtime.trim();
                    if (s.contains("w")) {
                        time += TimeUnit.DAYS.toMillis(Long.parseLong(s.split("w")[0].trim()) * 7);
                    } else if (s.contains("d")) {
                        time += TimeUnit.DAYS.toMillis(Long.parseLong(s.split("d")[0].trim()));

                    } else if (s.contains("h")) {
                        time += TimeUnit.HOURS.toMillis(Long.parseLong(s.split("h")[0].trim()));
                    } else if (s.contains("m")) {
                        time += TimeUnit.MINUTES.toMillis(Long.parseLong(s.split("m")[0].trim()));
                    } else if (s.contains("s")) {
                        time += TimeUnit.SECONDS.toMillis(Long.parseLong(s.split("s")[0].trim()));
                    }
                }
                DRPlayer.get().getStatistics().setPlayTime(time);
            }
//            if (playerName.startsWith("Player Kills: ")) {
//                DRPlayer.drPlayer.getStatistics().setPlayerKills(Integer.parseInt(playerName.split("Player Kills: ")[1].trim()));
//            }
//            if (playerName.startsWith("Deaths: ")) {
//                DRPlayer.drPlayer.getStatistics().setDeaths(Integer.parseInt(playerName.split("Deaths: ")[1].trim()));
//            }
            if (playerName.startsWith("Fish Caught: ")) {
                DRPlayer.drPlayer.getStatistics().setFishCaught(Integer.parseInt(playerName.split("Fish Caught: ")[1].trim()));
            }
            if (playerName.startsWith("Loot Opened: ")) {
                DRPlayer.drPlayer.getStatistics().setLootOpened(Integer.parseInt(playerName.split("Loot Opened: ")[1].trim()));
            }
//            if (playerName.startsWith("Ore Mined: ")) {
//                DRPlayer.drPlayer.getStatistics().setOreMined(Integer.parseInt(playerName.split("Ore Mined: ")[1].trim()));
//            }
//            if (playerName.startsWith("T1 Kills: ")) {
//                int kills = Integer.parseInt(playerName.split("T1 Kills: ")[1].trim().split(" Dry:")[0].trim());
//                int dry = Integer.parseInt(playerName.split("T1 Kills: ")[1].trim().split(" Dry:")[1].trim());
//                DRPlayer.drPlayer.getStatistics().setDryStreak(Tier.T1, dry);
//                DRPlayer.drPlayer.getStatistics().setMobKillCount(Tier.T1, kills);
//            }
//            if (playerName.startsWith("2 Kills: ")) {
//                int kills = Integer.parseInt(playerName.split("T2 Kills: ")[1].trim().split(" Dry:")[0].trim());
//                int dry = Integer.parseInt(playerName.split("T2 Kills: ")[1].trim().split(" Dry:")[1].trim());
//                DRPlayer.drPlayer.getStatistics().setDryStreak(Tier.T2, dry);
//                DRPlayer.drPlayer.getStatistics().setMobKillCount(Tier.T2, kills);
//            }
//            if (playerName.startsWith("T3 Kills: ")) {
//                int kills = Integer.parseInt(playerName.split("T3 Kills: ")[1].trim().split(" Dry:")[0].trim());
//                int dry = Integer.parseInt(playerName.split("T3 Kills: ")[1].trim().split(" Dry:")[1].trim());
//                DRPlayer.drPlayer.getStatistics().setDryStreak(Tier.T3, dry);
//                DRPlayer.drPlayer.getStatistics().setMobKillCount(Tier.T3, kills);
//            }
//            if (playerName.startsWith("T4 Kills: ")) {
//                int kills = Integer.parseInt(playerName.split("T4 Kills: ")[1].trim().split(" Dry:")[0].trim());
//                int dry = Integer.parseInt(playerName.split("T4 Kills: ")[1].trim().split(" Dry:")[1].trim());
//                DRPlayer.drPlayer.getStatistics().setDryStreak(Tier.T4, dry);
//                DRPlayer.drPlayer.getStatistics().setMobKillCount(Tier.T4, kills);
//            }
//            if (playerName.startsWith("T5 Kills: ")) {
//                int kills = Integer.parseInt(playerName.split("T5 Kills: ")[1].trim().split(" Dry:")[0].trim());
//                int dry = Integer.parseInt(playerName.split("T5 Kills: ")[1].trim().split(" Dry:")[1].trim());
//                DRPlayer.drPlayer.getStatistics().setDryStreak(Tier.T5, dry);
//                DRPlayer.drPlayer.getStatistics().setMobKillCount(Tier.T5, kills);
//            }
//            if (playerName.startsWith("T2 Mob Kills: ")) {
//                if (playerName.toLowerCase().contains("dry:")) {
//                    DRPlayer.drPlayer.getStatistics().setMobKillCount(Tier.T2, (long) Integer.parseInt(playerName.split("T2 Mob Kills: ")[1].trim().split(" Dry:")[0].trim()));
//                } else {
//                    DRPlayer.drPlayer.getStatistics().setMobKillCount(Tier.T2, (long) Integer.parseInt(playerName.split("T2 Mob Kills: ")[1].trim()));
//                }
//            }
//            if (playerName.startsWith("T3 Mob Kills: ")) {
//                if (playerName.toLowerCase().contains("dry:")) {
//                    DRPlayer.drPlayer.getStatistics().setMobKillCount(Tier.T3, (long) Integer.parseInt(playerName.split("T3 Mob Kills: ")[1].trim().split(" Dry:")[0].trim()));
//                } else {
//                    DRPlayer.drPlayer.getStatistics().setMobKillCount(Tier.T3, (long) Integer.parseInt(playerName.split("T3 Mob Kills: ")[1].trim()));
//                }
//            }
//            if (playerName.startsWith("T4 Mob Kills: ")) {
//                if (playerName.toLowerCase().contains("dry:")) {
//                    DRPlayer.drPlayer.getStatistics().setMobKillCount(Tier.T4, (long) Integer.parseInt(playerName.split("T4 Mob Kills: ")[1].trim().split(" Dry:")[0].trim()));
//                } else {
//                    DRPlayer.drPlayer.getStatistics().setMobKillCount(Tier.T4, (long) Integer.parseInt(playerName.split("T4 Mob Kills: ")[1].trim()));
//                }
//            }
//            if (playerName.startsWith("T5 Mob Kills: ")) {
//                if (playerName.toLowerCase().contains("dry:")) {
//                    DRPlayer.drPlayer.getStatistics().setMobKillCount(Tier.T5, (long) Integer.parseInt(playerName.split("T5 Mob Kills: ")[1].trim().split(" Dry:")[0].trim()));
//                } else {
//                    DRPlayer.drPlayer.getStatistics().setMobKillCount(Tier.T5, (long) Integer.parseInt(playerName.split("T5 Mob Kills: ")[1].trim()));
//                }
//            }
//            if (playerName.startsWith("Level ➜")) {
//                DRPlayer.drPlayer.getStatistics().setLevel(Integer.parseInt(playerName.split("Level ➜")[1].trim()));
//            }
//            if (playerName.toLowerCase().contains("please visit the guild")) {
//                guild = false;
//            }

        }
        DRPlayer.drPlayer.setUuid(Minecraft.getMinecraft().player.getUniqueID());
    }
}
