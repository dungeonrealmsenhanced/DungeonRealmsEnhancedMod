package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.keybinds.listeners;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.keybinds.KeyBindHandler;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.restful.guis.ChangelogGui;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.GuiSettings;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.setting.gui.GuiDRSettingsCategory;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.Listener;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

/**
 * Created by Matthew E on 12/31/2018 at 1:33 PM for the project DungeonRealmsDREnhanced
 */
public class KeyBindListener implements Listener {
    private KeyBindHandler keyBindHandler;

    public KeyBindListener(KeyBindHandler keyBindHandler) {
        this.keyBindHandler = keyBindHandler;
    }

    @SubscribeEvent
    public void onKeyPressed(InputEvent.KeyInputEvent event) {
        if (keyBindHandler.keyBinds.OPEN_DR_OVERLAY_SETTINGS.isPressed()) {
            new GuiSettings().display();
        } else if (keyBindHandler.keyBinds.OPEN_DR_SETTINGS.isPressed()) {
            new GuiDRSettingsCategory().display();
        }  else if (keyBindHandler.keyBinds.CHANGE_LOGS.isPressed()) {
            new ChangelogGui().display();
        }
    }
}
