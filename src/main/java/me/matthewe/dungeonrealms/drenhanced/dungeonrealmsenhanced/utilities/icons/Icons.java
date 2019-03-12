package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.icons;

import net.minecraft.util.ResourceLocation;

public enum Icons implements IIcon {
    ARROW_RIGHT,
    ARROW_DOWN,
    ARROW_UP,
    ARROW_LEFT,
    CHECK,
    CROSS,
    CROSS_SELECTED,
    ARROW_LEFT_SELECTED;

    private static final ResourceLocation ICON_ASSET = new ResourceLocation("dungeonrealmsenhanced:textures/gui/icons.png");

    private static final int ICON_SIZE = 10;
    private static final int GRID_SIZE = 20;

    @Override
    public ResourceLocation getIconAsset() {
        return ICON_ASSET;
    }

    @Override
    public int getIconSize() {
        return ICON_SIZE;
    }

    @Override
    public int getGridWidth() {
        return GRID_SIZE;
    }

    @Override
    public int getGridHeight() {
        return GRID_SIZE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSourceHeight() {
        return ICON_SIZE * GRID_SIZE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSourceWidth() {
        return ICON_SIZE * GRID_SIZE;
    }

    @Override
    public int getU() {
        return (ordinal() % GRID_SIZE) * ICON_SIZE;
    }

    @Override
    public int getV() {
        return (ordinal() / GRID_SIZE) * ICON_SIZE;
    }

    @Override
    public int getOrdinal() {
        return ordinal();
    }
}