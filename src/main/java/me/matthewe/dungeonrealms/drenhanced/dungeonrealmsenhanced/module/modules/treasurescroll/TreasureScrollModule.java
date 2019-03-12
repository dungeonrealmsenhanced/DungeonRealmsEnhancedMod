package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.modules.treasurescroll;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.Module;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.Tier;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

/**
 * Created by Matthew E on 3/12/2019 at 12:22 PM for the project DungeonRealmsDREnhanced
 */
public class TreasureScrollModule extends Module {
    public TreasureScrollModule() {
        super("Treasure Scroll");
    }

    @Override
    public void renderEditing(ScaledResolution resolution, float partialTicks) {
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("Treasure Scroll", this.posX,this.posY, Tier.T1.getColor());
    }

    @Override
    public void update(ScaledResolution resolution, float partialTicks) {
        this.width = 200;
        this.height = 200;
    }

    @Override
    public void renderOutline(ScaledResolution scaledResolution, float particleTicks) {
        RenderUtils.drawRectLines(posX, posY, this.width, this.height, 1140850688);
    }

    @Override
    public void render(ScaledResolution scaledResolution, float particleTicks) {
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("Treasure Scroll", this.posX,this.posY, Tier.T2.getColor());
    }

    @Override
    public void onLoad() {
        this.setEnabled(true);
        this.posX = 0;
        this.posY = 100;

    }

    @Override
    public void onUnload() {

    }
}


