package clutch.dungeonrealms;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DungeonRealmsMod {


    public static void init(){
        MinecraftForge.EVENT_BUS.register(new RenderOverlay());
        MinecraftForge.EVENT_BUS.register(new ArmorTooltipCompare());
    }
}
