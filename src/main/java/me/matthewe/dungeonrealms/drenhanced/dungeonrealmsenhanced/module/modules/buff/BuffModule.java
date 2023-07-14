package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.modules.buff;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.listeners.buff.Buff;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.listeners.buff.BuffListener;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.Module;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.modules.profession.ProfessionItem;
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
public class BuffModule extends Module {
    private int textX = 0;
    private int textY = 0;


    public BuffModule() {
        super("Active Buffs");
    }

    @Override
    public void renderEditing(ScaledResolution resolution, float partialTicks) {
        drawOutline();

        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow("Active Buffs", posX + 2, posY + 2, Tier.T1.getColor());
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
        this.width = 145;
        this.height = 35;
    }


    @Override
    public void renderOutline(ScaledResolution scaledResolution, float particleTicks) {
        drawOutline();
    }

    @Override
    public void render(ScaledResolution scaledResolution, float particleTicks) {

        DRPlayer drPlayer = DRPlayer.get();
        if (BuffListener.getActiveBuffs().isEmpty()){
            return;
        }


        List<String> lines = new ArrayList<>();
        for (Buff activeBuff : BuffListener.getActiveBuffs()) {
            switch (activeBuff.getBuffType()){

                case EXPERIENCE:
                    lines.add(TextFormatting.YELLOW+TextFormatting.BOLD.toString()+"+"+activeBuff.getPercent()+"% " + TextFormatting.YELLOW+"Experience Buff "+TextFormatting.YELLOW+activeBuff.getMinutes()+TextFormatting.BOLD+"m");

                    break;
                case LOOT:
                    lines.add(TextFormatting.YELLOW+TextFormatting.BOLD.toString()+"+"+activeBuff.getPercent()+"% " + TextFormatting.YELLOW+"Loot Buff "+TextFormatting.YELLOW+activeBuff.getMinutes()+TextFormatting.BOLD+"m");
                    break;
            }
        }


        renderOutline(scaledResolution, particleTicks);
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow( TextFormatting.GOLD+"Active Buffs", (posX + 2)+((width-2-Minecraft.getMinecraft().fontRenderer.getStringWidth("Active Buffs"))/2), posY + 2,Tier.T1.getColor());
        int y = posY + 5 + Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;


        for (String line : lines) {
            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(line, posX + 2, y, Tier.T5.getColor());
            y += Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 1;
        }


    }

    @Override
    public void onLoad() {
        this.setEnabled(true);

        this.posX = 15;
        this.posY = 250;

    }

    @Override
    public void onUnload() {

    }
}


