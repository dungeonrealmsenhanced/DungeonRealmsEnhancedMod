package me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.player.profession.mining;

import me.matthewe.dungeonrealms.drenhanced.dungeonrealmsenhanced.utilities.item.Tier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Matthew E on 3/13/2019 at 9:20 AM for the project DungeonRealmsDREnhanced
 */
public class Mining {
    private Map<Tier, List<MinedOre>> minedOreMap = new ConcurrentHashMap<>();

    public Mining(Map<Tier, List<MinedOre>> minedOreMap) {
        this.minedOreMap = minedOreMap;
    }

    public Mining() {
        this.minedOreMap = new ConcurrentHashMap<>();
    }

    public void mineOre(MinedOre minedOre) {
        List<MinedOre> minedOres = new ArrayList<>();
        if (this.minedOreMap.containsKey(minedOre.getTier())) {
            minedOres = this.minedOreMap.get(minedOre.getTier());
            this.minedOreMap.remove(minedOre.getTier());
        }
        minedOres.add(minedOre);
        this.minedOreMap.put(minedOre.getTier(), minedOres);
    }


    public Map<Tier, List<MinedOre>> getMinedOreMap() {
        return minedOreMap;
    }
}
