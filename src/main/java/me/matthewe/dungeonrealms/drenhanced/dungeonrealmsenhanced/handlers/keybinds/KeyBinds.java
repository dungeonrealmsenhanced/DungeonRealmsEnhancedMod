package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.keybinds;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class KeyBinds {

    public  KeyBinding OPEN_DR_OVERLAY_SETTINGS;
    public  KeyBinding OPEN_DR_SETTINGS;
    private KeyBindHandler keyBindHandler;

    public KeyBinds(KeyBindHandler keyBindHandler) {
        this.keyBindHandler = keyBindHandler;
    }

    public void init() {
        OPEN_DR_OVERLAY_SETTINGS = new KeyBinding("key.openDROverlaySettings", Keyboard.KEY_NUMPAD5, "key.categories.drenhanced");
        OPEN_DR_SETTINGS = new KeyBinding("key.openDRSettings", Keyboard.KEY_H, "key.categories.drenhanced");

        ClientRegistry.registerKeyBinding(OPEN_DR_OVERLAY_SETTINGS);
        ClientRegistry.registerKeyBinding(OPEN_DR_SETTINGS);

        keyBindHandler.logger.info("Registered key bind " + OPEN_DR_OVERLAY_SETTINGS.getDisplayName());
        keyBindHandler.logger.info("Registered key bind " + OPEN_DR_SETTINGS.getDisplayName());
    }
}
