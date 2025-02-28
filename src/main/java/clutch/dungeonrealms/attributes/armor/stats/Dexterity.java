package clutch.dungeonrealms.attributes.armor.stats;

import clutch.dungeonrealms.attributes.Attribute;
import clutch.dungeonrealms.utils.ArmorUtils;
import net.minecraft.item.ItemStack;

public class Dexterity extends Attribute {

    private double dexterity = 0;

    @Override
    public void updateInfo(ItemStack stack) {
        this.dexterity = ArmorUtils.getDoubleListFromList(stack, getCompare()).get(0);
    }

    @Override
    public double getCompareValue() {
        return dexterity;
    }

    @Override
    public String getCompare() {
        return "DEXTERITY";
    }

    @Override
    public String getTooltipName() {
        return "DEX";
    }
}
