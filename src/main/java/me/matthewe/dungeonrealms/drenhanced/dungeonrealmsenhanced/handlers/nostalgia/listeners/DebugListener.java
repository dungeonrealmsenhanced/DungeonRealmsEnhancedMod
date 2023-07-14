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

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onClientChatReceived(ClientChatReceivedEvent event) {

        String unformattedText = event.getMessage().getUnformattedText();
        if (unformattedText.trim().equalsIgnoreCase("You examine your catch...") && DRSettings.YOU_EXAMINE_YOUR_CATCH_MESSAGE.get(boolean.class)) {
            event.setCanceled(true);
            return;
        }
        if (unformattedText.toLowerCase().trim().startsWith("... you caught some") && DRSettings.YOU_CAUGHT.get(boolean.class)) {
            event.setCanceled(true);
            return;
        }
        if (DebugPatterns.DEBUG_PATTERN.matcher(unformattedText).matches() || DebugPatterns.DEBUG_DAMAGE_TAKEN_PATTERN.matcher(unformattedText).matches() || (unformattedText.contains("(Life Steal)")) || unformattedText.contains("DMG ->") || (unformattedText.contains("HP [")) || unformattedText.contains("(THORNS)")) {
            if (DRSettings.DEBUG_SPACING_ENABLE.get(boolean.class) && DRSettings.DEBUG_SPACING.get(double.class) > 0) {
                String spacing = "";
                double amount = DRSettings.DEBUG_SPACING.get(double.class);
                if (DebugPatterns.DEBUG_DAMAGE_TAKEN_PATTERN.matcher(unformattedText).matches()) {
                    if (amount - 1 < 0) {
                        amount = 0;
                    } else {
                        amount--;

                    }
                }
                for (int i = 0; i < amount; i++) {
                    spacing += " ";
                }
                event.setCanceled(true);
                Minecraft.getMinecraft().player.sendMessage(new TextComponentString("").appendText(spacing).appendSibling(event.getMessage().createCopy()));
//                event.setMessage(new TextComponentString("").appendText(spacing).appendSibling(event.getMessage().createCopy()));
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
        } catch (Exception e) {
            return false;
        }
    }
}
