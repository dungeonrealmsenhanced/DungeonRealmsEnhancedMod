package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced;

import com.google.gson.GsonBuilder;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.handlers.Handlers;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.Module;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.module.Modules;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.player.DRPlayer;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.proxy.IProxy;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.settings.DREnhancedConfig;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.texture.ImageUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
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
    @SidedProxy(
            clientSide = "me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.proxy.ClientProxy",
            serverSide = "me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.proxy.ServerProxy"
    )
    public static IProxy proxy;
    public static final String MOD_ID = "dungeonrealmsenhanced";
    public static final String MOD_NAME = "DREnhanced";
    public static final String VERSION = "1.0.2";


    public static final String[] DEVELOPERS = new String[]{
            "1d48bd80-4cd0-4874-ba65-94284bc24ecc",
            "56a838cd-afd8-4e30-a34f-822e936eb949"
    };

    public static boolean isDeveloper() {
        for (String developer : DEVELOPERS) {
            if (Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().player.getUniqueID().toString().equalsIgnoreCase(developer)) {
                return true;
            }
        }
        return false;
    }

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
        proxy.preInit(event);

    }


    private void shutdown() {
        saveModuleSettings();
        Modules.unloadModules();
        Handlers.disableHandlers();
    }

    @Mod.EventHandler
    public void onFMLServerStarting(FMLServerStartingEvent event) {

        proxy.serverStarting(event);

    }

    public static void loadModuleSettings() {
        File file = new File(DREnhanced.INSTANCE.folderLocation + "module_save.json");
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

    public static void saveModuleSettings() {
        File file = new File(DREnhanced.INSTANCE.folderLocation + "module_save.json");
        if (file.exists()) {
            try {
                FileUtils.forceDelete(file);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        file = new File(DREnhanced.INSTANCE.folderLocation + "module_save.json");

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
        proxy.init(event);
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));

    }

    /**
     * This is the final initialization event. Register actions from other mods here
     */
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }
}
