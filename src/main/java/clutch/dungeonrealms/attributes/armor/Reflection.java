package clutch.dungeonrealms.attributes.armor;

import clutch.dungeonrealms.attributes.Attribute;
import clutch.dungeonrealms.utils.ArmorUtils;
import net.minecraft.item.ItemStack;

public class Reflection extends Attribute {

    private double reflection = 0;

    @Override
    public void updateInfo(ItemStack stack) {
        this.reflection = ArmorUtils.getDoubleListFromList(stack, getCompare()).get(0);
    }

    @Override
    public double getCompareValue() {
        return reflection;
    }

    @Override
    public String getCompare() {
        return "REFLECTION";
    }

    @Override
    public String getTooltipName() {
        return "REFLECT";
    }
}
