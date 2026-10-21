package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.modules;

import java.text.DecimalFormat;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.listeners.RarityOverlayListener;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.Module;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.player.DRPlayer;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.Tier;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.render.RenderUtils;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.render.ScreenSize;
import net.minecraft.ChatFormatting;

/** Created by Matthew E on 12/27/2020 at 10:32 PM EST for the project DungeonRealmsDREnhanced */
public class GemsModule extends Module {
  public GemsModule() {
    super("Gems");
  }

  @Override
  public void renderEditing(ScreenSize resolution, float partialTicks) {
    RenderUtils.drawText(getTextToShow(), this.posX, this.posY, Tier.T2.getColor());
  }

  private String getTextToShow() {
    long gems = DRPlayer.get().getStatistics().getBankGems() + RarityOverlayListener.inventoryGems;
    return ChatFormatting.GREEN
        + new DecimalFormat("#,###").format(gems)
        + ChatFormatting.BOLD.toString()
        + "g";
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
    this.posX = 150;
    this.posY = 30;
  }

  @Override
  public void onUnload() {}
}
