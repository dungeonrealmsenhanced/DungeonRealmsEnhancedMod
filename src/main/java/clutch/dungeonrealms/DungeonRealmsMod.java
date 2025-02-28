package clutch.dungeonrealms;

import net.minecraftforge.common.MinecraftForge;

public class DungeonRealmsMod {


    public static void init(){
        MinecraftForge.EVENT_BUS.register(new RenderOverlay());
//        MinecraftForge.EVENT_BUS.register(new ArmorTooltipCompare());
    }
}
