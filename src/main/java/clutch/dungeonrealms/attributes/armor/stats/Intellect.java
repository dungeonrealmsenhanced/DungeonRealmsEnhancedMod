package clutch.dungeonrealms.attributes.armor.stats;

import clutch.dungeonrealms.attributes.Attribute;
import clutch.dungeonrealms.utils.ArmorUtils;
import net.minecraft.item.ItemStack;

public class Intellect extends Attribute {

    private double intellect = 0;

    @Override
    public void updateInfo(ItemStack stack) {
        this.intellect = ArmorUtils.getDoubleListFromList(stack, getCompare()).get(0);
    }

    @Override
    public double getCompareValue() {
        return intellect;
    }

    @Override
    public String getCompare() {
        return "INTELLECT";
    }

    @Override
    public String getTooltipName() {
        return "INT";
    }
}
