package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.modules.drysteak;

import java.text.DecimalFormat;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.Module;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.player.DRPlayer;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.Tier;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.render.RenderUtils;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.render.ScreenSize;

/** Created by Matthew E on 1/5/2023 at 7:34 AM for the project DungeonRealmsEnhancedMod */
public class DrySteakModule extends Module {
  public DrySteakModule() {
    super("DrySteak");
  }

  public Tier currentTier = Tier.T4;

  @Override
  public void renderEditing(ScreenSize resolution, float partialTicks) {
    RenderUtils.drawText(getTextToShow(), this.posX, this.posY, currentTier.getColor());
  }

  private String getTextToShow() {
    long dryStreak = DRPlayer.get().getStatistics().getDryStreak(currentTier);
    return currentTier.getChatFormatting()
        + "Dry Steak: "
        + new DecimalFormat("#,###").format(dryStreak);
  }

  @Override
  public void update(ScreenSize resolution, float partialTicks) {
    int[] ints = RenderUtils.getTextBoxDimentions(posX - 2, posY - 2, getTextToShow());
    this.width = ints[0];
    this.height = ints[1];
  }

  @Override
  public void renderOutline(ScreenSize scaledResolution, float particleTicks) {
    RenderUtils.drawRectLines(posX - 2, posY - 2, this.width, this.height, 1140850688);
  }

  @Override
  public void render(ScreenSize scaledResolution, float particleTicks) {
    RenderUtils.drawText(getTextToShow(), this.posX, this.posY, Tier.T2.getColor());
  }

  @Override
  public void onLoad() {
    this.posX = 100;
    this.posY = 200;
  }

  @Override
  public void onUnload() {}
}
