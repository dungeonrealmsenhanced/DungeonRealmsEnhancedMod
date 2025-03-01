package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.proxy;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.DungeonRealmsMod;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.commands.CommandInfo;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.commands.ZoneCommand;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.Handlers;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.listeners.*;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.Modules;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.texture.DRTextures;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import static me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.DREnhanced.loadModuleSettings;
import static me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.DREnhanced.statKeyDatabase;

public class ClientProxy implements IProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        DRTextures.loadTextures();

        Modules.init();
        Modules.loadModules();
        statKeyDatabase.loadDictionary();

        loadModuleSettings();

        Modules.registerListeners();
        MinecraftForge.EVENT_BUS.register(new StatisticListener());
        MinecraftForge.EVENT_BUS.register(new ItemCheckerListener());
        MinecraftForge.EVENT_BUS.register(new RarityOverlayListener());
        MinecraftForge.EVENT_BUS.register(new MenuReplacerListener());
        DungeonRealmsMod.init();

        Handlers.init();
        Handlers.enableHandlers();

    }


    @Override
    public void init(FMLInitializationEvent event) {
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        ClientCommandHandler.instance.registerCommand(new CommandInfo());
        ClientCommandHandler.instance.registerCommand(new ZoneCommand());
    }

    @Override
    public void serverStarting(FMLServerStartingEvent event) {
    }

    @Override
    public EntityPlayer getPlayerEntityFromContext(MessageContext ctx) {
        return ctx.getServerHandler().player;
    }
}