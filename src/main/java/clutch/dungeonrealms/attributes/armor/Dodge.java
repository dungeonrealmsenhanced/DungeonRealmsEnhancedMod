package clutch.dungeonrealms.attributes.armor;

import clutch.dungeonrealms.attributes.Attribute;
import clutch.dungeonrealms.utils.ArmorUtils;
import net.minecraft.item.ItemStack;

public class Dodge extends Attribute {

    private double dodge = 0;

    @Override
    public void updateInfo(ItemStack stack) {
        this.dodge = ArmorUtils.getDoubleListFromList(stack, getCompare()).get(0);
    }

    @Override
    public double getCompareValue() {
        return dodge;
    }

    @Override
    public String getCompare() {
        return "DODGE";
    }

    @Override
    public String getTooltipName() {
        return "DODGE";
    }
}
