package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.misc.listeners;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.Listener;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthew E on 12/31/2018 at 1:30 PM for the project DungeonRealmsDREnhanced
 */
public class NBTListener implements Listener {
    @SubscribeEvent(priority = EventPriority.LOW)
    public void onItemTooltip(ItemTooltipEvent event) {
        ItemStack itemStack = event.getItemStack();
        if (itemStack.hasTagCompound() && GuiScreen.isAltKeyDown()) {
            List<String> toolTip = new ArrayList<>();
            toolTip.add(" ");
            NBTTagCompound tagCompound = itemStack.getTagCompound();


            for (String s : tagCompound.getKeySet()) {
                toolTip.add(s + ":" + tagCompound.getTag(s).toString());
            }
            event.getToolTip().addAll(toolTip);
        }
    }
}
