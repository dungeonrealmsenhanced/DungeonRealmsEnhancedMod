package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.setting.gui;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.DREnhanced;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.setting.DRSettingCategory;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.setting.SettingCategory;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.setting.Settings;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.icons.Icons;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Matthew E on 12/31/2018 at 3:29 PM for the project DungeonRealmsDREnhanced
 */
public class GuiDRSettingsCategory extends GuiScreen {
    public static boolean settingsOpened = false;

    private final ResourceLocation texture = new ResourceLocation(DREnhanced.MOD_ID, "textures/gui/book.png");
    private int guiWidth = 175;
    private int guiHeight = 228;

    final double scale = 2;

    private String title = "Settings";
    private boolean crossSelected = false;

    private DRSettingCategory category;

    @Override
    public void initGui() {
        buttonList.clear();
        super.initGui();
    }

    public DRSettingCategory getCategory() {
        return category;
    }

    public void setCategory(DRSettingCategory category) {
        this.category = category;
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
            if (category == null) {
                Minecraft.getMinecraft().displayGuiScreen(null);
            } else {
                new GuiDRSettingsCategory().display();
            }
            return;
        }
        checkCategories(mouseX, mouseY, mouseButton);
    }

    public void display() {
        FMLCommonHandler.instance().bus().register(this);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        settingsOpened = false;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        FMLCommonHandler.instance().bus().unregister(this);
        Minecraft.getMinecraft().displayGuiScreen(this);

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

        if (this.category != null) {
            this.title = category.getName();
        }
        drawTitle(x, y, partialTicks);
        int padding = 8;
        if (category == null) {

//            Icons.CROSS.draw(mc, centerX + padding, centerY + padding);
            if (RenderUtils.isMouseInside(x, y, centerX + padding, centerY + padding, centerX + padding + 10, centerY + padding + 10)) {
                Icons.CROSS_SELECTED.draw(mc, centerX + padding, centerY + padding);
                crossSelected = true;
            } else {
                Icons.CROSS.draw(mc, centerX + padding, centerY + padding);
                crossSelected = false;
            }
        } else {
            if (RenderUtils.isMouseInside(x, y, centerX + padding, centerY + padding, centerX + padding + 10, centerY + padding + 10)) {
                Icons.ARROW_LEFT_SELECTED.draw(mc, centerX + padding, centerY + padding);
                crossSelected = true;
            } else {
                Icons.ARROW_LEFT.draw(mc, centerX + padding, centerY + padding);
                crossSelected = false;
            }
        }

        drawCategories(x, y, partialTicks);
        settingsOpened = true;


        super.drawScreen(x, y, partialTicks);
    }

    private void checkCategories(int x, int y, int mouseButton) {
        int centerY = (height / 2) - guiHeight / 2;
        int currentY = (centerY + 40) + fontRenderer.FONT_HEIGHT + 3;

        for (DRSettingCategory drSettingCategory : getCategories()) {
            SettingCategory category = Settings.get().getCategory(drSettingCategory);

            int xBox = 0;
            int yBox = 0;
            xBox = (width / 2) - 70;
            yBox = currentY;

            int[] aaaaaaaaaas = RenderUtils.drawRectLines(xBox, yBox, "aaaaaaaaaaaaaaaaaaaaaa");
            if (RenderUtils.isMouseInside(x, y, xBox, yBox, aaaaaaaaaas[0], aaaaaaaaaas[1])) {
                onCategoryClick(category);
            }

            currentY += fontRenderer.FONT_HEIGHT + 4;

        }
    }

    private void onCategoryClick(SettingCategory category) {
        new GuiDRSettings(category.getCategory()).display();
//        mc.displayGuiScreen(new GuiDRSettings(category.getCategory()));
    }

    private List<DRSettingCategory> getCategories() {
        List<DRSettingCategory> drSettingCategories = new ArrayList<>();
        if (category == null) {
            drSettingCategories.addAll(Arrays.asList(DRSettingCategory.values()));
        } else {
            drSettingCategories.addAll(category.getSubCategoryList());
        }
        return drSettingCategories;
    }

    private void drawCategories(int x, int y, float partialTicks) {
        int centerX = (width / 2) - guiWidth / 2;
        int centerY = (height / 2) - guiHeight / 2;

        int currentY = (centerY + 40) + fontRenderer.FONT_HEIGHT + 3;

        for (DRSettingCategory drSettingCategory : getCategories()) {
            SettingCategory category = Settings.get().getCategory(drSettingCategory);

            int xBox = 0;
            int yBox = 0;
            xBox = (width / 2) - 70;
            yBox = currentY;

            int[] aaaaaaaaaas = RenderUtils.drawRectLines(xBox, yBox, "aaaaaaaaaaaaaaaaaaaaaa");
            if (RenderUtils.isMouseInside(x, y, xBox, yBox, aaaaaaaaaas[0], aaaaaaaaaas[1])) {
                fontRenderer.drawString(category.getCategory().getName(), xBox + 2, yBox + 2, 0xffff);
            } else {
                fontRenderer.drawString(category.getCategory().getName(), xBox + 2, yBox + 2, 0x0000);

            }

            List<String> lines = new ArrayList<>();
            lines.add(category.getCategory().getName());

            if (RenderUtils.isMouseInside(x, y, xBox, yBox, aaaaaaaaaas[0], aaaaaaaaaas[1])) {
                drawHoveringText(lines, x, y);
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
