package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.modules.buff;

import java.util.ArrayList;
import java.util.List;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.listeners.buff.Buff;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.listeners.buff.BuffListener;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.Module;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.player.DRPlayer;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.Tier;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.render.RenderUtils;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.render.ScreenSize;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;

/** Created by Matthew E on 3/12/2019 at 12:22 PM for the project DungeonRealmsDREnhanced */
public class BuffModule extends Module {
  private int textX = 0;
  private int textY = 0;

  public BuffModule() {
    super("Active Buffs");
  }

  @Override
  public void renderEditing(ScreenSize resolution, float partialTicks) {
    drawOutline();

    RenderUtils.drawText("Active Buffs", posX + 2, posY + 2, Tier.T1.getColor());
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
  public void update(ScreenSize resolution, float partialTicks) {
    this.width = 145;
    this.height = 35;
  }

  @Override
  public void renderOutline(ScreenSize scaledResolution, float particleTicks) {
    drawOutline();
  }

  @Override
  public void render(ScreenSize scaledResolution, float particleTicks) {

    DRPlayer drPlayer = DRPlayer.get();
    if (BuffListener.getActiveBuffs().isEmpty()) {
      return;
    }

    List<String> lines = new ArrayList<>();
    for (Buff activeBuff : BuffListener.getActiveBuffs()) {
      switch (activeBuff.getBuffType()) {
        case EXPERIENCE:
          lines.add(
              ChatFormatting.YELLOW
                  + ChatFormatting.BOLD.toString()
                  + "+"
                  + activeBuff.getPercent()
                  + "% "
                  + ChatFormatting.YELLOW
                  + "Experience Buff "
                  + ChatFormatting.YELLOW
                  + activeBuff.getMinutes()
                  + ChatFormatting.BOLD
                  + "m");

          break;
        case LOOT:
          lines.add(
              ChatFormatting.YELLOW
                  + ChatFormatting.BOLD.toString()
                  + "+"
                  + activeBuff.getPercent()
                  + "% "
                  + ChatFormatting.YELLOW
                  + "Loot Buff "
                  + ChatFormatting.YELLOW
                  + activeBuff.getMinutes()
                  + ChatFormatting.BOLD
                  + "m");
          break;
      }
    }

    renderOutline(scaledResolution, particleTicks);
    RenderUtils.drawText(
        ChatFormatting.GOLD + "Active Buffs",
        (posX + 2) + ((width - 2 - Minecraft.getInstance().font.width("Active Buffs")) / 2),
        posY + 2,
        Tier.T1.getColor());
    int y = posY + 5 + Minecraft.getInstance().font.lineHeight;

    for (String line : lines) {
      RenderUtils.drawText(line, posX + 2, y, Tier.T5.getColor());
      y += Minecraft.getInstance().font.lineHeight + 1;
    }
  }

  @Override
  public void onLoad() {
    this.setEnabled(true);

    this.posX = 15;
    this.posY = 250;
  }

  @Override
  public void onUnload() {}
}
