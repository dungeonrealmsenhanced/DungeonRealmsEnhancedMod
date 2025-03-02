package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.listeners;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.DREnhanced;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.events.DungeonRealmsJoinEvent;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.listeners.buff.BuffListener;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.player.DRPlayer;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.player.StatisticTracker;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.setting.DRSettings;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.world.Location;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.concurrent.TimeUnit;

/**
 * Created by Matthew E on 12/31/2018 at 2:39 PM for the project DungeonRealmsDREnhanced
 */
public class StatisticListener {
    private StatisticTracker statisticTracker;
    private boolean started = false;
    private boolean playing = false;
    private long delay = 0;
    private boolean firstJoin = true;


    public StatisticListener() {
        this.statisticTracker = new StatisticTracker();
    }

    private long delay2;

    @SubscribeEvent
    public void onTickClientTick(TickEvent.ClientTickEvent event) {
        if (System.currentTimeMillis() > this.delay && playing) {
            this.delay = System.currentTimeMillis() + 250L;
            EntityPlayerSP player = Minecraft.getMinecraft().player;
            if (player != null) {
                DRPlayer.get().update();
                DRPlayer.get().setLocation(new Location(player.posX, player.posY, player.posZ, player.cameraYaw, player.cameraPitch));
            }
        }
        if (System.currentTimeMillis() > this.delay2 && playing) {
            this.delay2 = System.currentTimeMillis() + 3000L;
            EntityPlayerSP player = Minecraft.getMinecraft().player;
            if (player != null) {
                BuffListener.requestActiveBuffs();
            }
        }



    }

    int test = 0;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onClientChatReceived(ClientChatReceivedEvent event) {
        if (!started) {
            this.statisticTracker.start();
            started = true;
        }
        if (firstJoin) {
            firstJoin = false;
            DungeonRealmsJoinEvent dungeonRealmsJoinEvent = new DungeonRealmsJoinEvent();
            MinecraftForge.EVENT_BUS.post(dungeonRealmsJoinEvent);
        }
        if (!playing) {
            playing = true;
        }
        final String unformattedText = event.getMessage().getUnformattedText();

        if (event.getMessage().getUnformattedText().trim().contains("https://www.dungeonrealms.net/")) {
           test++;
        }
        if (event.getMessage().getUnformattedText().trim().contains("You are on the ")){
            test++;
        }
        if (test>=2){
            DRPlayer.get().setLoaded(true);
            DREnhanced.log("LOADED");

        }
    }

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedInEvent event) {
        DRPlayer.drPlayer.setLoaded(false);
        DRPlayer.drPlayer.setUuid(event.player.getUniqueID());
        playing = true;
    }

    public boolean isPlaying() {
        return playing;
    }

    private long lastExecutionTime = 0;
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastExecutionTime >= TimeUnit.MINUTES.toMillis(5)) { // 60,000 milliseconds = 1 minute
                DREnhanced.saveModuleSettings();
                lastExecutionTime = currentTime;
            }
        }
    }
    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        playing = false;

        if (started) {
            started = false;
            try {
                statisticTracker.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            statisticTracker.setRunning(false);
        }

        DREnhanced.saveModuleSettings();
    }

}
