package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.events;

import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Created by Matthew E on 3/9/2019 at 7:08 PM for the project DungeonRealmsUtils
 */
@Cancelable
public class TipReceiveEvent extends Event  {
    private final String tip;

    public TipReceiveEvent(String tip) {
        this.tip = tip;
    }

    public String getTip() {
        return tip;
    }
}
