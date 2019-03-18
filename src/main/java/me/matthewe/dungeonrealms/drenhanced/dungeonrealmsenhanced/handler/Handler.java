package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handler;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handler.logger.HandlerLogger;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.command.CommandBase;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthew E on 3/12/2019 at 10:41 AM for the project DungeonRealmsDREnhanced
 */
public abstract class Handler {
    private boolean enabled;
    public HandlerLogger logger;
    protected List<Listener> listenerList;
    public Minecraft mc;
    public FontRenderer fontRenderer;

    public Handler(boolean debug) {
        this.logger = new HandlerLogger(this.getClass(), debug);
        this.listenerList = new ArrayList<>();
        this.mc = Minecraft.getMinecraft();
        this.fontRenderer = this.mc.fontRenderer;
    }

    public void enable() {
        if (this.enabled) {
            return;
        }
        this.onEnable();
        this.registerListeners();
        this.enabled = true;
        this.logger.info("Enabled.");
    }

    public void disable() {
        if (!this.enabled) {
            return;
        }
        this.onDisable();
        this.unregisterListeners();
        this.enabled = false;
        this.logger.info("Disabled.");
    }

    private void unregisterListeners() {
        this.listenerList.forEach(listener -> {
            MinecraftForge.EVENT_BUS.unregister(listener);
            logger.info("Unregistered listener %s.", listener.getClass().getSimpleName());
        });
        this.listenerList.clear();
    }

    public void registerCommand(CommandBase command) {
        ClientCommandHandler.instance.registerCommand(command);
        logger.info("Registered command /%s", command.getName() + ".");
    }

    public boolean isEnabled() {
        return enabled;
    }

    protected void registerListener(Listener listener) {
        this.listenerList.add(listener);
    }

    private void registerListeners() {
        this.listenerList.forEach(listener -> {
            MinecraftForge.EVENT_BUS.register(listener);
            logger.info("Registered listener %s", listener.getClass().getSimpleName() + ".");
        });
    }

    public abstract void onEnable();

    public abstract void onDisable();
}
