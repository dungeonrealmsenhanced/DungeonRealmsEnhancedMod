package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.Module;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.Modules;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.modules.buff.BuffModule;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.modules.profession.ProfessionModule;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.modules.treasurescroll.TreasureScrollModule;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.IOException;

/**
 * Created by Matthew E on 12/31/2018 at 3:29 PM for the project DungeonRealmsDREnhanced
 */
public class GuiSettings extends GuiScreen {
    private boolean dragging = false;
    private long lastX;
    private long lastY;
    private Module selectedModule;

    private GuiButton resetToDefaultButton;
    private GuiButton toggleButton;
    public static boolean settingsOpened = false;

    @Override
    public void initGui() {
        super.initGui();
        this.resetToDefaultButton = new GuiButton(0, width / 2 - 50, height / 2 + 50, 100, 20, "Reset to default");
        this.toggleButton = new GuiButton(1, resetToDefaultButton.x + 120, height / 2 + 50, 100, 20, " ");
        this.buttonList.add(this.resetToDefaultButton);
        this.buttonList.add(this.toggleButton);
        toggleButton.enabled = true;
        resetToDefaultButton.enabled = true;
        toggleButton.visible = false;
    }

    public void display() {
        FMLCommonHandler.instance().bus().register(this);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        if (selectedModule != null) {
            selectedModule.setEditing(false);
        }
        dragging = false;
        settingsOpened = false;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        FMLCommonHandler.instance().bus().unregister(this);
        Minecraft.getMinecraft().displayGuiScreen(this);

    }

    @Override
    public void drawScreen(int x, int y, float partialTicks) {
        if ((selectedModule != null) && dragging) {
            selectedModule.onRender(new ScaledResolution(Minecraft.getMinecraft()), partialTicks);
            RenderUtils.drawText(width / 2 - 50, (height / 2) + 60, selectedModule.getName(), 0xFFFFFF);
            toggleButton.displayString = selectedModule.isEnabled() ? "Disable" : "Enable";
            toggleButton.enabled = true;
            toggleButton.visible = true;
        }

        settingsOpened = true;

        super.drawScreen(x, y, partialTicks);
    }


    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
    }


    @Override
    protected void mouseClicked(int x, int y, int mouseButton) throws IOException {
        boolean foundModule = false;
        boolean overButton = false;
        for (Module module : Modules.getModules()) {

            if (module instanceof TreasureScrollModule) {
                TreasureScrollModule treasureScrollModule = (TreasureScrollModule) module;
                if (treasureScrollModule.isMouseWithin(x, y)) {
                    selectModule(module, x, y);
                    foundModule = true;
                }
            } else if (module instanceof ProfessionModule) {
                ProfessionModule professionModule = (ProfessionModule) module;
                if (professionModule.isMouseWithin(x, y)) {
                    selectModule(module, x, y);
                    foundModule = true;
                }
            }else if (module instanceof BuffModule) {
                BuffModule buffModule = (BuffModule) module;
                if (buffModule.isMouseWithin(x, y)) {
                    selectModule(module, x, y);
                    foundModule = true;
                }
            } else {
                int minX = module.posX - 2;
                int minY = module.posY - 2;
                int maxX = module.getWidth() + 2;
                int maxY = module.getHeight() + 2;
                if ((x >= minX) && (x <= maxX) && (y >= minY) && (y <= maxY)) {
                    selectModule(module, x, y);
                    foundModule = true;
                }
            }
        }
        for (GuiButton guiButton : buttonList) {
            if (guiButton.isMouseOver()) {
                overButton = true;
                break;
            }
        }
        if (!foundModule && selectedModule != null && !overButton) {
            this.selectedModule.setEditing(false);
            this.selectedModule = null;
            dragging = false;
            toggleButton.visible = false;
        }
        super.mouseClicked(x, y, mouseButton);
    }

    private void selectModule(Module module, int x, int y) {
        this.dragging = true;
        this.lastX = x;
        selectedModule = module;
        module.setEditing(true);
        this.lastY = y;
    }


    @Override
    protected void mouseClickMove(int x, int y, int lastButtonClicked, long timeSinceClick) {
        if (this.dragging && this.selectedModule != null) {
            selectedModule.posX += x - this.lastX;
            selectedModule.posY += y - this.lastY;

            this.lastX = x;
            this.lastY = y;
        }
        super.mouseClickMove(x, y, lastButtonClicked, timeSinceClick);
    }


    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        switch (button.id) {
            case 0:
                for (Module module : Modules.getModules()) {
                    module.posY = module.defaultPosY;
                    module.posX = module.defaultPosX;
                    module.setEnabled(true);
                }
                selectedModule = null;
                dragging = false;

                break;
            case 1:
                if (this.selectedModule == null) {
                    break;
                }
                dragging = false;
                selectedModule.setEnabled(!selectedModule.isEnabled());
                toggleButton.displayString = selectedModule.isEnabled() ? "Disable" : "Enable";
                break;
            default:
                break;
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
