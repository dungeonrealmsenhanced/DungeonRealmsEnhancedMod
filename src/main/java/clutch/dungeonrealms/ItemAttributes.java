package clutch.dungeonrealms;

import clutch.dungeonrealms.attributes.Attribute;
import clutch.dungeonrealms.attributes.armor.*;
import clutch.dungeonrealms.attributes.armor.health.Healing;
import clutch.dungeonrealms.attributes.armor.health.HealthPoints;
import clutch.dungeonrealms.attributes.armor.health.HealthRegen;
import clutch.dungeonrealms.attributes.armor.resistance.ElementalResistance;
import clutch.dungeonrealms.attributes.armor.resistance.FireResistance;
import clutch.dungeonrealms.attributes.armor.resistance.IceResistance;
import clutch.dungeonrealms.attributes.armor.resistance.PoisonResistance;
import clutch.dungeonrealms.attributes.armor.stats.Dexterity;
import clutch.dungeonrealms.attributes.armor.stats.Intellect;
import clutch.dungeonrealms.attributes.armor.stats.Strength;
import clutch.dungeonrealms.attributes.armor.stats.Vitality;
import clutch.dungeonrealms.attributes.weapon.*;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Clutch
 * @since 6/24/2017
 */
public class ItemAttributes {

    private static Map<String, Attribute> attributes = new HashMap<>();
    private static Map<String, String> keyOneMap = new HashMap<>();
//    private static Map<String, String> keyTwoMap = new HashMap<>();

    static {
        new ItemAttributes();

    }
    public ItemAttributes() {
        // Armor Main
        registerAttribute(new Armor());
        registerAttribute(new Block());
        registerAttribute(new Dodge());
        registerAttribute(new Dps());
        registerAttribute(new EnergyRegen());
        registerAttribute(new GemFind());
        registerAttribute(new ItemFind());
        registerAttribute(new Reflection());
        registerAttribute(new Thorns());
        // Armor Health
        registerAttribute(new Healing());
        registerAttribute(new HealthPoints());
        registerAttribute(new HealthRegen());
        // Armor Resistance
        registerAttribute(new ElementalResistance());
        registerAttribute(new FireResistance());
        registerAttribute(new IceResistance());
        registerAttribute(new PoisonResistance());
        // Armor Stats
        registerAttribute(new Dexterity());
        registerAttribute(new Intellect());
        registerAttribute(new Strength());
        registerAttribute(new Vitality());



        registerAttribute(new MeleeDamage());
        registerAttribute(new PoisonDamage());
        registerAttribute(new FireDamage());
        registerAttribute(new PureDamage());
        registerAttribute(new IceDamage());
        registerAttribute(new VSPlayers());
        registerAttribute(new VSMonsters());


        for (Attribute value : attributes.values()) {
            if (value.getCompare() == null) continue;
            keyOneMap.put(value.getCompare(), value.getTooltipName());
        }
    }

    public void updateItemInfo(ItemStack stack) {
        for (Attribute attribute : this.attributes.values()) {
            attribute.updateInfo(stack);
        }
    }

    public static Attribute get(String input) {
        return attributes.get(input);

    }


    public String getCompareName(String name) {
        return this.attributes.get(name).getTooltipName();
    }

    public double getCompareValue(String name) {
        return this.attributes.get(name).getCompareValue();
    }

    public Set<String> getAttributes() {
        return this.attributes.keySet();
    }

    private void registerAttribute(Attribute attribute) {
        this.attributes.put(attribute.getCompare(), attribute);
    }
}
