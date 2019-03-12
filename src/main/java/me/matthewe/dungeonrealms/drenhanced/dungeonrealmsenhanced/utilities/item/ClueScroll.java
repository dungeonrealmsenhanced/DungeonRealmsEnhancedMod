package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

/**
 * Created by Matthew Eisenberg on 3/12/2019 at 1:33 PM for the project DungeonRealmsDREnhanced
 */
public class ClueScroll {
    private String objective;
    private List<String> lore;
    private int progress;
    private int goal;
    private String displayName;

    public ClueScroll(String objective, List<String> lore, int progress, int goal, String displayName) {
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
        NBTTagCompound tagCompound = itemStack.getTagCompound();
        if (tagCompound == null) {
            return null;
        }
        if (tagCompound.hasKey("goal") && tagCompound.hasKey("objective")) {
            List<String> lore = ItemUtils.getLore(itemStack);

            int goal = tagCompound.getInteger("goal");
            int progress = tagCompound.hasKey("progress") ? tagCompound.getInteger("progress") : 0;
            String objective = tagCompound.hasKey("objective") ? tagCompound.getString("objective") : null;
            return new ClueScroll(objective, lore, progress, goal, itemStack.getDisplayName());
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
