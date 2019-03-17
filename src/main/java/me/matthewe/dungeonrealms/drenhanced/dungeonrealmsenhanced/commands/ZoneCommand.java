package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.commands;


import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.world.Zone;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.init.SoundEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

/**
 * Created by Matthew E on 3/10/2019 at 4:49 PM for the project DungeonRealmsDREnhanced
 */
public class ZoneCommand extends CommandBase {
    public ZoneCommand() {
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public String getName() {
        return "zone";

    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/zone";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        Zone zone = Zone.get();
        switch (zone) {
            case SAFE:
                sender.sendMessage(new TextComponentString(TextFormatting.GREEN + "                " + TextFormatting.BOLD + "*** SAFE ZONE (DMG-OFF) ***"));
                break;
            case WILDERNESS:
                sender.sendMessage(new TextComponentString(TextFormatting.YELLOW + "           " + TextFormatting.BOLD + "*** WILDERNESS (MOBS-ON, PVP-OFF) ***"));
                break;
            case CHAOTIC:
                sender.sendMessage(new TextComponentString(TextFormatting.RED + "                " + TextFormatting.BOLD + "*** CHAOTIC ZONE (PVP-ON) ***"));
                break;
        }
        Minecraft.getMinecraft().player.playSound(SoundEvents.ENTITY_WITHER_SHOOT, 0.25F, 0.30F);

    }

}
