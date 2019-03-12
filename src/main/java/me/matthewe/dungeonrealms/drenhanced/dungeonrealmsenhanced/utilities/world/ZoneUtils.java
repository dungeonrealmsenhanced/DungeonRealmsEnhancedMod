package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.world;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiBossOverlay;
import net.minecraft.world.BossInfo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Matthew E on 12/31/2018 at 7:56 PM for the project DungeonRealmsDREnhanced
 */
public class ZoneUtils {
    public static boolean isInSafeZone() {
        try {
            List<BossInfo> bossBarInfos = getBossBarInfos(Minecraft.getMinecraft());
            if ((bossBarInfos == null) || bossBarInfos.isEmpty()) {
                return true;
            }
            BossInfo bossInfo = bossBarInfos.get(0);
            BossInfo.Color color = bossInfo.getColor();
            if (color == BossInfo.Color.GREEN) {
                return true;
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private static List<BossInfo> getBossBarInfos(Minecraft minecraft) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {

        GuiBossOverlay bossOverlay = minecraft.ingameGUI.getBossOverlay();

        String nameAfter = null;

        // Find the name of the field as it will change with each obfuscation of forge
        // If you're using this, you'll want to move this lookup elsewhere and store the result
        for (Field s : GuiBossOverlay.class.getDeclaredFields()) {

            if (s.getType().getName().equals("java.util.Map")) {
                nameAfter = s.getName();
                break;
            }

        }

        List<BossInfo> names = new ArrayList<BossInfo>();

        if (nameAfter != null) {

            Field bossField = GuiBossOverlay.class.getDeclaredField(nameAfter);
            bossField.setAccessible(true);

            Map<UUID, BossInfo> mapBossInfos = (Map<UUID, BossInfo>) bossField.get(bossOverlay);

            for (BossInfo bIL : mapBossInfos.values())
                names.add(bIL);

        }

        return names;

    }
}
