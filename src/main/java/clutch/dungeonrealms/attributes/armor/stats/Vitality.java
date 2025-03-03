package clutch.dungeonrealms.attributes.armor.stats;

import clutch.dungeonrealms.attributes.Attribute;
import clutch.dungeonrealms.utils.ArmorUtils;
import net.minecraft.item.ItemStack;

public class Vitality extends Attribute {

    private double vitality = 0;

    @Override
    public void updateInfo(ItemStack stack) {
        this.vitality = ArmorUtils.getDoubleListFromList(stack, getCompare()).get(0);
    }

    @Override
    public double getCompareValue() {
        return vitality;
    }

    @Override
    public String getCompare() {
        return "VITALITY";
    }

    @Override
    public String getTooltipName() {
        return "VIT";
    }
}
