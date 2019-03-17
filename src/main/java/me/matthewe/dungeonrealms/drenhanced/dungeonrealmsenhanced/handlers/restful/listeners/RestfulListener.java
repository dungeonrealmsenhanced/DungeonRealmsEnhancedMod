package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.restful.listeners;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.DREnhanced;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.events.DungeonRealmsJoinEvent;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.restful.RestfulHandler;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by Matthew E on 3/16/2019 at 2:50 PM for the project DungeonRealmsDREnhanced
 */
public class RestfulListener implements Listener {
    private RestfulHandler restfulHandler;

    public RestfulListener(RestfulHandler restfulHandler) {
        this.restfulHandler = restfulHandler;
    }

    @SubscribeEvent
    public void onDungeonRealmsJoin(DungeonRealmsJoinEvent event) {
        restfulHandler.getDrEnhancedRestful().update(information -> {
            if (!DREnhanced.VERSION.equalsIgnoreCase(information.getVersion())) {
                Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.AQUA + TextFormatting.BOLD.toString() + "UPDATE" + TextFormatting.WHITE + TextFormatting.BOLD.toString() + ":" + TextFormatting.GRAY + " The version " + information.getVersion() + " is now available."));
            }
        });
    }
}
