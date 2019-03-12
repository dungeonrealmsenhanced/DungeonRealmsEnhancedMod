package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.world;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

/**
 * Created by Matthew E on 5/28/2018 at 11:49 AM for the project DungeonRealmsEnhanced
 */
public class WorldUtils {
    public static List<EntityItem> getItemsAroundPlayer(int radius) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        double v = radius / 2;
        AxisAlignedBB scanAround = new AxisAlignedBB(player.posX-v, player.posY-v, player.posZ-v, player.posX+v, player.posY+v, player.posZ+v);
       return Minecraft.getMinecraft().world.getEntitiesWithinAABB(EntityItem.class, scanAround);
    }

    public static <T extends Entity> List<T> getEntitiesAroundPlayer(int radius, Class<T> tClass) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        double v = radius / 2;
        AxisAlignedBB scanAround = new AxisAlignedBB(player.posX-v, player.posY-v, player.posZ-v, player.posX+v, player.posY+v, player.posZ+v);
       return Minecraft.getMinecraft().world.getEntitiesWithinAABB(tClass, scanAround);
    }
}
