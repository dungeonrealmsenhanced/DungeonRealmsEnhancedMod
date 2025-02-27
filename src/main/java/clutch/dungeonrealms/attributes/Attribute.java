package clutch.dungeonrealms.attributes;

import net.minecraft.item.ItemStack;

public abstract class Attribute {

    public abstract void updateInfo(ItemStack stack);
    public abstract double getCompareValue();

    public abstract String getCompare();
    public abstract String getTooltipName();

}
