package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.restful.listeners;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.DREnhanced;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.events.DungeonRealmsJoinEvent;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.restful.RestfulHandler;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.player.DRPlayer;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.setting.DRSettings;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
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
            if (information==null){
                return;
            }
            System.out.println(DREnhanced.gsonBuilder.create().toJson(information));
            if (!DREnhanced.VERSION.equalsIgnoreCase(information.getVersion())) {
                Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.AQUA + TextFormatting.BOLD.toString() + "UPDATE" + TextFormatting.WHITE + TextFormatting.BOLD.toString() + ":" + TextFormatting.GRAY + " The version " + information.getVersion() + " is now available."));
            }
            if (DRPlayer.get().getVersion()==null||!DRPlayer.get().getVersion().equalsIgnoreCase(information.getVersion())|| DRSettings.TESTING.get(boolean.class)) {
                DRPlayer.get().setVersion(DREnhanced.VERSION);
                TextComponentString textComponents = new TextComponentString(TextFormatting.AQUA + TextFormatting.BOLD.toString() + "UPDATED" + TextFormatting.WHITE + TextFormatting.BOLD.toString() + ":" + TextFormatting.GRAY + " You have updated to version ");
                TextComponentString versionComponents = new TextComponentString(TextFormatting.WHITE + information.getVersion());

                versionComponents.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString(TextFormatting.GRAY + "Click to view changelog.")));
                versionComponents.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/changelogs"));
                versionComponents.getStyle().setUnderlined(true);
                versionComponents.getStyle().setBold(true);
                textComponents.appendSibling(versionComponents);
                textComponents.appendText(TextFormatting.GRAY + ".");

                Minecraft.getMinecraft().player.sendMessage(textComponents);
            }
            DRPlayer.get().setVersion(DREnhanced.VERSION);
        });
    }
}
