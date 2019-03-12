package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.render;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.DREnhanced;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.texture.ToolTip;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class RenderUtils {
    public static void drawTexture(int x, int y, int w, int h) {
        GL11.glColor4f(1F, 1F, 1F, 1F);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        double zLevel = 0;
        buffer.pos(x + 0, y + h, zLevel).tex(0D, 1D);
        buffer.pos(x + w, y + h, zLevel).tex(1D, 1D);
        buffer.pos(x + w, y + 0, zLevel).tex(1D, 0D);
        buffer.pos(x + 0, y + 0, zLevel).tex(0D, 0D);
        tessellator.draw();
    }
    public static void drawRectWithTexture(double x, double y, float u, float v, int width, int height, float textureWidth, float textureHeight, int sourceWidth, int sourceHeight)
    {
        float scaleWidth = 1.0F / sourceWidth;
        float scaleHeight = 1.0F / sourceHeight;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(x, y + height, 0).tex((double)(u * scaleWidth), (double)(v + textureHeight) * scaleHeight).endVertex();
        buffer.pos(x + width, y + height, 0).tex((double)(u + textureWidth) * scaleWidth, (double)(v + textureHeight) * scaleHeight).endVertex();
        buffer.pos(x + width, y, 0).tex((double)(u + textureWidth) * scaleWidth, (double)(v * scaleHeight)).endVertex();
        buffer.pos(x, y, 0).tex((double)(u * scaleWidth), (double)(v * scaleHeight)).endVertex();
        tessellator.draw();
    }

    public static void drawRectWithTexture(double x, double y, double z, float u, float v, int width, int height, float textureWidth, float textureHeight)
    {
        float scale = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(x, y + height, z).tex((double)(u * scale), (double)(v + textureHeight) * scale).endVertex();
        buffer.pos(x + width, y + height, z).tex((double)(u + textureWidth) * scale, (double)(v + textureHeight) * scale).endVertex();
        buffer.pos(x + width, y, z).tex((double)(u + textureWidth) * scale, (double)(v * scale)).endVertex();
        buffer.pos(x, y, z).tex((double)(u * scale), (double)(v * scale)).endVertex();
        tessellator.draw();
    }

    public static int[] drawRectLines(int x, int y, String text) {
        Gui.drawRect(x, y, x + Minecraft.getMinecraft().fontRenderer.getStringWidth(text) + 4, y + 12, 1140850688);
        return new int[]{x + Minecraft.getMinecraft().fontRenderer.getStringWidth(text) + 4, y + 12};
    }


    public static int[] drawRectLines(int x, int y, int width, int height, int color) {
        Gui.drawRect(x, y, width, height, color);
        return new int[]{width, height};
    }

    public static int[] getTextBoxDimentions(int x, int y, String text) {
        return new int[]{x + Minecraft.getMinecraft().fontRenderer.getStringWidth(text) + 4, y + 12};
    }
    public static boolean isMouseInside(int mouseX, int mouseY, int x1, int y1, int x2, int y2)
    {
        return mouseX >= x1 && mouseX < x2 && mouseY >= y1 && mouseY < y2;
    }

    public static boolean isMouseWithin(int mouseX, int mouseY, int x, int y, int width, int height)
    {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }
    public static void drawToolTip(ToolTip toolTip) {
        int highestWidth = 0;
        int x = toolTip.getX();
        int y = toolTip.getY();
        String title = toolTip.getTitle();
        String[] lines = toolTip.getLines();
        int padding = toolTip.getPadding();
        Minecraft mc = Minecraft.getMinecraft();
        int height = 0;
        for (String line : lines) {
            int width = mc.fontRenderer.getStringWidth(line);
            if (width > highestWidth) {
                highestWidth = width;
            }
            height += mc.fontRenderer.FONT_HEIGHT;

        }
        if (mc.fontRenderer.getStringWidth(title) > highestWidth) {
            highestWidth = mc.fontRenderer.getStringWidth(title);
        }
        height += mc.fontRenderer.FONT_HEIGHT + 5;

        int currentX = x;
        int currentY = y + padding;
        currentY += mc.fontRenderer.FONT_HEIGHT;

        RenderUtils.drawItemCustomItem("tooltip", x, y, highestWidth + (padding * 2), height + padding);
        mc.fontRenderer.drawStringWithShadow(title, (x + (highestWidth - mc.fontRenderer.getStringWidth(title)) / 2),
                y + padding, Color.WHITE.getRGB());

        for (String line : lines) {
            mc.fontRenderer.drawString(line, currentX + padding, currentY + padding, Color.WHITE.getRGB());
            currentY += mc.fontRenderer.FONT_HEIGHT;
        }
    }

    public static void drawGradientRect(int par1, int par2, int par3, int par4, int par5, int par6) {
        double zLevel = 0;
        float f = (float) (par5 >> 24 & 255) / 255.0F;
        float f1 = (float) (par5 >> 16 & 255) / 255.0F;
        float f2 = (float) (par5 >> 8 & 255) / 255.0F;
        float f3 = (float) (par5 & 255) / 255.0F;
        float f4 = (float) (par6 >> 24 & 255) / 255.0F;
        float f5 = (float) (par6 >> 16 & 255) / 255.0F;
        float f6 = (float) (par6 >> 8 & 255) / 255.0F;
        float f7 = (float) (par6 & 255) / 255.0F;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);;
        buffer.color(f1, f2, f3, f);

        buffer.pos((double) par3, (double) par2, (double) zLevel);
        buffer.pos((double) par1, (double) par2, (double) zLevel);
        buffer.color(f5, f6, f7, f4);
        buffer.pos((double) par1, (double) par4, (double) zLevel);
        buffer.pos((double) par3, (double) par4, (double) zLevel);
        tessellator.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public static void drawItemCustomItem(String name, int x, int y, int width, int height) {
        String path = DREnhanced.MOD_ID + ":textures/gui/" + name + ".png".toLowerCase();
        ResourceLocation location = new ResourceLocation(path);
        Minecraft.getMinecraft().renderEngine.bindTexture(location);
        drawTexture(x, y, width, height);
    }

    public static void drawItem(String name, int x, int y, int width, int height) {
        String path = "textures/items/" + name + ".png".toLowerCase();
        ResourceLocation location = new ResourceLocation(path);
        Minecraft.getMinecraft().renderEngine.bindTexture(location);
        drawTexture(x, y, width, height);
    }

    public static void drawText(int x, int y, String text, int color) {
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(text, x, y, color);
    }
}
