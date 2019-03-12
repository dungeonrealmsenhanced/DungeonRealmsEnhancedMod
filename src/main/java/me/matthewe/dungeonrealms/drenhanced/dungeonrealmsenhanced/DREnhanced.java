package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced;

import com.google.gson.GsonBuilder;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.commands.CommandInfo;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.listeners.MenuReplacerListener;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.Handlers;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.listeners.ItemCheckerListener;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.listeners.RarityOverlayListener;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.listeners.StatisticListener;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.Module;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.Modules;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.player.DRPlayer;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.DREnhancedConfig;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.texture.DRTextures;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.texture.ImageUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.commons.io.FileUtils;
import org.lwjgl.opengl.Display;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Date;


@Mod(
        modid = DREnhanced.MOD_ID,
        name = DREnhanced.MOD_NAME,
        version = DREnhanced.VERSION
)
public class DREnhanced {

    public static final String MOD_ID = "dungeonrealmsenhanced";
    public static final String MOD_NAME = "DREnhanced";
    public static final String VERSION = "1.0.0";


    /**
     * This is the instance of your mod as created by Forge. It will never be null.
     */
    @Mod.Instance(MOD_ID)
    public static DREnhanced INSTANCE;

    public String folderLocation = "";


    /**
     * This is the first initialization event. Register tile entities here.
     * The registry events below will have fired prior to entry to this method.
     */
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        log("DREnhanced Version %s found!", VERSION);
        log("Pre-Loading initialized...");
        this.folderLocation = event.getSuggestedConfigurationFile().getParentFile().getParent() + "\\dungeonrealmsenhanced\\";
        if (!new File(folderLocation).exists()) {
            new File(folderLocation).mkdirs();
        }
        log(folderLocation);
        if (FMLCommonHandler.instance().getSide().isClient()) {

            Display.setTitle("DREnhanced");

            if (Util.getOSType() != Util.EnumOS.OSX) {

                try {
                    Display.setIcon(new ByteBuffer[]{
                            ImageUtils.readImage(Minecraft.getMinecraft().getResourceManager()
                                    .getResource(new ResourceLocation(
                                            MOD_ID + ":textures/gui/icon_16x16.png"))
                                    .getInputStream()),
                            ImageUtils.readImage(Minecraft.getMinecraft().getResourceManager()
                                    .getResource(new ResourceLocation(
                                            MOD_ID + ":textures/gui/icon_32x32.png"))
                                    .getInputStream())});
                } catch (IOException ioexception) {
                    ioexception.printStackTrace();
                }
            }
        }

        Modules.init();
        Modules.loadModules();

        loadModuleSettings();

        Modules.registerListeners();

        this.registerEvents();

        Handlers.init();
        Handlers.enableHandlers();
    }

    private void registerEvents() {
        MinecraftForge.EVENT_BUS.register(new StatisticListener());
        MinecraftForge.EVENT_BUS.register(new ItemCheckerListener());
        MinecraftForge.EVENT_BUS.register(new RarityOverlayListener());
        MinecraftForge.EVENT_BUS.register(new MenuReplacerListener());
    }

    private void shutdown() {
        saveModuleSettings();
        Modules.unloadModules();
        Handlers.disableHandlers();
    }


    public void loadModuleSettings() {
        File file = new File(folderLocation + "module_save.json");
        if (file.exists()) {
            try {
                String readFileToString = FileUtils.readFileToString(file, Charset.forName("UTF-8"));
                DREnhancedConfig drEnhancedConfig = new GsonBuilder().setPrettyPrinting().create().fromJson(readFileToString, DREnhancedConfig.class);
                for (Module module : Modules.getModules()) {
                    Integer[] coords = drEnhancedConfig.getCoords(module.getName());
                    if (coords != null) {
                        module.posX = coords[0];
                        module.posY = coords[1];
                        module.setEnabled(coords[0] == 1);
                        log("[" + module.getName() + "] set posX to " + coords[0]);
                        log("[" + module.getName() + "] set posY to " + coords[1]);
                        log("[" + module.getName() + "] set status to " + module.isEnabled());
                    }
                }
                DRPlayer.drPlayer = drEnhancedConfig.drPlayer;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            saveModuleSettings();
            loadModuleSettings();
        }

    }

    public void saveModuleSettings() {
        File file = new File(folderLocation + "module_save.json");
        if (file.exists()) {
            try {
                FileUtils.forceDelete(file);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        file = new File(folderLocation + "module_save.json");

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        DREnhancedConfig drEnhancedConfig = new DREnhancedConfig();
        for (Module module : Modules.getModules()) {
            drEnhancedConfig.setCoords(module.getName(), module.posX, module.posY, module.isEnabled());
        }
        try {
            FileUtils.writeStringToFile(file, new GsonBuilder().setPrettyPrinting().create().toJson(drEnhancedConfig), Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void log(String par1, Object... par2) {
        String var1 = String.format(par1, par2);
        System.out.println("[" + new Date().toLocaleString() + "] [DREnhanced] " + var1);
    }

    /**
     * This is the second initialization event. Register custom recipes
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));

    }

    /**
     * This is the final initialization event. Register actions from other mods here
     */
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        ClientCommandHandler.instance.registerCommand(new CommandInfo());

        DRTextures.loadTextures();
    }
}
