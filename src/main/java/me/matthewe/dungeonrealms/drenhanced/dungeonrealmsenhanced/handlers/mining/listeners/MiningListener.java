package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.mining.listeners;

import com.sun.prism.shader.DrawPgram_ImagePattern_Loader;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.DREnhanced;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.modules.profession.ProfessionItem;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.player.DRPlayer;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.player.profession.mining.Mining;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.setting.DRSettings;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.Listener;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.Percent;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.ItemType;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.ItemUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static net.minecraftforge.fml.common.FMLLog.getLogger;

/**
 * Created by Matthew E on 3/19/2019 at 2:43 PM for the project DungeonRealmsDREnhanced
 */
public class MiningListener implements Listener {
    private Pattern pattern = Pattern.compile(".*\\+([0-9]*)\\sEXP\\s(\\[)([0-9]*)\\/([0-9]*)\\sEXP\\]");
    private Pattern patternExperienceBuff = Pattern.compile(".*\\+([0-9]*)\\sEXP\\s\\[EXP\\sBUFF\\]\\s(\\[)([0-9]*)\\/([0-9]*)\\sEXP\\]");
    public static final boolean BOTTLE_EXPERIENCE_ENABLED = false;
    public static final boolean NEW_LORE_ENABLED = false;
    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent event) {
        ItemStack itemStack = event.getItemStack();
        if (itemStack != null && !itemStack.isEmpty() && itemStack.hasTagCompound()) {

            if (DRSettings.MISC_PROFESSION_EXP.get(boolean.class) && ProfessionItem.of(itemStack) != null) {
                NBTTagCompound tagCompound = itemStack.getTagCompound();

                List<String> newToolTip = new ArrayList<>();
                String last = event.getToolTip().subList(event.getToolTip().size() - 1, event.getToolTip().size()).get(0);
                for (String s : event.getToolTip().subList(0, event.getToolTip().size() - 1)) {
                    newToolTip.add(s);
                }
                if (tagCompound.hasKey("bottleXP") && BOTTLE_EXPERIENCE_ENABLED) {
                    double integer = tagCompound.getDouble("bottleXP");
                    double percentage = ((double) integer * 100.0D) / 100000.0D;
//                    TextFormatting color = TextFormatting.WHITE;
//
//                    int time = percentage <= 33.3333 ? 0 : percentage >= 66.6666 && percentage < 99.9999 ? 1 : 2;
//                    switch (time) {
//                        case 0:
//                            color = TextFormatting.GREEN;
//                            break;
//                        case 1:
//                            color = TextFormatting.YELLOW;
//                            break;
//                        case 2:
//                            color = TextFormatting.RED;
//                            break;
//                    }

                    if (GuiScreen.isShiftKeyDown()) {
                        newToolTip.add(TextFormatting.GRAY + TextFormatting.BOLD.toString() + "XP Bottles Used: " + new Percent(percentage).getColor() + new DecimalFormat("#,###").format(integer) + TextFormatting.GRAY + "/" + new Percent(100).getColor() + "100,000");

                    } else {
                        String expBar = "||||||||||||||||||||||||||||||||||||||||||||||||||";
                        double percentDone = percentage;
                        int display = (int) (percentDone / 2D);
                        display = Math.max(1, Math.min(display, 50));
                        String formattedXPBar;
                        if (percentDone == 0) {
                            formattedXPBar = TextFormatting.RED + expBar;
                        } else {
                            formattedXPBar = TextFormatting.GREEN + expBar.substring(0, display) + TextFormatting.RED + expBar.substring(display, expBar.length());
                        }

                        newToolTip.add(TextFormatting.GRAY + TextFormatting.BOLD.toString() + "XP Bottles Used: " + formattedXPBar);
                    }
                }
                newToolTip.add(last);

                if (!newToolTip.isEmpty()) {
                    event.getToolTip().clear();
                    event.getToolTip().addAll(newToolTip);

                }
            }
            ProfessionItem of = ProfessionItem.of(itemStack);
            if (of != null && (DRSettings.MISC_PROFESSION_PERCENT_LEFT.get(boolean.class)) &&DRPlayer.get()!=null ) {

                List<String> newToolTip = new ArrayList<>();
                String last = event.getToolTip().subList(event.getToolTip().size() - 1, event.getToolTip().size()).get(0);
                for (String s : event.getToolTip().subList(0, event.getToolTip().size() - 1)) {
                    newToolTip.add(s);
                }
                Mining mining = DRPlayer.get().getMining();

//                mining.getExperiencePerMinute()

                long remainingExperienceForNextTier = mining.getRemainingExperienceForNextTier(of);

                long maxXPForTier = of.getTier().getExperienceRequirement();



                if ((remainingExperienceForNextTier > 0) && (maxXPForTier > 0)) {
                    double percent = (100.0D - ((double) remainingExperienceForNextTier * 100.0D) / (double) maxXPForTier);
                    if (percent > 100.0D) {
                        percent = 100.0D;
                    }
                    newToolTip.add(TextFormatting.GRAY + "Progress: "  + new Percent(percent).getColor() + new DecimalFormat(DRPlayer.get().getPercentFormat()).format(percent) + TextFormatting.BOLD + "%" );


                }



                newToolTip.add(last);

                if (!newToolTip.isEmpty()) {
                    event.getToolTip().clear();
                    event.getToolTip().addAll(newToolTip);

                }


            }

//            if (NEW_LORE_ENABLED && DRSettings.MISC_PROFESSION_ENCHANT_INFO.get(boolean.class) && itemStack.hasDisplayName()) {
////            if (false) {
//                List<String> newToolTip = new ArrayList<>();
//                NBTTagCompound tagCompound = itemStack.getTagCompound();
//                if (ItemType.isWeapon(itemStack)) {
//                    newToolTip.add(itemStack.getDisplayName());
//                    Map<String, double[]> modifierMap = ItemUtils.getModifierMap(itemStack);
//                    int minDamage = 0;
//                    int maxDamage = 0;
//                    if (modifierMap.containsKey("MELEE_DAMAGE")) {
//                        double[] ints = modifierMap.get("MELEE_DAMAGE");
//                        minDamage = (int) ints[0];
//                        maxDamage = (int) ints[1];
//                        modifierMap.remove("MELEE_DAMAGE");
//                    }
//                    int plus = 0;
//                    if (tagCompound.hasKey("enchant")) {
//                        plus = tagCompound.getInteger("enchant");
//                    }
//
//
//                    newToolTip.add(TextFormatting.RED + "DMG: " + minDamage + " - " + maxDamage);
//                    if (modifierMap.containsKey("ICE_DAMAGE")) {
//                        newToolTip.add(TextFormatting.RED + "ICE DMG: +" + modifierMap.get("ICE_DAMAGE")[0]);
//                        newToolTip.add(TextFormatting.GRAY + "Ice damage has a chance to to " + TextFormatting.BLUE + TextFormatting.BOLD.toString() + "FREEZE" + TextFormatting.GRAY + " opponents,");
//                        newToolTip.add(TextFormatting.GRAY + "Also ice damage increases damage by " + TextFormatting.BLUE + modifierMap.get("ICE_DAMAGE")[0] + TextFormatting.GRAY + ".");
//                        newToolTip.add(" ");
//                        modifierMap.remove("ICE_DAMAGE");
//                    }
//                    if (modifierMap.containsKey("FIRE_DAMAGE")) {
//                        newToolTip.add(TextFormatting.RED + "FIRE DMG: +" + modifierMap.get("FIRE_DAMAGE")[0]);
//                        newToolTip.add(TextFormatting.GRAY + "Fire damage has a chance to to " + TextFormatting.RED + TextFormatting.BOLD.toString() + "BURN" + TextFormatting.GRAY + " opponents,");
//                        newToolTip.add(TextFormatting.GRAY + "Also fire damage increases damage by " + TextFormatting.RED + TextFormatting.BOLD.toString() + modifierMap.get("FIRE_DAMAGE")[0] + TextFormatting.GRAY + ".");
//                        newToolTip.add(" ");
//                        modifierMap.remove("FIRE_DAMAGE");
//                    }
//                    if (modifierMap.containsKey("POISON_DAMAGE")) {
//                        newToolTip.add(TextFormatting.RED + "POISON DMG: +" + modifierMap.get("POISON_DAMAGE")[0]);
//                        newToolTip.add(TextFormatting.GRAY + "Poison damage has a chance to to " + TextFormatting.DARK_GREEN + TextFormatting.BOLD.toString() + "POISON" + TextFormatting.GRAY + " opponents,");
//                        newToolTip.add(TextFormatting.GRAY + "Also poison damage increases damage by " + TextFormatting.DARK_GREEN + TextFormatting.BOLD.toString() + modifierMap.get("POISON_DAMAGE")[0] + TextFormatting.GRAY + ".");
//                        newToolTip.add(" ");
//                        modifierMap.remove("POISON_DAMAGE");
//                    }
//                    if (modifierMap.containsKey("PURE_DAMAGE")) {
//                        newToolTip.add(TextFormatting.RED + "PURE DMG: +" + modifierMap.get("PURE_DAMAGE")[0]);
//                        newToolTip.add(TextFormatting.GRAY + "Pure damage " + TextFormatting.GOLD + TextFormatting.BOLD.toString() + "PENETRATES" + TextFormatting.GRAY + " your opponents armor,");
//                        newToolTip.add(TextFormatting.GRAY + "Also pure damage increases damage by " + TextFormatting.GOLD + TextFormatting.BOLD.toString() + modifierMap.get("PURE_DAMAGE")[0] + TextFormatting.GRAY + ".");
//                        newToolTip.add(" ");
//                        modifierMap.remove("PURE_DAMAGE");
//                    }
//                    for (Map.Entry<String, double[]> stringEntry : modifierMap.entrySet()) {
//                        double[] value = stringEntry.getValue();
//                        String valueString = "";
//                        if (value.length == 1) {
//                            valueString = value[0] + "%";
//                        } else {
//                            valueString = value[0] + " - " + value[1];
//                        }
//                        if (stringEntry.getKey().equalsIgnoreCase("CRITICAL_HIT")) {
//                            newToolTip.add(TextFormatting.RED + "CRITICAL HIT: " + value[0] + "%");
//                            newToolTip.add(TextFormatting.GRAY + "Critical hit has a chance to " + TextFormatting.YELLOW + TextFormatting.BOLD.toString() + "DOUBLE " + TextFormatting.GRAY + "damage.");
//                            newToolTip.add(" ");
//                        } else if (stringEntry.getKey().equalsIgnoreCase("")) {
//
//                        } else {
//
//                            newToolTip.add(TextFormatting.RED + new String(stringEntry.getKey()).replaceAll("_", " ").toUpperCase() + ": " + valueString);
//                            newToolTip.add(TextFormatting.GRAY + stringEntry.getKey());
//                        }
//                        newToolTip.add(" ");
//                    }
//
//
//                }
//
//                if (GuiScreen.isShiftKeyDown()) {
//                    if (!newToolTip.isEmpty()) {
//                        event.getToolTip().clear();
//                        event.getToolTip().addAll(newToolTip);
//                    }
//                }
//
//            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onClientChatReceived(ClientChatReceivedEvent event) {
        long start =System.currentTimeMillis();
        String message = event.getMessage().getUnformattedText();
        if (ProfessionItem.has() && ((pattern.matcher(message).matches() || patternExperienceBuff.matcher(message).matches()))) {
            ProfessionItem professionItem = ProfessionItem.get();
            if (professionItem == null) {
                return;
            }
            message = message.replaceAll("\\(EXP BUFF\\)", "").replaceAll("\\[EXP BUFF\\]", "").trim().replaceAll(" {2}", " ");
            int experienceGain = Integer.parseInt(message.split(" EXP \\[")[0].trim().split("\\+")[1].trim());
            String[] strings = message.split("\\[")[1].trim().split("EXP \\]")[0].trim().replaceAll(" EXP]", "").trim().split("/");
            int newExperience = Integer.parseInt(strings[0].trim());
            int maxExperience = Integer.parseInt(strings[1].trim());

            double xp = newExperience - experienceGain;
            if (xp < 0) {
                xp = 0;
            }
            double percentage = ((double) newExperience * 100.0D) / (double) maxExperience;
            if (percentage > 100.0D) {
                percentage = 100.0D;
            }
            try {
                if (professionItem.isFishingRod()) {

                    if (DRSettings.FISHING_EXP_PERCENTAGE.get(boolean.class)) {
                        ITextComponent message1 = event.getMessage();


                        TextComponentString componentString = new TextComponentString(TextFormatting.GRAY + "[" + new Percent(percentage).getColor() + new DecimalFormat(DRPlayer.get().getPercentFormat()).format(percentage) + TextFormatting.BOLD + "%" + TextFormatting.GRAY + "]");
                        Style style = componentString.getStyle();

                        double v = ((double) xp * 100.0D) / (double) maxExperience;
                        double v1 = percentage - v;
                        style.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString(TextFormatting.GREEN + TextFormatting.BOLD.toString() + "+" + TextFormatting.GREEN +   new DecimalFormat(DRPlayer.get().getPercentFormat()).format(v1) + TextFormatting.BOLD.toString() + "%")));
                        componentString.setStyle(style);
                        message1.appendText(" ").appendSibling(componentString);

                        event.setMessage(message1);
                    }
                } else if (professionItem.isPickaxe()) {
                    if (DRSettings.MINING_EXP_PERCENTAGE.get(boolean.class)) {
                        ITextComponent message1 = event.getMessage();
                        TextComponentString componentString = new TextComponentString(TextFormatting.GRAY + "[" + new Percent(percentage).getColor() + new DecimalFormat(DRPlayer.get().getPercentFormat()).format(percentage) + TextFormatting.BOLD + "%" + TextFormatting.GRAY + "]");
                        Style style = componentString.getStyle();

                        double v = ((double) xp * 100.0D) / (double) maxExperience;
                        double v1 = percentage - v;
                        style.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString(TextFormatting.GREEN + TextFormatting.BOLD.toString() + "+" + TextFormatting.GREEN +  new DecimalFormat(DRPlayer.get().getPercentFormat()).format(v1) + TextFormatting.BOLD.toString() + "%")));
                        componentString.setStyle(style);
                        message1.appendText(" ").appendSibling(componentString);

                        event.setMessage(message1);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                getLogger().error(e);
            }
            if (professionItem.getNeededExperience(professionItem.getLevel()) == maxExperience) {
                DRPlayer.get().getMining().onExperienceGain(experienceGain);

            }
        }
    }

}
