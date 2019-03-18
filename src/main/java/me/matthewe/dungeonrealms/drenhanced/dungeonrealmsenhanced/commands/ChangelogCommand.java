package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.commands;


import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.restful.RestfulHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

/**
 * Created by Matthew E on 3/10/2019 at 4:49 PM for the project DungeonRealmsDREnhanced
 */
public class ChangelogCommand extends CommandBase {
    private RestfulHandler restfulHandler;

    public ChangelogCommand(RestfulHandler restfulHandler) {
        this.restfulHandler = restfulHandler;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public String getName() {
        return "changelogs";

    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/changelogs";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {


    }

}
