package clutch.dungeonrealms.attributes.armor.health;

import clutch.dungeonrealms.attributes.Attribute;
import clutch.dungeonrealms.utils.ArmorUtils;
import net.minecraft.item.ItemStack;

public class HealthRegen extends Attribute {

    private double healthRegen = 0;

    @Override
    public void updateInfo(ItemStack stack) {
        this.healthRegen = ArmorUtils.getDoubleListFromList(stack, getCompare()).get(0);
    }

    @Override
    public double getCompareValue() {
        return healthRegen;
    }

    @Override
    public String getCompare() {
        return "HPS";
    }

    @Override
    public String getTooltipName() {
        return "HP REGEN";
    }
}
