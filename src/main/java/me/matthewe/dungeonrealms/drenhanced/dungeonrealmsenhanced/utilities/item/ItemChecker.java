package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.world.WorldUtils;
import net.minecraft.entity.item.EntityItem;

import java.util.function.Consumer;

/**
 * Created by Matthew E on 5/28/2018 at 10:58 AM for the project DungeonRealmsEnhanced
 */
public class ItemChecker {
    private Consumer<EntityItem> findItemConsumer;

    public ItemChecker(Consumer<EntityItem> findItemConsumer) {
        this.findItemConsumer = findItemConsumer;
    }

    public void update() {

        for (EntityItem entityItem : WorldUtils.getItemsAroundPlayer(25)) {
            if (entityItem.getTags().contains("M_CHECKED")) {
                continue;
            }
//            for (String tag : entityItem.getTags()) {
//                System.out.println(tag);
//            }
            entityItem.addTag("M_CHECKED");
            this.findItemConsumer.accept(entityItem);
        }
    }
}
