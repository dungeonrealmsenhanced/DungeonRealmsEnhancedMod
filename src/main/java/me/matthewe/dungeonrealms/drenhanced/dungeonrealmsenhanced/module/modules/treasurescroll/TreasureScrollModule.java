package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.modules.treasurescroll;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.Module;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.player.DRPlayer;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.ClueScroll;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.ItemUtils;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.Tier;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;

import java.util.List;

/**
 * Created by Matthew E on 3/12/2019 at 12:22 PM for the project DungeonRealmsDREnhanced
 */
public class TreasureScrollModule extends Module {
    public TreasureScrollModule() {
        super("Clue Scroll");
    }

    private int textX = 0;
    private int textY = 0;

    @Override
    public void renderEditing(ScaledResolution resolution, float partialTicks) {
        drawOutline();

        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("Clue Scroll", posX + 2, posY + 2, Tier.T1.getColor());
    }

    private void drawOutline() {
        int[] ints = RenderUtils.drawRectLines(posX, posY, posX + width, posY + height, 1140850688);
        textX = ints[0];
        textY = ints[1];
    }

    public int getTextX() {
        return textX;
    }

    public boolean isMouseWithin(int mouseX, int mouseY) {
        return RenderUtils.isMouseWithin(mouseX, mouseY, posX, posY, width, height);
    }

    public int getTextY() {
        return textY;
    }

    @Override
    public void update(ScaledResolution resolution, float partialTicks) {
        this.width = 135;
        this.height = 85;
    }

    @Override
    public void renderOutline(ScaledResolution scaledResolution, float particleTicks) {
        drawOutline();
    }

    @Override
    public void render(ScaledResolution scaledResolution, float particleTicks) {
        DRPlayer drPlayer = DRPlayer.get();

        EntityPlayerSP player = mc.player;
        if ((drPlayer != null) && (player != null) && ItemUtils.hasClueScrolls()) {
            List<ClueScroll> clueScrolls = ItemUtils.getClueScrolls();
            if (clueScrolls != null) {
                ClueScroll clueScroll = clueScrolls.get(0);
                Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(clueScroll.getDisplayName(), posX + 2, posY + 2, Tier.T5.getColor());
                int y = posY + 5 + Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
                for (String s : clueScroll.getLore()) {
                    Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(s, posX + 2, y, Tier.T5.getColor());
                    y += Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 1;
                }
            }
        }
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


