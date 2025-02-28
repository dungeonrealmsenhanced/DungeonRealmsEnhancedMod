package clutch.dungeonrealms.attributes.weapon;

import clutch.dungeonrealms.attributes.Attribute;

public class MeleeDamage extends Attribute {

    @Override
    public String getCompare() {
        return "MELEE_DAMAGE";
    }

    @Override
    public String getTooltipName() {
        return "DMG";
    }
}
