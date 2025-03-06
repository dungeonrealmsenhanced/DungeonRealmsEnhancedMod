package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.listeners;

import net.minecraft.client.gui.advancements.GuiAdvancement;
import net.minecraft.client.gui.advancements.GuiScreenAdvancements;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class AdvancementHiderListener {


    @SubscribeEvent //For some reason advancements are enabled on dungeon realms servers...
    public static void onGuiRender(GuiScreenEvent.DrawScreenEvent.Post event) {
        if (event.getGui() instanceof GuiScreenAdvancements) {
            event.setCanceled(true);
        }
    }
}