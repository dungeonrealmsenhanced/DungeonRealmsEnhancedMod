package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.reflection;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;

public enum ReflectionField {

    GUI_CHEST_LOWER_CHEST_INVENTORY(GuiChest.class, "lowerChestInventory", "field_147015_w");

    final Field field;

    ReflectionField(Class<?> holdingClass, String... values) {
        this.field = ReflectionHelper.findField(holdingClass, values);
        this.field.setAccessible(true);
    }

    public Object getValue(Object parent) {
        try {
            return field.get(parent);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setValue(Object parent, Object value) {
        try {
            field.set(parent, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
