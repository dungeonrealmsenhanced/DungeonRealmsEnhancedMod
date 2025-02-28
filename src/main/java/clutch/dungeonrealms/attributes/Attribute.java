package clutch.dungeonrealms.attributes;

import net.minecraft.item.ItemStack;

public abstract class Attribute {

    public  void updateInfo(ItemStack stack) {}
    public  double getCompareValue() {return 0;};

    public  String getCompare() {return null;}
    public abstract String getTooltipName();

}
