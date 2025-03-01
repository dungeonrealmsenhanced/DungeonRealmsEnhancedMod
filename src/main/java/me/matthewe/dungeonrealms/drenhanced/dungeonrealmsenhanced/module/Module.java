package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.Listener;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.common.MinecraftForge;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

import static me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.GuiSettings.settingsOpened;

/**
 * Created by Matthew E on 12/31/2018 at 3:27 PM for the project DungeonRealmsDREnhanced
 */
public abstract class Module {
    private static final Log log = LogFactory.getLog(Module.class);
    protected String name;
    public int posX;
    public int posY;
    private boolean enabled;
    private boolean loaded;
    public int width;
    public int height;
    public int defaultPosX;
    public int defaultPosY;
    private boolean editing = false;
    protected List<Listener> listeners;
    protected Minecraft mc;

    public Module(String name) {
        this.name = name;
        this.enabled = false;
        this.posX = 0;
        this.posY = 0;
        this.width = 0;
        this.height = 0;
        this.mc = Minecraft.getMinecraft();
        this.listeners = new ArrayList<>();
    }
    public boolean isMouseWithin(int mouseX, int mouseY) {
        return RenderUtils.isMouseWithin(mouseX, mouseY, posX, posY, width, height);
    }

    public void onRender(ScaledResolution scaledResolution, float particleTicks) {
        update(scaledResolution, particleTicks);

        if (settingsOpened) {
            renderOutline(scaledResolution, particleTicks);
            if (editing) {

                renderEditing(scaledResolution, particleTicks);
            } else {
                render(scaledResolution, particleTicks);
            }
        } else {
            if (isEnabled()) {
                render(scaledResolution, particleTicks);
            }
        }
    }

    public void registerListener(Listener listener) {
        this.listeners.add(listener);
    }

    public abstract void renderEditing(ScaledResolution resolution, float partialTicks);

    public abstract void update(ScaledResolution resolution, float partialTicks);

    public String getName() {
        return name;
    }

    public int getWidth() {
        return width;
    }

    public List<Listener> getListeners() {
        return listeners;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public abstract void renderOutline(ScaledResolution scaledResolution, float particleTicks);

    public abstract void render(ScaledResolution scaledResolution, float particleTicks);


    public void load() {
        if (this.loaded) {
            return;
        }

        MinecraftForge.EVENT_BUS.register(this);
        this.onLoad();
        log.info("[" + name + "] Enabled.");

        this.loaded = true;
        defaultPosX = new Integer(this.posX).intValue();
        defaultPosY = new Integer(this.posY).intValue();
    }

    public void unload() {
        if (!this.loaded) {
            return;
        }
        MinecraftForge.EVENT_BUS.unregister(this);
        this.onUnload();
        log.info("[" + name + "] Disabled.");

        this.loaded = false;
    }

    public abstract void onLoad();

    public abstract void onUnload();

    public boolean isLoaded() {
        return loaded;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void loadSettings() {

    }

    public boolean isEditing() {
        return editing;
    }

    public void setEditing(boolean editing) {
        this.editing = editing;
    }
}
