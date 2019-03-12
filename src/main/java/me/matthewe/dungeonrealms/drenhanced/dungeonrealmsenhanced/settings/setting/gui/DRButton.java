package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.setting.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

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
    public boolean hovered;
    public boolean visible;
    protected static final ResourceLocation BUTTON_TEXTURES = new ResourceLocation("textures/gui/widgets.png");

    public DRButton(int id, int x, int y, String displayString, int width, int height, boolean visible) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.displayString = displayString;
        this.width = width;
        this.height = height;
        this.visible = visible;
    }
    protected int getHoverState(boolean mouseOver) {
        int i = 1;

        if (mouseOver) {
            i = 2;
        }

        return i;
    }
    public void drawButton(Minecraft mc, GuiDRSettings guiDRSettings, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            FontRenderer fontrenderer = mc.fontRenderer;
            mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            int i = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            guiDRSettings.drawTexturedModalRect(this.x, this.y, 0, 46 + i * 20, this.width / 2, this.height);
            guiDRSettings.drawTexturedModalRect(this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
//            this.mouseDragged(mc, mouseX, mouseY);
            int j = 14737632;

         /*   if (packedFGColour != 0) {
                j = packedFGColour;
            } else if (!this.enabled) {
                j = 10526880;
            } else if (this.hovered) {
                j = 16777120;
            }*/
            if (this.hovered) {
                j = 16777120;
            }
            guiDRSettings.drawCenteredString(fontrenderer, this.displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2, j);
//            this.drawCenteredString(fontrenderer, this.displa/**/yString, this.x + this.width / 2, this.y + (this.height - 8) / 2, j);
        }
    }
}
