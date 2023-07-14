package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.setting.gui;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.DREnhanced;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.player.DRPlayer;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.setting.DRSettingCategory;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.setting.DRSettings;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.icons.Icons;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Matthew E on 12/31/2018 at 3:29 PM for the project DungeonRealmsDREnhanced
 */
public class GuiDRSettings extends GuiScreen {
    public static boolean settingsOpened = false;

    private final ResourceLocation texture = new ResourceLocation(DREnhanced.MOD_ID, "textures/gui/book.png");
    private int guiWidth = 175;
    private int guiHeight = 228;

    private boolean arrowSelected = false;
    private DRSettingCategory category;

    final double scale = 2;

    private Map<DRSettings, DRButton> buttonMap = new ConcurrentHashMap<>();
    private Map<DRSettings, GuiTextField> textMap = new ConcurrentHashMap<>();

    public GuiDRSettings(DRSettingCategory category) {
        this.category = category;
        settingsOpened = false;
    }

    public DRButton getButton(DRSettings settings) {
        return buttonMap.get(settings);
    }

    public void updateTextBoxes() {
        for (GuiTextField textBox : textMap.values()) {

            if (!textBox.getText().isEmpty()) {
                if (!textBox.isFocused()) {
                    updateValues();
                }
            }
        }
    }

    private void updateValues() {
        for (Map.Entry<DRSettings, GuiTextField> entry : textMap.entrySet()) {
            Class clazz = entry.getKey().getClazz();
            if (clazz == double.class) {
                double value = 0;
                try {
                    value = Double.parseDouble(entry.getValue().getText());
                } catch (Exception e) {
                    value = DRPlayer.get().getSettings().getCategory(entry.getKey().getCategory()).getSettingValue(entry.getKey(), double.class);
                }
                DRPlayer.get().setSettingValue(entry.getKey(), value);
            } else if (clazz == String.class) {
                DRPlayer.get().setSettingValue(entry.getKey(), entry.getValue().getText());
            } else if (clazz == long.class) {
                long value = 0;
                try {
                    value = Long.parseLong(entry.getValue().getText());
                } catch (Exception e) {
                    value = DRPlayer.get().getSettings().getCategory(entry.getKey().getCategory()).getSettingValue(entry.getKey(), long.class);
                }
                DRPlayer.get().setSettingValue(entry.getKey(), value);
            }else if (clazz == int.class) {
                int value = 0;
                try {
                    value = Integer.parseInt(entry.getValue().getText());
                } catch (Exception e) {
                    value = DRPlayer.get().getSettings().getCategory(entry.getKey().getCategory()).getSettingValue(entry.getKey(), int.class);
                }
                DRPlayer.get().setSettingValue(entry.getKey(), value);
            }
        }
    }

