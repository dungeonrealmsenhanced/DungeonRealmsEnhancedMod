package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.modules.profession;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.Module;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.player.DRPlayer;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.player.profession.mining.Mining;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.MiningUtils;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.Percent;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.TimeUtils;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.ItemType;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.Tier;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthew E on 3/12/2019 at 12:22 PM for the project DungeonRealmsDREnhanced
 */
public class ProfessionModule extends Module {
    private int textX = 0;
    private int textY = 0;


    public ProfessionModule() {
        super("Profession");
    }

    @Override
    public void renderEditing(ScaledResolution resolution, float partialTicks) {
        drawOutline();

        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("Profession", posX + 2, posY + 2, Tier.T1.getColor());
    }

    private void drawOutline() {
        int[] ints = RenderUtils.drawRectLines(posX, posY, posX + width, posY + height, 1140850688);
        textX = ints[0];
        textY = ints[1];
    }

    public int getTextX() {
        return textX;
    }

    public boolean isMouseWithin(int mouseX, int mouseY) {
        return RenderUtils.isMouseWithin(mouseX, mouseY, posX, posY, width, height);
    }

    public int getTextY() {
        return textY;
    }

    @Override
    public void update(ScaledResolution resolution, float partialTicks) {
        this.width = 135;
        this.height = 85;
    }


    @Override
    public void renderOutline(ScaledResolution scaledResolution, float particleTicks) {
        drawOutline();
    }

    @Override
    public void render(ScaledResolution scaledResolution, float particleTicks) {

        DRPlayer drPlayer = DRPlayer.get();

        EntityPlayerSP player = mc.player;
        ItemStack itemStack = player.getHeldItem(EnumHand.MAIN_HAND);
        if ((itemStack.getItem() == Items.AIR) || (itemStack.getCount() == 0)) {
            return;
        }
        if ((drPlayer != null) && (player != null) && ItemType.isProfessionItem(itemStack)) {
            if (!drPlayer.isLoaded()){
               return;
            }
            ProfessionItem professionItem = ProfessionItem.of(itemStack);
            if (professionItem != null) {
                List<String> lines = new ArrayList<>();
                Mining mining = drPlayer.getMining();
                if (mining==null){
                    return;
                }
                if (professionItem.getTier()==null){
                    return;
                }
                int experiencePerMinute = mining.getExperiencePer5Minutes();
                if (experiencePerMinute<10){
                    return;
                }

                long neededExperience = MiningUtils.getNeededExperience(professionItem.getLevel());
                if (neededExperience<1){
                    return;
                }
                long secondsUntilNextLevel = mining.getSecondsUntilNextLevel(professionItem);
                if (secondsUntilNextLevel<1){
                    return;
                }

                double percentage = ((double) professionItem.getExperience() * 100.0D) / (double) neededExperience;
                if (percentage > 100.0D) {
                    percentage = 100.0D;
                }
                if (experiencePerMinute >= 1) {
                    lines.add(TextFormatting.YELLOW.toString() + (((double)experiencePerMinute)/5.0D) + " " + TextFormatting.BOLD.toString() + "EXP/m");
                }
                if (secondsUntilNextLevel >= 1) {
                    String timeFormat = TimeUtils.formatTimeToHHMMSS(secondsUntilNextLevel * 1000L, TextFormatting.YELLOW, TextFormatting.YELLOW, true);
                    lines.add(TextFormatting.YELLOW + "Next Level" + TextFormatting.WHITE + ": " + timeFormat);
                    lines.add(TextFormatting.WHITE + "(" + new Percent(percentage).getColor() + new DecimalFormat(drPlayer.getPercentFormat()).format(percentage) + TextFormatting.BOLD + "%" + TextFormatting.WHITE + ")");

                }

                if (lines.size() >= 2) {

                    if( professionItem.getLevel()<1||professionItem.getExperience()<0){
                        return;
                    }
                    lines.add(" ");

                    long remainingExperienceForNextTier = mining.getRemainingExperienceForNextTier(professionItem);
                    if (remainingExperienceForNextTier<1){
                        return;
                    }
                    long maxXPForTier = mining.getStartingNeededXPForWholeTier(professionItem.getTier());
                    long secondsUntilNextTier = mining.getSecondsUntilNextTier(professionItem);
                    if ((remainingExperienceForNextTier > 0) && (maxXPForTier > 0) && secondsUntilNextTier > 0) {
                        if (professionItem.isFishingRod()) {
                            double percent = (100.0D - ((double) remainingExperienceForNextTier * 100.0D) / (double) maxXPForTier);
                            String timeFormat = TimeUtils.formatTimeToHHMMSS(secondsUntilNextTier * 1000L, TextFormatting.YELLOW, TextFormatting.YELLOW, true);
                            lines.add(TextFormatting.YELLOW + "Next Tier" + TextFormatting.WHITE + ": " + new Percent(percent).getColor() + new DecimalFormat(drPlayer.getPercentFormat()).format(percent) + TextFormatting.BOLD + "%");
                            lines.add(timeFormat);

                        } else if(professionItem.isPickaxe()){
                            double percent = (100.0D - ((double) remainingExperienceForNextTier * 100.0D) / (double) maxXPForTier);
                            String timeFormat = TimeUtils.formatTimeToHHMMSS(secondsUntilNextTier * 1000L, TextFormatting.YELLOW, TextFormatting.YELLOW, true);
                            lines.add(TextFormatting.YELLOW + "Next Tier" + TextFormatting.WHITE + ": " + new Percent(percent).getColor() + new DecimalFormat(drPlayer.getPercentFormat()).format(percent) + TextFormatting.BOLD + "%");
                            lines.add(timeFormat);

                        }
                    }
                }
                if (lines.size() <2) {
                    return;
                }
                renderOutline(scaledResolution, particleTicks);
                Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(ItemType.getFromItemStack(itemStack).getName() + " Information", posX + 2, posY + 2, professionItem.getTier().getColor());
                int y = posY + 5 + Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;


                for (String line : lines) {
                    Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(line, posX + 2, y, Tier.T5.getColor());
                    y += Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 1;
                }
            }
        }
    }

    @Override
    public void onLoad() {
        this.setEnabled(true);

        this.posX = 5;
        this.posY = 190;

    }

    @Override
    public void onUnload() {

    }
}


