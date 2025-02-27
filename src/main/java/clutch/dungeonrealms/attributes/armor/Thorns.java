package clutch.dungeonrealms.attributes.armor;

import clutch.dungeonrealms.attributes.Attribute;
import clutch.dungeonrealms.utils.ArmorUtils;
import net.minecraft.item.ItemStack;

public class Thorns extends Attribute {

    private double thorns = 0;

    @Override
    public void updateInfo(ItemStack stack) {
        this.thorns = ArmorUtils.getDoubleListFromList(stack, getCompare()).get(0);
    }

    @Override
    public double getCompareValue() {
        return thorns;
    }

    @Override
    public String getCompare() {
        return "THORNS";
    }

    @Override
    public String getTooltipName() {
        return "THORNS";
    }
}
