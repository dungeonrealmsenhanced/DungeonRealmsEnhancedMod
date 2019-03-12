package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.setting.gui;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.Tier;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.render.RenderUtils;
import net.minecraft.client.Minecraft;

/**
 * Created by Matthew Eisenberg on 3/12/2019 at 3:42 PM for the project DungeonRealmsDREnhanced
 */
public class DRButton {
    public int id;
    public int x;
    public int y;
    public String displayString;
    public int width;
    public int height;
    public boolean selected;
    public boolean visible;

    public DRButton(int id, int x, int y, String displayString, int width, int height, boolean visible) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.displayString = displayString;
        this.width = width;
        this.height = height;
        this.visible = visible;
    }

    public void drawButton(Minecraft mc, int guiHeight, int mouseX, int mouseY, float partialTicks) {
        int centerY = (height / 2) - guiHeight / 2;
        int currentY = (centerY + 40) + mc.fontRenderer.FONT_HEIGHT + 3;

        RenderUtils.drawRectLines(this.x, this.y, this.width, this.height, 1140850688);
        RenderUtils.drawText(this.x, this.y, this.displayString, Tier.T1.getColor());
        int xBox = 0;
        int yBox = 0;
        xBox = (width / 2) - 70;
        yBox = currentY;

        int[] aaaaaaaaaas = RenderUtils.drawRectLines(xBox, yBox, "aaaaaaaaaaaaaaaaaaaaaa");
        if (RenderUtils.isMouseInside(x, y, xBox, yBox, aaaaaaaaaas[0], aaaaaaaaaas[1])) {
            mc.fontRenderer.drawString(displayString, xBox + 2, yBox + 2, 0xffff);
            this.selected = true;
        } else {
            mc.fontRenderer.drawString(displayString, xBox + 2, yBox + 2, 0x0000);
            this.selected = false;

        }
        currentY += mc.fontRenderer.FONT_HEIGHT + 4;
    }
}
