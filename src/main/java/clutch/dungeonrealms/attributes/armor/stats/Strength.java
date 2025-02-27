package clutch.dungeonrealms.attributes.armor.stats;

import clutch.dungeonrealms.attributes.Attribute;
import clutch.dungeonrealms.utils.ArmorUtils;
import net.minecraft.item.ItemStack;

public class Strength extends Attribute {

    private double strength = 0;

    @Override
    public void updateInfo(ItemStack stack) {
        this.strength = ArmorUtils.getDoubleListFromList(stack, getCompare()).get(0);
    }

    @Override
    public double getCompareValue() {
        return strength;
    }

    @Override
    public String getCompare() {
        return "STRENGTH";
    }

    @Override
    public String getTooltipName() {
        return "STR";
    }
}
