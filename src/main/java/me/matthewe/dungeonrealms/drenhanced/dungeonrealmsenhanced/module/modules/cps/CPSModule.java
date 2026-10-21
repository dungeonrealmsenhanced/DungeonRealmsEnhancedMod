package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.modules.cps;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.Module;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.player.DRPlayer;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.Tier;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.render.RenderUtils;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.render.ScreenSize;

/** Created by Matthew E on 3/13/2019 at 10:32 AM for the project DungeonRealmsDREnhanced */
public class CPSModule extends Module {
  public CPSModule() {
    super("CPS");
  }

  @Override
  public void renderEditing(ScreenSize resolution, float partialTicks) {
    RenderUtils.drawText(
        DRPlayer.drPlayer.getCPS() + " CPS", this.posX, this.posY, Tier.T1.getColor());
  }

  @Override
  public void update(ScreenSize resolution, float partialTicks) {
    int[] ints =
        RenderUtils.getTextBoxDimentions(posX - 2, posY - 2, DRPlayer.drPlayer.getCPS() + " CPS");
    this.width = ints[0];
    this.height = ints[1];
  }

  @Override
  public void renderOutline(ScreenSize scaledResolution, float particleTicks) {
    RenderUtils.drawRectLines(posX - 2, posY - 2, this.width, this.height, 1140850688);
  }

  @Override
  public void render(ScreenSize scaledResolution, float particleTicks) {
    RenderUtils.drawText(
        DRPlayer.drPlayer.getCPS() + " CPS", this.posX, this.posY, Tier.T3.getColor());
  }

  @Override
  public void onLoad() {
    this.setEnabled(true);
    this.posX = 5;
    this.posY = 35;
  }

  @Override
  public void onUnload() {}
}
