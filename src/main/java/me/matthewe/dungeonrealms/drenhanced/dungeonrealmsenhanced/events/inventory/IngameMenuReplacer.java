package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.events.inventory;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraftforge.common.MinecraftForge;

import java.io.IOException;

public class IngameMenuReplacer extends GuiIngameMenu {

    @Override
    public void initGui() {
        super.initGui();

        MinecraftForge.EVENT_BUS.post(new GuiOverlapEvent.IngameMenuOverlap.InitGui(this, buttonList));
    }

    @Override
    public void actionPerformed(GuiButton btn) throws IOException {
        if(MinecraftForge.EVENT_BUS.post(new GuiOverlapEvent.IngameMenuOverlap.ActionPerformed(this, btn))) {
            return;
        }
        super.actionPerformed(btn);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        MinecraftForge.EVENT_BUS.post(new GuiOverlapEvent.IngameMenuOverlap.DrawScreen(this, mouseX, mouseY, partialTicks));
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        MinecraftForge.EVENT_BUS.post(new GuiOverlapEvent.IngameMenuOverlap.MouseClicked(this, mouseX, mouseY, mouseButton));
    }

    @Override
    public void drawHoveringText(String text, int x, int y) {
        super.drawHoveringText(text, x, y);
    }

}
