package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities;
import java.lang.reflect.Field;
import java.util.regex.*;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.modules.ShardCountModule;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;

public class FooterExtractor {
    private static final Pattern ONLINE_PLAYER_PATTERN = Pattern.compile("(\\d+) here");
    private static final Pattern SHARD_PATTERN = Pattern.compile("^(\\S+) \\d+ms \\* play\\.dungeonrealms\\.net");

    public static void extractFooterInfo(GuiPlayerTabOverlay tabList) {
        if (tabList == null) return;

        String footerText = getFooterText(tabList);
        if (footerText == null) return;

        // Remove color codes and trim
        footerText = footerText.replaceAll("§.", "").trim();
        System.out.println("Extracted Footer: " + footerText);

        // Extract player count
        Matcher playerMatcher = ONLINE_PLAYER_PATTERN.matcher(footerText);
        if (playerMatcher.find()) {
            int number = Integer.parseInt(playerMatcher.group(1));
            ShardCountModule.setCount(number);
//            System.out.println("Extracted Player Count: " + number);
        } else {
            ShardCountModule.setCount(0);
//            System.out.println("Player count not found.");
        }

        // Extract shard name
        Matcher shardMatcher = SHARD_PATTERN.matcher(footerText);
        if (shardMatcher.find()) {
            String shardName = shardMatcher.group(1);
            ShardCountModule.setShard(shardName);
//            System.out.println("Extracted Shard: " + shardName);
        } else {
            ShardCountModule.setShard(null);
//            System.out.println("Shard name not found.");
        }
    }

    public static String getFooterText( GuiPlayerTabOverlay tabList) {

        try {
            // Access the footer field using reflection
            Field footerField = GuiPlayerTabOverlay.class.getDeclaredField("field_175255_h");
            footerField.setAccessible(true);
            ITextComponent footerComponent = (ITextComponent) footerField.get(tabList);

            return footerComponent != null ? footerComponent.getUnformattedText() : null;
        } catch (NoSuchFieldException e) {
            System.err.println("Error: Could not find the 'footer' field. Listing all fields:");

            // Print all available field names
            for (Field field : GuiPlayerTabOverlay.class.getDeclaredFields()) {
                System.out.println("Field: " + field.getName() + " | Type: " + field.getType().getName());
            }
        } catch (IllegalAccessException e) {
            System.err.println("Error: Could not access the 'footer' field.");
        }
        return null;
    }
}

