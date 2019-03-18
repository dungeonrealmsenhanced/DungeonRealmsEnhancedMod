package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.restful.guis;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.DREnhanced;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.Handlers;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.restful.RestfulHandler;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.icons.Icons;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.render.RenderUtils;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.restful.change.Change;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChangelogGui extends GuiScreen {
    public static boolean changelogsOpened = false;

    private final ResourceLocation texture = new ResourceLocation(DREnhanced.MOD_ID, "textures/gui/book.png");
    private final RestfulHandler restfulHandler;
    private int guiWidth = 175;
    private int guiHeight = 228;

    final double scale = 2;

    private String title = "Changelog";
    private boolean crossSelected = false;


    @Override
    public void initGui() {
        buttonList.clear();
        super.initGui();
    }

    public ChangelogGui() {
        changelogsOpened = false;
        this.restfulHandler = Handlers.get(RestfulHandler.class);
    }


    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.crossSelected) {
            new GuiButton(4334, 0, 0, "").playPressSound(Minecraft.getMinecraft().getSoundHandler());
            Minecraft.getMinecraft().displayGuiScreen(null);
        }
    }

    public void display() {
        changelogsOpened = true;
        Minecraft.getMinecraft().displayGuiScreen(this);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        changelogsOpened = false;
    }

    private double scaleX(int x) {
        return ((double) x * (double) width) / guiWidth;
    }

    private double scaleY(int y) {
        return ((double) y * (double) height) / guiHeight;
    }

    @Override
    public void drawScreen(int x, int y, float partialTicks) {
        drawDefaultBackground();
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        int centerX = (width / 2) - guiWidth / 2;
        int centerY = (height / 2) - guiHeight / 2;
        GlStateManager.pushMatrix();
        {
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.color(1, 1, 1, 1);
            Minecraft.getMinecraft().renderEngine.bindTexture(texture);
            drawTexturedModalRect(centerX, centerY, 0, 0, guiWidth, guiHeight);
        }
        GlStateManager.popMatrix();

        if (restfulHandler.getDrEnhancedRestful().getInformation() != null && restfulHandler.getDrEnhancedRestful().getInformation().getChangelog() != null) {
            this.title = restfulHandler.getDrEnhancedRestful().getInformation().getChangelog().getVersion();
        }
        drawTitle(x, y, partialTicks);
        int padding = 8;
        if (RenderUtils.isMouseInside(x, y, centerX + padding, centerY + padding, centerX + padding + 10, centerY + padding + 10)) {
            Icons.CROSS_SELECTED.draw(mc, centerX + padding, centerY + padding);
            crossSelected = true;
        } else {
            Icons.CROSS.draw(mc, centerX + padding, centerY + padding);
            crossSelected = false;
        }
        drawChanges(x, y, partialTicks);
        changelogsOpened = true;


        super.drawScreen(x, y, partialTicks);
    }


    private List<Change> getChanges() {
//        return new ArrayList<>();
        return ((restfulHandler.getDrEnhancedRestful() != null) && (restfulHandler.getDrEnhancedRestful().getInformation() != null) && (restfulHandler.getDrEnhancedRestful().getInformation().getChangelog() != null)) ? restfulHandler.getDrEnhancedRestful().getInformation().getChangelog().getChanges() : new ArrayList<>();
    }

    private void drawChanges(int x, int y, float partialTicks) {
        int centerX = (width / 2) - guiWidth / 2;
        int centerY = (height / 2) - guiHeight / 2;

        int currentY = (centerY + 40) + fontRenderer.FONT_HEIGHT + 3;

        for (Change change : getChanges()) {

            int xBox = 0;
            int yBox = 0;
            xBox = (width / 2) - 70;
            yBox = currentY;

            int[] dimentions = null;
            int[] aaaaaaaaaas = null;
            if (change.getTitle().length() > "aaaaaaaaaaaaaaaaaaaaaa".length()) {
                dimentions = RenderUtils.getTextBoxDimentions(xBox, yBox, change.getTitle());

            } else {
                dimentions = RenderUtils.getTextBoxDimentions(xBox, yBox, "aaaaaaaaaaaaaaaaaaaaaa");
            }
            List<String> lines = new ArrayList<>();
            lines.add(TextFormatting.WHITE.toString() + TextFormatting.BOLD.toString() + change.getTitle());
            for (String s : change.getDescription()) {
                lines.add(TextFormatting.GRAY + s);
            }

            if (RenderUtils.isMouseInside(x, y, xBox, yBox, dimentions[0], dimentions[1])) {

                fontRenderer.drawString(change.getTitle(), xBox + 2, yBox + 2, 0xffff);
            } else {
                fontRenderer.drawString(change.getTitle(), xBox + 2, yBox + 2, 0x0000);
            }

            if (change.getTitle().length() > "aaaaaaaaaaaaaaaaaaaaaa".length()) {
                aaaaaaaaaas = RenderUtils.drawRectLines(xBox, yBox, change.getTitle());

            } else {
                aaaaaaaaaas = RenderUtils.drawRectLines(xBox, yBox, "aaaaaaaaaaaaaaaaaaaaaa");
            }
            if (!RenderUtils.isMouseInside(x, y, xBox, yBox, dimentions[0], dimentions[1])) {
                fontRenderer.drawString(change.getTitle(), xBox + 2, yBox + 2, 0x0000);
            }
            if (RenderUtils.isMouseInside(x, y, xBox, yBox, dimentions[0], dimentions[1])) {

                fontRenderer.drawString(change.getTitle(), xBox + 2, yBox + 2, 0xffff);
                drawHoveringText(lines,x,y);
            }


            currentY += fontRenderer.FONT_HEIGHT + 4;

        }
    }

    private void drawIcon(Icons icons, int x, int y) {
        icons.draw(mc, x, y);
    }

    private void drawTitle(int x, int y, float partialTicks) {
        int centerX = (width / 2) - guiWidth / 2;
        int centerY = (height / 2) - guiHeight / 2;

        GlStateManager.pushMatrix();
        {
            GlStateManager.translate((width / 2) - fontRenderer.getStringWidth(title), centerY + 10, 0);
            GlStateManager.scale(scale, scale, scale);
            fontRenderer.drawString(title, 0, 0, 0x3000);
        }
        GlStateManager.popMatrix();
    }
}
