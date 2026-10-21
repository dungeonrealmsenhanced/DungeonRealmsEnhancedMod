package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item;

import java.util.function.Consumer;
import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.world.WorldUtils;
import net.minecraft.world.entity.item.ItemEntity;

/** Created by Matthew E on 5/28/2018 at 10:58 AM for the project DungeonRealmsEnhanced */
public class ItemChecker {
  private Consumer<ItemEntity> findItemConsumer;

  public ItemChecker(Consumer<ItemEntity> findItemConsumer) {
    this.findItemConsumer = findItemConsumer;
  }

  public void update() {

    for (ItemEntity entityItem : WorldUtils.getItemsAroundPlayer(25)) {
      if (entityItem.entityTags().contains("M_CHECKED")) {
        continue;
      }

      entityItem.addTag("M_CHECKED");
      this.findItemConsumer.accept(entityItem);
    }
  }
}
