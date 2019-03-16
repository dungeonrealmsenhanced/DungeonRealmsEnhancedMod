package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.listeners;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.DREnhanced;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.player.DRPlayer;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.player.StatisticTracker;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.setting.DRSettings;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.world.Location;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
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

    @SubscribeEvent
    public void onClientChatReceived(ClientChatReceivedEvent event) {
        if (!started) {
            this.statisticTracker.start();
            started = true;
        }
        if (!playing) {
            playing = true;
        }
        final String unformattedText = event.getMessage().getUnformattedText();

        if (event.getMessage().getUnformattedText().trim().contains("https://www.dungeonrealms.net/")) {
            DRPlayer.get().setLoaded(true);
        }
        if (isDebugText(unformattedText)) {
            if (DRSettings.DEBUG_SPACING_ENABLE.get(boolean.class) && DRSettings.DEBUG_SPACING.get(double.class) > 0) {
                String spacing = "";
                for (Integer i = 0; i < DRSettings.DEBUG_SPACING.get(double.class); i++) {
                    spacing += " ";
                }
                event.setCanceled(true);
                Minecraft.getMinecraft().player.sendMessage(new TextComponentString("").appendText(spacing).appendSibling(event.getMessage().createCopy()));
                event.setMessage(new TextComponentString("").appendText(spacing).appendSibling(event.getMessage().createCopy()));
            }
            String trim = unformattedText.split(" ")[0].trim();
            try {
                int damage = Integer.parseInt(trim);
                DRPlayer.drPlayer.addDamage(damage);
            } catch (Exception ignored) {
            }
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
//
//    @SubscribeEvent
//    public void onCommand(CommandEvent event) {
//        if (event.getCommand().getName().startsWith("/debug")) {
//            event.setCanceled(true);
//            Minecraft.getMinecraft().player.sendChatMessage("/toggledebug");
//        }
//    }

    private boolean isDebugText(String string) {
        if (string.contains(" ")) {
            String trim = string.split(" ")[0].trim();
            try {
                Double.parseDouble(trim);
            } catch (Exception e) {
                return false;
            }
            return string.startsWith(trim + " DMG ->");
        }
        return false;
    }
}
