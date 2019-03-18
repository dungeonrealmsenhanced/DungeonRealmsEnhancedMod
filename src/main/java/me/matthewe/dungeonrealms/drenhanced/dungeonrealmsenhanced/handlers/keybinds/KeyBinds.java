package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.keybinds;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class KeyBinds {

    public  KeyBinding OPEN_DR_OVERLAY_SETTINGS;
    public  KeyBinding OPEN_DR_SETTINGS;
    public  KeyBinding CHANGE_LOGS;
    private KeyBindHandler keyBindHandler;

    public KeyBinds(KeyBindHandler keyBindHandler) {
        this.keyBindHandler = keyBindHandler;
    }

    public void init() {
        OPEN_DR_OVERLAY_SETTINGS = new KeyBinding("Open Overlay Settings", Keyboard.KEY_NUMPAD5, "DungeonRealms Enhanced");
        OPEN_DR_SETTINGS = new KeyBinding("Open Settings", Keyboard.KEY_H, "DungeonRealms Enhanced");
        CHANGE_LOGS = new KeyBinding("Open Changelogs", Keyboard.KEY_G, "DungeonRealms Enhanced");

        ClientRegistry.registerKeyBinding(OPEN_DR_OVERLAY_SETTINGS);
        ClientRegistry.registerKeyBinding(OPEN_DR_SETTINGS);
        ClientRegistry.registerKeyBinding(CHANGE_LOGS);

        keyBindHandler.logger.info("Registered key bind " + OPEN_DR_OVERLAY_SETTINGS.getDisplayName());
        keyBindHandler.logger.info("Registered key bind " + OPEN_DR_SETTINGS.getDisplayName());
        keyBindHandler.logger.info("Registered key bind " + CHANGE_LOGS.getDisplayName());
    }
}
