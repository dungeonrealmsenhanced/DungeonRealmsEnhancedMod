package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.MathUtils;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.NumberUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Matthew E on 5/28/2018 at 11:55 AM for the project DungeonRealmsEnhanced
 */
public class ItemUtils {
    public static String getStringLore(ItemStack is) {
        StringBuilder toReturn = new StringBuilder();
        for (String loreLine : getLore(is)) {
            toReturn.append(loreLine);
        }
        return toReturn.toString();
    }

    public static List<String> getLore(ItemStack item) {
        List<String> lore = new ArrayList<>();
        if ((item == null) || !item.hasTagCompound()) {
            return lore;
        }
        if (item.getTagCompound().hasKey("display", 10)) {
            NBTTagCompound nbtTagCompound = item.getTagCompound().getCompoundTag("display");

            if (nbtTagCompound.getTagId("Lore") == 9) {
                NBTTagList nbtTagList = nbtTagCompound.getTagList("Lore", 8);

                if (!nbtTagList.isEmpty()) {
                    for (int l1 = 0; l1 < nbtTagList.tagCount(); ++l1) {
                        lore.add(nbtTagList.getStringTagAt(l1));
                    }
                }
            }
        }
        return lore;
    }

    public static ItemRarity getRarity(ItemStack itemStack) {
        return ItemRarity.getByName(NbtTagUtils.getString("rarity", itemStack));
    }

    public static Map<String, double[]> getModifierMap(ItemStack itemStack) {
        Map<String, double[]> modifierMap = new ConcurrentHashMap<>();
        if ((itemStack.getItem() != Items.AIR) && itemStack.hasDisplayName() && itemStack.hasTagCompound()) {
            NBTTagCompound tagCompound = itemStack.getTagCompound();
            if (tagCompound == null) {
                return null;
            }

            if (tagCompound.hasKey("augmentations")) {
                NBTTagCompound augmentations = tagCompound.getCompoundTag("augmentations");
                for (String s : augmentations.getKeySet()) {
                    double v = Double.parseDouble(augmentations.getTag(s).toString());
                    double[] values = {v};
                    modifierMap.put(s,values);
                }
            }

            NBTTagCompound modifiers = tagCompound.getCompoundTag("modifiers");
            for (String s : modifiers.getKeySet()) {
                String s1 = modifiers.getTag(s).toString();
                if (s1.startsWith("[")) {
                    String trim = s1.split("\\[")[1].trim().split("\\]")[0].trim();
                    if (trim.contains(",")) {
                        String[] split = trim.split(",");
                        double[] ints = new double[split.length];
                        for (int i = 0; i < split.length; i++) {
                            String s2 = split[i];
                            ints[i] = NumberUtils.getNumber(s2.trim());
                        }
                        if (modifierMap.containsKey(s)){
                            modifierMap.put(s, MathUtils.elementWiseSum(ints, modifierMap.get(s)));
                        } else {

                            modifierMap.put(s, ints);
                        }
                    } else {

                        double[] ints = new double[1];
                        ints[0] = NumberUtils.getNumber(trim.trim());
                        if (modifierMap.containsKey(s)){
                            modifierMap.put(s, MathUtils.elementWiseSum(ints, modifierMap.get(s)));
                        } else {
                            modifierMap.put(s, ints);
                        }
                    }
                }
            }
        }
        return !modifierMap.isEmpty() ? modifierMap : null;
    }


