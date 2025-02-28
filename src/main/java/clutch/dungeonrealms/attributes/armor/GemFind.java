package clutch.dungeonrealms.attributes.armor;

import clutch.dungeonrealms.attributes.Attribute;
import clutch.dungeonrealms.utils.ArmorUtils;
import net.minecraft.item.ItemStack;

public class GemFind extends Attribute {

    private double gemFind = 0;

    @Override
    public void updateInfo(ItemStack stack) {
        this.gemFind = ArmorUtils.getDoubleListFromList(stack, getCompare()).get(0);
    }

    @Override
    public double getCompareValue() {
        return gemFind;
    }

    @Override
    public String getCompare() {
        return "GEM_FIND";
    }

    @Override
    public String getTooltipName() {
        return "GEM FIND";
    }
}
