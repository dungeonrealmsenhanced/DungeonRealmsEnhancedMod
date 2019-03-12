package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.texture;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.DREnhanced;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Matthew E on 3/12/2019 at 9:57 AM for the project DungeonRealmsDREnhanced
 */
public enum DRTextures {
    RARITY_OVERLAY(DREnhanced.MOD_ID + ":textures/overlay/rarity.png");

    public static Map<DRTextures, DRTexture> textureMap;
    private String path;

    public static void loadTextures() {
        textureMap = new ConcurrentHashMap<>();

        for (DRTextures value : values()) {
            DRTexture load = value.load();
            System.out.println("[DRTextures] Loaded texture " + load.getResourceLocation().getPath());
        }
    }

    DRTextures(String path) {
        this.path = path;
    }

    public DRTexture get() {
        return textureMap.get(this);
    }

    public DRTexture load() {
        if (textureMap.containsKey(this)) {
            return textureMap.get(this);
        }
        DRTexture drTexture = new DRTexture(new ModelResourceLocation(this.path));
        textureMap.put(this, drTexture);
        return textureMap.get(this);
    }
}
