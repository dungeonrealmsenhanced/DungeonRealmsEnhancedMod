package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item;

import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

/** Created by Matthew Eisenberg on 3/12/2019 at 1:33 PM for the project DungeonRealmsDREnhanced */
public class ClueScroll {
  private String objective;
  private List<String> lore;
  private int progress;
  private int goal;
  private String displayName;

  public ClueScroll(
      String objective, List<String> lore, int progress, int goal, String displayName) {
    this.objective = objective;
    this.lore = lore;
    this.progress = progress;
    this.goal = goal;
    this.displayName = displayName;
  }

  public static ClueScroll of(ItemStack itemStack) {
    if (!ItemType.isClueScroll(itemStack)) {
      return null;
    }
    CompoundTag tagCompound = ItemData.tag(itemStack);
    if (tagCompound == null) {
      return null;
    }
    if (tagCompound.contains("goal") && tagCompound.contains("objective")) {
      List<String> lore = ItemUtils.getLore(itemStack);

      int goal = tagCompound.getIntOr("goal", 0);
      int progress = tagCompound.contains("progress") ? tagCompound.getIntOr("progress", 0) : 0;
      String objective =
          tagCompound.contains("objective") ? tagCompound.getStringOr("objective", "") : null;
      return new ClueScroll(objective, lore, progress, goal, itemStack.getHoverName().getString());
    }
    return null;
  }

  public String getObjective() {
    return objective;
  }

  public List<String> getLore() {
    return lore;
  }

  public int getProgress() {
    return progress;
  }

  public int getGoal() {
    return goal;
  }

  public String getDisplayName() {
    return displayName;
  }
}
