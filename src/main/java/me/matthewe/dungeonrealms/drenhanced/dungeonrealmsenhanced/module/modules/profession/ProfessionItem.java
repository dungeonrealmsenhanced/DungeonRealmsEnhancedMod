package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.modules.profession;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.player.DRPlayer;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.ItemType;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.ItemUtils;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.Tier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Matthew Eisenberg on 3/12/2019 at 6:39 PM for the project DungeonRealmsDREnhanced
 */
public class ProfessionItem {
    private int level;
    private int experience;
    private Map<String, Integer> enchants;
    private String trinket;
    private int trinketLevel;
    private ItemType itemType;

    public ProfessionItem(int level, int experience, Map<String, Integer> enchants, String trinket, int trinketLevel, ItemType itemType) {
        this.level = level;
        this.experience = experience;
        this.enchants = enchants;
        this.trinket = trinket;
        this.trinketLevel = trinketLevel;
        this.itemType = itemType;
    }

    public int getNeededExperience(int level) {
        if (level <= 1)
            return 176;

        if (level == 100)
            return 0;

        int lastLevel = level - 1;
        return (int) (Math.pow(lastLevel, 2) + (lastLevel * 20) + 150 + (lastLevel * 4) + getNeededExperience(lastLevel));
    }

    public static boolean has() {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        ItemStack itemStack = player.getHeldItem(EnumHand.MAIN_HAND);
        if ((itemStack.getItem() == Items.AIR) || (itemStack.getCount() == 0)) {
            return false;
        }
        DRPlayer drPlayer = DRPlayer.get();
        if ((drPlayer != null) && (player != null) && ItemType.isProfessionItem(itemStack)) {
            ProfessionItem professionItem = ProfessionItem.of(itemStack);
            return professionItem != null;
        }
        return false;
    }

    public static ProfessionItem get() {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        ItemStack itemStack = player.getHeldItem(EnumHand.MAIN_HAND);
        if ((itemStack.getItem() == Items.AIR) || (itemStack.getCount() == 0)) {
            return null;
        }
        DRPlayer drPlayer = DRPlayer.get();
        if ((drPlayer != null) && (player != null) && ItemType.isProfessionItem(itemStack)) {
            return ProfessionItem.of(itemStack);
        }
        return null;
    }

    public ProfessionItem() {
    }

    public int getLevel() {
        return level;
    }

    public int getExperience() {
        return experience;
    }

    public Map<String, Integer> getEnchants() {
        return enchants;
    }

    public String getTrinket() {
        return trinket;
    }

    public int getTrinketLevel() {
        return trinketLevel;
    }

    public static ProfessionItem of(ItemStack itemStack) {
        if (ItemType.isProfessionItem(itemStack) && itemStack.hasTagCompound() && (itemStack.getTagCompound() != null)) {
            NBTTagCompound tagCompound = itemStack.getTagCompound();
            Map<String, int[]> modifierMap = ItemUtils.getModifierMap(itemStack);

            Map<String, Integer> enchants = new ConcurrentHashMap<>();
            if ((modifierMap != null) && !modifierMap.isEmpty()) {
                modifierMap.forEach((s, ints) -> enchants.put(s, ints[0]));
            }
            int level = tagCompound.hasKey("level") ? tagCompound.getInteger("level") : 0;
            int experience = tagCompound.hasKey("xp") ? tagCompound.getInteger("xp") : 0;
            return new ProfessionItem(level, experience, enchants, null, -1, ItemType.getFromItemStack(itemStack));
        }
        return null;
    }

    public Tier getTier() {
        if (level < 20) {
            return Tier.T1;
        } else if (level >= 20 && level < 40) {
            return Tier.T2;
        } else if (level >= 40 && level < 60) {
            return Tier.T3;
        } else if (level >= 60 && level < 80) {
            return Tier.T4;
        } else if (level >= 80 && level < 100) {
            return Tier.T5;
        } else if (level >= 100) {
            return Tier.T5;
        }
        return null;
    }
}
