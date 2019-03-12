package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.misc.listeners;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.events.inventory.GuiOverlapEvent;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class PauseMenuButtonListener implements Listener {

    @SubscribeEvent
    public void initGui(GuiOverlapEvent.IngameMenuOverlap.InitGui event) {
        List<GuiButton> toRemoveList = new ArrayList<>();
        List<GuiButton> buttonList = event.getButtonList();

        for (GuiButton guiButton : buttonList) {
            if (guiButton.id >= 5 && guiButton.id <= 7) {
                toRemoveList.add(guiButton);
            } else if (guiButton.id == 1) {
                guiButton.displayString = TextFormatting.RED.toString() + guiButton.displayString;
            } else if (guiButton.id == 12 || guiButton.id == 0) {
                guiButton.displayString = TextFormatting.GRAY + guiButton.displayString.replaceAll("\\.", "");
            }
        }

        buttonList.removeAll(toRemoveList);
        buttonList.add(new GuiButton(753, event.getGui().width / 2 - 100, event.getGui().height / 4 + 48 + -16, TextFormatting.GREEN + "Logout"));
        buttonList.add(new GuiButton(754, event.getGui().width / 2 - 100, event.getGui().height / 4 + 72 + -16, TextFormatting.GREEN + "Back to Hub"));
    }

    @SubscribeEvent
    public void actionPerformed(GuiOverlapEvent.IngameMenuOverlap.ActionPerformed event) {
        switch (event.getButton().id) {
            case 753:
                Minecraft.getMinecraft().player.sendChatMessage("/logout");
                event.setCanceled(true);
                break;
            case 754:
                event.setCanceled(true);
                Minecraft.getMinecraft().player.sendChatMessage("/hub");
                break;
            default:
                break;

        }
    }
}