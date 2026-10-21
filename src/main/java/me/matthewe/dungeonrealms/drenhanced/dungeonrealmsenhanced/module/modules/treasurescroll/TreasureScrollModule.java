package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.modules.treasurescroll;

import java.util.List;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.Module;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.player.DRPlayer;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.ClueScroll;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.ItemUtils;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.Tier;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.render.RenderUtils;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.render.ScreenSize;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

/** Created by Matthew E on 3/12/2019 at 12:22 PM for the project DungeonRealmsDREnhanced */
public class TreasureScrollModule extends Module {
  public TreasureScrollModule() {
    super("Clue Scroll");
  }

  private int textX = 0;
  private int textY = 0;

  @Override
  public void renderEditing(ScreenSize resolution, float partialTicks) {
    drawOutline();

    RenderUtils.drawText("Clue Scroll", posX + 2, posY + 2, Tier.T1.getColor());
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
    this.width = 135;
    this.height = 85;
  }

  @Override
  public void renderOutline(ScreenSize scaledResolution, float particleTicks) {
    drawOutline();
  }

  @Override
  public void render(ScreenSize scaledResolution, float particleTicks) {
    DRPlayer drPlayer = DRPlayer.get();

    LocalPlayer player = mc.player;
    if ((drPlayer != null) && (player != null) && ItemUtils.hasClueScrolls()) {
      List<ClueScroll> clueScrolls = ItemUtils.getClueScrolls();
      if (clueScrolls != null) {
        ClueScroll clueScroll = clueScrolls.get(0);
        RenderUtils.drawText(clueScroll.getDisplayName(), posX + 2, posY + 2, Tier.T5.getColor());
        int y = posY + 5 + Minecraft.getInstance().font.lineHeight;
        for (String s : clueScroll.getLore()) {
          RenderUtils.drawText(s, posX + 2, y, Tier.T5.getColor());
          y += Minecraft.getInstance().font.lineHeight + 1;
        }
      }
    }
  }

  @Override
  public void onLoad() {
    this.setEnabled(true);
    this.posX = 5;
    this.posY = 75;
  }

  @Override
  public void onUnload() {}
}
