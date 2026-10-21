package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.world;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.AABB;

/** Created by Matthew E on 5/28/2018 at 11:49 AM for the project DungeonRealmsEnhanced */
public class WorldUtils {
  public static List<ItemEntity> getItemsAroundPlayer(int radius) {
    LocalPlayer player = Minecraft.getInstance().player;
    double v = radius / 2;
    AABB scanAround =
        new AABB(
            player.getX() - v,
            player.getY() - v,
            player.getZ() - v,
            player.getX() + v,
            player.getY() + v,
            player.getZ() + v);
    return Minecraft.getInstance().level.getEntitiesOfClass(ItemEntity.class, scanAround);
  }

  public static <T extends Entity> List<T> getEntitiesAroundPlayer(int radius, Class<T> tClass) {
    LocalPlayer player = Minecraft.getInstance().player;
    double v = radius / 2;
    AABB scanAround =
        new AABB(
            player.getX() - v,
            player.getY() - v,
            player.getZ() - v,
            player.getX() + v,
            player.getY() + v,
            player.getZ() + v);
    return Minecraft.getInstance().level.getEntitiesOfClass(tClass, scanAround);
  }
}
