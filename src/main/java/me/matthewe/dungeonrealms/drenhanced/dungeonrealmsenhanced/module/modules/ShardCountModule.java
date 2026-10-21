package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.modules;

import java.text.DecimalFormat;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.Module;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.Tier;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.render.RenderUtils;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.render.ScreenSize;
import net.minecraft.ChatFormatting;

/** Created by Matthew E on 3/1/2025 at 12:47 AM EST for the project DungeonRealmsDREnhanced */
public class ShardCountModule extends Module {
  public ShardCountModule() {
    super("ShardCount");
  }

  public static void setCount(int count) {
    ShardCountModule.count = count;
  }

  private static int count;
  private static String shard;

  @Override
  public void renderEditing(ScreenSize resolution, float partialTicks) {
    RenderUtils.drawText(getTextToShow(), this.posX, this.posY, 0xFFFF55);
  }

  public static void setShard(String shard) {
    ShardCountModule.shard = shard;
  }

  private final DecimalFormat format = new DecimalFormat("#,###");

  private String getTextToShow() {

    if (shard != null) {
      if (count > 1) {

        return ChatFormatting.YELLOW
            + ChatFormatting.BOLD.toString()
            + shard.toUpperCase()
            + ": "
            + ChatFormatting.WHITE
            + ChatFormatting.BOLD.toString()
            + format.format(count)
            + " Players";
      }
      return ChatFormatting.YELLOW
          + ChatFormatting.BOLD.toString()
          + shard.toUpperCase()
          + ": "
          + ChatFormatting.WHITE
          + ChatFormatting.BOLD.toString()
          + format.format(count)
          + " Player";

    } else {
      return ChatFormatting.RED + "NO SHARD FOUND";
    }
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
    this.posX = 5;
    this.posY = 50;
  }

  @Override
  public void onUnload() {}
}
