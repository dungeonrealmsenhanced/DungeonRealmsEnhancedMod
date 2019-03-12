package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.misc.listeners;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.Modules;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.Listener;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by Matthew E on 12/31/2018 at 1:48 PM for the project DungeonRealmsDREnhanced
 */
public class DefaultOverlayListener implements Listener {
    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent event) {
        switch (event.getType()) {
            case POTION_ICONS:
            case ARMOR:
                event.setCanceled(true);
                break;
            case TEXT:
                Modules.getModules().forEach(module -> module.onRender(event.getResolution(), event.getPartialTicks()));
                break;
            default:
                break;
        }
    }
}
