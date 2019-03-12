package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.render;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.texture.Texture;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

/**
 * Credit https://github.com/Wynntils/Wynntils/blob/development/src/main/java/com/wynntils/core/framework/rendering/ScreenRenderer.java
 * Using some parts of the file
 */
public class ScreenRenderer {

    public static FontRenderer fontRenderer = null;
    private static boolean rendering = false;
    private static float scale = 1.0f;
    private static float rotation = 0;
    private static boolean mask = false;
    private static Point drawingOrigin = new Point(0, 0);

    private static Point transformationOrigin = new Point(0, 0);


    private static RenderItem itemRenderer = null;

    public static boolean isRendering() {
        return rendering;
    }

    public static float getScale() {
        return scale;
    }

    public static float getRotation() {
        return rotation;
    }

    public static boolean isMasking() {
        return mask;
    }


    public static void beginGL(int x, int y) {
        if (rendering) return;
        rendering = true;
        GlStateManager.pushMatrix();
        drawingOrigin = new Point(x, y);
        transformationOrigin = new Point(0, 0);
        resetScale();
        resetRotation();
        GlStateManager.enableAlpha();
        GlStateManager.color(1, 1, 1);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    }

    public static void endGL() {
        if (!rendering) return;
        resetScale();
        resetRotation();
        if (mask) {
            GlStateManager.depthMask(true);
            GlStateManager.clear(GL_DEPTH_BUFFER_BIT);
            GlStateManager.enableDepth();
            GlStateManager.depthFunc(GL_LEQUAL);
            GlStateManager.clearColor(1.0F, 1.0F, 1.0F, 1.0F);
            mask = false;
        }
        drawingOrigin = new Point(0, 0);
        transformationOrigin = new Point(0, 0);
        GlStateManager.popMatrix();
        GlStateManager.color(1, 1, 1);
        rendering = false;
    }


    public static void rotate(float degrees) {
        if (!rendering) return;
        GlStateManager.translate((drawingOrigin.x + transformationOrigin.x), (drawingOrigin.y + transformationOrigin.y), 0);
        GlStateManager.rotate(degrees, 0, 0, 1);
        GlStateManager.translate((-drawingOrigin.x - transformationOrigin.x), (-drawingOrigin.y - transformationOrigin.y), 0);
        rotation += degrees;
    }

    public static void resetRotation() {
        if (!rendering) return;
        if (rotation != 0.0f) {
            GlStateManager.translate(drawingOrigin.x + transformationOrigin.x, drawingOrigin.y + transformationOrigin.y, 0);
            GlStateManager.rotate(rotation, 0, 0, -1);
            GlStateManager.translate(-drawingOrigin.x - transformationOrigin.x, -drawingOrigin.y - transformationOrigin.y, 0);
            rotation = 0;
        }
    }


    public static void scale(float multiplier) {
        if (!rendering) return;
        GlStateManager.translate(drawingOrigin.x + transformationOrigin.x, drawingOrigin.y + transformationOrigin.y, 0);
        GlStateManager.scale(multiplier, multiplier, multiplier);
        GlStateManager.translate(-drawingOrigin.x - transformationOrigin.x, -drawingOrigin.y - transformationOrigin.y, 0);
        scale *= multiplier;
    }

    public static void resetScale() {
        if (!rendering) return;
        if (scale != 1.0f) {
            float m = 1.0f / scale;
            GlStateManager.translate(drawingOrigin.x + transformationOrigin.x, drawingOrigin.y + transformationOrigin.y, 0);
            GlStateManager.scale(m, m, m);
            GlStateManager.translate(-drawingOrigin.x - transformationOrigin.x, -drawingOrigin.y - transformationOrigin.y, 0);
            scale = 1.0f;
        }
    }

    public void drawRect(Texture texture, int x1, int y1, int x2, int y2, float tx1, float ty1, float tx2, float ty2) {
        if (!rendering || texture == null || !texture.isLoaded()) return;
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        texture.bind();

        int xMin = x1 + drawingOrigin.x,
                xMax = x2 + drawingOrigin.x,
                yMin = y1 + drawingOrigin.y,
                yMax = y2 + drawingOrigin.y;

        GlStateManager.glBegin(GL_QUADS);
        GlStateManager.glTexCoord2f(tx1, ty1);
        GlStateManager.glVertex3f(xMin, yMin, 0);
        GlStateManager.glTexCoord2f(tx1, ty2);
        GlStateManager.glVertex3f(xMin, yMax, 0);
        GlStateManager.glTexCoord2f(tx2, ty2);
        GlStateManager.glVertex3f(xMax, yMax, 0);
        GlStateManager.glTexCoord2f(tx2, ty1);
        GlStateManager.glVertex3f(xMax, yMin, 0);
        GlStateManager.glEnd();
    }

    public void drawRect(Texture texture, int x1, int y1, int x2, int y2, int tx1, int ty1, int tx2, int ty2) {
        drawRect(texture, x1, y1, x2, y2, (float) tx1 / texture.getWidth(), (float) ty1 / texture.getHeight(), (float) tx2 / texture.getWidth(), (float) ty2 / texture.getHeight());
    }

    public void drawRect(Texture texture, int x, int y, int tx, int ty, int width, int height) {
        drawRect(texture, x, y, x + width, y + height, tx, ty, tx + width, ty + height);
    }



    public void color(float r, float g, float b, float alpha) {
        GlStateManager.color(r, g, b, alpha);
    }

    public void drawItemStack(ItemStack is, int x, int y) {
        drawItemStack(is, x, y, false, "", true);
    }

    public void drawItemStack(ItemStack is, int x, int y, boolean count) {
        drawItemStack(is, x, y, count, "", true);
    }

    public void drawItemStack(ItemStack is, int x, int y, boolean count, boolean effects) {
        drawItemStack(is, x, y, count, "", effects);
    }

    public void drawItemStack(ItemStack is, int x, int y, String text) {
        drawItemStack(is, x, y, false, text, true);
    }

    public void drawItemStack(ItemStack is, int x, int y, String text, boolean effects) {
        drawItemStack(is, x, y, false, text, effects);
    }

    private void drawItemStack(ItemStack is, int x, int y, boolean count, String text, boolean effects) {
        if (!rendering) return;
        RenderHelper.enableGUIStandardItemLighting();
        itemRenderer.zLevel = 200.0F;
        net.minecraft.client.gui.FontRenderer font = is.getItem().getFontRenderer(is);
        if (font == null) font = fontRenderer;
        if (effects)
            itemRenderer.renderItemAndEffectIntoGUI(is, x + drawingOrigin.x, y + drawingOrigin.y);
        else
            itemRenderer.renderItemIntoGUI(is, x + drawingOrigin.x, y + drawingOrigin.y);
        itemRenderer.renderItemOverlayIntoGUI(font, is, x + drawingOrigin.x, y + drawingOrigin.y, text.isEmpty() ? count ? Integer.toString(is.getCount()) : null : text);
        itemRenderer.zLevel = 0.0F;
        RenderHelper.disableStandardItemLighting();
    }
}
