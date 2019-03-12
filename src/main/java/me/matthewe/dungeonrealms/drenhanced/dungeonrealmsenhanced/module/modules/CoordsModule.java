package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.modules;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.player.DRPlayer;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.Module;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.render.RenderUtils;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.Tier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

/**
 * Created by Matthew E on 12/31/2018 at 4:08 PM for the project DungeonRealmsDREnhanced
 */
public class CoordsModule extends Module {
    public CoordsModule() {
        super("Coords");
    }

    @Override
    public void renderEditing(ScaledResolution resolution, float partialTicks) {
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(DRPlayer.get().getLocation().getXYZToString(), this.posX, this.posY, Tier.T1.getColor());
    }

    @Override
    public void update(ScaledResolution resolution, float partialTicks) {
        int[] ints = RenderUtils.getTextBoxDimentions(posX-2, posY-2, DRPlayer.get().getLocation().getXYZToString());
        this.width = ints[0];
        this.height = ints[1];
    }

    @Override
    public void renderOutline(ScaledResolution scaledResolution, float particleTicks) {
        RenderUtils.drawRectLines(posX-2, posY-2, this.width, this.height,1140850688);

    }

    @Override
    public void render(ScaledResolution scaledResolution, float particleTicks) {
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(DRPlayer.get().getLocation().getXYZToString(), this.posX, this.posY, Tier.T3.getColor());
    }

    @Override
    public void onLoad() {
        this.setEnabled(true);
        this.posX = 50;
        this.posY = 50;
    }

    @Override
    public void onUnload() {

    }
}
