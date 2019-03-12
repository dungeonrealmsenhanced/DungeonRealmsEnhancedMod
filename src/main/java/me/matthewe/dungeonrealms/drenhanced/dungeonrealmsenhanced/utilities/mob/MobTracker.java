package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.mob;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.player.DRPlayer;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.ItemUtils;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.Tier;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.text.TextFormatting;

import java.util.List;


/**
 * Created by Matthew E on 5/29/2018 at 11:37 AM for the project DungeonRealmsEnhanced
 */
public class MobTracker {
    private double health;
    private double maxHealth;
    private List<Integer> damageLogs;
    private boolean dead;
    private String realName;
    private int tier = -1;
    private boolean dropped;

    public void update(Entity target) {

        if (target.hasCustomName()) {
            String displayName = TextFormatting.getTextWithoutFormattingCodes(target.getCustomNameTag());
            if (displayName == null) {
                return;
            }

            if (displayName.contains("[") && displayName.contains("]") && displayName.contains(" ")) {

                String trim = displayName.split(" ")[1].trim();
                int hp = -1;
                try {
                    hp = Integer.parseInt(trim);
                } catch (NumberFormatException ignored) {

                }
                if (hp > -1) {
                    if (maxHealth == 0) {
                        maxHealth = hp;
                    }
                    health = hp;
                }
            } else {
                if (realName != null) {
                    return;
                }
                String displayName1 = target.getCustomNameTag();
                this.realName = displayName1;
                if (displayName1.isEmpty() || displayName1.length() < 4) {
                    return;
                }
                switch ("&" + realName.substring(1, 2)) {
                    case "&b":
                        this.tier = 3;
                        break;
                    case "&d":
                        this.tier = 4;
                        break;

                    case "&e":
                        this.tier = 5;
                        break;
                    case "&a":
                        this.tier = 2;
                        break;
                    case "&f":
                        this.tier = 1;
                        break;
                }
                if (tier > -1) {
                    DRPlayer.drPlayer.setLastTier(Tier.getByNumber(tier));
                }
            }
        }
    }


    public boolean isDropped() {
        return dropped;
    }

    public void setDropped(boolean dropped) {
        this.dropped = dropped;
    }

    public void death(Entity target) {
        health = 0;
        dead = true;

        if (tier > -1) {
            DRPlayer.drPlayer.setLastTier(Tier.getByNumber(tier));
        }
        for (EntityItem capturedDrop : target.capturedDrops) {
            if ((ItemUtils.isWeapon(capturedDrop.getItem().getItem()) || ItemUtils.isArmor(capturedDrop.getItem().getItem()))) {
                dropped = true;
                DRPlayer.drPlayer.getStatistics().setDryStreak(Tier.getByNumber(tier), 0);
            }
        }
        if (tier > -1 && !dropped) {
            DRPlayer.drPlayer.getStatistics().increaseDryStreak(Tier.getByNumber(tier), 1);
        }
        DRPlayer.drPlayer.fixDryStreak();
    }

    public int getTier() {
        return tier;
    }
}
