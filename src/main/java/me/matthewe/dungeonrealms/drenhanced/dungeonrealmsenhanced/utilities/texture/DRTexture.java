package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.texture;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

/**
 * Created by Matthew E on 3/12/2019 at 9:34 AM for the project DungeonRealmsDREnhanced
 */
public class DRTexture implements Texture {
    private ResourceLocation resourceLocation;
    private boolean loaded;
    private int width;
    private int height;
    protected Minecraft mc;

    public DRTexture(ResourceLocation resourceLocation) {
        this.resourceLocation = resourceLocation;
        this.loaded = false;
        this.mc = Minecraft.getMinecraft();
        this.load();
    }

    @Override
    public TextureResult bind() {
        if (!this.loaded) {
            return TextureResult.FAILED;
        }
        this.mc.getTextureManager().bindTexture(this.resourceLocation);
        return TextureResult.BINDED;
    }

    @Override
    public TextureResult unload() {
        if (!this.loaded) {
            return TextureResult.FAILED;
        }
        this.mc.getTextureManager().deleteTexture(this.resourceLocation);
        this.loaded = false;
        return TextureResult.UNLOADED;
    }

    @Override
    public TextureResult load() {
        if (this.loaded) {
            return TextureResult.FAILED;
        }
        try {
            this.bind();
            BufferedImage bufferedImage = ImageIO.read(this.mc.getResourceManager().getResource(this.resourceLocation).getInputStream());
            this.width = bufferedImage.getWidth();
            this.height = bufferedImage.getHeight();
            this.loaded = true;
            return TextureResult.LOADED;
        } catch (Exception e) {
            e.printStackTrace();
            this.loaded = false;
            return TextureResult.ERROR;
        }
    }

    public ResourceLocation getResourceLocation() {
        return resourceLocation;
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public boolean isLoaded() {
        return loaded;
    }
}
