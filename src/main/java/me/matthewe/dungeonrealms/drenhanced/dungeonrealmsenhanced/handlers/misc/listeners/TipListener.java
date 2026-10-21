package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.misc.listeners;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.events.TipReceiveEvent;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.setting.DRSettings;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.Listener;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientChatReceivedEvent;
import net.neoforged.neoforge.common.NeoForge;

/** Created by Matthew E on 3/10/2019 at 5:48 PM for the project DungeonRealmsDREnhanced */
public class TipListener implements Listener {
  @SubscribeEvent
  public void onClientChatReceived(ClientChatReceivedEvent event) {
    if (!me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.listeners.ServerContext
        .active()) return;
    String unformattedText = event.getMessage().getString();
    if (unformattedText.startsWith(">> TIP -")) {
      String[] s = event.getMessage().getString().split(" ");
      if (s[3] == null) {
        return;
      }
      String tipMessage = "";
      for (int i = 3; i < s.length; i++) {

        tipMessage += s[i] + " ";
      }
      if (tipMessage.endsWith(" ")) {
        tipMessage = tipMessage.trim();
      }
      tipMessage = tipMessage.trim();
      TipReceiveEvent tipReceiveEvent = new TipReceiveEvent(tipMessage);
      NeoForge.EVENT_BUS.post(tipReceiveEvent);
      if (tipReceiveEvent.isCanceled()) {
        event.setCanceled(true);
      }
    }
  }

  @SubscribeEvent
  public void onTipReceive(TipReceiveEvent event) {
    if (!me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.listeners.ServerContext
        .active()) return;
    if (DRSettings.TESTING.get(boolean.class)) {}
  }
}
