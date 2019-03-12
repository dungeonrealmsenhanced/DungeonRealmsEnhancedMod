package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.texture;

/**
 * Created by Matthew E on 3/12/2019 at 9:26 AM for the project DungeonRealmsDREnhanced
 */
public interface Texture {
    TextureResult load();

    float getWidth();

    float getHeight();

    TextureResult bind();

    boolean isLoaded();

    TextureResult unload();
}
