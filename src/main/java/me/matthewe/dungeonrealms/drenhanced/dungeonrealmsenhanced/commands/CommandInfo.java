package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.commands;


import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.mining.MiningHandler;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.player.profession.mining.MiningRequestType;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

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
        MiningHandler.getMiningThread().request(MiningRequestType.ORE_REMAINING, o -> {
            sender.sendMessage(new TextComponentString((String.valueOf(o))));
        });
    }
}
