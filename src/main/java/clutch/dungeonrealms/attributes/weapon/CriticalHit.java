package clutch.dungeonrealms.attributes.weapon;

import clutch.dungeonrealms.attributes.Attribute;

public class CriticalHit extends Attribute {

    @Override
    public String getCompare() {
        return "CRITICAL_HIT";
    }

    @Override
    public String getTooltipName() {
        return "Critical Hit";
    }
}
