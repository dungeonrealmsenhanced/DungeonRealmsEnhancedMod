package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item;

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
import java.util.Arrays;
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

    public static int[] calculateDPS(ItemStack itemStack) {
        if (!isWeapon(itemStack.getItem())) return new int[]{0,0};
        Map<String, double[]> modifierMap = getModifierMap(itemStack);


        double[] damage = getDamage(itemStack, modifierMap);

        double dmgToAdd = 0;


        for (String elemental : Arrays.asList("ICE_DAMAGE", "PURE_DAMAGE", "FIRE_DAMAGE", "POISON_DAMAGE")) {

            if (modifierMap.containsKey(elemental)) {

                dmgToAdd += modifierMap.get(elemental)[0];
            }
        }

        double multipliers = 1.0D;
        if (modifierMap.containsKey("VS_MONSTERS")) {
            double multiplier = modifierMap.get("VS_MONSTERS")[0];

            multipliers+= (multiplier/100.0D);
        }

        double criticalMultiplier = 1.0;
        if (modifierMap.containsKey("CRITICAL_HIT")) {
            double criticalChance = modifierMap.get("CRITICAL_HIT")[0] / 100.0; // Convert % to decimal
            criticalMultiplier = 1 + criticalChance; // Weighted average of crit damage
        }

        double executeMultiplier = 1.0;
        if (modifierMap.containsKey("EXECUTE")) {
            double executePercent = modifierMap.get("EXECUTE")[0] / 100.0;
            executeMultiplier += executePercent * 0.50; // Assume 50% effectiveness over time
        }

        // Apply estimated Crushing multiplier (50% effectiveness over a fight)
        double crushingMultiplier = 1.0;
        if (modifierMap.containsKey("CRUSHING")) {
            double crushingPercent = modifierMap.get("CRUSHING")[0] / 100.0;
            crushingMultiplier += crushingPercent * 0.50; // Assume 50% effectiveness over time
        }

        // Final DPS calculation
        double dpsMin = damage[0] * multipliers * criticalMultiplier * executeMultiplier * crushingMultiplier;
        double dpsMax = damage[1] * multipliers * criticalMultiplier * executeMultiplier * crushingMultiplier;

        dpsMax+=dmgToAdd;
        dpsMin+=dmgToAdd;
        return new int[] {(int) Math.round(dpsMin), (int) Math.round(dpsMax)};


    }

    public static double[] getDamage(ItemStack itemStack, Map<String, double[]> modifierMap) {

        if (itemStack.getItem()==Items.BOW) {
           return modifierMap.get("RANGE_DAMAGE");
        }
        return modifierMap.get("MELEE_DAMAGE");
    }

    public static Map<String, double[]> getModifierMap(ItemStack itemStack) {
        return getModifierMap(itemStack, false);
    }
    public static Map<String, double[]> getModifierMap(ItemStack itemStack, boolean skipAugmentations) {
        Map<String, double[]> modifierMap = new ConcurrentHashMap<>();
        if ((itemStack.getItem() != Items.AIR) && itemStack.hasDisplayName() && itemStack.hasTagCompound()) {
            NBTTagCompound tagCompound = itemStack.getTagCompound();
            if (tagCompound == null) {
                return null;
            }

            if (!skipAugmentations) {

                if (tagCompound.hasKey("augmentations")) {
                    NBTTagCompound augmentations = tagCompound.getCompoundTag("augmentations");
                    for (String s : augmentations.getKeySet()) {
                        double v = Double.parseDouble(augmentations.getTag(s).toString());
                        double[] values = {v};

                        modifierMap.put(s,values);
                    }
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

                            double[] doubles = modifierMap.get(s);
                            double[] news = new  double[] {ints[0]+doubles[0],ints[1]+doubles[1] };

                            modifierMap.put(s,news);
                        } else {

                            modifierMap.put(s, ints);
                        }
                    } else {

                        double[] ints = new double[1];
                        ints[0] = NumberUtils.getNumber(trim.trim());


                        if (modifierMap.containsKey(s)){
                            double[] doubles = modifierMap.get(s);
                            modifierMap.put(s,  new double[] {doubles[0]+ints[0]});
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


    public static int getLevel(ItemStack itemStack) {
        if ((itemStack.getItem() != Items.AIR) && itemStack.hasDisplayName() && itemStack.hasTagCompound()) {
            NBTTagCompound tagCompound = itemStack.getTagCompound();
            if (tagCompound == null) {
                return 0;
            }

            if (tagCompound.hasKey("level")) {
                return tagCompound.getInteger("level");
            }
        }
        return 0;
    }
    public static int getEnchant(ItemStack itemStack) {
        if ((itemStack.getItem() != Items.AIR) && itemStack.hasDisplayName() && itemStack.hasTagCompound()) {
            NBTTagCompound tagCompound = itemStack.getTagCompound();
            if (tagCompound == null) {
                return 0;
            }

            if (tagCompound.hasKey("enchant")) {
                return tagCompound.getInteger("enchant");
            }
        }
        return 0;
    }

    public static ItemType getType(ItemStack stack) {
        // Helmets
        if (stack.getItem() == Items.LEATHER_HELMET || stack.getItem() == Items.CHAINMAIL_HELMET ||
                stack.getItem() == Items.IRON_HELMET || stack.getItem() == Items.DIAMOND_HELMET ||
                stack.getItem() == Items.GOLDEN_HELMET) {
            return ItemType.HELMET;
        }
        // Chestplate
        if (stack.getItem() == Items.LEATHER_CHESTPLATE || stack.getItem() == Items.CHAINMAIL_CHESTPLATE ||
                stack.getItem() == Items.IRON_CHESTPLATE || stack.getItem() == Items.DIAMOND_CHESTPLATE ||
                stack.getItem() == Items.GOLDEN_CHESTPLATE) {
            return ItemType.CHESTPLATE;
        }
        // Leggings
        if (stack.getItem() == Items.LEATHER_LEGGINGS || stack.getItem() == Items.CHAINMAIL_LEGGINGS ||
                stack.getItem() == Items.IRON_LEGGINGS || stack.getItem() == Items.DIAMOND_LEGGINGS ||
                stack.getItem() == Items.GOLDEN_LEGGINGS) {
            return ItemType.LEGGINGS;
        }
        // Boots
        if (stack.getItem() == Items.LEATHER_BOOTS || stack.getItem() == Items.CHAINMAIL_BOOTS ||
                stack.getItem() == Items.IRON_BOOTS || stack.getItem() == Items.DIAMOND_BOOTS ||
                stack.getItem() == Items.GOLDEN_BOOTS) {
            return ItemType.BOOTS;
        }
        if (isSword(stack.getItem())) return ItemType.SWORD;
        if (isAxe(stack.getItem())) return ItemType.AXE;
        if (isPickaxe(stack.getItem())) return ItemType.PICKAXE;
        if (isClub(stack.getItem())) return ItemType.POLEARM;
        if (isScythe(stack.getItem())) return ItemType.HOE;
        if (stack.getItem()== Items.FISHING_ROD) return ItemType.FISHING_ROD;
        return ItemType.NONE;
    }

    public enum ItemType {
        NONE,
        BOOTS,
        LEGGINGS,
        CHESTPLATE,
        HELMET,
        SWORD,
        BOW,
        FISHING_ROD,
        PICKAXE,
        AXE,
        POLEARM,
        HOE;
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

            if ((itemStack.getItem() == Items.AIR) || (itemStack.getCount() == 0) || (!me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.ItemType.isClueScroll(itemStack))) {
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
