package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.nostalgia.listeners;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.nostalgia.DebugPatterns;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.nostalgia.NostalgiaHandler;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.player.DRPlayer;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.setting.DRSettings;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by Matthew E on 3/17/2019 at 4:21 PM for the project DungeonRealmsDREnhanced
 */
public class DebugListener implements Listener {
    private NostalgiaHandler nostalgiaHandler;

    public DebugListener(NostalgiaHandler nostalgiaHandler) {
        this.nostalgiaHandler = nostalgiaHandler;
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onClientChatReceived(ClientChatReceivedEvent event) {

        String unformattedText = event.getMessage().getUnformattedText();

        if (DebugPatterns.DEBUG_PATTERN.matcher(unformattedText).matches() || DebugPatterns.DEBUG_DAMAGE_TAKEN_PATTERN.matcher(unformattedText).matches() || unformattedText.contains("(THORNS)")) {
            if (DRSettings.DEBUG_SPACING_ENABLE.get(boolean.class) && DRSettings.DEBUG_SPACING.get(double.class) > 0) {
                String spacing = "";
                for (Integer i = 0; i < DRSettings.DEBUG_SPACING.get(double.class); i++) {
                    spacing += " ";
                }
                event.setCanceled(true);
                Minecraft.getMinecraft().player.sendMessage(new TextComponentString("").appendText(spacing).appendSibling(event.getMessage().createCopy()));
                event.setMessage(new TextComponentString("").appendText(spacing).appendSibling(event.getMessage().createCopy()));
            }
        }
        if (isDebugText(unformattedText)) {
            String trim = unformattedText.split(" ")[0].trim();
            try {
                int damage = Integer.parseInt(trim);
                DRPlayer.drPlayer.addDamage(damage);
            } catch (Exception ignored) {
            }
        }
    }

    private boolean isDebugText(String string) {
       try {
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
       } catch ( Exception e) {
           return false;
       }
    }
}
