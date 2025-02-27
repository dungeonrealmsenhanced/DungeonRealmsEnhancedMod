package clutch.dungeonrealms.attributes.armor;

import clutch.dungeonrealms.attributes.Attribute;
import clutch.dungeonrealms.utils.ArmorUtils;
import net.minecraft.item.ItemStack;

public class ItemFind extends Attribute {

    private double itemFind = 0;

    @Override
    public void updateInfo(ItemStack stack) {
        this.itemFind = ArmorUtils.getDoubleListFromList(stack, getCompare()).get(0);
    }

    @Override
    public double getCompareValue() {
        return itemFind;
    }

    @Override
    public String getCompare() {
        return "ITEM_FIND";
    }

    @Override
    public String getTooltipName() {
        return "ITEM FIND";
    }
}
