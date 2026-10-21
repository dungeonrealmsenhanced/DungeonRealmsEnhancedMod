package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.events;

import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

/** Created by Matthew E on 3/9/2019 at 7:08 PM for the project DungeonRealmsUtils */
public class TipReceiveEvent extends Event implements ICancellableEvent {
  private final String tip;

  public TipReceiveEvent(String tip) {
    this.tip = tip;
  }

  public String getTip() {
    return tip;
  }
}
