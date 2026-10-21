package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities;

import java.util.*;
import java.util.function.Consumer;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

/** Keep unmodified rich components intact when applying legacy calculation results. */
public final class TooltipText {
  public static void edit(ItemTooltipEvent event, Consumer<List<String>> editor) {
    var originals = new ArrayList<>(event.getToolTip());
    List<String> lines = new ArrayList<>(originals.stream().map(Component::getString).toList());
    editor.accept(lines);
    var result = new ArrayList<Component>();
    for (String line : lines) {
      Component original =
          originals.stream().filter(c -> c.getString().equals(line)).findFirst().orElse(null);
      if (original != null) {
        result.add(original);
      } else {
        Component base =
            originals.stream()
                .filter(
                    c ->
                        !c.getString().isEmpty()
                            && (line.startsWith(c.getString()) || line.endsWith(c.getString())))
                .max(Comparator.comparingInt(c -> c.getString().length()))
                .orElse(null);
        if (base == null) {
          result.add(Component.literal(line));
        } else {
          int start = line.indexOf(base.getString());
          result.add(
              Component.literal(line.substring(0, start))
                  .append(base)
                  .append(Component.literal(line.substring(start + base.getString().length()))));
        }
      }
    }
    event.getToolTip().clear();
    event.getToolTip().addAll(result);
  }
}
