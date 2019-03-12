package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.misc.listeners;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.events.TipReceiveEvent;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.setting.DRSettings;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by Matthew E on 3/10/2019 at 5:48 PM for the project DungeonRealmsDREnhanced
 */
public class TipListener implements Listener {
    @SubscribeEvent
    public void onClientChatReceived(ClientChatReceivedEvent event) {
        String unformattedText = event.getMessage().getUnformattedText();
        if (unformattedText.startsWith(">> TIP -")) {
            String tipMessage = event.getMessage().getFormattedText().split(TextFormatting.YELLOW+TextFormatting.BOLD.toString()+">>" + TextFormatting.YELLOW+" TIP -")[1];
            if (tipMessage != null) {
                tipMessage = tipMessage.trim();
                TipReceiveEvent tipReceiveEvent = new TipReceiveEvent(tipMessage);
                MinecraftForge.EVENT_BUS.post(tipReceiveEvent);
                if (tipReceiveEvent.isCanceled()) {
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public void onTipReceive(TipReceiveEvent event) {
        if (DRSettings.TESTING.get(boolean.class)) {
            Minecraft.getMinecraft().player.sendMessage(new TextComponentString(event.getTip()));
        }
    }
}
