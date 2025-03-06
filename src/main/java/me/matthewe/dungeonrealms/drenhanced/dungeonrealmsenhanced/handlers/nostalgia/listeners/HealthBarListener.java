package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.nostalgia.listeners;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.DREnhanced;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.modules.profession.ProfessionItem;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.setting.DRSettings;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.Listener;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.world.Zone;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.BossInfoClient;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.BossInfo;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by Matthew E on 3/17/2019 at 5:59 PM for the project DungeonRealmsDREnhanced
 */
public class HealthBarListener implements Listener {
    private static int[] currentHealth;
    private static Zone zone;

    public HealthBarListener() {
        currentHealth = new int[]{-1, -1};
        zone = Zone.SAFE;
    }

    @SubscribeEvent
    public void onRenderGameOverlayBossInfo(RenderGameOverlayEvent.BossInfo event) {
        BossInfoClient bossInfo = event.getBossInfo();
        String unformattedText = bossInfo.getName().getUnformattedText();
        if (unformattedText.contains("- HP ")) {
            String hp = unformattedText.split("- HP ")[0].trim();


            String trim = unformattedText.split("- HP ")[1].trim();
            if (trim.contains(" - XP")) {
                String healthString = trim.split(" - XP")[0].trim();
                int health = -1;
                int maxHealth = -1;
                Zone zoneByColor = Zone.byColor(bossInfo.getColor());
                if (zoneByColor != null) {
                    zone = zoneByColor;
                }
                if (healthString.contains(" / ")) {
                    String[] split = healthString.split(" / ");
                    health = Integer.parseInt(split[0].trim());
                    maxHealth = Integer.parseInt(split[1].trim());
                } else {
                    try {
                        health = Integer.parseInt(healthString.trim());
                        maxHealth = Integer.parseInt(healthString.trim());
                    } catch (NumberFormatException e) {
                    }
                }
                if ((health != -1) && (maxHealth != -1)) {
                    currentHealth[0] = health;
                    currentHealth[1] = maxHealth;
                }
            }
        }

        if (DRSettings.OLD_BOSS_BAR.get(boolean.class)) {
            bossInfo.setName(new TextComponentString(TextFormatting.LIGHT_PURPLE + TextFormatting.BOLD.toString() + "HP " + TextFormatting.LIGHT_PURPLE + getHealth() + " " + TextFormatting.BOLD + "/" + TextFormatting.LIGHT_PURPLE + " " + getMaxHealth()));
            bossInfo.setColor(BossInfo.Color.PINK);
            bossInfo.setOverlay(BossInfo.Overlay.PROGRESS);

        } else {
            if (DRSettings.LEVEL_HEALTH.get(boolean.class)) {
                event.setCanceled(true);
                Minecraft.getMinecraft().player.experienceLevel = getHealth();

            }
        }
    }

    public static Zone getZone() {
        return zone;
    }

    public static int getHealth() {
        return currentHealth[0];
    }

    public static int getMaxHealth() {
        return currentHealth[1];
    }

    public static int[] getCurrentHealth() {
        return currentHealth;
    }
}
