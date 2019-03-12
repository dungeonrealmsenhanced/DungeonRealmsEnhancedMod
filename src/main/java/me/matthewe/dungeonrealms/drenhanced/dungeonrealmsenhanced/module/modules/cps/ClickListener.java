package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.modules.cps;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.player.DRPlayer;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.setting.DRSettings;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.Listener;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ClickListener implements Listener {
    private boolean hasClickedThisTick = false;

    @SubscribeEvent
    public void onMouse(MouseEvent event) {
        if (event.getButton() != 0) {
            return;
        }
        if ((event.isButtonstate()) && (this.hasClickedThisTick)) {
            event.setCanceled(true);
            return;
        }
        if (event.isButtonstate()) {
            this.hasClickedThisTick = true;
            System.out.println(Minecraft.getMinecraft().player.experience);
            if (Minecraft.getMinecraft().player.experienceLevel >= 15){
                DRPlayer.drPlayer.addClick();
            } else {
                if (DRSettings.TESTING.get(boolean.class)) {
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        this.hasClickedThisTick = false;
    }
}