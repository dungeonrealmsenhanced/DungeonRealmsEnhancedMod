package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.listeners;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.events.inventory.ChestReplacer;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.events.inventory.IngameMenuReplacer;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.events.inventory.InventoryReplacer;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.Listener;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.reflection.ReflectionField;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MenuReplacerListener implements Listener {

    @SubscribeEvent
    public void onGuiOpened(GuiOpenEvent event) {
        GuiScreen gui = event.getGui();
        if (gui instanceof GuiInventory) {
            if (gui instanceof InventoryReplacer) {
                return;
            }
            event.setGui(new InventoryReplacer(Minecraft.getMinecraft().player));
        } else if (gui instanceof GuiChest) {
            if (gui instanceof ChestReplacer){
                return;
            }
            event.setGui(new ChestReplacer(Minecraft.getMinecraft().player.inventory, (IInventory) ReflectionField.GUI_CHEST_LOWER_CHEST_INVENTORY.getValue(gui)));
        } else if (gui instanceof GuiIngameMenu) {
            if (gui instanceof IngameMenuReplacer) {
                return;
            }
            event.setGui(new IngameMenuReplacer());
        }
    }
}