    public static boolean isWeapon(Item item) {
        if (item == Items.DIAMOND_SWORD) return true;
        if (item == Items.DIAMOND_AXE) return true;
        if (item == Items.DIAMOND_SHOVEL) return true;
        if (item == Items.DIAMOND_HOE) return true;

        if (item == Items.BOW) return true;

        if (item == Items.IRON_AXE) return true;
        if (item == Items.IRON_SWORD) return true;
        if (item == Items.IRON_SHOVEL) return true;
        if (item == Items.IRON_HOE) return true;

        if (item == Items.GOLDEN_SWORD) return true;
        if (item == Items.GOLDEN_AXE) return true;
        if (item == Items.GOLDEN_HOE) return true;
        if (item == Items.GOLDEN_SHOVEL) return true;

        if (item == Items.WOODEN_AXE) return true;
        if (item == Items.WOODEN_SHOVEL) return true;
        if (item == Items.WOODEN_SWORD) return true;
        if (item == Items.WOODEN_HOE) return true;

        if (item == Items.STONE_AXE) return true;
        if (item == Items.STONE_SWORD) return true;
        if (item == Items.STONE_HOE) return true;
        if (item == Items.STONE_SHOVEL) return true;

        return false;
    }


    public static boolean isHelmet(Item item) {
        if (item == Items.DIAMOND_HELMET) return true;
        if (item == Items.GOLDEN_HELMET) return true;
        if (item == Items.LEATHER_HELMET) return true;
        if (item == Items.CHAINMAIL_HELMET) return true;
        if (item == Items.IRON_HELMET) return true;
        return false;
    }

    public static boolean isChestplate(Item item) {
        if (item == Items.DIAMOND_CHESTPLATE) return true;
        if (item == Items.GOLDEN_CHESTPLATE) return true;
        if (item == Items.LEATHER_CHESTPLATE) return true;
        if (item == Items.CHAINMAIL_CHESTPLATE) return true;
        if (item == Items.IRON_CHESTPLATE) return true;
        return false;
    }

    public static boolean isLeggings(Item item) {
        if (item == Items.DIAMOND_LEGGINGS) return true;
        if (item == Items.GOLDEN_LEGGINGS) return true;
        if (item == Items.LEATHER_LEGGINGS) return true;
        if (item == Items.CHAINMAIL_LEGGINGS) return true;
        if (item == Items.IRON_LEGGINGS) return true;
        return false;
    }
    public static boolean isBoots(Item item) {
        if (item == Items.DIAMOND_BOOTS) return true;
        if (item == Items.GOLDEN_BOOTS) return true;
        if (item == Items.LEATHER_BOOTS) return true;
        if (item == Items.CHAINMAIL_BOOTS) return true;
        if (item == Items.IRON_BOOTS) return true;
        return false;
    }

    public static boolean isAxe(Item item) {
        if (item == Items.DIAMOND_AXE) return true;
        if (item == Items.GOLDEN_AXE) return true;
        if (item == Items.WOODEN_AXE) return true;

        if (item == Items.STONE_AXE) return true;
        if (item == Items.IRON_AXE) return true;
        return false;
    }

    public static boolean isClub(Item item) {
        if (item == Items.DIAMOND_SHOVEL) return true;
        if (item == Items.GOLDEN_SHOVEL) return true;
        if (item == Items.WOODEN_SHOVEL) return true;

        if (item == Items.STONE_SHOVEL) return true;
        if (item == Items.IRON_SHOVEL) return true;
        return false;
    }

    public static boolean isEquippedOrInHotbarSlot0(ItemStack itemStack) {
        Minecraft mc = Minecraft.getMinecraft();

        // If the item is armor, check if it's equipped in an armor slot
        if (isArmor(itemStack.getItem())) {
            for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
                if (slot.getSlotType() == EntityEquipmentSlot.Type.ARMOR) {
                    if (mc.player.getItemStackFromSlot(slot) == itemStack) {
                        return false; // Armor is equipped, do not allow
                    }
                }
            }
        }

        // If the item is neither armor nor a weapon, allow it
        if (!isArmor(itemStack.getItem()) && !isWeapon(itemStack.getItem())) {
            return true;
        }

        // Allow pickaxes specifically
        if (isPickaxe(itemStack.getItem())) {
            return true;
        }

        // Check if the item is in the main hand or offhand
        if (mc.player.getHeldItemMainhand() == itemStack || mc.player.getHeldItemOffhand() == itemStack) {
            return true;
        }

        // Check if the item is in hotbar slot 0
        if (!mc.player.inventory.getStackInSlot(0).isEmpty() && mc.player.inventory.getStackInSlot(0) == itemStack) {
            return true;
        }

