package clutch.dungeonrealms.attributes.armor;

import clutch.dungeonrealms.attributes.Attribute;
import clutch.dungeonrealms.utils.ArmorUtils;
import net.minecraft.item.ItemStack;

import java.util.List;

public class Armor extends Attribute {

    private double armorMax = 0;

    @Override
    public void updateInfo(ItemStack stack) {
        List<Double> armorInfo = ArmorUtils.getDoubleListFromList(stack, getCompare());
        if (armorInfo.size() >= 2) this.armorMax = armorInfo.get(1);
    }

    @Override
    public double getCompareValue() {
        return armorMax;
    }

    @Override
    public String getCompare() {
        return "ARMOR";
    }

    @Override
    public String getTooltipName() {
        return "ARMOR";
    }
}
