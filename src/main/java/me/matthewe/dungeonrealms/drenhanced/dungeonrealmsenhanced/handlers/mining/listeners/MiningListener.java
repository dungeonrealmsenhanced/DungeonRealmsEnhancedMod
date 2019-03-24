package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.mining.listeners;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.modules.profession.ProfessionItem;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.player.DRPlayer;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.Listener;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.regex.Pattern;

/**
 * Created by Matthew E on 3/19/2019 at 2:43 PM for the project DungeonRealmsDREnhanced
 */
public class MiningListener implements Listener {
    private Pattern pattern = Pattern.compile(".*\\+([0-9]*)\\sEXP\\s(\\[)([0-9]*)\\/([0-9]*)\\sEXP\\]");
    private Pattern patternExperienceBuff = Pattern.compile(".*\\+([0-9]*)\\sEXP\\s\\(EXP\\sBUFF\\)\\s(\\[)([0-9]*)\\/([0-9]*)\\sEXP\\]");

    @SubscribeEvent
    public void onClientChatReceived(ClientChatReceivedEvent event) {
        String message = event.getMessage().getUnformattedText();
        if (ProfessionItem.has() && ((pattern.matcher(message).matches() || patternExperienceBuff.matcher(message).matches()))) {
            ProfessionItem professionItem = ProfessionItem.get();
            if (professionItem == null) {
                return;
            }
            message = message.replaceAll("\\(EXP BUFF\\)", "").trim();
            int experienceGain = Integer.parseInt(message.split(" EXP \\[")[0].trim().split("\\+")[1].trim());
            String[] strings = message.split("\\[")[1].trim().split("EXP \\]")[0].trim().replaceAll(" EXP]", "").trim().split("/");
            int newExperience = Integer.parseInt(strings[0].trim());
            int maxExperience = Integer.parseInt(strings[1].trim());
            if (professionItem.getNeededExperience(professionItem.getLevel()) == maxExperience) {
                DRPlayer.get().getMining().onExperienceGain(experienceGain);
            }
        }
    }

}
