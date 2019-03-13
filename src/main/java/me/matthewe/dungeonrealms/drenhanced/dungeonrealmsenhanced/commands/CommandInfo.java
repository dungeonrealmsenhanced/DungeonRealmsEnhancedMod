package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.commands;


import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.mining.MiningHandler;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.modules.profession.ProfessionItem;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.player.profession.mining.MiningDataResult;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.player.profession.mining.MiningRequestType;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.HoverEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthew E on 3/10/2019 at 4:49 PM for the project DungeonRealmsDREnhanced
 */
public class CommandInfo extends CommandBase {
    public CommandInfo() {
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public String getName() {
        return "info1";

    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/info1 (player)";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length != 1) {
            sender.sendMessage(new TextComponentString(getUsage(sender)));
            return;
        }
        String player = args[0];
        MiningHandler.getMiningThread().request(MiningRequestType.MINING_STATS, o -> {

            if (o instanceof MiningDataResult) {
                MiningDataResult miningDataResult = (MiningDataResult) o;
                ProfessionItem professionItem = ProfessionItem.get();
                if (professionItem != null) {
                    sender.sendMessage(new TextComponentString(TextFormatting.GRAY + "Level" + TextFormatting.WHITE + ": " + TextFormatting.AQUA + professionItem.getLevel()));
                    TextComponentString components = new TextComponentString(TextFormatting.GRAY + "Next Level" + TextFormatting.WHITE + ": " + TextFormatting.AQUA + professionItem.getLevel());
                    List<String> lines = new ArrayList<>();
                    lines.add("&b&lCurrent Tier Ore&f&l:");
                    lines.add(" &bOre Remaining&f: &7"+miningDataResult.getCurrentTierOreRemaining());
                    lines.add(" &cFail Ore&f: &7"+miningDataResult.getCurrentTierFailCount());
                    lines.add(" &aSuccessful Ore&f: &7"+miningDataResult.getCurrentTierSuccessCount());
                    lines.add(" ");
                    lines.add("&b&lLower Tier Ore&f&l:");
                    lines.add(" &bOre Remaining&f: &7"+miningDataResult.getLowerTierOreRemaining());
                    lines.add(" &cFail Ore&f: &7"+miningDataResult.getLowerTierFailCount());
                    lines.add(" &aSuccessful Ore&f: &7"+miningDataResult.getLowerTierSuccessCount());
                    String lineString = "";
                    for (String line : lines) {
                        lineString += line.replaceAll("&","\u00a7") + "\n";
                    }
                    if (lineString.endsWith("\n")) {
                        lineString = lineString.substring(0, lineString.length() - "\n".length());
                    }
                    TextComponentString text = new TextComponentString(lineString);
                    components.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, text));
                    sender.sendMessage(components);

                }
            }
        });
    }
}
