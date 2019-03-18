package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.listeners;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.DREnhanced;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.events.DungeonRealmsJoinEvent;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.player.DRPlayer;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.player.StatisticTracker;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.world.Location;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

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

    @SubscribeEvent
    public void onTickClientTick(TickEvent.ClientTickEvent event) {
        if (System.currentTimeMillis() > this.delay && playing) {
            this.delay = System.currentTimeMillis() + 250L;
            EntityPlayerSP player = Minecraft.getMinecraft().player;
            if (player == null) {
                return;
            }
            DRPlayer.get().update();
            DRPlayer.get().setLocation(new Location(player.posX, player.posY, player.posZ, player.cameraYaw, player.cameraPitch));
        }

}
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
            DRPlayer.get().setLoaded(true);
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

        DREnhanced.INSTANCE.saveModuleSettings();
    }

}
