package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.keybinds;

import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.sdl.SDLKeycode;

public final class KeyBinds {
  private static final KeyMapping.Category CATEGORY =
      KeyMapping.Category.register(
          Identifier.fromNamespaceAndPath("dungeonrealmsenhanced", "keys"));
  public static final KeyMapping OPEN_DR_SETTINGS =
      new KeyMapping("key.drenhanced.settings", SDLKeycode.SDLK_H, CATEGORY);
  public static final KeyMapping OPEN_DR_OVERLAY_SETTINGS =
      new KeyMapping("key.drenhanced.overlay", SDLKeycode.SDLK_KP_5, CATEGORY);
  public static final KeyMapping CHANGE_LOGS =
      new KeyMapping("key.drenhanced.changelog", SDLKeycode.SDLK_G, CATEGORY);

  public KeyBinds(KeyBindHandler handler) {}

  public void init() {}
}
