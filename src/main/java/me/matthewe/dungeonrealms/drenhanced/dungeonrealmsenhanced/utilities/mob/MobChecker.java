package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.mob;

import java.util.function.Consumer;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.world.WorldUtils;
import net.minecraft.world.entity.Entity;

/** Created by Matthew E on 5/28/2018 at 10:58 AM for the project DungeonRealmsEnhanced */
public class MobChecker<T extends Entity> {
  private Consumer<T> consumer;
  private Class<T> clazz;

  public MobChecker(Consumer<T> consumer, Class<T> clazz) {
    this.consumer = consumer;
    this.clazz = clazz;
  }

  public void update() {
    for (T entityItem : WorldUtils.getEntitiesAroundPlayer(25, clazz)) {
      if (entityItem.entityTags().contains("M_CHECKED")) {
        continue;
      }
      entityItem.addTag("M_CHECKED");
      this.consumer.accept(entityItem);
    }
  }
}