        return false;
    }
    public static boolean isScythe(Item item) {
        if (item == Items.DIAMOND_HOE) return true;
        if (item == Items.GOLDEN_HOE) return true;
        if (item == Items.WOODEN_HOE) return true;

        if (item == Items.STONE_HOE) return true;
        if (item == Items.IRON_HOE) return true;
        return false;
    }
    public static boolean isSword(Item item) {
        if (item == Items.DIAMOND_SWORD) return true;
        if (item == Items.GOLDEN_SWORD) return true;
        if (item == Items.WOODEN_SWORD) return true;

        if (item == Items.STONE_SWORD) return true;
        if (item == Items.IRON_SWORD) return true;
        return false;
    }
    public static boolean isPickaxe(Item item) {
        if (item == Items.DIAMOND_PICKAXE) return true;
        if (item == Items.GOLDEN_PICKAXE) return true;
        if (item == Items.IRON_PICKAXE) return true;

        if (item == Items.STONE_PICKAXE) return true;
        if (item == Items.WOODEN_PICKAXE) return true;
        return false;
    }

    public static boolean isArmor(Item item) {
        if (item == Items.DIAMOND_HELMET) return true;
        if (item == Items.DIAMOND_CHESTPLATE) return true;
        if (item == Items.DIAMOND_BOOTS) return true;
        if (item == Items.DIAMOND_LEGGINGS) return true;


        if (item == Items.IRON_HELMET) return true;
        if (item == Items.IRON_CHESTPLATE) return true;
        if (item == Items.IRON_LEGGINGS) return true;
        if (item == Items.IRON_BOOTS) return true;

        if (item == Items.GOLDEN_HELMET) return true;
        if (item == Items.GOLDEN_CHESTPLATE) return true;
        if (item == Items.GOLDEN_LEGGINGS) return true;
        if (item == Items.GOLDEN_BOOTS) return true;

        if (item == Items.LEATHER_HELMET) return true;
        if (item == Items.LEATHER_CHESTPLATE) return true;
        if (item == Items.LEATHER_LEGGINGS) return true;
        if (item == Items.LEATHER_BOOTS) return true;


        if (item == Items.CHAINMAIL_BOOTS) return true;
        if (item == Items.CHAINMAIL_CHESTPLATE) return true;
        if (item == Items.CHAINMAIL_LEGGINGS) return true;
        if (item == Items.CHAINMAIL_HELMET) return true;

        return false;
    }

    public static Tier getTier(ItemStack itemStack) {
        return Tier.getByNumber(NbtTagUtils.getInt("tier", itemStack));
    }

    public static boolean hasClueScrolls() {
        return getClueScrolls() != null;
    }

    public static List<ClueScroll> getClueScrolls() {
        Minecraft minecraft = Minecraft.getMinecraft();
        EntityPlayerSP player = minecraft.player;
        if (player == null) {
            return null;
        }

        List<ClueScroll> clueScrolls = new ArrayList<>();
        for (Slot slot : player.inventoryContainer.inventorySlots) {
            ItemStack itemStack = slot.getStack();

            if ((itemStack.getItem() == Items.AIR) || (itemStack.getCount() == 0) || (!ItemType.isClueScroll(itemStack))) {
                continue;
            }
            ClueScroll clueScroll = ClueScroll.of(itemStack);
            if (clueScroll != null) {
                clueScrolls.add(clueScroll);
            }
        }
        return !clueScrolls.isEmpty() ? clueScrolls : null;
    }

    public static boolean isMythic(ItemStack itemStack) {
        if ((itemStack.getItem() != Items.AIR) && itemStack.hasDisplayName() && itemStack.hasTagCompound()) {
            NBTTagCompound tagCompound = itemStack.getTagCompound();
            if (tagCompound == null) {
                return false;
            }
            return tagCompound.hasKey("mythic") && tagCompound.getBoolean("mythic");
        }
        return false;
    }
}