    @Override
    public void initGui() {
        buttonList.clear();
        super.initGui();
        initSettings();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {

        super.actionPerformed(button);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        updateTextBoxes();
        for (GuiTextField value : textMap.values()) {
            if (value.isFocused()) {
                value.textboxKeyTyped(typedChar, keyCode);
            }
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (this.arrowSelected) {
            this.arrowSelected = false;
            new GuiButton(4334, 0, 0, "").playPressSound(Minecraft.getMinecraft().getSoundHandler());
            if (category.isSubCategory()) {
                DRSettingCategory parentCategory = category.getParentCategory();
                if (parentCategory != null) {

                    GuiDRSettingsCategory guiDRSettingsCategory = new GuiDRSettingsCategory();
                    guiDRSettingsCategory.setCategory(parentCategory);
                    guiDRSettingsCategory.display();
                    return;
                } else {
                    new GuiDRSettingsCategory().display();
                }
            } else {
                new GuiDRSettingsCategory().display();
            }
            return;
        }
        for (GuiTextField value : textMap.values()) {
            value.mouseClicked(mouseX, mouseY, mouseButton);

        }
        for (Map.Entry<DRSettings, DRButton> entry : buttonMap.entrySet()) {
            if (entry.getValue().hovered) {
                if (entry.getKey().getClazz() == boolean.class) {
                    DRPlayer.get().setSettingValue(entry.getKey(), !DRPlayer.get().getSettings().getCategory(entry.getKey().getCategory()).getSettingValue(entry.getKey(), boolean.class));
                }
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public void display() {
        Minecraft.getMinecraft().displayGuiScreen(GuiDRSettings.this);
        settingsOpened = true;
    }

    @Override
    public void onGuiClosed() {
        updateValues();
        super.onGuiClosed();
        settingsOpened = false;
    }
//
//    @SubscribeEvent
//    public void onClientTick(TickEvent.ClientTickEvent event) {
//        FMLCommonHandler.instance().bus().unregister(this);
//        Minecraft.getMinecraft().displayGuiScreen(this);
//
//    }

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
        drawTitle(x, y, partialTicks);
        int padding = 8;
        if (RenderUtils.isMouseInside(x, y, centerX + padding, centerY + padding, centerX + padding + 10, centerY + padding + 10)) {
            Icons.ARROW_LEFT_SELECTED.draw(mc, centerX + padding, centerY + padding);
            arrowSelected = true;
        } else {
            Icons.ARROW_LEFT.draw(mc, centerX + padding, centerY + padding);
            arrowSelected = false;
        }


        for (Map.Entry<DRSettings, DRButton> entry : buttonMap.entrySet()) {
            DRSettings key = entry.getKey();

            DRButton value = entry.getValue();
            value.displayString = DRPlayer.get().getSettings().getCategory(key.getCategory()).getSettingValue(key, boolean.class).toString();
            value.visible = true;

            value.drawButton(mc, this, x, y, partialTicks);
            if (!hovering) {
            }
        }

        for (GuiTextField value : textMap.values()) {
            value.setVisible(true);
            value.setEnabled(true);
            value.drawTextBox();
            if (!hovering){
            }
        }
        drawSettings(x, y, partialTicks);

        settingsOpened = true;
        super.drawScreen(x, y, partialTicks);
    }


    private void initSettings() {
        int centerY = (height / 2) - guiHeight / 2;
        int currentY = (centerY + 40) + fontRenderer.FONT_HEIGHT + 3;

        final int paddingLeft = 3;
        int currentButtonId = 5;
        int currentGuiTextFieldId = 50;

        for (DRSettings drSettings : DRSettings.getByCategory(category)) {
            int xBox = 0;
            int yBox = 0;
            xBox = (width / 2) - 80;
            yBox = currentY;

            if (drSettings.getClazz() == boolean.class) {
                DRButton guiButton = new DRButton(currentButtonId, xBox + paddingLeft + fontRenderer.getStringWidth(drSettings.getName()), yBox, drSettings.get(boolean.class) ? "Disable" : "Enable", 50, 10, true);
                guiButton.visible = true;
                guiButton.displayString = drSettings.get(boolean.class).toString();
                buttonMap.put(drSettings, guiButton);
                currentButtonId++;
            } else if (drSettings.getClazz() == double.class) {
                GuiTextField guiButton = new GuiTextField(currentGuiTextFieldId, fontRenderer, xBox + paddingLeft + fontRenderer.getStringWidth(drSettings.getName()), yBox, 60, 10);
                guiButton.setText(drSettings.get(double.class).intValue() + "");
                guiButton.setValidator(input -> {
                    if (input == null) {
                        return false;
                    }
                    if (input.isEmpty()) {
                        return true;
                    }
                    try {
                        Double.parseDouble(input);
                    } catch (Exception e) {
                        return false;
                    }
                    return true;
                });
                textMap.put(drSettings, guiButton);
                currentGuiTextFieldId++;
            }else if (drSettings.getClazz() == long.class) {
                GuiTextField guiButton = new GuiTextField(currentGuiTextFieldId, fontRenderer, xBox + paddingLeft + fontRenderer.getStringWidth(drSettings.getName()), yBox, 60, 10);
                guiButton.setText(drSettings.get(long.class) + "");
                guiButton.setValidator(input -> {
                    if (input == null) {
                        return false;
                    }
                    if (input.isEmpty()) {
                        return true;
                    }
                    try {
                        Long.parseLong(input);
                    } catch (Exception e) {
                        return false;
                    }
                    return true;
                });
                textMap.put(drSettings, guiButton);
                currentGuiTextFieldId++;
            } else if (drSettings.getClazz() == String.class) {

                int width = fontRenderer.getStringWidth(drSettings.get(String.class))+10;
                GuiTextField guiButton = new GuiTextField(currentGuiTextFieldId, fontRenderer, xBox + paddingLeft + fontRenderer.getStringWidth(drSettings.getName()), yBox, width, 10);
                guiButton.setText(drSettings.get(String.class) + "");

                textMap.put(drSettings, guiButton);
                currentGuiTextFieldId++;
            }
            currentY += fontRenderer.FONT_HEIGHT + 8;
        }
    }

    private void drawSettings(int x, int y, float partialTicks) {
        int centerX = (width / 2) - guiWidth / 2;
        int centerY = (height / 2) - guiHeight / 2;
        int currentY = (centerY + 40) + fontRenderer.FONT_HEIGHT + 3;

        for (DRSettings drSettings : DRSettings.getByCategory(category)) {
            int xBox = 0;
            int yBox = 0;
            xBox = (width / 2) - 80;
            yBox = currentY;

            fontRenderer.drawString(drSettings.getName(), xBox, yBox + 3, 0x3000);
            if (!hovering) {
            }
            List<String> lines = new ArrayList<>();
            lines.add(TextFormatting.GREEN + drSettings.getName());
            for (String s : drSettings.getDescription()) {
                lines.add(TextFormatting.GRAY + s);
            }

            if (RenderUtils.isMouseInside(x, y, xBox + 3, yBox + 3, xBox + fontRenderer.getStringWidth(drSettings.getName()) + 3, yBox + 3 + fontRenderer.FONT_HEIGHT)) {
                hovering = true;
                GlStateManager.pushMatrix();
                {
                    drawHoveringText(lines, x, y, fontRenderer);
                }
                GlStateManager.popMatrix();


            } else {
                hovering = false;
            }
            currentY += fontRenderer.FONT_HEIGHT + 8;
        }
    }
    boolean hovering = false;

    private void drawIcon(Icons icons, int x, int y) {
        icons.draw(mc, x, y);
    }

    private void drawTitle(int x, int y, float partialTicks) {
        int centerY = (height / 2) - guiHeight / 2;

        GlStateManager.pushMatrix();
        {
            GlStateManager.translate((width / 2) - fontRenderer.getStringWidth(category.getName()), centerY + 10, 0);
            GlStateManager.scale(scale, scale, scale);
            fontRenderer.drawString(category.getName(), 0, 0, 0x3000);
        }
        GlStateManager.popMatrix();
    }
}
