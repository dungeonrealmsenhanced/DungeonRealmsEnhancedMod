package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.MiningUtils;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Created by Matthew E on 12/31/2018 at 1:18 PM for the project DungeonRealmsDREnhanced
 */
public enum Tier {
    T1("T1", 1, new Item[]{
            Items.LEATHER_BOOTS,
            Items.LEATHER_LEGGINGS,
            Items.LEATHER_CHESTPLATE,
            Items.LEATHER_HELMET,
            Items.BOW,
            Items.WOODEN_SWORD,
            Items.WOODEN_AXE,
            Items.WOODEN_SHOVEL,
            Items.WOODEN_PICKAXE,
    }, Items.LEATHER, null, ChatFormatting.WHITE, 0xFFFFFF, 67184),
    T2("T2", 2, new Item[]{
            Items.CHAINMAIL_BOOTS,
            Items.CHAINMAIL_LEGGINGS,
            Items.CHAINMAIL_CHESTPLATE,
            Items.CHAINMAIL_HELMET,
            Items.BOW,
            Items.STONE_SWORD,
            Items.STONE_AXE,
            Items.STONE_SHOVEL,
            Items.STONE_PICKAXE}, Item.getItemFromBlock(Blocks.IRON_BARS), null, ChatFormatting.GREEN, 0x55FF55,
            480610),
    T3("T3", 3, new Item[]{
            Items.IRON_BOOTS,
            Items.IRON_LEGGINGS,
            Items.IRON_CHESTPLATE,
            Items.IRON_HELMET,
            Items.BOW,
            Items.IRON_SWORD,
            Items.IRON_AXE,
            Items.IRON_SHOVEL,
            Items.IRON_PICKAXE}, Items.DYE, EnumDyeColor.SILVER, ChatFormatting.AQUA, 0x55FFFF,

            1550010),
    T4("T4", 4, new Item[]{
            Items.DIAMOND_BOOTS,
            Items.DIAMOND_LEGGINGS,
            Items.DIAMOND_CHESTPLATE,
            Items.DIAMOND_HELMET,
            Items.BOW,
            Items.DIAMOND_SWORD,
            Items.DIAMOND_AXE,
            Items.DIAMOND_SHOVEL,
            Items.DIAMOND_PICKAXE}, Items.DYE, EnumDyeColor.LIGHT_BLUE, ChatFormatting.LIGHT_PURPLE, 0xff55ff, 3595410),
    T5("T5", 5, new Item[]{
            Items.GOLDEN_BOOTS,
            Items.GOLDEN_LEGGINGS,
            Items.GOLDEN_CHESTPLATE,
            Items.GOLDEN_HELMET,
            Items.BOW,
            Items.GOLDEN_SWORD,
            Items.GOLDEN_AXE,
            Items.GOLDEN_SHOVEL,
            Items.GOLDEN_PICKAXE}, Items.DYE, EnumDyeColor.YELLOW, ChatFormatting.YELLOW, 0xffff00,6936810),
    ;

    private String name;
    private int number;
    private Item[] items;
    private Item scrapItem;
    private EnumDyeColor dyeColor;
    private ChatFormatting chatFormatting;
    private int color;
    private long experienceRequirement;

    Tier(String name, int number, Item[] items, Item scrapItem, EnumDyeColor dyeColor, ChatFormatting chatFormatting, int color, long experienceRequirement) {
        this.name = name;
        this.number = number;
        this.items = items;
        this.scrapItem = scrapItem;
        this.dyeColor = dyeColor;
        this.chatFormatting = chatFormatting;
        this.color = color;
        this.experienceRequirement = experienceRequirement;
    }

    public long getExperienceRequirement() {
        return experienceRequirement;
    }

    public int getColor() {
        return color;
    }

    public static Tier getByNumber(int number) {
        return getByPredicate(tier -> Objects.equals(tier.getNumber(), number));
    }

    public static Tier getByChatFormatting(ChatFormatting chatFormatting) {
        return getByPredicate(tier -> Objects.equals(tier.getChatFormatting(), chatFormatting));
    }

    public static Tier getByPredicate(Predicate<Tier> predicate) {
        return Arrays.stream(values()).filter(predicate).findFirst().orElse(null);
    }

    public Item getScrapItem() {
        return scrapItem;
    }


    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }


    public Item[] getItems() {
        return items;
    }

    public EnumDyeColor getDyeColor() {
        return dyeColor;
    }

    public ChatFormatting getChatFormatting() {
        return chatFormatting;
    }}
